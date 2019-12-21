package cn.action.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.action.common.config.Global;
import cn.action.common.persistence.Page;
import cn.action.common.utils.DateUtils;
import cn.action.common.utils.StringUtils;
import cn.action.common.utils.UserUtils;
import cn.action.common.web.BaseController;
import cn.action.modules.sys.entity.Menu;
import cn.action.modules.sys.entity.Office;
import cn.action.modules.sys.entity.Role;
import cn.action.modules.sys.entity.User;
import cn.action.modules.sys.service.OfficeService;
import cn.action.modules.sys.service.SystemService;

@Controller
@RequestMapping(value = "${adminPath}/sys/user")
public class UserController extends BaseController {

	@Autowired
	private SystemService systemService;
	

	
	@ModelAttribute
	public User get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return systemService.getUser(id);
		}else{
			return new User();
		}
	}

	
	
	
	@RequestMapping(value = {"index"})
	public String index(User user, Model model) {
		return "modules/sys/userIndex";
	}

	
	@RequestMapping(value = {"list", ""})
	public String list(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
        model.addAttribute("page", page);
		return "modules/sys/userList";
	}
	
	@ResponseBody
	@RequestMapping(value = {"listData"})
	public Page<User> listData(User user, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<User> page = systemService.findUser(new Page<User>(request, response), user);
		return page;
	}


	@RequestMapping(value = "form")
	public String form(User user, Model model) {
		if (user.getCompany()==null || user.getCompany().getId()==null){
			user.setCompany(UserUtils.getUser().getCompany());
		}
		if (user.getOffice()==null || user.getOffice().getId()==null){
			user.setOffice(UserUtils.getUser().getOffice());
		}
		model.addAttribute("user", user);
		model.addAttribute("allRoles", systemService.findAllRole());
		return "modules/sys/userForm";
	}

	
	@RequestMapping(value = "save")
	public String save(User user, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
		if(Global.isDemoMode()){
			addMessage(redirectAttributes, "��ʾģʽ�������������");
			return "redirect:" + adminPath + "/sys/user/list?repage";
		}
		// �������ø�ֵ���⣬��֪��Ϊ�Σ�Company��Office���õ�һ��ʵ����ַ���޸���һ��������һ�������޸ġ�
		user.setCompany(new Office(request.getParameter("company.id")));
		user.setOffice(new Office(request.getParameter("office.id")));
		// ���������Ϊ�գ��򲻸�������
		if (StringUtils.isNotBlank(user.getNewPassword())) {
			user.setPassword(SystemService.entryptPassword(user.getNewPassword()));
		}
		
		if (!"true".equals(checkLoginName(user.getOldLoginName(), user.getLoginName()))){
			addMessage(model, "�����û�'" + user.getLoginName() + "'ʧ�ܣ���¼���Ѵ���");
			return form(user, model);
		}
		// ��ɫ������Ч����֤�����˲�����Ȩ�ڵĽ�ɫ
		List<Role> roleList = Lists.newArrayList();
		List<String> roleIdList = user.getRoleIdList();
		for (Role r : systemService.findAllRole()){
			if (roleIdList.contains(r.getId())){
				roleList.add(r);
			}
		}
		user.setRoleList(roleList);
		// �����û���Ϣ
		systemService.saveUser(user);
		// �����ǰ�û�����
		if (user.getLoginName().equals(UserUtils.getUser().getLoginName())){
			//UserUtils.clearCache();
			//UserUtils.getCacheMap().clear();
		}
		addMessage(redirectAttributes, "�����û�'" + user.getLoginName() + "'�ɹ�");
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	

	@RequestMapping(value = "delete")
	public String delete(User user, RedirectAttributes redirectAttributes) {
		if (UserUtils.getUser().getId().equals(user.getId())){
			addMessage(redirectAttributes, "ɾ���û�ʧ��, ������ɾ����ǰ�û�");
		}else if (User.isAdmin(user.getId())){
			addMessage(redirectAttributes, "ɾ���û�ʧ��, ������ɾ����������Ա�û�");
		}else{
			systemService.deleteUser(user);
			addMessage(redirectAttributes, "ɾ���û��ɹ�");
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
	}
	
	/**
	 * �����û�����
	 * @param user
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(User user, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "�û�����"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<User> page = systemService.findUser(new Page<User>(request, response, -1), user);
    		//new ExportExcel("�û�����", User.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "�����û�ʧ�ܣ�ʧ����Ϣ��"+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * �����û�����
	 * @param file
	 * @param redirectAttributes
	 * @return
	 */
//	@RequiresPermissions("sys:user:edit")
//    @RequestMapping(value = "import", method=RequestMethod.POST)
//    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
//		if(Global.isDemoMode()){
//			addMessage(redirectAttributes, "��ʾģʽ�������������");
//			return "redirect:" + adminPath + "/sys/user/list?repage";
//		}
//		try {
//			int successNum = 0;
//			int failureNum = 0;
//			StringBuilder failureMsg = new StringBuilder();
//			//ImportExcel ei = new ImportExcel(file, 1, 0);
//			List<User> list = ei.getDataList(User.class);
//			for (User user : list){
//				try{
//					if ("true".equals(checkLoginName("", user.getLoginName()))){
//						user.setPassword(SystemService.entryptPassword("123456"));
//						//BeanValidators.validateWithException(validator, user);
//						systemService.saveUser(user);
//						successNum++;
//					}else{
//						failureMsg.append("<br/>��¼�� "+user.getLoginName()+" �Ѵ���; ");
//						failureNum++;
//					}
//				}catch(ConstraintViolationException ex){
//					failureMsg.append("<br/>��¼�� "+user.getLoginName()+" ����ʧ�ܣ�");
//					List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
//					for (String message : messageList){
//						failureMsg.append(message+"; ");
//						failureNum++;
//					}
//				}catch (Exception ex) {
//					failureMsg.append("<br/>��¼�� "+user.getLoginName()+" ����ʧ�ܣ�"+ex.getMessage());
//				}
//			}
//			if (failureNum>0){
//				failureMsg.insert(0, "��ʧ�� "+failureNum+" ���û���������Ϣ���£�");
//			}
//			addMessage(redirectAttributes, "�ѳɹ����� "+successNum+" ���û�"+failureMsg);
//		} catch (Exception e) {
//			addMessage(redirectAttributes, "�����û�ʧ�ܣ�ʧ����Ϣ��"+e.getMessage());
//		}
//		return "redirect:" + adminPath + "/sys/user/list?repage";
//    }
	
	/**
	 * ���ص����û�����ģ��
	 * @param response
	 * @param redirectAttributes
	 * @return
	 */

    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "�û����ݵ���ģ��.xlsx";
    		List<User> list = Lists.newArrayList(); list.add(UserUtils.getUser());
    		//new ExportExcel("�û�����", User.class, 2).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "����ģ������ʧ�ܣ�ʧ����Ϣ��"+e.getMessage());
		}
		return "redirect:" + adminPath + "/sys/user/list?repage";
    }

	/**
	 * ��֤��¼���Ƿ���Ч
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkLoginName")
	public String checkLoginName(String oldLoginName, String loginName) {
		if (loginName !=null && loginName.equals(oldLoginName)) {
			return "true";
		} else if (loginName !=null && systemService.getUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}

	/**
	 * �û���Ϣ��ʾ������
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "info")
	public String info(User user, HttpServletResponse response, Model model) {
		User currentUser = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getName())){
			currentUser.setEmail(user.getEmail());
			currentUser.setPhone(user.getPhone());
			currentUser.setMobile(user.getMobile());
			currentUser.setRemarks(user.getRemarks());
			currentUser.setPhoto(user.getPhoto());
			systemService.updateUserInfo(currentUser);
			model.addAttribute("message", "�����û���Ϣ�ɹ�");
		}
		model.addAttribute("user", currentUser);
		//�޸�Global û��˽�й��캯����ʵ������ʽ����ģʽ.�ڵ�һ�ε��õ�ʱ��ʵ�����Լ���
		model.addAttribute("Global", Global.getInstance());
		return "modules/sys/userInfo";
	}

	/**
	 * �����û���Ϣ
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "infoData")
	public User infoData() {
		return UserUtils.getUser();
	}
	
	/**
	 * �޸ĸ����û�����
	 * @param oldPassword
	 * @param newPassword
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "modifyPwd")
	public String modifyPwd(String oldPassword, String newPassword, Model model) {
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)){
			if(Global.isDemoMode()){
				model.addAttribute("message", "��ʾģʽ�������������");
				return "modules/sys/userModifyPwd";
			}
			if (SystemService.validatePassword(oldPassword, user.getPassword())){
				systemService.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
				model.addAttribute("message", "�޸�����ɹ�");
			}else{
				model.addAttribute("message", "�޸�����ʧ�ܣ����������");
			}
		}
		model.addAttribute("user", user);
		return "modules/sys/userModifyPwd";
	}
	
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String officeId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<User> list = systemService.findUserByOfficeId(officeId);
		for (int i=0; i<list.size(); i++){
			User e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "u_"+e.getId());
			map.put("pId", officeId);
			map.put("name", StringUtils.replace(e.getName(), " ", ""));
			mapList.add(map);
		}
		return mapList;
	}
    

}
