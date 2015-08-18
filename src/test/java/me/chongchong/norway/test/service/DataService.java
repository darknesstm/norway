/**
 * 
 */
package me.chongchong.norway.test.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import me.chongchong.norway.annotation.Builder;
import me.chongchong.norway.test.model.ResultObject;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
@Service
public class DataService {

	@Builder
	public <T> Map<T, ResultObject<T>> getString(Collection<T> ids) {
		Map<T, ResultObject<T>> result = Maps.newHashMap();
		for (T id : ids) {
			ResultObject<T> obj = new ResultObject<T>();
			obj.setId(id);
			result.put(id, obj);
		}
		return result;
	}
	
}
