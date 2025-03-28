package top.aprdec.onepractice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.aprdec.onepractice.eenum.UserTypeEnum;

@SpringBootTest
class OnepracticeApplicationTests {

    @Test
    void contextLoads() {
        String userTypeByCode = UserTypeEnum.getUserTypeByCode(1);
        System.out.println(userTypeByCode);
    }

}
