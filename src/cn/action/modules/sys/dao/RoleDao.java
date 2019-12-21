package cn.action.modules.sys.dao;

import cn.action.common.persistence.CrudDao;
import cn.action.modules.sys.entity.Role;

public interface RoleDao extends CrudDao<Role> {

	public Role getByName(Role role);
	
	public Role getByEnname(Role role);

	/**
	 * ά����ɫ��˵�Ȩ�޹�ϵ
	 * @param role
	 * @return
	 */
	public int deleteRoleMenu(Role role);

	public int insertRoleMenu(Role role);
	
	/**
	 * ά����ɫ�빫˾���Ź�ϵ
	 * @param role
	 * @return
	 */
	public int deleteRoleOffice(Role role);

	public int insertRoleOffice(Role role);

}
