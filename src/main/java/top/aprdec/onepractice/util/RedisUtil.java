package top.aprdec.onepractice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Long RELEASE_SUCCESS = 1L;
    private static final String RELEASE_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "return redis.call('del', KEYS[1]) " +
            "else " +
            "return 0 " +
            "end";

    public void set(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long time, TimeUnit unit){
        redisTemplate.opsForValue().set(key, value, time, unit);
    }

    public void set(String key, Object value, long time){
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public String getString(String key){
        Object o = redisTemplate.opsForValue().get(key);
        return o == null ? null : o.toString();
    }

    public Boolean del(String key){
        return redisTemplate.delete(key);
    }

    public Boolean haskey(String key){
        return redisTemplate.hasKey(key);
    }

    // 如果不存在，则设置
    public Boolean setNx(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public Boolean setNx(String key, Object value, long time, TimeUnit unit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, unit);
    }

    public Boolean setNx(String key, Object value, long time) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
    }

    public boolean expire(String key, long time, TimeUnit unit) {
        Boolean ret = redisTemplate.expire(key, time, unit);
        return ret != null && ret;
    }
}
