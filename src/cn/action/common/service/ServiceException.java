package cn.action.common.service;

/**
 * Service�㹫�õ�Exception, ����Spring��������ĺ������׳�ʱ�ᴥ������ع�.
 * @author ThinkGem
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
