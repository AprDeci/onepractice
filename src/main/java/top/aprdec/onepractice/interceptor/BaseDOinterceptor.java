package top.aprdec.onepractice.interceptor;

import com.easy.query.core.basic.extension.interceptor.EntityInterceptor;
import com.easy.query.core.expression.sql.builder.EntityInsertExpressionBuilder;
import com.easy.query.core.expression.sql.builder.EntityUpdateExpressionBuilder;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.entity.abstractDO.BaseDO;

import java.time.LocalDateTime;


@Component
public class BaseDOinterceptor implements EntityInterceptor  {
    @Override
    public void configureInsert(Class<?> aClass, EntityInsertExpressionBuilder entityInsertExpressionBuilder, Object entity) {
        BaseDO base = (BaseDO) entity;
        if(base.getCreateTime() == null){
            base.setCreateTime(LocalDateTime.now());
        }
        if(base.getUpdateTime() == null){
            base.setUpdateTime(LocalDateTime.now());
        }
    }

    @Override
    public void configureUpdate(Class<?> aClass, EntityUpdateExpressionBuilder entityUpdateExpressionBuilder, Object o) {
        BaseDO base = (BaseDO) o;
        base.setUpdateTime(LocalDateTime.now());
    }

    @Override
    public String name() {
        return "BaseDOInterceptor";
    }

    @Override
    public boolean apply(Class<?> entityClass) {
        return BaseDO.class.isAssignableFrom(entityClass);
    }
}
