package cn.action.modules.sys.dao;

import java.util.List;

import cn.action.common.persistence.CrudDao;
import cn.action.modules.sys.entity.User;

public interface UserDao  extends CrudDao<User> {
	
	public User findByLoginNameAndPwd(User user);
	
	/**
	 * ���ݵ�¼���Ʋ�ѯ�û�
	 * @param loginName
	 * @return
	 */
	public User getByLoginName(User user);

	/**
	 * ͨ��OfficeId��ȡ�û��б��������û�id��name������ѯ�û�ʱ�ã�
	 * @param user
	 * @return
	 */
	public List<User> findUserByOfficeId(User user);
	
	/**
	 * ��ѯȫ���û���Ŀ
	 * @return
	 */
	public long findAllCount(User user);
	
	/**
	 * �����û�����
	 * @param user
	 * @return
	 */
	public int updatePasswordById(User user);
	
	/**
	 * ���µ�¼��Ϣ���磺��¼IP����¼ʱ��
	 * @param user
	 * @return
	 */
	public int updateLoginInfo(User user);

	/**
	 * ɾ���û���ɫ��������
	 * @param user
	 * @return
	 */
	public int deleteUserRole(User user);
	
	/**
	 * �����û���ɫ��������
	 * @param user
	 * @return
	 */
	public int insertUserRole(User user);
	
	/**
	 * �����û���Ϣ
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user);

}