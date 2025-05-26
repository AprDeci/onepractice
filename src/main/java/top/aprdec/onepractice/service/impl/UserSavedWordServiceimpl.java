package top.aprdec.onepractice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.dto.req.UserSaveWordReqDTO;
import top.aprdec.onepractice.entity.UserSavedWordsDO;
import top.aprdec.onepractice.entity.WordsDO;
import top.aprdec.onepractice.entity.proxy.UserSavedWordsDOProxy;
import top.aprdec.onepractice.exception.CommonException;
import top.aprdec.onepractice.service.UserSavedWordService;
import top.aprdec.onepractice.util.RedisUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSavedWordServiceimpl implements UserSavedWordService {
    private final EasyEntityQuery easyEntityQuery;
    private final RedisTemplate redisTemplate;
    private final RedisUtil redisUtil;

    //查询words表 ->无->插入words->插入save表
    //查询save表 ->有->插入save表
    //TODO: 缓存
    @Transactional
    @Override
    public void addSavedWord(UserSaveWordReqDTO dto) {
        String word = dto.getWord();
        log.info("word:{}",word);
        Long userId = StpUtil.getLoginIdAsLong();
        WordsDO existingWord  = easyEntityQuery.queryable(WordsDO.class).where(o -> o.word().eq(word)).firstOrNull();
        if (existingWord  == null) {
            //无word
            WordsDO wordsDO = new WordsDO();
            wordsDO.setWord(word);
            long wordid = easyEntityQuery.insertable(wordsDO).executeRows(true);
            UserSavedWordsDO userSavedWordsDO = new UserSavedWordsDO();
            userSavedWordsDO.setUserId(userId);
            userSavedWordsDO.setWordId(wordid);
            //插入mapping
            easyEntityQuery.insertable(userSavedWordsDO).executeRows();
        }
        if(existingWord  != null){
            //有word
            Long wordid = existingWord .getId();
            //判断是否已经收藏
            UserSavedWordsDO hasexit = easyEntityQuery.queryable(UserSavedWordsDO.class).where(o -> {
                o.userId().eq(userId);
                o.and(() -> o.wordId().eq(wordid));
            }).firstOrNull();
            if(hasexit == null) {
                UserSavedWordsDO userSavedWordsDO = new UserSavedWordsDO();
                userSavedWordsDO.setUserId(userId);
                userSavedWordsDO.setWordId(wordid);
                long l = easyEntityQuery.insertable(userSavedWordsDO).executeRows();
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
//       //查询word表是否有word 若无一定无
        WordsDO existingword = easyEntityQuery.queryable(WordsDO.class).where(o -> o.word().eq(word)).firstOrNull();
        if(existingword == null){
            return false;
        }
        UserSavedWordsDO saveword = easyEntityQuery.queryable(UserSavedWordsDO.class).where(o -> {
            o.userId().eq(userId);
            o.and(() -> {
                o.wordId().eq(existingword.getId());
            });
        }).firstOrNull();
        return saveword!=null;
    }

//    用户登录缓存收藏表到Redis
    public void cacheUserSavedWords(){
        Long userId = StpUtil.getLoginIdAsLong();
        List<UserSavedWordsDO> usersavewordList = easyEntityQuery.queryable(UserSavedWordsDO.class)
                .select(u -> new UserSavedWordsDOProxy()
                        .wordId().set(u.wordId())
                        .userId().set(u.userId()))
                .where(u -> u.userId().eq(14L))
                .toList();
        if(!usersavewordList.isEmpty()){
            List<Long> wordIdList = usersavewordList.stream().map(UserSavedWordsDO::getWordId).toList();
            //过期时间和token时间一致
            redisTemplate.opsForSet().add(RedisKeyConstant.USER_SAVED_WORD_LIST+userId, wordIdList.toArray());
        }
    }



}
