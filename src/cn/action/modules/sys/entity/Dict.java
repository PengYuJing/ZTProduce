package cn.action.modules.sys.entity;

import javax.validation.constraints.NotNull;

import cn.action.common.persistence.DataEntity;

public class Dict extends DataEntity<Dict> {

	private static final long serialVersionUID = 1L;
	private String value;	// ����ֵ
	private String label;	// ��ǩ��
	private String type;	// ����
	private String description;// ����
	private Integer sort;	// ����
	private String parentId;//��Id

	public Dict() {
		super();
	}
	
	public Dict(String id){
		super(id);
	}
	
	public Dict(String value, String label){
		this.value = value;
		this.label = label;
	}
	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Override
	public String toString() {
		return label;
	}
}
