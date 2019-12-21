package cn.action.modules.sys.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.action.common.web.BaseController;

/**
 * ��ǩController
 */
@Controller
@RequestMapping(value = "${adminPath}/tag")
public class TagController extends BaseController {
	
	/**
	 * ���ṹѡ���ǩ��treeselect.tag��
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getParameter("url")); 	// ���ṹ����URL
		model.addAttribute("extId", request.getParameter("extId")); // �ų��ı��ID
		model.addAttribute("checked", request.getParameter("checked")); // �Ƿ�ɸ�ѡ
		model.addAttribute("selectIds", request.getParameter("selectIds")); // ָ��Ĭ��ѡ�е�ID
		model.addAttribute("isAll", request.getParameter("isAll")); 	// �Ƿ��ȡȫ�����ݣ�������Ȩ�޹���
		model.addAttribute("module", request.getParameter("module"));	// ������Ŀģ�ͣ������CMS��Category����
		return "modules/sys/tagTreeselect";
	}
	
	/**
	 * ͼ��ѡ���ǩ��iconselect.tag��
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "iconselect")
	public String iconselect(HttpServletRequest request, Model model) {
		model.addAttribute("value", request.getParameter("value"));
		return "modules/sys/tagIconselect";
	}
	
}
