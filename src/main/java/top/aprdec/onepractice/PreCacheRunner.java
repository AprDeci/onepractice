package top.aprdec.onepractice;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.entity.WordsDO;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public  class PreCacheRunner implements CommandLineRunner {
    private final EasyEntityQuery easyEntityQuery;
    private final RedisTemplate redisTemplate;
    @Override
    public void run(String... args) throws Exception {
        log.info("PreCacheRunner: 开始缓存单词映射表");
        cacheWordIdMapping();

    }

    @Async
    protected void cacheWordIdMapping() {
        List<WordsDO> words = easyEntityQuery.queryable(WordsDO.class).select(w -> w.FETCHER.id().word()).toList();
        for(WordsDO word : words){
            redisTemplate.opsForValue().set(RedisKeyConstant.WORDIDMAPPING + word.getId(), word.getWord());
        }
    }
}
