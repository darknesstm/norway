/**
 * 
 */
package me.chongchong.norway.internal.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.springframework.util.ClassUtils;

import com.google.common.base.Throwables;

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
	
	public Map<Object, Object> getObjects(Collection<?> ids, int buildFlag) {
		try {
			return (Map<Object, Object>) method.invoke(bean, ids);
		} catch (Throwable t) {
			throw Throwables.propagate(t);
		}
	}
}
