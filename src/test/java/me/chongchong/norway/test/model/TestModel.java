/**
 * 
 */
package me.chongchong.norway.test.model;

import me.chongchong.norway.annotation.BuildField;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class TestModel {

	private Long id;
	
	@BuildField(flag=1, idProperty = "id")
	private String idString;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}
	@Override
	public String toString() {
		return String.format("TestModel [id=%s, idString=%s]", id, idString);
	}
	
	
}
