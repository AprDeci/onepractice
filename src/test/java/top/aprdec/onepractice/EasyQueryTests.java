package top.aprdec.onepractice;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.aprdec.onepractice.dto.resp.AnswersRespDTO;
import top.aprdec.onepractice.dto.resp.proxy.AnswersRespDTOProxy;
import top.aprdec.onepractice.eenum.PaperTypeEnum;
import top.aprdec.onepractice.entity.QuestionsDO;
import top.aprdec.onepractice.entity.UserDO;
import top.aprdec.onepractice.entity.proxy.QuestionsDOProxy;
import top.aprdec.onepractice.entity.proxy.UserDOProxy;
import top.aprdec.onepractice.entity.questionsubentity.answer;
import top.aprdec.onepractice.entity.questionsubentity.option;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SpringBootTest
public class EasyQueryTests {
    @Autowired
    EasyEntityQuery easyEntityQuery;
    private EasyEntityQuery entityQuery;

    @Test
    void testselect() {
        QuestionsDO questionsDO = easyEntityQuery.queryable(QuestionsDO.class)
                .where(p -> p.questionId().eq(14))
                .firstOrNull();
        System.out.println(questionsDO);
        List<String> options = questionsDO.getWordBank();
        System.out.println(options);

    }

    @Test
    void testuserregister(){
        UserDO user = new UserDO();
        user.setUsername("test");
        user.setEmail("test@qq.com");
        user.setPassword("123456");
        user.setUsertype(1);
        easyEntityQuery.insertable(user).executeRows();
    }

    @Test
    void testsm(){
        System.out.println(PaperTypeEnum.CET4.getType());
        System.out.println(PaperTypeEnum.CET6.getType());
    }

    @Test
    void testgetanswer(){
        String email = "Luchen126@gmail.com";
        long count = easyEntityQuery.queryable(UserDO.class).where(u -> u.email().eq(email)).count();
        System.out.println(count);
    }
}
