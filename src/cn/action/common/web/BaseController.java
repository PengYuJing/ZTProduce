package cn.action.common.web;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cn.action.common.mapper.JsonMapper;

public abstract class BaseController {

	/**
	 * ��־����
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * �������·��
	 */
	@Value("${adminPath}")//���ע���ȡ���������ļ�action.properties�е�����
	protected String adminPath;
	
	/**
	 * ǰ�˻���·��
	 */
	@Value("${frontPath}")
	protected String frontPath;
	
	/**
	 * ǰ��URL��׺
	 */
	@Value("${urlSuffix}")
	protected String urlSuffix;
	
	/**
	 * ��֤Beanʵ������
	 */
	@Autowired
	protected Validator validator;

	
	
	/**
	 * ���Model��Ϣ
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	
	/**
	 * ���Flash��Ϣ
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * �ͻ��˷���JSON�ַ���
	 * @param response
	 * @param object
	 * @return
	 */
	protected String renderString(HttpServletResponse response, Object object) {
		return renderString(response, JsonMapper.toJsonString(object), "application/json");
	}
	
	/**
	 * �ͻ��˷����ַ���
	 * @param response
	 * @param string
	 * @return
	 */
	protected String renderString(HttpServletResponse response, String string, String type) {
		try {
			response.reset();
	        response.setContentType(type);
	        response.setCharacterEncoding("utf-8");
			response.getWriter().print(string);
			return null;
		} catch (IOException e) {
			return null;
		}
	}



	
	/**
	 * ��ʼ�����ݰ�
	 * 1. �����д��ݽ�����String����HTML���룬��ֹXSS����
	 * 2. ���ֶ���Date����ת��ΪString����
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String����ת���������д��ݽ�����String����HTML���룬��ֹXSS����
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date ����ת��
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				try {
					setValue(DateUtils.parseDate(text));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}