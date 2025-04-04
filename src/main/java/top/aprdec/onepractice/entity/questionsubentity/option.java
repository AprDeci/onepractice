package top.aprdec.onepractice.entity.questionsubentity;

import com.alibaba.fastjson2.TypeReference;
import com.easy.query.core.basic.extension.complex.ComplexPropType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.lang.reflect.Type;

@Data
@EqualsAndHashCode
public class option implements ComplexPropType, Serializable {
    @Serial
    private static final long serialVersionUID = 195661275471910389L;
    private String label;
    private String content;

    @Override
    public Type complexType() {
        return myType(new TypeReference<List<option>>() {
        });
    }
    private <T> Type myType(TypeReference<T> typeReference) {
        return typeReference.getType();
    }
}
