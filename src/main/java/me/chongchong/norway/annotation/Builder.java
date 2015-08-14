package me.chongchong.norway.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 构建对象的方法<BR>
 * 
 * 方法的定义必须是
 * <pre>Map&lt;?, ?&gt; method(Collection&lt;?&gt;, int)</pre>
 * 或者
 * <pre>Map&lt;?, ?&gt; method(Collection&lt;?&gt;)</pre>
 * @author DarknessTM (askkoy@163.com)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface Builder {
	
	
	/**
	 * id名称，用于被指定引用
	 * @return
	 */
	String id() default "";
	
	Class<?> forType() default void.class;
	
	/**
	 * 是否会被自动使用
	 * @return
	 */
	boolean auto() default true;
	
}
