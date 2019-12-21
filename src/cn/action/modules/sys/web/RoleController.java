package cn.action.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.action.common.config.Global;
import cn.action.common.persistence.Page;
import cn.action.common.utils.Collections3;
import cn.action.common.utils.StringUtils;
import cn.action.common.utils.UserUtils;
import cn.action.common.web.BaseController;
import cn.action.modules.sys.entity.Office;
import cn.action.modules.sys.entity.Role;
import cn.action.modules.sys.entity.User;
import cn.action.modules.sys.service.OfficeService;
import cn.action.modules.sys.service.SystemService;

@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getRole(id);
		}else{
			return new Role();
		}
	}
	

	@RequestMapping(value = {"list", ""})
	public String list(Role role, Model model) {
		List<Role> list = systemService.findAllRole();
		model.addAttribute("list", list);
		return "modules/sys/roleList";
	}


	@RequestMapping(value = "form")
	public String form(Role role, Model model) {
		if (role.getOffice()==null){
			role.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("role", role);
		model.addAttribute("menuList", systemService.findAllMenu());
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/roleForm";
	}
	

	@RequestMapping(value = "save")
	public String save(Role role, Model model, RedirectAttributes redirectAttributes) {
		if(!UserUtils.getUser().isAdmin()&&role.getSysData().equals(Global.YES)){
			addMessage(redirectAttributes, "ԽȨ������ֻ�г�������Ա�����޸Ĵ����ݣ�");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "��ʾģʽ�������������");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}

		if (!"true".equals(checkName(role.getOldName(), role.getName()))){
			addMessage(model, "�����ɫ'" + role.getName() + "'ʧ��, ��ɫ���Ѵ���");
			return form(role, model);
		}
		if (!"true".equals(checkEnname(role.getOldEnname(), role.getEnname()))){
			addMessage(model, "�����ɫ'" + role.getName() + "'ʧ��, Ӣ�����Ѵ���");
			return form(role, model);
		}
		systemService.saveRole(role);
		addMessage(redirectAttributes, "�����ɫ'" + role.getName() + "'�ɹ�");
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	

	@RequestMapping(value = "delete")
	public String delete(Role role, RedirectAttributes redirectAttributes) {
		if(!UserUtils.getUser().isAdmin() && role.getSysData().equals(Global.YES)){
			addMessage(redirectAttributes, "ԽȨ������ֻ�г�������Ա�����޸Ĵ����ݣ�");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "��ʾģʽ�������������");
			return "redirect:" + adminPath + "/sys/role/?repage";
		}
//		if (Role.isAdmin(id)){
//			addMessage(redirectAttributes, "ɾ����ɫʧ��, ���������ý�ɫ���ſ�");
////		}else if (UserUtils.getUser().getRoleIdList().contains(id)){
////			addMessage(redirectAttributes, "ɾ����ɫʧ��, ����ɾ����ǰ�û����ڽ�ɫ");
//		}else{
			systemService.deleteRole(role);
			addMessage(redirectAttributes, "ɾ����ɫ�ɹ�");
//		}
		return "redirect:" + adminPath + "/sys/role/?repage";
	}
	
	/**
	 * ��ɫ����ҳ��
	 * @param role
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
		List<User> userList = systemService.findUser(new User(new Role(role.getId())));
		model.addAttribute("userList", userList);
		return "modules/sys/roleAssign";
	}
	
	/**
	 * ��ɫ���� -- �򿪽�ɫ����Ի���
	 * @param role
	 * @param model
	 * @return
	 */

	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		List<User> userList = systemService.findUser(new User(new Role(role.getId())));
		model.addAttribute("role", role);
		model.addAttribute("userList", userList);
		model.addAttribute("selectIds", Collections3.extractToString(userList, "name", ","));
		model.addAttribute("officeList", officeService.findAll());
		return "modules/sys/selectUserToRole";
	}
	
	/**
	 * ��ɫ���� -- ���ݲ��ű�Ż�ȡ�û��б�
	 * @param officeId
	 * @param response
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		User user = new User();
		user.setOffice(new Office(officeId));
		Page<User> page = systemService.findUser(new Page<User>(1, -1), user);
		for (User e : page.getList()) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getName());
			mapList.add(map);			
		}
		return mapList;
	}
	
	/**
	 * ��ɫ���� -- �ӽ�ɫ���Ƴ��û�
	 * @param userId
	 * @param roleId
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "outrole")
	public String outrole(String userId, String roleId, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "��ʾģʽ�������������");
			return "redirect:" + adminPath + "/sys/role/assign?id="+roleId;
		}
		Role role = systemService.getRole(roleId);
		User user = systemService.getUser(userId);
		if (UserUtils.getUser().getId().equals(userId)) {
			addMessage(redirectAttributes, "�޷��ӽ�ɫ��" + role.getName() + "�����Ƴ��û���" + user.getName() + "���Լ���");
		}else {
			if (user.getRoleList().size() <= 1){
				addMessage(redirectAttributes, "�û���" + user.getName() + "���ӽ�ɫ��" + role.getName() + "�����Ƴ�ʧ�ܣ����Ѿ��Ǹ��û���Ψһ��ɫ�������Ƴ���");
			}else{
				Boolean flag = systemService.outUserInRole(role, user);
				if (!flag) {
					addMessage(redirectAttributes, "�û���" + user.getName() + "���ӽ�ɫ��" + role.getName() + "�����Ƴ�ʧ�ܣ�");
				}else {
					addMessage(redirectAttributes, "�û���" + user.getName() + "���ӽ�ɫ��" + role.getName() + "�����Ƴ��ɹ���");
				}
			}		
		}
		return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
	}
	
	/**
	 * ��ɫ����
	 * @param role
	 * @param idsArr
	 * @param redirectAttributes
	 * @return
	 */

	@RequestMapping(value = "assignrole")
	public String assignRole(Role role, String[] idsArr, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "��ʾģʽ�������������");
			return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
		}
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			User user = systemService.assignUserToRole(role, systemService.getUser(idsArr[i]));
			if (null != user) {
				msg.append("<br/>�����û���" + user.getName() + "������ɫ��" + role.getName() + "����");
				newNum++;
			}
		}
		addMessage(redirectAttributes, "�ѳɹ����� "+newNum+" ���û�"+msg);
		return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
	}

	/**
	 * ��֤��ɫ���Ƿ���Ч
	 * @param oldName
	 * @param name
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String oldName, String name) {
		if (name!=null && name.equals(oldName)) {
			return "true";
		} else if (name!=null && systemService.getRoleByName(name) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * ��֤��ɫӢ�����Ƿ���Ч
	 * @param oldName
	 * @param name
	 * @return
	 */

	@ResponseBody
	@RequestMapping(value = "checkEnname")
	public String checkEnname(String oldEnname, String enname) {
		if (enname!=null && enname.equals(oldEnname)) {
			return "true";
		} else if (enname!=null && systemService.getRoleByEnname(enname) == null) {
			return "true";
		}
		return "false";
	}

}
