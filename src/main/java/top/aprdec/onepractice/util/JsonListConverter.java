package top.aprdec.onepractice.util;

import com.alibaba.druid.support.json.JSONUtils;
import com.easy.query.core.basic.extension.conversion.ValueConverter;
import com.easy.query.core.metadata.ColumnMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class JsonListConverter implements ValueConverter<Object, String> {
    @Override
    public String serialize(Object o, ColumnMetadata columnMetadata) {
        if (o == null) {
            return "[]";
        }
        return JSONUtils.toJSONString(o);
    }

    @Override
    public Object deserialize(String s, ColumnMetadata columnMetadata) {
        if (StringUtils.isBlank(s)) {
            return new ArrayList<>();
        }
        return null;
    }
}