package cn.action.modules.sys.entity;

import org.hibernate.validator.constraints.Length;

import cn.action.common.persistence.TreeEntity;

/**
 * ����
 * @author Administrator
 *
 */
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;
//	private Area parent;	// �������
//	private String parentIds; // ���и������
	private String code; 	// �������
//	private String name; 	// ��������
//	private Integer sort;		// ����
	private String type; 	// �������ͣ�1�����ң�2��ʡ�ݡ�ֱϽ�У�3�����У�4�����أ�
	
	public Area(){
		super();
		this.sort = 30;
	}

	public Area(String id){
		super(id);
	}
	

	public Area getParent() {
		return parent;
	}

	public void setParent(Area parent) {
		this.parent = parent;
	}


	@Length(min=1, max=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
//
//	public String getParentId() {
//		return parent != null && parent.getId() != null ? parent.getId() : "0";
//	}
	
	@Override
	public String toString() {
		return name;
	}
}