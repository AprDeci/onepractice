package top.aprdec.onepractice.service;

import com.easy.query.core.api.pagination.EasyPageResult;
import top.aprdec.onepractice.dto.req.PaperqueryDTO;
import top.aprdec.onepractice.dto.resp.PaperIntroRespDTO;
import top.aprdec.onepractice.dto.resp.PaperdataRespDTO;
import top.aprdec.onepractice.entity.PaperDO;
import top.aprdec.onepractice.entity.QuestionsDO;

import java.util.List;

public interface PaperService {

    List<PaperDO> getAllPapers();

    PaperdataRespDTO getPapersByPageAndSize(Integer page, Integer size);


    EasyPageResult<PaperDO> getPaperswithQuerysByPageAndSize(PaperqueryDTO querys);

    PaperDO getPaperById(Integer id);

    List<PaperDO> getQuestionsByPaperId(Integer id);

    List<PaperDO> getPapersBytype(String type);

    List<String> getAllTypes();

    PaperIntroRespDTO getPaperIntro(Integer id);
}
