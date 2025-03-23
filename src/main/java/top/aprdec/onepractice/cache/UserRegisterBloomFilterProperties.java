package top.aprdec.onepractice.cache;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = UserRegisterBloomFilterProperties.PREFIX)
public final class UserRegisterBloomFilterProperties {

    public static final String PREFIX = "cache.redis.bloom-filter.user-register";

    /**
     * 用户注册布隆过滤器实例名称
     */
    private String name = "user_register_cache_penetration_bloom_filter";

    /**
     * 每个元素的预期插入量
     */
    private Long expectedInsertions = 64L;

    /**
     * 预期错误概率
     */
    private Double falseProbability = 0.03D;
}
