package cn.action.common.persistence;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;

import cn.action.common.config.Global;
import cn.action.common.utils.StringUtils;
import cn.action.common.utils.UserUtils;
import cn.action.modules.sys.entity.User;



public abstract class BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ʵ���ţ�Ψһ��ʶ��
	 */
	protected String id;
	
	/**
	 * ��ǰ�û�
	 */
	protected User currentUser;
	
	/**
	 * ��ǰʵ���ҳ����
	 */
	protected Page<T> page;
	
	/**
	 * �Զ���SQL��SQL��ʶ��SQL���ݣ�
	 */
	protected Map<String, String> sqlMap;
	
	/**
	 * �Ƿ����¼�¼��Ĭ�ϣ�false��������setIsNewRecord()�����¼�¼��ʹ���Զ���ID��
	 * ����Ϊtrue��ǿ��ִ�в�����䣬ID�����Զ����ɣ�����ֶ����롣
	 */
	protected boolean isNewRecord = false;

	public BaseEntity() {
		
	}
	
	public BaseEntity(String id) {
		this();
		this.id = id;
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@JsonIgnore
	@XmlTransient
	public User getCurrentUser() {
		if(currentUser == null){
			currentUser = UserUtils.getUser();
		}
		return currentUser;
	}
	
	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	@JsonIgnore
	@XmlTransient
	public Page<T> getPage() {
		if (page == null){
			page = new Page<T>();
		}
		return page;
	}
	
	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}
	
	/**
	 * ����֮ǰִ�з���������ʵ��
	 */
	public abstract void preInsert();
	
	/**
	 * ����֮ǰִ�з���������ʵ��
	 */
	public abstract void preUpdate();
	
    /**
	 * �Ƿ����¼�¼��Ĭ�ϣ�false��������setIsNewRecord()�����¼�¼��ʹ���Զ���ID��
	 * ����Ϊtrue��ǿ��ִ�в�����䣬ID�����Զ����ɣ�����ֶ����롣
     * @return
     */
	public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isBlank(getId());
    }

	/**
	 * �Ƿ����¼�¼��Ĭ�ϣ�false��������setIsNewRecord()�����¼�¼��ʹ���Զ���ID��
	 * ����Ϊtrue��ǿ��ִ�в�����䣬ID�����Զ����ɣ�����ֶ����롣
	 */
	public void setIsNewRecord(boolean isNewRecord) {
		this.isNewRecord = isNewRecord;
	}

	/**
	 * ȫ�ֱ�������
	 */
	@JsonIgnore
	public Global getGlobal() {
		return Global.getInstance();
	}
	
	/**
	 * ��ȡ���ݿ�����
	 */
	@JsonIgnore
	public String getDbName(){
		return Global.getConfig("jdbc.type");
	}
	
    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!getClass().equals(obj.getClass())) {
            return false;
        }
        BaseEntity<?> that = (BaseEntity<?>) obj;
        return null == this.getId() ? false : this.getId().equals(that.getId());
    }
    
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
    
	/**
	 * ɾ����ǣ�0��������1��ɾ����2����ˣ���
	 */
	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	public static final String DEL_FLAG_AUDIT = "2";

}
