package cn.action.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.action.common.service.CrudService;
import cn.action.modules.sys.dao.DictDao;
import cn.action.modules.sys.entity.Dict;

/**
 * �ֵ�Service
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly = true)
public class DictService extends CrudService<DictDao, Dict> {
	
	/**
	 * ��ѯ�ֶ������б�
	 * @return
	 */
	public List<String> findTypeList(){
		return dao.findTypeList(new Dict());
	}
}
