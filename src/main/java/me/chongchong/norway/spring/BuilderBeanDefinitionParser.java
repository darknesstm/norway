/**
 * 
 */
package me.chongchong.norway.spring;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import me.chongchong.norway.bean.BuilderBean;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class BuilderBeanDefinitionParser extends AbstractSingleBeanDefinitionParser  {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#getBeanClass(org.w3c.dom.Element)
	 */
	@Override
	protected Class<?> getBeanClass(Element element) {
		return BuilderBean.class;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#shouldGenerateId()
	 */
	@Override
	protected boolean shouldGenerateId() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser#doParse(org.w3c.dom.Element, org.springframework.beans.factory.support.BeanDefinitionBuilder)
	 */
	@Override
	protected void doParse(Element element, BeanDefinitionBuilder builder) {
		
		String refBean = element.getAttribute("ref");
		Assert.notNull(refBean, "ref cannot be null");
		builder.addPropertyReference("bean", refBean);
		
		String name = element.getAttribute("name");
		builder.addPropertyValue("name", name);
		
		String defaultBuilder = element.getAttribute("default");
		builder.addPropertyValue("defaultBuilder", Boolean.parseBoolean(defaultBuilder));
		
		String forType = element.getAttribute("forType");
		Assert.notNull(forType, "forType cannot be null");
		builder.addPropertyValue("forType", forType);
		
		
		String methodName = element.getAttribute("method");
		Assert.notNull(methodName, "method cannot be null");
		builder.addPropertyValue("method", methodName);
	}
	
}
