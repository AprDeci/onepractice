package top.aprdec.onepractice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.dto.req.RecordReqDTO;
import top.aprdec.onepractice.eenum.CodeEnum;
import top.aprdec.onepractice.eenum.UserTypeEnum;
import top.aprdec.onepractice.entity.UserExamRecordDO;
import top.aprdec.onepractice.util.BeanUtil;

@SpringBootTest
class OnepracticeApplicationTests {
    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Test
    void contextLoads() {
        String pattern = RedisKeyConstant.PAPER_VOTE_KEY+"*";
//        scan获取所有数据
        try( Cursor cursor = redisTemplate.scan(ScanOptions.scanOptions().match(pattern).count(100).build())){
            while (cursor.hasNext()){
                String key = cursor.next().toString();
                String id = key.split(":")[3];

            }
        }

    }

    @Test
    void testEnum(){
        System.out.println(CodeEnum.PASSWORD_ERROR.getCode());
    }

}
