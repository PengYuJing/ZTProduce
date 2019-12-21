package cn.action.common.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;

import cn.action.common.utils.StringUtils;

public abstract class TreeEntity <T> extends DataEntity<T> {

	private static final long serialVersionUID = 1L;

	protected T parent;	// �������
	protected String parentIds; // ���и������
	protected String name; 	// ��������
	protected Integer sort;		// ����
	
	public TreeEntity() {
		super();
		this.sort = 30;
	}
	
	public TreeEntity(String id) {
		super(id);
	}
	
	/**
	 * ������ֻ��ͨ������ʵ�֣�����ʵ��mybatis�޷���ȡ
	 * @return
	 */
	@JsonBackReference
	public abstract T getParent();

	/**
	 * ������ֻ��ͨ������ʵ�֣�����ʵ��mybatis�޷���ȡ
	 * @return
	 */
	public abstract void setParent(T parent);


	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getParentId() {
		String id = null;
		if (parent != null){
			id = (String)Reflections.getFieldValue(parent, "id");
		}
		return StringUtils.isNotBlank(id) ? id : "0";
	}
	
}