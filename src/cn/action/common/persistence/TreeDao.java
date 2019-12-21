package cn.action.common.persistence;

import java.util.List;

public interface TreeDao <T extends TreeEntity<T>> extends CrudDao<T> {

	/**
	 * �ҵ������ӽڵ�
	 * @param entity
	 * @return
	 */
	public List<T> findByParentIdsLike(T entity);

	/**
	 * �������и��ڵ��ֶ�
	 * @param entity
	 * @return
	 */
	public int updateParentIds(T entity);
	
}
