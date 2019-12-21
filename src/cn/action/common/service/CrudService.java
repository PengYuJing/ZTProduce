package cn.action.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import cn.action.common.persistence.CrudDao;
import cn.action.common.persistence.DataEntity;
import cn.action.common.persistence.Page;

public class CrudService <D extends CrudDao<T>, T extends DataEntity<T>> extends BaseService {
	
	/**
	 * �־ò����
	 */
	@Autowired
	protected D dao;
	
	/**
	 * ��ȡ��������
	 * @param id
	 * @return
	 */
	public T get(String id) {
		T t = dao.get(id);
		System.out.println(t);
		return t;
	}
	
	/**
	 * ��ȡ��������
	 * @param entity
	 * @return
	 */
	public T get(T entity) {
		return dao.get(entity);
	}
	
	/**
	 * ��ѯ�б�����
	 * @param entity
	 * @return
	 */
	public List<T> findList(T entity) {
		return dao.findList(entity);
	}
	
	/**
	 * ��ѯ��ҳ����
	 * @param page ��ҳ����
	 * @param entity
	 * @return
	 */
	public Page<T> findPage(Page<T> page, T entity) {
		entity.setPage(page);
		page.setList(dao.findList(entity));
		return page;
	}

	/**
	 * �������ݣ��������£�
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void save(T entity) {
		if (entity.getIsNewRecord()){
			entity.preInsert();
			dao.insert(entity);
		}else{
			entity.preUpdate();
			dao.update(entity);
		}
	}
	
	/**
	 * ɾ������
	 * @param entity
	 */
	@Transactional(readOnly = false)
	public void delete(T entity) {
		dao.delete(entity);
	}
	/**
	 * ��ѯ��������
	 * @param entity
	 * @return
	 */
	@Transactional(readOnly=false)
	public List<T> findAllList(T entity){
		return dao.findAllList(entity);
	}
}
