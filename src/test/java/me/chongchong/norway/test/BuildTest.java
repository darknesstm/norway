/**
 * 
 */
package me.chongchong.norway.test;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.chongchong.norway.NorwayBuildService;
import me.chongchong.norway.test.model.Model1;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
		"classpath:main-test.xml"
})
public class BuildTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private NorwayBuildService buildService;
	
	@Test
	public void test1() {
		Model1 model = new Model1();
		model.setId(System.currentTimeMillis());
		
		buildService.build(Arrays.asList(model), Model1.class, Model1.FLAG_1);
		
		Assert.assertNotNull(model.getObject1());
		Assert.assertEquals(model.getId(), model.getObject1().getId());
	}
	
	@Test
	public void test2() {
		Model1 model = new Model1();
		model.setId(System.currentTimeMillis());
		
		buildService.build(Arrays.asList(model), Model1.class, Model1.FLAG_2);
		
		Assert.assertNotNull(model.getObject2());
		Assert.assertEquals(model.getId(), model.getObject2().getId());
	}
	
	@Test
	public void test3() {
		Model1 model = new Model1();
		model.setId(System.currentTimeMillis());
		
		buildService.build(Arrays.asList(model), Model1.class, Model1.FLAG_3);
		
		Assert.assertNotNull(model.getObject3());
		Assert.assertEquals(model.getId(), model.getObject3().getId());
	}
	
	@Test
	public void test4() {
		Model1 model = new Model1();
		model.setId(System.currentTimeMillis());
		
		buildService.build(Arrays.asList(model), Model1.class, Model1.FLAG_4);
		
		Assert.assertNotNull(model.getObject4());
		Assert.assertEquals(model.getId(), model.getObject4().getId());
	}
}
