package cn.action.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.action.modules.sys.entity.User;

public abstract class BaseService {
	/**
	 * ��־����
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * ���ݷ�Χ����
	 * @param user ��ǰ�û�����ͨ����entity.getCurrentUser()����ȡ
	 * @param officeAlias ���������������á�,�����Ÿ�����
	 * @param userAlias �û������������á�,�����Ÿ��������ݿգ����Դ˲���
	 * @return ��׼������������
	 */
	public static String dataScopeFilter(User user, String officeAlias, String userAlias) {
		return null;
	}
}
