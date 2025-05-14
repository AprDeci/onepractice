package top.aprdec.onepractice.service;

import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.dto.req.UserSaveWordReqDTO;

public interface UserSavedWordService {
    //查询words表 ->无->插入words->插入save表
    //查询save表 ->有->插入save表
    //TODO: 缓存
    @Transactional
    void addSavedWord(UserSaveWordReqDTO dto);
}
