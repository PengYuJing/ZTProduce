package cn.action.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import cn.action.common.persistence.Reflections;

public class Collections3 {
	/**
	 * ��ȡ�����еĶ������������(ͨ��Getter����), ��ϳ�Map.
	 * 
	 * @param collection ��Դ����.
	 * @param keyPropertyName Ҫ��ȡΪMap�е�Keyֵ��������.
	 * @param valuePropertyName Ҫ��ȡΪMap�е�Valueֵ��������.
	 */
	@SuppressWarnings("unchecked")
	public static Map extractToMap(final Collection collection, final String keyPropertyName,
			final String valuePropertyName) {
		Map map = new HashMap(collection.size());

		try {
			for (Object obj : collection) {
				map.put(PropertyUtils.getProperty(obj, keyPropertyName),
						PropertyUtils.getProperty(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return map;
	}

	/**
	 * ��ȡ�����еĶ����һ������(ͨ��Getter����), ��ϳ�List.
	 * 
	 * @param collection ��Դ����.
	 * @param propertyName Ҫ��ȡ��������.
	 */
	@SuppressWarnings("unchecked")
	public static List extractToList(final Collection collection, final String propertyName) {
		List list = new ArrayList(collection.size());

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * ��ȡ�����еĶ����һ������(ͨ��Getter����), ��ϳ��ɷָ���ָ����ַ���.
	 * 
	 * @param collection ��Դ����.
	 * @param propertyName Ҫ��ȡ��������.
	 * @param separator �ָ���.
	 */
	public static String extractToString(final Collection collection, final String propertyName, final String separator) {
		List list = extractToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * ת��Collection����Ԫ��(ͨ��toString())ΪString, �м��� separator�ָ���
	 */
	public static String convertToString(final Collection collection, final String separator) {
		return StringUtils.join(collection, separator);
	}

	/**
	 * ת��Collection����Ԫ��(ͨ��toString())ΪString, ÿ��Ԫ�ص�ǰ�����prefix���������postfix����<div>mymessage</div>��
	 */
	public static String convertToString(final Collection collection, final String prefix, final String postfix) {
		StringBuilder builder = new StringBuilder();
		for (Object o : collection) {
			builder.append(prefix).append(o).append(postfix);
		}
		return builder.toString();
	}

	/**
	 * �ж��Ƿ�Ϊ��.
	 */
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}

	/**
	 * ȡ��Collection�ĵ�һ��Ԫ�أ����collectionΪ�շ���null.
	 */
	public static <T> T getFirst(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		return collection.iterator().next();
	}

	/**
	 * ��ȡCollection�����һ��Ԫ�� �����collectionΪ�շ���null.
	 */
	public static <T> T getLast(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		//������ΪListʱ��ֱ��ȡ�����һ��Ԫ�� ��
		if (collection instanceof List) {
			List<T> list = (List<T>) collection;
			return list.get(list.size() - 1);
		}

		//��������ͨ��iterator���������һ��Ԫ��.
		Iterator<T> iterator = collection.iterator();
		while (true) {
			T current = iterator.next();
			if (!iterator.hasNext()) {
				return current;
			}
		}
	}

	/**
	 * ����a+b����List.
	 */
	public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
		List<T> result = new ArrayList<T>(a);
		result.addAll(b);
		return result;
	}

	/**
	 * ����a-b����List.
	 */
	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		List<T> list = new ArrayList<T>(a);
		for (T element : b) {
			list.remove(element);
		}

		return list;
	}

	/**
	 * ����a��b�Ľ�������List.
	 */
	public static <T> List<T> intersection(Collection<T> a, Collection<T> b) {
		List<T> list = new ArrayList<T>();

		for (T element : a) {
			if (b.contains(element)) {
				list.add(element);
			}
		}
		return list;
	}
}
