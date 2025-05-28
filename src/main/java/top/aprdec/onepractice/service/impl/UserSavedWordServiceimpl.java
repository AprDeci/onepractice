package top.aprdec.onepractice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.dto.req.UserSaveWordReqDTO;
import top.aprdec.onepractice.entity.UserSavedWordsDO;
import top.aprdec.onepractice.entity.WordsDO;
import top.aprdec.onepractice.entity.proxy.UserSavedWordsDOProxy;
import top.aprdec.onepractice.entity.proxy.WordsDOProxy;
import top.aprdec.onepractice.exception.CommonException;
import top.aprdec.onepractice.service.UserSavedWordService;
import top.aprdec.onepractice.util.RedisUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSavedWordServiceimpl implements UserSavedWordService {
    private final EasyEntityQuery easyEntityQuery;
    private final RedisTemplate redisTemplate;
    private final RedisUtil redisUtil;
    private final RedissonClient redissonClient;

    //查询words表 ->无->插入words->插入save表
    //查询save表 ->有->插入save表
    @Transactional
    @Override
    public void addSavedWord(UserSaveWordReqDTO dto) {
        String word = dto.getWord();
        Long userId = StpUtil.getLoginIdAsLong();
        //检查用户收藏集缓存是否存在?
        Boolean cached = redisTemplate.hasKey(RedisKeyConstant.USER_SAVED_WORD_LIST + userId);
        if(!cached){
            cacheUserSavedWords();
        }
        //检查缓存中是否有该单词
        Boolean haswordKey = redisTemplate.hasKey(RedisKeyConstant.WORD + word);
        WordsDO existingWord;
        if(!haswordKey){
            //无word 查看数据库
             existingWord  = easyEntityQuery.queryable(WordsDO.class).where(o -> o.word().eq(word)).firstOrNull();
        }else{
            existingWord = (WordsDO) redisTemplate.opsForValue().get(RedisKeyConstant.WORD + word);
        }
        if (existingWord == null) {
            RLock lock = redissonClient.getLock("wordLock:" + userId);
            try{
                boolean locked = lock.tryLock(0, -1, TimeUnit.SECONDS);
                if(!locked){

                }
                existingWord  = easyEntityQuery.queryable(WordsDO.class).where(o -> o.word().eq(word)).firstOrNull();
                if(existingWord == null){
                    //无word
                    WordsDO wordsDO = new WordsDO();
                    wordsDO.setWord(word);
                    // 创建word
                    long wordid = easyEntityQuery.insertable(wordsDO).executeRows(true);
                    UserSavedWordsDO userSavedWordsDO = new UserSavedWordsDO();
                    userSavedWordsDO.setUserId(userId);
                    userSavedWordsDO.setWordId(wordid);
                    //插入mapping
                    easyEntityQuery.insertable(userSavedWordsDO).executeRows();
                    //插入缓存
                    redisTemplate.opsForSet().add(RedisKeyConstant.USER_SAVED_WORD_LIST+userId,wordid);
                    redisTemplate.opsForValue().set(RedisKeyConstant.WORD + word,wordsDO);
                }else {
                    //有word
                    Long wordid = existingWord.getId();
                    //判断是否已经收藏
                    UserSavedWordsDO userSavedWordsDO = easyEntityQuery.queryable(UserSavedWordsDO.class).where(o -> o.wordId().eq(wordid).and(o.userId().eq(userId))).firstOrNull();
                    if (userSavedWordsDO == null) {
                        //未收藏
                        UserSavedWordsDO userSavedWordsDO1 = new UserSavedWordsDO();
                        userSavedWordsDO1.setUserId(userId);
                        userSavedWordsDO1.setWordId(wordid);
                        //插入mapping
                        easyEntityQuery.insertable(userSavedWordsDO1).executeRows();
                    }
                }
            }catch (Exception e){
                log.error("收藏单词失败:{}",e.getMessage());
            }finally {
                lock.unlock();
            }

        }
        if(existingWord != null){
            //有word
            Long wordid = existingWord.getId();
            //判断是否已经收藏
            Boolean hasexit = redisTemplate.opsForSet().isMember(RedisKeyConstant.USER_SAVED_WORD_LIST+userId,wordid);
            if(hasexit == null) {
                UserSavedWordsDO userSavedWordsDO = new UserSavedWordsDO();
                userSavedWordsDO.setUserId(userId);
                userSavedWordsDO.setWordId(wordid);
                easyEntityQuery.insertable(userSavedWordsDO).executeRows();
                redisTemplate.opsForSet().add(RedisKeyConstant.USER_SAVED_WORD_LIST+userId,wordid);
                redisTemplate.opsForValue().set(RedisKeyConstant.WORD + word,existingWord);
            }else{
                throw new CommonException("该单词已经收藏过");
            }
        }
    }


//    检查用户是否收藏
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
        WordsDO wordsDO;
        wordsDO = (WordsDO) redisTemplate.opsForValue().get(RedisKeyConstant.WORD + word);
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
    public void cacheAllWords(){
        List<WordsDO> wordList = easyEntityQuery.queryable(WordsDO.class)
                .select(w -> new WordsDOProxy()
                        .id().set(w.id())
                        .word().set(w.word()))
                .toList();
        if(!wordList.isEmpty()){
            wordList.forEach(word -> {
                redisTemplate.opsForValue().set(RedisKeyConstant.WORD+word.getWord(),word);
            });
        }
    }



}
