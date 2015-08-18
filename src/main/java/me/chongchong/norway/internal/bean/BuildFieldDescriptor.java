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
		this.propertyName = propertyName;
		
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

	@Override
	public String toString() {
		return String.format("BuildFieldDescriptor [tagertClass=%s, propertyName=%s]", tagertClass, propertyName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		result = prime * result + ((tagertClass == null) ? 0 : tagertClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildFieldDescriptor other = (BuildFieldDescriptor) obj;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		if (tagertClass == null) {
			if (other.tagertClass != null)
				return false;
		} else if (!tagertClass.equals(other.tagertClass))
			return false;
		return true;
	}
	
}
