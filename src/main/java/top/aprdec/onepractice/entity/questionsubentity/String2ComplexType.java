package top.aprdec.onepractice.entity.questionsubentity;


import com.alibaba.fastjson2.TypeReference;
import com.easy.query.core.basic.extension.complex.ComplexPropType;

import java.lang.reflect.Type;
import java.util.List;


public class String2ComplexType extends TypeReference<List<String>> implements ComplexPropType {
    @Override
    public Type complexType() {
        return this.getType();
    }
}
