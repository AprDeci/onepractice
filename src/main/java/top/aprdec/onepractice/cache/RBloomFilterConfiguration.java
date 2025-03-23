package top.aprdec.onepractice.cache;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(UserRegisterBloomFilterProperties.class)
public class RBloomFilterConfiguration {

    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationFilter(RedissonClient redissonClient, UserRegisterBloomFilterProperties properties) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter(properties.getName());
        cachePenetrationBloomFilter.tryInit(properties.getExpectedInsertions(),properties.getFalseProbability());
        return cachePenetrationBloomFilter;
    }


}
