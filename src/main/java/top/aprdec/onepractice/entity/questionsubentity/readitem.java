package top.aprdec.onepractice.entity.questionsubentity;

import com.alibaba.fastjson2.TypeReference;
import com.easy.query.core.basic.extension.complex.ComplexPropType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
public class readitem implements ComplexPropType, Serializable {
    @Serial
    private static final long serialVersionUID = -803685311585804340L;
    private int id;
        private String content;

    @Override
    public Type complexType() {
        return myType(new TypeReference<List<readitem>>() {
        });
    }
    private <T> Type myType(TypeReference<T> typeReference) {
        return typeReference.getType();
    }
}


