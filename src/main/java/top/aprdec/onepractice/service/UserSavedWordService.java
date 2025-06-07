package top.aprdec.onepractice.service;

import com.easy.query.core.api.pagination.EasyPageResult;
import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.dto.req.CommonPageResultReqDTO;
import top.aprdec.onepractice.dto.req.UserSaveWordReqDTO;
import top.aprdec.onepractice.entity.WordsDO;

import java.util.List;

public interface UserSavedWordService {
    //查询words表 ->无->插入words->插入save表
    //查询save表 ->有->插入save表
    //TODO: 缓存
    @Transactional
    void addSavedWord(UserSaveWordReqDTO dto);

    //    检查用户是否收藏
    Boolean hascollected(UserSaveWordReqDTO dto);


    List<WordsDO> getUserAllCollectedWords();

    EasyPageResult<WordsDO> getUserCollectedWords(CommonPageResultReqDTO dto);
}
