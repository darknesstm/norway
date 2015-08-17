/**
 * 
 */
package me.chongchong.norway.internal;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.reflectasm.MethodAccess;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class MethodAccessCache {
	
	private Map<Class<?>, MethodAccess> cache = new HashMap<Class<?>, MethodAccess>();
	
	private MethodAccessCache() {}
	
	public static MethodAccessCache Instance = new MethodAccessCache();
	
	public MethodAccess get(Class<?> clazz) {
		MethodAccess ma = cache.get(clazz);
		if (ma == null) {
			ma = MethodAccess.get(clazz);
			cache.put(clazz, ma);
		}
		return ma;
	}
}
