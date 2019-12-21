package cn.action.common.persistence;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.action.common.utils.IdGen;
import cn.action.common.utils.UserUtils;
import cn.action.modules.sys.entity.User;


public abstract class DataEntity<T> extends BaseEntity<T> {
	
	private static final long serialVersionUID = 1L;
	
	protected String remarks;	// ��ע
	protected User createBy;	// ������
	protected Date createDate;	// ��������
	protected User updateBy;	// ������
	protected Date updateDate;	// ��������
	protected String delFlag; 	// ɾ����ǣ�0��������1��ɾ����2����ˣ�
	
	
	public DataEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	public DataEntity(String id) {
		super(id);
	}
	
	/**
	 * ����֮ǰִ�з�������Ҫ�ֶ�����
	 */
	@Override
	public void preInsert(){
		// ������IDΪUUID������setIsNewRecord()ʹ���Զ���ID
		if (!this.isNewRecord){
			setId(IdGen.uuid());
		}
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
	}
	
	/**
	 * ����֮ǰִ�з�������Ҫ�ֶ�����
	 */
	@Override
	public void preUpdate(){
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
		}
		this.updateDate = new Date();
	}
	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@JsonIgnore
	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@JsonIgnore
	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@JsonIgnore
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	
	public Page<T> setPage(Page<T> page) {
		this.page = page;
		return page;
	}
}


