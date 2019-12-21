package cn.action.common.persistence.interceptor;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import cn.action.common.persistence.Page;
import cn.action.common.persistence.Reflections;

/**
 * Mybatis���ݿ��ҳ���������StatementHandler��prepare����

 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})
})
public class PreparePaginationInterceptor extends BaseInterceptor {
    
    private static final long serialVersionUID = 1L;

    public PreparePaginationInterceptor() {
        super();
    }

    @Override
    public Object intercept(Invocation ivk) throws Throwable {
        if (ivk.getTarget().getClass().isAssignableFrom(RoutingStatementHandler.class)) {
            final RoutingStatementHandler statementHandler = (RoutingStatementHandler) ivk.getTarget();
            final BaseStatementHandler delegate = (BaseStatementHandler) Reflections.getFieldValue(statementHandler, DELEGATE);
            final MappedStatement mappedStatement = (MappedStatement) Reflections.getFieldValue(delegate, MAPPED_STATEMENT);

//            //������Ҫ��ҳ��SQL
////            if (mappedStatement.getId().matches(_SQL_PATTERN)) { 
//            if (StringUtils.indexOfIgnoreCase(mappedStatement.getId(), _SQL_PATTERN) != -1) {
                BoundSql boundSql = delegate.getBoundSql();
                //��ҳSQL<select>��parameterType���Զ�Ӧ��ʵ���������Mapper�ӿ���ִ�з�ҳ�����Ĳ���,�ò�������Ϊ��
                Object parameterObject = boundSql.getParameterObject();
                if (parameterObject == null) {
                    log.error("����δʵ����");
                    throw new NullPointerException("parameterObject��δʵ������");
                } else {
                    final Connection connection = (Connection) ivk.getArgs()[0];
                    final String sql = boundSql.getSql();
                    //��¼ͳ��
                    final int count = SQLHelper.getCount(sql, connection, mappedStatement, parameterObject, boundSql, log);
                    Page<Object> page = null;
                    page = convertParameter(parameterObject, page);
                    page.setCount(count);
                    String pagingSql = SQLHelper.generatePageSql(sql, page, DIALECT);
                    if (log.isDebugEnabled()) {
                        log.debug("PAGE SQL:" + pagingSql);
                    }
                    //����ҳsql��䷴���BoundSql.
                    Reflections.setFieldValue(boundSql, "sql", pagingSql);
                }
                
                if (boundSql.getSql() == null || "".equals(boundSql.getSql())){
                    return null;
                }
                
            }
//        }
        return ivk.proceed();
    }


    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
        initProperties(properties);
    }
}
