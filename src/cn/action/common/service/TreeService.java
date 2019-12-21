package cn.action.common.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import cn.action.common.persistence.Reflections;
import cn.action.common.persistence.TreeDao;
import cn.action.common.persistence.TreeEntity;
import cn.action.common.utils.StringUtils;

@Transactional(readOnly = false)
public abstract class TreeService <D extends TreeDao<T>, T extends TreeEntity<T>> extends CrudService<D, T> {
	
	@Transactional(readOnly = false)
	public void save(T entity) {
		
		@SuppressWarnings("unchecked")
		Class<T> entityClass = Reflections.getClassGenricType(getClass(), 1);
		
		// ���û�����ø��ڵ㣬�����Ϊ���ڵ㣬�����ȡ���ڵ�ʵ��
		if (entity.getParent() == null || StringUtils.isBlank(entity.getParentId()) 
				|| "0".equals(entity.getParentId())){
			entity.setParent(null);
		}else{
			entity.setParent(super.get(entity.getParentId()));
		}
		if (entity.getParent() == null){
			T parentEntity = null;
			try {
				parentEntity = entityClass.getConstructor(String.class).newInstance("0");
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			entity.setParent(parentEntity);
			entity.getParent().setParentIds(StringUtils.EMPTY);
		}
		
		// ��ȡ�޸�ǰ��parentIds�����ڸ����ӽڵ��parentIds
		String oldParentIds = entity.getParentIds(); 
		
		// �����µĸ��ڵ㴮
		entity.setParentIds(entity.getParent().getParentIds()+entity.getParent().getId()+",");
		
		// ��������ʵ��
		super.save(entity);
		
		// �����ӽڵ� parentIds
		T o = null;
		try {
			o = entityClass.newInstance();
		} catch (Exception e) {
			//throw new ServiceException(e);
		}
		o.setParentIds("%,"+entity.getId()+",%");
		List<T> list = dao.findByParentIdsLike(o);
		for (T e : list){
			if (e.getParentIds() != null && oldParentIds != null){
				e.setParentIds(e.getParentIds().replace(oldParentIds, entity.getParentIds()));
				preUpdateChild(entity, e);
				dao.updateParentIds(e);
			}
		}
		
	}
	
	/**
	 * Ԥ���ӿڣ��û������ӽ�ǰ����
	 * @param childEntity
	 */
	protected void preUpdateChild(T entity, T childEntity) {
		
	}

}
