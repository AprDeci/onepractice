package top.aprdec.onepractice.service;

import top.aprdec.onepractice.dto.resp.PaperIntroRespDTO;
import top.aprdec.onepractice.entity.PaperDO;
import top.aprdec.onepractice.entity.QuestionsDO;

import java.util.List;

public interface PaperService {

    List<PaperDO> getAllPapers();

    PaperDO getPaperById(Integer id);

    List<PaperDO> getQuestionsByPaperId(Integer id);

    List<PaperDO> getPapersBytype(String type);

    List<String> getAllTypes();

    PaperIntroRespDTO getPaperIntro(Integer id);
}
