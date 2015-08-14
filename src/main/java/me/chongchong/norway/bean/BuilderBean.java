/**
 * 
 */
package me.chongchong.norway.bean;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class BuilderBean {
	
	private Object bean;
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
	

}
