package top.aprdec.onepractice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import top.aprdec.onepractice.dto.resp.ExamQuestionRespDTO;
import top.aprdec.onepractice.entity.QuestionsDO;
import top.aprdec.onepractice.service.QuestionService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootTest
public class ServiceTests {
    @Autowired
    QuestionService questionService;

    @Autowired
    RedisTemplate redisTemplate;


    @Test
    public void questionServiceTest() {
        ExamQuestionRespDTO questionsByPaperIdSplitByPart = questionService.getQuestionsByPaperIdSplitByPart(1);
        System.out.println(questionsByPaperIdSplitByPart);
    }

    @Test
    public void redisMultigetTest(){
        Set<String> set = new HashSet<>();
        set.add("onepractice:record:user:14:881d374f-ca2d-4a7d-9aea-ca7a4bb27071");
        set.add("onepractice:record:user:14:1c414e3d-f793-4562-98e7-232633fe6698");
        log.info(set.toString());
        List result = redisTemplate.opsForValue().multiGet(set);
        System.out.println(result.toString());
    }
}
