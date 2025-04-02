package top.aprdec.onepractice.entity.questionsubentity;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.alibaba.fastjson2.TypeReference;
import com.easy.query.core.basic.extension.complex.ComplexPropType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Type;

@Data
@EqualsAndHashCode
public class answer implements ComplexPropType, Serializable {
    @Serial
    private static final long serialVersionUID = 4177361457641623628L;

    private Integer index;
    private String answer;

    @Override
    public Type complexType() {
        return myType(new TypeReference<List<answer>>(){});
    }

    private <T> Type myType(TypeReference<T> typeReference) {
        return typeReference.getType();
    }


}
