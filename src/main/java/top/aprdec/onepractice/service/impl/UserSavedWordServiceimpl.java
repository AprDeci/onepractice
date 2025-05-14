package top.aprdec.onepractice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.dto.req.UserSaveWordReqDTO;
import top.aprdec.onepractice.entity.UserSavedWordsDO;
import top.aprdec.onepractice.entity.WordsDO;
import top.aprdec.onepractice.exception.CommonException;
import top.aprdec.onepractice.service.UserSavedWordService;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserSavedWordServiceimpl implements UserSavedWordService {
    private final EasyEntityQuery easyEntityQuery;

    //查询words表 ->无->插入words->插入save表
    //查询save表 ->有->插入save表
    //TODO: 缓存
    @Transactional
    @Override
    public void addSavedWord(UserSaveWordReqDTO dto) {
        String word = dto.getWord();
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

}
