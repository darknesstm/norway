/**
 * 
 */
package me.chongchong.norway.test;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import me.chongchong.norway.NorwayBuildService;
import me.chongchong.norway.test.model.TestModel;

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
	public void testModel() {
		TestModel model = new TestModel();
		model.setId(111l);
		
		buildService.build(Arrays.asList(model), TestModel.class, 1);
		
		System.out.println(model);
	}
}
