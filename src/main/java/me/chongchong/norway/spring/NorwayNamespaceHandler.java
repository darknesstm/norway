/**
 * 
 */
package me.chongchong.norway.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class NorwayNamespaceHandler extends NamespaceHandlerSupport {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	@Override
	public void init() {
		registerBeanDefinitionParser("builder", new BuilderBeanDefinitionParser());
		registerBeanDefinitionParser("buildField", new BuildFieldBeanDefinitionParser());
	}

}
