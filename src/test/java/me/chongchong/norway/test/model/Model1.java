/**
 * 
 */
package me.chongchong.norway.test.model;

import me.chongchong.norway.annotation.BuildField;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class Model1 {
	
	public static final int FLAG_1 = 1;
	public static final int FLAG_2 = 1 << 2;
	public static final int FLAG_3 = 1 << 3;
	public static final int FLAG_4 = 1 << 4;
	
	private Long id;
	
	@BuildField(idProperty="id", flag=FLAG_1)
	private ResultObject<Long> object1;
	
	private ResultObject<Long> object2;

	@BuildField(idProperty="id", flag=FLAG_3)
	private ResultObject<Long> object3;
	
	private ResultObject<Long> object4;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ResultObject<Long> getObject1() {
		return object1;
	}

	public void setObject1(ResultObject<Long> object1) {
		this.object1 = object1;
	}

	public ResultObject<Long> getObject2() {
		return object2;
	}

	public void setObject2(ResultObject<Long> object2) {
		this.object2 = object2;
	}

	public ResultObject<Long> getObject3() {
		return object3;
	}

	public void setObject3(ResultObject<Long> object3) {
		this.object3 = object3;
	}

	public ResultObject<Long> getObject4() {
		return object4;
	}

	public void setObject4(ResultObject<Long> object4) {
		this.object4 = object4;
	}
	
	
}
