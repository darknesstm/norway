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
		String group = element.getAttribute("group");
		Assert.notNull(group, "group cannot be null");
		builder.addPropertyReference("group", group);
		
		String type = element.getAttribute("type");
		builder.addPropertyValue("type", type);
		
		String idField = element.getAttribute("idField");
		Assert.notNull(idField, "idField cannot be null");
		builder.addPropertyValue("idField", idField);
		
		
		String buildGroups = element.getAttribute("buildGroups");
		builder.addPropertyValue("buildGroups", buildGroups);
		
		String builderName = element.getAttribute("builder");
		builder.addPropertyValue("builder", builderName);
	}
	
}
