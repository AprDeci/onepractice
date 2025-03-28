package top.aprdec.onepractice;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.aprdec.onepractice.entity.QuestionsDO;
import top.aprdec.onepractice.service.QuestionService;

import java.util.List;


@SpringBootTest
public class ServiceTests {
    @Autowired
    QuestionService questionService;

    @Test
    public void questionServiceTest() {
        List<QuestionsDO> listening = questionService.getQuestionByPaperIdAndType(1, "listening");
        System.out.println(listening);
    }
}
