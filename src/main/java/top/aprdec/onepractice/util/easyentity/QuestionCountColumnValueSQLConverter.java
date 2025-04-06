package top.aprdec.onepractice.util.easyentity;

import com.easy.query.core.api.SQLClientApiFactory;
import com.easy.query.core.basic.api.select.ClientQueryable;
import com.easy.query.core.basic.extension.conversion.ColumnValueSQLConverter;
import com.easy.query.core.basic.extension.conversion.SQLPropertyConverter;
import com.easy.query.core.basic.jdbc.parameter.SQLParameter;
import com.easy.query.core.context.QueryRuntimeContext;
import com.easy.query.core.expression.parser.core.available.TableAvailable;
import com.easy.query.core.expression.parser.core.base.SimpleEntitySQLTableOwner;
import com.easy.query.core.metadata.ColumnMetadata;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.entity.QuestionsDO;

@Component
public class QuestionCountColumnValueSQLConverter implements ColumnValueSQLConverter {

    @Override
    public boolean isRealColumn() {
        return false;
    }

    @Override
    public void selectColumnConvert(TableAvailable tableAvailable, ColumnMetadata columnMetadata, SQLPropertyConverter sqlPropertyConverter, QueryRuntimeContext queryRuntimeContext) {
        SQLClientApiFactory sqlClientApiFactory = queryRuntimeContext.getSQLClientApiFactory();
        ClientQueryable<QuestionsDO> queryable = sqlClientApiFactory.createQueryable(QuestionsDO.class, queryRuntimeContext);
        ClientQueryable<Long> query = queryable.where(q -> q.eq(new SimpleEntitySQLTableOwner<>(tableAvailable), "paperId", "paperId"))
                .select(Long.class, q -> q.columnCount("questionId"));
        sqlPropertyConverter.sqlNativeSegment("{0}",context->{
            context.expression(query);
            context.setAlias(columnMetadata.getName());
        });
    }

    @Override
    public void propertyColumnConvert(TableAvailable table, ColumnMetadata columnMetadata, SQLPropertyConverter sqlPropertyConverter, QueryRuntimeContext runtimeContext) {
        SQLClientApiFactory sqlClientApiFactory = runtimeContext.getSQLClientApiFactory();
        ClientQueryable<QuestionsDO> queryable = sqlClientApiFactory.createQueryable(QuestionsDO.class, runtimeContext);
        ClientQueryable<Long> query = queryable.where(q -> q.eq(new SimpleEntitySQLTableOwner<>(table), "paperId", "paperId"))
                .select(Long.class, s -> s.columnCount("questionId"));
        sqlPropertyConverter.sqlNativeSegment("{0}",context->{
            context.expression(query);
        });
    }

    @Override
    public void valueConvert(TableAvailable tableAvailable, ColumnMetadata columnMetadata, SQLParameter sqlParameter, SQLPropertyConverter sqlPropertyConverter, QueryRuntimeContext queryRuntimeContext, boolean b) {
        sqlPropertyConverter.sqlNativeSegment("{0}",context->{
            context.value(sqlParameter);
        });
    }
}
