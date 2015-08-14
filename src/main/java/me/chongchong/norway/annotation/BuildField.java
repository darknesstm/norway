/**
 * 
 */
package me.chongchong.norway.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Documented
public @interface BuildField {
	
	/**
	 * 本对象对应的flag
	 * @return
	 */
	int flag() default 0;
		
	/**
	 * 指明获取id的字段名
	 * @return
	 */
	String idProperty();
	
	/**
	 * 本对象构建时使用的flag
	 * @return
	 */
	int buildFlag() default 0;
		
	/**
	 * 当是集合类型的时候，需要指明类型
	 * @return
	 */
	Class<?> type() default void.class;
	
	/**
	 * 指明需要的构建方法，留空是自动适配
	 * @return
	 */
	String buildMethod() default "";
}
