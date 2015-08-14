/**
 * 
 */
package me.chongchong.norway.internal.bean;

import org.springframework.beans.BeanUtils;

import jodd.bean.BeanUtil;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class BuildFieldDescriptor {
	
	private Class<?> tagertClass;
	private String propertyName;
	
	private int flag;
	private int buildFlag;
	private String idPropertyName;
	private Class<?> type;
	private String builderName;
	
	/**
	 * @param tagertClass
	 * @param propertyName
	 * @param flag
	 * @param buildFlag
	 * @param idPropertyName
	 * @param type
	 * @param builderName
	 */
	public BuildFieldDescriptor(Class<?> tagertClass, String propertyName, int flag, int buildFlag, String idPropertyName, Class<?> type, String builderName) {
		super();
		this.tagertClass = tagertClass;
		this.propertyName = propertyName;
		this.flag = flag;
		this.buildFlag = buildFlag;
		this.idPropertyName = idPropertyName;
		this.type = type;
		this.builderName = builderName;
	}

	public Class<?> getTagertClass() {
		return tagertClass;
	}

	public String getPropertyName() {
		return propertyName;
	}


	public String getIdPropertyName() {
		return idPropertyName;
	}

	public Class<?> getType() {
		return type;
	}

	public String getBuilderName() {
		return builderName;
	}
	
	public int getFlag() {
		return flag;
	}

	public int getBuildFlag() {
		return buildFlag;
	}

	public Object getIdObject(Object bean) {
		return BeanUtil.getProperty(bean, idPropertyName);
	}
	
	public void setObject(Object bean, Object value) {
		BeanUtil.setProperty(bean, propertyName, value);
	}
	
}
