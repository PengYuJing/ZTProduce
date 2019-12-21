package cn.action.modules.sys.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import cn.action.common.persistence.TreeEntity;

public class Office extends TreeEntity<Office> {

	private static final long serialVersionUID = 1L;
//	private Office parent;	// �������
	private String parentIds; // ���и������
	private Area area;		// ��������
	private String code; 	// ��������
//	private String name; 	// ��������
//	private Integer sort;		// ����
	private String type; 	// �������ͣ�1����˾��2�����ţ�3��С�飩
	private String grade; 	// �����ȼ���1��һ����2��������3��������4���ļ���
	private String address; // ��ϵ��ַ
	private String zipCode; // ��������
	private String master; 	// ������
	private String phone; 	// �绰
	private String fax; 	// ����
	private String email; 	// ����
	private String useable;//�Ƿ����
	private User primaryPerson;//��������
	private User deputyPerson;//��������
	private List<String> childDeptList;//��������Ӳ���
	
	public Office(){
		super();
//		this.sort = 30;
		this.type = "2";
	}

	public Office(String id){
		super(id);
	}
	
	public List<String> getChildDeptList() {
		return childDeptList;
	}

	public void setChildDeptList(List<String> childDeptList) {
		this.childDeptList = childDeptList;
	}

	public String getUseable() {
		return useable;
	}

	public void setUseable(String useable) {
		this.useable = useable;
	}

	public User getPrimaryPerson() {
		return primaryPerson;
	}

	public void setPrimaryPerson(User primaryPerson) {
		this.primaryPerson = primaryPerson;
	}

	public User getDeputyPerson() {
		return deputyPerson;
	}

	public void setDeputyPerson(User deputyPerson) {
		this.deputyPerson = deputyPerson;
	}


	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}


	@NotNull
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	
}
