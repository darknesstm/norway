/**
 * 
 */
package me.chongchong.norway.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import me.chongchong.norway.bean.BuildFieldBean;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class BuildFieldBeanDefinitionParser extends AbstractSingleBeanDefinitionParser  {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return BuildFieldBean.class;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element, org.springframework.beans.factory.support.BeanDefinitionBuilder)
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		String clazz = element.getAttribute("clazz");
		Assert.notNull(clazz, "clazz cannot be null");
		builder.addPropertyValue("clazz", clazz);
		
		String property = element.getAttribute("property");
		Assert.notNull(clazz, "property cannot be null");
		builder.addPropertyValue("property", property);
		
		String flag = element.getAttribute("flag");
		builder.addPropertyValue("flag", Integer.parseInt(flag));
		
		String idProperty = element.getAttribute("idProperty");
		Assert.notNull(clazz, "idProperty cannot be null");
		builder.addPropertyValue("idProperty", idProperty);
		
		String buildFlag = element.getAttribute("buildFlag");
		builder.addPropertyValue("buildFlag", Integer.parseInt(buildFlag));
		
		String type = element.getAttribute("type");
		builder.addPropertyValue("type", type);
		
		String builderName = element.getAttribute("builder");
		builder.addPropertyValue("builder", builderName);
	}
	
}
