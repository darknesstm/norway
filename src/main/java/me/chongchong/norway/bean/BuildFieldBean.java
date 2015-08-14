/**
 * 
 */
package me.chongchong.norway.bean;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import me.chongchong.norway.NorwayBuildService;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class BuildFieldBean extends AutoStartBean {
	
	private String clazz;
	private String property;
	private String flag;
	private String idProperty;
	private String buildFlag;
	private String type;
	private String builder;
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getIdProperty() {
		return idProperty;
	}
	public void setIdProperty(String idProperty) {
		this.idProperty = idProperty;
	}
	public String getBuildFlag() {
		return buildFlag;
	}
	public void setBuildFlag(String buildFlag) {
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
//		try {
//			Class<?> c = ClassUtils.forName(clazz, ClassUtils.getDefaultClassLoader());
//			
//			Class<?> groupClass = ClassUtils.forName(group, ClassUtils.getDefaultClassLoader());
//			Set<Class<?>> buildGroupClassSet = Sets.newHashSet();
//			for (String buildGroup : Splitter.on(",").omitEmptyStrings().trimResults().split(buildGroups)) {
//				Class<?> buildGroupClass = ClassUtils.forName(buildGroup, ClassUtils.getDefaultClassLoader());
//				buildGroupClassSet.add(buildGroupClass);
//			}
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (LinkageError e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		BuildFieldDescriptor fd = new BuildFieldDescriptor(c, getIdField(), getField(), groupClass, buildGroupClassSet, builder);
//		norwayBuildService.addBuildFieldDescriptor(c, fd);
	}
	/* (non-Javadoc)
	 * @see me.chongchong.norway.bean.AutoStartBean#doStop()
	 */
	@Override
	protected void doStop() {
		
	}
}
