package com.efida.esearch.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

/**
 * 对bean的copy处理 不为空的字段进行copy
 */
public class DiyBeanUtils {

	/**
	 * 将数据源空的字段取出 ，这些字段将被忽略不会被复制到目标对象
	 *  ignore 中的字段 不判断是否为空都会被复制
	 * @param source
	 *            数据源
	 * @param target
	 *            目标源
	 * @param ignore
	 * 			     需要忽略掉那些字段不用copy
	 */
	public static void copyNotNullProperties(Object source, Object target,String... ignore) {
		org.springframework.beans.BeanUtils.copyProperties(source, target, getNullProperties(source,ignore));
	}

	/**
	 * 将数据源空的字段取出 ，这些字段将被忽略不会被复制到目标对象
	 * ignore 中的字段 不判断是否为空都会被复制
	 *
	 * @param source
	 *            数据源
	 *
	 * @param ignore 如果存在将不判断是否为空都将不会被忽略
	 * @return 将数据源空的字段取出 ，这些字段将被忽略不会被复制到目标对象
	 */
	private static String[] getNullProperties(Object source,String... ignore) {
		BeanWrapper srcBean = new BeanWrapperImpl(source);
		PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
		Set<String> noEmptyName = new HashSet<>();
		for (PropertyDescriptor p : pds) {
			boolean isIgnore=false;
			for (String string : ignore) {
				if(p.getName().equals(string)){
					isIgnore=true;
					break;
				}
			}
			if(isIgnore){
				continue;
			}
			Object value = srcBean.getPropertyValue(p.getName());
			if (value == null){
				noEmptyName.add(p.getName());
				System.out.println(p.getName());
			}


		}
		String[] result = new String[noEmptyName.size()];
		return noEmptyName.toArray(result);
	}
}
