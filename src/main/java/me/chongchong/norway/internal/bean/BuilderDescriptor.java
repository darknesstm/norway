/**
 * 
 */
package me.chongchong.norway.internal.bean;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.springframework.util.ClassUtils;

import com.esotericsoftware.reflectasm.MethodAccess;

import me.chongchong.norway.internal.MethodAccessCache;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class BuilderDescriptor {
	
	private boolean auto;
	private Class<?> forType;
	private String name;
	
	private Object bean;
	private Method method;
	
	MethodAccess methodAccess;
	int methodIndex;
	int parameterLength;
	
	/**
	 * @param name
	 * @param bean
	 * @param method
	 * @param forType
	 * @param auto
	 */
	public BuilderDescriptor(String name, Object bean, Method method, Class<?> forType, boolean auto) {
		super();
		this.name = name;
		this.bean = bean;
		this.method = method;
		this.forType = forType;
		this.auto = auto;
		
		methodAccess = MethodAccessCache.Instance.get(ClassUtils.getUserClass(bean));
		parameterLength = method.getParameterTypes().length;
		methodIndex = methodAccess.getIndex(method.getName(), method.getParameterTypes());
		
		if (parameterLength == 0 || parameterLength > 2) {
			throw new IllegalArgumentException("参数数量不正确");
		}
	}

	public boolean isAuto() {
		return auto;
	}

	public Class<?> getForType() {
		return forType;
	}

	public String getName() {
		return name;
	}

	public Object getBean() {
		return bean;
	}

	public Method getMethod() {
		return method;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
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
		BuilderDescriptor other = (BuilderDescriptor) obj;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("BuilderDescriptor [method=%s]", method);
	}

	public Map<Object, Object> getObjects(Collection<?> ids, int buildFlag) {
		if (parameterLength == 1) {
			return (Map<Object, Object>) methodAccess.invoke(bean, methodIndex, ids);
		} else {
			return (Map<Object, Object>) methodAccess.invoke(bean, methodIndex, ids, buildFlag);
		}
	}
}
