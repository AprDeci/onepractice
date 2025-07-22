package top.aprdec.onepractice;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.entity.WordsDO;

@Slf4j
@SpringBootTest
public class RedisTests {
    @Autowired
    private  RedisTemplate redisTemplate;

    @Test
    public void test() {
        Object o = redisTemplate.opsForValue().get("onepractice:words:She");
        WordsDO wordsDO = JSON.parseObject(o.toString(), WordsDO.class);
        log.info("{}", wordsDO);

    }

    @Test
    public void test2() {
        Boolean member = redisTemplate.opsForSet().isMember(RedisKeyConstant.USER_SAVED_WORD_LIST + 14, 2L);
        System.out.println(member);
    }


}
