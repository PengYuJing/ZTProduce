package cn.action.common.config;

import java.util.Map;

import com.google.common.collect.Maps;

import cn.action.common.utils.PropertiesLoader;
import cn.action.common.utils.StringUtils;

public class Global {
	
	private Global() {}

	/**
	 * ��ǰ����ʵ��
	 */
	private static Global global = null;

	/**
	 * ��̬�������� ��ȡ��ǰ����ʵ�� ���̰߳�ȫ����ģʽ(ʹ��˫��ͬ����)
	 */

	public static synchronized Global getInstance() {

		if (global == null) {
			synchronized (Global.class) {
				if (global == null)
					global = new Global();
			}
		}
		return global;
	}

	/**
	 * ����ȫ������ֵ
	 */
	private static Map<String, String> map = Maps.newHashMap();

	/**
	 * �����ļ����ض���
	 */
	private static PropertiesLoader loader = new PropertiesLoader("action.properties");

	/**
	 * ��ʾ/����
	 */
	public static final String SHOW = "1";
	public static final String HIDE = "0";

	/**
	 * ��/��
	 */
	public static final String YES = "1";
	public static final String NO = "0";

	/**
	 * ��/��
	 */
	public static final String TRUE = "true";
	public static final String FALSE = "false";

	/**
	 * �ϴ��ļ���������·��
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";

	/**
	 * ��ȡ����
	 * 
	 * @see ${fns:getConfig('adminPath')}
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null) {
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}

	/**
	 * ��ȡ����˸�·��
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}

	/**
	 * ��ȡǰ�˸�·��
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}

	/**
	 * ��ȡURL��׺
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}

	/**
	 * �Ƿ�����ʾģʽ����ʾģʽ�²����޸��û�����ɫ�����롢�˵�����Ȩ
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}

	
	/**
	 * ҳ���ȡ����
	 * 
	 * @see ${fns:getConst('YES')}
	 */
	public static Object getConst(String field) {
		try {
			return Global.class.getField(field).get(null);
		} catch (Exception e) {
			// �쳣���������ã�����ʲôҲ����
		}
		return null;
	}
}
