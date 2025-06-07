package top.aprdec.onepractice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.api.pagination.EasyPageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.dto.req.CommonPageResultReqDTO;
import top.aprdec.onepractice.dto.req.UserSaveWordReqDTO;
import top.aprdec.onepractice.entity.UserSavedWordsDO;
import top.aprdec.onepractice.entity.WordsDO;
import top.aprdec.onepractice.entity.proxy.UserSavedWordsDOProxy;
import top.aprdec.onepractice.entity.proxy.WordsDOProxy;
import top.aprdec.onepractice.exception.CommonException;
import top.aprdec.onepractice.service.UserSavedWordService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSavedWordServiceimpl implements UserSavedWordService {
    private final EasyEntityQuery easyEntityQuery;
    private final RedisTemplate redisTemplate;
    private final RedissonClient redissonClient;

    @Transactional
    @Override
    public void addSavedWord(UserSaveWordReqDTO dto) {
        final String word = dto.getWord();
        final Long userId = StpUtil.getLoginIdAsLong();

        // 1. 确保用户收藏集缓存存在
        ensureUserSavedCacheExists(userId);

        // 2. 获取或创建单词
        WordsDO targetWord = getOrCreateWord(word);

        // 3. 添加收藏关系
        addSavedWordMapping(userId, targetWord.getId());
    }

//    检查用户是否收藏
    @Override
    public Boolean hascollected(UserSaveWordReqDTO dto){
        //        先判断有无 无-插入word-为收藏 有-拿到ID-查询有无
        String word = dto.getWord();
        Long userId = StpUtil.getLoginIdAsLong();
        //检查用户收藏集缓存是否存在?
        Boolean cached = redisTemplate.hasKey(RedisKeyConstant.USER_SAVED_WORD_LIST + userId);
        if(!cached){
            cacheUserSavedWords();
        }
        //查看word缓存是否存在
        WordsDO wordsDO = null;
        Object wordObject = redisTemplate.opsForValue().get(RedisKeyConstant.WORDINFO + word);
        if(wordObject!= null){
            wordsDO =JSON.parseObject(wordObject.toString(),WordsDO.class);
        }
        if(wordsDO == null) {
//       //查询word表是否有word 若无一定无
            wordsDO = easyEntityQuery.queryable(WordsDO.class).where(o -> o.word().eq(word)).firstOrNull();
        }
        if(wordsDO == null){
            return false;
        }
        return redisTemplate.opsForSet().isMember(RedisKeyConstant.USER_SAVED_WORD_LIST+userId,wordsDO.getId());
    }

//    用户登录缓存收藏表到Redis
    @Async("asyncTaskExecutor")
    public void cacheUserSavedWords(){
        Long userId = StpUtil.getLoginIdAsLong();
        List<UserSavedWordsDO> usersavewordList = easyEntityQuery.queryable(UserSavedWordsDO.class)
                .select(u -> new UserSavedWordsDOProxy()
                        .wordId().set(u.wordId())
                        .userId().set(u.userId()))
                .where(u -> u.userId().eq(userId))
                .toList();
        if(!usersavewordList.isEmpty()){
            List<Long> wordIdList = usersavewordList.stream().map(UserSavedWordsDO::getWordId).toList();
            //过期时间和token时间一致
            redisTemplate.opsForSet().add(RedisKeyConstant.USER_SAVED_WORD_LIST+userId, wordIdList.toArray());
        }
    }
//  单词表缓存
    @Async("asyncTaskExecutor")
    public void cacheAllWords(){
        List<WordsDO> wordList = easyEntityQuery.queryable(WordsDO.class)
                .select(w -> new WordsDOProxy()
                        .id().set(w.id())
                        .word().set(w.word()))
                .toList();
        if(!wordList.isEmpty()){
            wordList.forEach(word -> {
                redisTemplate.opsForValue().set(RedisKeyConstant.WORDINFO+word.getWord(),word);
            });
        }
    }

    // 确保用户收藏集缓存存在
    private void ensureUserSavedCacheExists(Long userId) {
        Boolean cached = redisTemplate.hasKey(RedisKeyConstant.USER_SAVED_WORD_LIST + userId);
        if (!cached) {
            cacheUserSavedWords();
        }
    }

    // 获取或创建单词（原子操作）
    private WordsDO getOrCreateWord(String word) {
        // 优先查缓存
        WordsDO existingWord = (WordsDO) redisTemplate.opsForValue().get(RedisKeyConstant.WORDINFO + word);
        if (existingWord != null) {
            return existingWord;
        }

        // 缓存未命中查数据库
        existingWord = easyEntityQuery.queryable(WordsDO.class)
                .where(o -> o.word().eq(word))
                .firstOrNull();

        if (existingWord != null) {
            redisTemplate.opsForValue().set(RedisKeyConstant.WORDINFO + word, existingWord);
            return existingWord;
        }

        // 需要创建新单词（加单词级锁）
        RLock lock = redissonClient.getLock("wordLock:" + word); // 改为单词粒度锁
        try {
            lock.lock();
            // 双重检查
            existingWord = easyEntityQuery.queryable(WordsDO.class)
                    .where(o -> o.word().eq(word))
                    .firstOrNull();

            if (existingWord == null) {
                WordsDO newWord = new WordsDO();
                newWord.setWord(word);
                easyEntityQuery.insertable(newWord).executeRows(true);
                // 缓存新单词
                redisTemplate.opsForValue().set(RedisKeyConstant.WORDINFO + word, newWord);
                return newWord;
            }
            return existingWord;
        } finally {
            lock.unlock();
        }
    }

    // 添加收藏关系（原子操作）
    private void addSavedWordMapping(Long userId, Long wordId) {
        String userSavedKey = RedisKeyConstant.USER_SAVED_WORD_LIST + userId;

        // 1. 先查缓存
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(userSavedKey, wordId))) {
            throw new CommonException("该单词已经收藏过");
        }

        // 2. 加用户级锁操作
        RLock lock = redissonClient.getLock("userSavedLock:" + userId);
        try {
            lock.lock();
            // 双重检查缓存
            if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(userSavedKey, wordId))) {
                throw new CommonException("该单词已经收藏过");
            }

            // 检查数据库（防止缓存失效）
            boolean existsInDB = easyEntityQuery.queryable(UserSavedWordsDO.class)
                    .where(u -> {
                        u.userId().eq(userId);
                        u.and(()->{
                            u.wordId().eq(wordId);
                        });
                    }).firstOrNull() != null;
            if (existsInDB) {
                // 缓存补偿
                redisTemplate.opsForSet().add(userSavedKey, wordId);
                throw new CommonException("该单词已经收藏过");
            }

            // 创建收藏关系
            UserSavedWordsDO mapping = new UserSavedWordsDO();
            mapping.setUserId(userId);
            mapping.setWordId(wordId);
            easyEntityQuery.insertable(mapping).executeRows();

            // 更新缓存
            redisTemplate.opsForSet().add(userSavedKey, wordId);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<WordsDO> getUserAllCollectedWords(){
        // 1. 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();
        
        // 2. 确保用户收藏集缓存存在
        ensureUserSavedCacheExists(userId);
        
        // 3. 从Redis缓存获取用户收藏的单词ID列表
        String userSavedKey = RedisKeyConstant.USER_SAVED_WORD_LIST + userId;
        List<Long> wordIdList = redisTemplate.opsForSet().members(userSavedKey).stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();
        
        if (wordIdList.isEmpty()) {
            return List.of(); // 返回空列表
        }
        
        // 4. 根据单词ID列表查询单词详情
        List<WordsDO> wordsList = easyEntityQuery.queryable(WordsDO.class)
                .whereByIds(wordIdList)
                .toList();

        // 5. 返回单词列表
        return wordsList;
    }

    @Override
    public EasyPageResult<WordsDO> getUserCollectedWords(CommonPageResultReqDTO dto){
        // 1. 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 2. 确保用户收藏集缓存存在
        ensureUserSavedCacheExists(userId);

        // 3. 从Redis缓存获取用户收藏的单词ID列表
        String userSavedKey = RedisKeyConstant.USER_SAVED_WORD_LIST + userId;
        List<Long> wordIdList = redisTemplate.opsForSet().members(userSavedKey).stream()
                .map(id -> Long.valueOf(id.toString()))
                .toList();


        // 4. 根据单词ID列表查询单词详情
        EasyPageResult<WordsDO> pageResult = easyEntityQuery.queryable(WordsDO.class)
                .whereByIds(wordIdList)
                .toPageResult(dto.getPage(), dto.getSize());


        // 5. 返回单词列表
        return pageResult;
    }


}
