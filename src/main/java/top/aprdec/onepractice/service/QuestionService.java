package top.aprdec.onepractice.service;

import top.aprdec.onepractice.entity.QuestionsDO;

import java.util.List;

public interface QuestionService {
    List<QuestionsDO> getQuestionByPaperId(Integer paperId);
}
