package cn.action.common.web;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.net.HttpHeaders;

import cn.action.common.config.Global;
import cn.action.common.utils.Encodes;
import cn.action.common.utils.StringUtils;

public class Servlets{
// -- ������ֵ���� --//
public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

// ��̬�ļ���׺
private final static String[] staticFiles = StringUtils.split(Global.getConfig("web.staticFile"), ",");

// ��̬ӳ��URL��׺
private final static String urlSuffix = Global.getUrlSuffix();

/**
 * ���ÿͻ��˻������ʱ�� ��Header.
 */
public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
	// Http 1.0 header, set a fix expires date.
	response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
	// Http 1.1 header, set a time after now.
	response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
}

/**
 * ���ý�ֹ�ͻ��˻����Header.
 */
public static void setNoCacheHeader(HttpServletResponse response) {
	// Http 1.0 header
	response.setDateHeader(HttpHeaders.EXPIRES, 1L);
	response.addHeader(HttpHeaders.PRAGMA, "no-cache");
	// Http 1.1 header
	response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
}

/**
 * ����LastModified Header.
 */
public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
	response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
}

/**
 * ����Etag Header.
 */
public static void setEtag(HttpServletResponse response, String etag) {
	response.setHeader(HttpHeaders.ETAG, etag);
}

/**
 * ���������If-Modified-Since Header, �����ļ��Ƿ��ѱ��޸�.
 * 
 * ������޸�, checkIfModify����false ,����304 not modify status.
 * 
 * @param lastModified ���ݵ�����޸�ʱ��.
 */
public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
		long lastModified) {
	long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
	if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
		response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		return false;
	}
	return true;
}

/**
 * ��������� If-None-Match Header, ����Etag�Ƿ�����Ч.
 * 
 * ���Etag��Ч, checkIfNoneMatch����false, ����304 not modify status.
 * 
 * @param etag ���ݵ�ETag.
 */
public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
	String headerValue = request.getHeader(HttpHeaders.IF_NONE_MATCH);
	if (headerValue != null) {
		boolean conditionSatisfied = false;
		if (!"*".equals(headerValue)) {
			StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

			while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
				String currentToken = commaTokenizer.nextToken();
				if (currentToken.trim().equals(etag)) {
					conditionSatisfied = true;
				}
			}
		} else {
			conditionSatisfied = true;
		}

		if (conditionSatisfied) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			response.setHeader(HttpHeaders.ETAG, etag);
			return false;
		}
	}
	return true;
}

/**
 * ������������������ضԻ����Header.
 * 
 * @param fileName ���غ���ļ���.
 */
public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
	try {
		// �����ļ���֧��
		String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedfileName + "\"");
	} catch (UnsupportedEncodingException e) {
		e.getMessage();
	}
}

/**
 * ȡ�ô���ͬǰ׺��Request Parameters, copy from spring WebUtils.
 * 
 * ���صĽ����Parameter����ȥ��ǰ׺.
 */
@SuppressWarnings("rawtypes")
public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
	Validate.notNull(request, "Request must not be null");
	Enumeration paramNames = request.getParameterNames();
	Map<String, Object> params = new TreeMap<String, Object>();
	String pre = prefix;
	if (pre == null) {
		pre = "";
	}
	while (paramNames != null && paramNames.hasMoreElements()) {
		String paramName = (String) paramNames.nextElement();
		if ("".equals(pre) || paramName.startsWith(pre)) {
			String unprefixed = paramName.substring(pre.length());
			String[] values = request.getParameterValues(paramName);
			if (values == null || values.length == 0) {
				values = new String[]{};
				// Do nothing, no values found at all.
			} else if (values.length > 1) {
				params.put(unprefixed, values);
			} else {
				params.put(unprefixed, values[0]);
			}
		}
	}
	return params;
}

/**
 * ���Parameters����Query String��Parameter����,����paramter name�ϼ���prefix.
 * 
 */
public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
	StringBuilder queryStringBuilder = new StringBuilder();

	String pre = prefix;
	if (pre == null) {
		pre = "";
	}
	Iterator<Entry<String, Object>> it = params.entrySet().iterator();
	while (it.hasNext()) {
		Entry<String, Object> entry = it.next();
		queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
		if (it.hasNext()) {
			queryStringBuilder.append("&");
		}
	}
	return queryStringBuilder.toString();
}

/**
 * �ͻ��˶�Http Basic��֤�� Header���б���.
 */
public static String encodeHttpBasic(String userName, String password) {
	String encode = userName + ":" + password;
	return "Basic " + Encodes.encodeBase64(encode.getBytes());
}

/**
 * �Ƿ���Ajax�첽����
 * @param request
 */
//public static boolean isAjaxRequest(HttpServletRequest request){
//	
//	String accept = request.getHeader("accept");
//	String xRequestedWith = request.getHeader("X-Requested-With");
//	Principal principal = UserUtils.getPrincipal();
//
//	// ������첽��������ֻ��ˣ���ֱ�ӷ�����Ϣ
//	return ((accept != null && accept.indexOf("application/json") != -1 
//		|| (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)
//		|| (principal != null && principal.isMobileLogin())));
//}

/**
 * ��ȡ��ǰ�������
 * @return
 */
public static HttpServletRequest getRequest(){
	try{
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}catch(Exception e){
		return null;
	}
}

/**
 * �жϷ���URI�Ƿ��Ǿ�̬�ļ�����
 * @throws Exception 
 */
public static boolean isStaticFile(String uri){
	if (staticFiles == null){
		try {
			throw new Exception("��⵽��app.properties����û�����á�web.staticFile�����ԡ�����ʾ����\n#��̬�ļ���׺\n"
				+"web.staticFile=.css,.js,.png,.jpg,.gif,.jpeg,.bmp,.ico,.swf,.psd,.htc,.crx,.xpi,.exe,.ipa,.apk");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	if ((StringUtils.startsWith(uri, "/static/") || StringUtils.endsWithAny(uri, sfs)) 
//			&& !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
//		return true;
//	}
	if (StringUtils.endsWithAny(uri, staticFiles) && !StringUtils.endsWithAny(uri, urlSuffix)
			&& !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
		return true;
	}
	return false;
}
}
