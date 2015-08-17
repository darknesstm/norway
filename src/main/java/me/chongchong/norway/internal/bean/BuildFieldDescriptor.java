/**
 * 
 */
package me.chongchong.norway.internal.bean;

import org.springframework.beans.BeanUtils;

import com.esotericsoftware.reflectasm.MethodAccess;

import me.chongchong.norway.internal.MethodAccessCache;

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
	
	MethodAccess methodAccess;
	int getIdIndex;
	int setObjectIndex;
	
	
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
		this.flag = flag;
		this.buildFlag = buildFlag;
		this.type = type;
		this.builderName = builderName;
		
		methodAccess = MethodAccessCache.Instance.get(tagertClass);
		getIdIndex = methodAccess.getIndex(BeanUtils.getPropertyDescriptor(tagertClass, idPropertyName).getReadMethod().getName());
		setObjectIndex = methodAccess.getIndex(BeanUtils.getPropertyDescriptor(tagertClass, propertyName).getWriteMethod().getName());
		
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
		return methodAccess.invoke(bean, getIdIndex);
	}
	
	public void setObject(Object bean, Object value) {
		methodAccess.invoke(bean, setObjectIndex, value);
	}
	
}
