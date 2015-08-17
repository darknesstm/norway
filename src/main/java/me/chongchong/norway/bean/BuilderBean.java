/**
 * 
 */
package me.chongchong.norway.bean;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.google.common.base.Throwables;

import me.chongchong.norway.NorwayBuildService;
import me.chongchong.norway.internal.bean.BuilderDescriptor;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class BuilderBean extends AutoStartBean  {
	
	private Object bean;
	private String name;
	private String method;
	private String forType;
	private boolean defaultBuilder;
	
	public Object getBean() {
		return bean;
	}
	public void setBean(Object bean) {
		this.bean = bean;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getForType() {
		return forType;
	}
	public void setForType(String forType) {
		this.forType = forType;
	}
	public boolean isDefaultBuilder() {
		return defaultBuilder;
	}
	public void setDefaultBuilder(boolean defaultBuilder) {
		this.defaultBuilder = defaultBuilder;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Autowired
	private NorwayBuildService norwayBuildService;
	
	/* (non-Javadoc)
	 * @see org.springframework.context.Phased#getPhase()
	 */
	@Override
	public int getPhase() {
		return norwayBuildService.getPhase() - 1;
	}
	/* (non-Javadoc)
	 * @see me.chongchong.norway.bean.AutoStartBean#doStart()
	 */
	@Override
	protected void doStart() {
		try {
			Class<?> serviceClass = ClassUtils.getUserClass(bean);
			
			for (Method m : serviceClass.getMethods()) {
				
				if (!m.getName().equals(method)) {
					continue;
				}
				
				Class<?> valueType = null;
				if (StringUtils.hasText(forType)) {
					valueType = ClassUtils.forName(forType, ClassUtils.getDefaultClassLoader());
				}
				
				if (valueType == null ) {
					if (m.getGenericReturnType() != null) {
						Type[] keyValueTypes = ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments();
						valueType = (Class<?>) keyValueTypes[1];
					} else {
						continue;
					}
				}
				
				BuilderDescriptor db = new BuilderDescriptor(name, bean, m, valueType, defaultBuilder);
				
				if (defaultBuilder) {
					norwayBuildService.addDefaultBuilderDescrptor(valueType, db);
				}
				
				if (StringUtils.hasText(name)) {
					norwayBuildService.addNamedBuilderDescrptor(name, db);
				}
			}
			
		} catch (Throwable t) {
			throw Throwables.propagate(t);
		}
	}
	/* (non-Javadoc)
	 * @see me.chongchong.norway.bean.AutoStartBean#doStop()
	 */
	@Override
	protected void doStop() {
		
	}
}
