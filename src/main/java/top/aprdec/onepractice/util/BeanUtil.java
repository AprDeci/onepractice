package top.aprdec.onepractice.util;


import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtil {
    protected static Mapper BEAN_MAPPER_BUILDER;

    static{
        BEAN_MAPPER_BUILDER = DozerBeanMapperBuilder.buildDefault();
    }

    public static <T,S> T convert(S source,T target) {
        Optional.ofNullable(source)
                .ifPresent(s -> BEAN_MAPPER_BUILDER.map(source, target));
        return target;
    }

    public static <T, S> T convert(S source, Class<T> clazz) {
        return Optional.ofNullable(source)
                .map(each -> BEAN_MAPPER_BUILDER.map(each, clazz))
                .orElse(null);
    }
}
