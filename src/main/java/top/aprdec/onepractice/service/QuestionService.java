package top.aprdec.onepractice.service;

import top.aprdec.onepractice.dto.resp.ExamQuestionRespDTO;
import top.aprdec.onepractice.entity.QuestionsDO;

import java.util.List;

public interface QuestionService {
    List<QuestionsDO> getQuestionByPaperId(Integer paperId);

    List<QuestionsDO> getQuestionByPaperIdOrderd(Integer paperId, String type);

    List<QuestionsDO> getQuestionByPaperIdAndType(Integer paperId, String type);

    ExamQuestionRespDTO getQuestionsByPaperIdSplitByPart(Integer paperId);
}
