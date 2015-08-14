/**
 * 
 */
package me.chongchong.norway.test.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import me.chongchong.norway.annotation.Builder;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
@Service
public class DataService {

	@Builder
	public Map<Long, String> getString(Collection<Long> ids) {
		Map<Long, String> result = Maps.newHashMap();
		for (Long id : ids) {
			result.put(id, String.valueOf(id) + "-");
		}
		return result;
	}
	
	@Builder
	public Map<Long, Integer> getInteger(Collection<Long> ids) {
		Map<Long, String> result = Maps.newHashMap();
		for (Long id : ids) {
			result.put(id, String.valueOf(id) + "-");
		}
		return null;
	}
}
