package top.aprdec.onepractice.util.easyentity;


import com.easy.query.core.basic.extension.generated.PrimaryKeyGenerator;
import com.easy.query.core.metadata.ColumnMetadata;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

@Component
public class UUIDPrimaryKeyGenerator implements PrimaryKeyGenerator {
    @Override
    public Serializable getPrimaryKey() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    @Override
    public void setPrimaryKey(Object entity, ColumnMetadata columnMetadata) {
        Serializable primaryKey = getPrimaryKey();
        Object oldValue = columnMetadata.getGetterCaller().apply(entity);
        if(oldValue!=null)
        {
            columnMetadata.getSetterCaller().call(entity, primaryKey);
        }
    }
}
