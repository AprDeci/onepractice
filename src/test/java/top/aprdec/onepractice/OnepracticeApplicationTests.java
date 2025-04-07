package top.aprdec.onepractice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import top.aprdec.onepractice.dto.req.RecordReqDTO;
import top.aprdec.onepractice.eenum.UserTypeEnum;
import top.aprdec.onepractice.entity.UserExamRecordDO;
import top.aprdec.onepractice.util.BeanUtil;

@SpringBootTest
class OnepracticeApplicationTests {

    @Test
    void contextLoads() {
        RecordReqDTO dto = new RecordReqDTO();
        dto.setIsfinished(0);
        dto.setType("free");
        dto.setScore(82);
        dto.setAnswers("1,2,3,4,5");
        dto.setPaperId(1);
        dto.setTimespend(123);
        UserExamRecordDO convert = BeanUtil.convert(dto, UserExamRecordDO.class);
        System.out.println(convert);


    }

    @Test
    void testEnum(){
        System.out.println(UserTypeEnum.VIP);
    }

}
