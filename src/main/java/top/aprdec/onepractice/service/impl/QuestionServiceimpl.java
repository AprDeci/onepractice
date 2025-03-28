package top.aprdec.onepractice.service.impl;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.aprdec.onepractice.entity.QuestionsDO;
import top.aprdec.onepractice.service.QuestionService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceimpl implements QuestionService {

    private final EasyEntityQuery easyEntityQuery;

    @Override
    public List<QuestionsDO> getQuestionByPaperId(Integer paperId) {
        List<QuestionsDO> questions = easyEntityQuery.queryable(QuestionsDO.class)
                .where(q -> q.paperId().eq(paperId))
                .toList();
        return questions;
    }

    @Override
    public List<QuestionsDO> getQuestionByPaperIdAndType(Integer paperId,String type) {
        List<QuestionsDO> list = easyEntityQuery.queryable(QuestionsDO.class)
                .where(q -> {
                    q.paperId().eq(paperId);
                    q.and(()->q.questionType().eq(type));
                })
                .toList();
        return list;
    }
}
