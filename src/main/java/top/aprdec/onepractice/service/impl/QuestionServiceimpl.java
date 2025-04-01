package top.aprdec.onepractice.service.impl;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import top.aprdec.onepractice.dto.resp.ExamQuestionRespDTO;
import top.aprdec.onepractice.dto.resp.subentity.QuestionPart;
import top.aprdec.onepractice.entity.QuestionsDO;
import top.aprdec.onepractice.service.QuestionService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "onepractice:question:")
public class QuestionServiceimpl implements QuestionService {
    final String  DESC = "desc";
    final String ASC = "asc";

    private final EasyEntityQuery easyEntityQuery;

    @Override
    public List<QuestionsDO> getQuestionByPaperId(Integer paperId) {
        List<QuestionsDO> questions = easyEntityQuery.queryable(QuestionsDO.class)
                .where(q -> q.paperId().eq(paperId))
                .toList();
        return questions;
    }

    @Override
    public List<QuestionsDO> getQuestionByPaperIdOrderd(Integer paperId, String type) {
        List<QuestionsDO> questions = new ArrayList<>();
        if(type.equals(DESC)) {
            questions = easyEntityQuery.queryable(QuestionsDO.class)
                    .where(q -> q.paperId().eq(paperId)).orderBy(q -> q.partName().desc())
                    .toList();
        }else{
            questions = easyEntityQuery.queryable(QuestionsDO.class)
                    .where(q -> q.paperId().eq(paperId)).orderBy(q -> q.partName().asc())
                    .toList();
        }
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

    @Override
    public ExamQuestionRespDTO getQuestionsByPaperIdSplitByPart(Integer paperId) {
        List<QuestionsDO> questions = getQuestionByPaperIdOrderd(paperId,ASC);

        //按 partName 分组，并保留顺序
        Map<String, List<QuestionsDO>> questionsByPart = questions.stream()
                .collect(Collectors.groupingBy(
                        QuestionsDO::getPartName,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        // 转换为 QuestionPart 数组（顺序与数据库一致）
        QuestionPart[] questionParts = questionsByPart.entrySet().stream()
                .map(entry -> {
                    QuestionPart part = new QuestionPart();
                    part.setQuestions(entry.getValue().toArray(new QuestionsDO[0]));
                    return part;
                })
                .toArray(QuestionPart[]::new);

        ExamQuestionRespDTO examQuestionRespDTO = new ExamQuestionRespDTO();
        examQuestionRespDTO.setPaperId(paperId);
        examQuestionRespDTO.setQuestionParts(questionParts);
        return examQuestionRespDTO;
    }
}
