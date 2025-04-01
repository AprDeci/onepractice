package top.aprdec.onepractice;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.aprdec.onepractice.dto.resp.ExamQuestionRespDTO;
import top.aprdec.onepractice.entity.QuestionsDO;
import top.aprdec.onepractice.service.QuestionService;

import java.util.List;


@SpringBootTest
public class ServiceTests {
    @Autowired
    QuestionService questionService;



    @Test
    public void questionServiceTest() {
        ExamQuestionRespDTO questionsByPaperIdSplitByPart = questionService.getQuestionsByPaperIdSplitByPart(1);
        System.out.println(questionsByPaperIdSplitByPart);
    }
}
