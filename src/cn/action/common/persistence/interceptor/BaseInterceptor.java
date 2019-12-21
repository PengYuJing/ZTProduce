package cn.action.common.persistence.interceptor;

import java.io.Serializable;
import java.util.Properties;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;

import cn.action.common.config.Global;
import cn.action.common.persistence.Page;
import cn.action.common.persistence.Reflections;
import cn.action.common.persistence.dialect.Dialect;
import cn.action.common.persistence.dialect.db.MySQLDialect;

/**
 * Mybatis��ҳ����������
 */
public abstract class BaseInterceptor implements Interceptor, Serializable {
	
	private static final long serialVersionUID = 1L;

    protected static final String PAGE = "page";
    
    protected static final String DELEGATE = "delegate";

    protected static final String MAPPED_STATEMENT = "mappedStatement";

    protected Log log = LogFactory.getLog(this.getClass());

    protected Dialect DIALECT;

//    /**
//     * ���ص�ID����mapper�е�id������ƥ������
//     */
//    protected String _SQL_PATTERN = "";

    /**
     * �Բ�������ת���ͼ��
     * @param parameterObject ��������
     * @param page            ��ҳ����
     * @return ��ҳ����
     * @throws NoSuchFieldException �޷��ҵ�����
     */
    @SuppressWarnings("unchecked")
	protected static Page<Object> convertParameter(Object parameterObject, Page<Object> page) {
    	try{
            if (parameterObject instanceof Page) {
                return (Page<Object>) parameterObject;
            } else {
                return (Page<Object>)Reflections.getFieldValue(parameterObject, PAGE);
            }
    	}catch (Exception e) {
			return null;
		}
    }

    /**
     * �������ԣ�֧���Զ��巽������ƶ����ݿ�ķ�ʽ
     * <code>dialectClass</code>,�Զ��巽���ࡣ���Բ���������
     * <ode>dbms</ode> ���ݿ����ͣ����֧�ֵ����ݿ�
     * <code>sqlPattern</code> ��Ҫ���ص�SQL ID
     * @param p ����
     */
    protected void initProperties(Properties p) {
    	Dialect dialect = null;
        String dbType = Global.getConfig("jdbc.type");
        if ("db2".equals(dbType)){
        	//dialect = new DB2Dialect();
        }else if("derby".equals(dbType)){
        	//dialect = new DerbyDialect();
        }else if("h2".equals(dbType)){
        	//dialect = new H2Dialect();
        }else if("hsql".equals(dbType)){
        	//dialect = new HSQLDialect();
        }else if("mysql".equals(dbType)){
        	dialect = new MySQLDialect();
        }else if("oracle".equals(dbType)){
        	//dialect = new OracleDialect();
        }else if("postgre".equals(dbType)){
        	//dialect = new PostgreSQLDialect();
        }else if("mssql".equals(dbType) || "sqlserver".equals(dbType)){
        	//dialect = new SQLServer2005Dialect();
        }else if("sybase".equals(dbType)){
        	//dialect = new SybaseDialect();
        }
        if (dialect == null) {
            throw new RuntimeException("mybatis dialect error.");
        }
        DIALECT = dialect;
//        _SQL_PATTERN = p.getProperty("sqlPattern");
//        _SQL_PATTERN = Global.getConfig("mybatis.pagePattern");
//        if (StringUtils.isEmpty(_SQL_PATTERN)) {
//            throw new RuntimeException("sqlPattern property is not found!");
//        }
    }
}
