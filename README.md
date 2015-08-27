
# norway

![Maven Central](https://img.shields.io/maven-central/v/me.chongchong/norway.svg)


===========

一个数据构建框架，本框架依赖Spring core。

本框架解耦了数据的使用者和数据的生产者，可以更加灵活的构建需要的内容。
在JEE领域，对于常见的展现层的数据组装提供简约的代码编写方式。

一般的数据组装代码如下

```java
class ViewModel {
	private Long subId;
	private SubModel subObject;

}

List<ViewModel> getList(List<Long> ids) {
	List<ViewModel> list = xxx.getListFromDB(ids);

	Set<Long> subIds = new HashSet<Long>();
	for (ViewModel m : list) {
		subIds.add(m.getSubId());
	}

	Map<Long, SubModel> subModelMap = yyy.getSubModelMap(subIds);

	for (ViewModel m : list) {
		m.setSubModel(subModelMap.get(m.getSubId()));
	}

	return list;
}
```

有些时间对于ViewModel的SubModel的组装不一定是必须的，所以还需要增加参数来指定是否组装

```java
List<ViewModel> getList(List<Long> ids, int flag) {
	List<ViewModel> list = xxx.getListFromDB(ids);

	// 判断是否需要组装
	if ((flag & BUILD_SUBMODEL) == BUILD_SUBMODEL) {
		Set<Long> subIds = new HashSet<Long>();
		for (ViewModel m : list) {
				subIds.add(m.getSubId());
		}

		Map<Long, SubModel> subModelMap = yyy.getSubModelMap(subIds);

		for (ViewModel m : list) {
			m.setSubModel(subModelMap.get(m.getSubId()));
		}
	}

	return list;
}
```

类似的代码会重复出现在我们的程序中，尤其是当一个复杂类型的模型出现的时候，使用了本框架后，就可以简化这些步骤

```xml
<dependency>
  <groupId>me.chongchong</groupId>
  <artifactId>norway</artifactId>
  <version>0.2.0</version>
</dependency>
```

首先需要在spring的xml中配置增加服务实例，buildBeanPackages参数是包含后之后需要组装的模型的包名，多个包可以用英文逗号分割
```xml
<bean class="me.chongchong.norway.NorwayBuildService">
	<property name="buildBeanPackages" value="xxx.xxx.xxx.xxx.xxx"></property>
</bean>
```
然后在模型上标注需要按需组装的注解
```java
class ViewModel {
	private Long subId;
	@BuildField(idProperty="subId", flag=BUILD_SUBMODEL)
	private SubModel subObject;

}
```
在可以创建组装类型的方法上标记注解，构建的方法支持2种格式，可以查看@Builder的注释说明
```java
	@Builder
	Map<Long, SubModel> getSubModelMap(Collection<Long> ids) {...}
```
可以进行构建了
```java
@Autowired
private NorwayBuildService buildService;

List<ViewModel> getList(List<Long> ids, int flag) {
	List<ViewModel> list = xxx.getListFromDB(ids);

	buildService.build(list, ViewModel.class, flag);

	return list;
}
```
如果ViewModel自身也已经注册了对应的Builder，则可以更简化为
```java
buildService.getBuildedList(ids, ViewModel.class, flag);
```

当然也支持非嵌入式的配置方式，需要在spring的配置文件中进行
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:norway="http://chongchong.me/schema/norway"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://chongchong.me/schema/norway http://chongchong.me/schema/norway/norway.xsd">
	
	<norway:builder ref="id" method="xxxxx"/>
	<norway:buildField flag="1" idProperty="id" property="subObject" class="test.Model"/>

</beans>

```
