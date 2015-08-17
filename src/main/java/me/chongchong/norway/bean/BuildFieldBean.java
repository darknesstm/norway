/**
 * 
 */
package me.chongchong.norway.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.google.common.base.Throwables;

import me.chongchong.norway.NorwayBuildService;
import me.chongchong.norway.internal.bean.BuildFieldDescriptor;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class BuildFieldBean extends AutoStartBean {
	
	private String clazz;
	private String property;
	private int flag;
	private String idProperty;
	private int buildFlag;
	private String type;
	private String builder;
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getIdProperty() {
		return idProperty;
	}
	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}
	public int getBuildFlag() {
		return buildFlag;
	}
	public void setBuildFlag(int buildFlag) {
		this.buildFlag = buildFlag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBuilder() {
		return builder;
	}
	public void setBuilder(String builder) {
		this.builder = builder;
	}
	
	public String getClazz() {
		return clazz;
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
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
			Class<?> c = ClassUtils.forName(clazz, ClassUtils.getDefaultClassLoader());
			Class<?> typeClass = null;
			if (StringUtils.hasText(type)) {
				typeClass = ClassUtils.forName(type, ClassUtils.getDefaultClassLoader());
			}
			
			norwayBuildService.addBuildFieldDescriptor(c, new BuildFieldDescriptor(c, property, flag, buildFlag, idProperty, typeClass, builder));
			
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
