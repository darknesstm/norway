/**
 * 
 */
package me.chongchong.norway;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import me.chongchong.norway.annotation.BuildField;
import me.chongchong.norway.annotation.Builder;
import me.chongchong.norway.bean.BuildFieldBean;
import me.chongchong.norway.bean.BuilderBean;
import me.chongchong.norway.internal.bean.BuildFieldDescriptor;
import me.chongchong.norway.internal.bean.BuilderDescriptor;

/**
 * Norway数据构建框架的服务入口
 * 
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class NorwayBuildService extends ApplicationObjectSupport implements ResourceLoaderAware, SmartLifecycle {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

	private Map<Class<?>, BuilderDescriptor> defaultBuilders = Maps.newHashMap();
	
	private Map<String, BuilderDescriptor> namedBuilders = Maps.newHashMap();
	
	private SetMultimap<Class<?>, BuildFieldDescriptor> fieldDescriptorMap = MultimapBuilder.hashKeys().hashSetValues().build();
	
	private Map<Class<?>, DataBuildSuit> beanBuildSuitMap = Maps.newHashMap();
	
	private boolean running = false;
	
	private String buildBeanPackages;

	public String getBuildBeanPackages() {
		return buildBeanPackages;
	}

	public void setBuildBeanPackages(String buildBeanPackages) {
		this.buildBeanPackages = buildBeanPackages;
	}

	private void addDefaultBuilderDescrptor(Class<?> clazz, BuilderDescriptor bd) {
		logger.info("add default BuilderDescriptor:{}", bd);
		BuilderDescriptor old = defaultBuilders.put(clazz, bd);
		if (old != null) {
			logger.warn("{} is replaced by {} for type {}", old, bd, clazz);
		}
	}
	
	private void addNamedBuilderDescrptor(String name, BuilderDescriptor bd) {
		logger.info("add named BuilderDescriptor:{}", bd);
		BuilderDescriptor old = namedBuilders.put(name, bd);
		if (old != null) {
			logger.warn("{} is replaced by {} for name {}", old, bd, name);
		}
	}
	
	private void addBuildFieldDescriptor(Class<?> clazz, BuildFieldDescriptor fd) {
		logger.info("add BuildFieldDescriptor:{}", fd);
		fieldDescriptorMap.put(clazz, fd);
	}

	/**
	 * 根据配置构建模型内容
	 * 
	 * @param beans 需要构建内容的模块集合
	 * @param clazz 构建的模型类型
	 * @param flags 构建的参数
	 */
	public <T> void build(Collection<? extends T> beans, Class<T> clazz, int flags) {
		try {
			DataBuildSuit suit = beanBuildSuitMap.get(clazz);
			if (suit == null) {
				return;
			}
			suit.buildBeans(beans, flags);
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
	
	/**
	 * 专门为单个对象适用的构建方法
	 * @param bean
	 * @param clazz
	 * @param flags
	 */
	public <T, V extends T> void build(V bean, Class<T> clazz, int flags) {
		build(Arrays.asList(bean), clazz, flags);
	}
	
	/**
	 * 专门为单个对象适用的构建方法
	 * @param bean
	 * @param flags
	 */
	public <V> void build(V bean, int flags) {
		build(Arrays.asList(bean), (Class<V>)bean.getClass(), flags);
	}

	/**
	 * 可以直接通过id列表创建出模型列表
	 * 
	 * @param ids
	 * @param clazz
	 * @param flags
	 * @return
	 */
	public <T> List<T> getBuildedList(List<Object> ids, Class<T> clazz, int flags) {
		BuilderDescriptor bd = defaultBuilders.get(clazz);
		if (bd == null) {
			throw new IllegalArgumentException(clazz + " canot build.");
		}
		
		Map<Object, Object> objects = bd.getObjects(ids, flags);
		List<T> list = Lists.newArrayList();
		for (Object id : ids) {
			list.add((T) objects.get(id));
		}
		return list;
	}
	
	/**
	 * 可以直接通过id列表创建出模型列表
	 * 
	 * @param ids
	 * @param builder
	 * @param flags
	 * @return
	 */
	public <T> List<T> getBuildedList(List<Object> ids, String builder, int flags) {
		BuilderDescriptor bd = namedBuilders.get(builder);
		if (bd == null) {
			throw new IllegalArgumentException(builder + " is not a builder name.");
		}
		
		Map<Object, Object> objects = bd.getObjects(ids, flags);
		List<T> list = Lists.newArrayList();
		for (Object id : ids) {
			list.add((T) objects.get(id));
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader(org.springframework.core.io.ResourceLoader)
	 */
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
	}

	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(getApplicationContext().getEnvironment().resolveRequiredPlaceholders(basePackage));
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.Lifecycle#start()
	 */
	@Override
	public void start() {
		running = true;
				
		scanBuildBeanPackages();
		
		scanBuilder();
		
		createBuildSuits();
	}

	private void createBuildSuits() {
		
		for (Entry<Class<?>, Collection<BuildFieldDescriptor>> entry : fieldDescriptorMap.asMap().entrySet()) {
			SetMultimap<BuilderDescriptor, BuildFieldDescriptor> builder2FieldsMap = MultimapBuilder.hashKeys().hashSetValues().build();
			
			for (BuildFieldDescriptor fieldDescriptor : entry.getValue()) {
				BuilderDescriptor builderDescriptor = null;
				if (StringUtils.hasText(fieldDescriptor.getBuilderName())) {
					builderDescriptor = namedBuilders.get(fieldDescriptor.getBuilderName());
				} else {
					Class<?> type;
					// 获取从类中获取
					try {
						Field field = entry.getKey().getDeclaredField(fieldDescriptor.getPropertyName());
						type = field.getType();
						if (Collection.class.isAssignableFrom(type)) {
							Type gt = field.getGenericType();
							if (gt != null) {
								type = (Class<?>) ((ParameterizedType)gt).getActualTypeArguments()[0];
							} else {
								type = fieldDescriptor.getType();
							}
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						continue;
					} 
					
					builderDescriptor = defaultBuilders.get(type);
				}
				
				if (builderDescriptor == null) {
					logger.warn("{} cannot be build.", fieldDescriptor);
					continue;
				}
				
				builder2FieldsMap.put(builderDescriptor, fieldDescriptor);
			}
			
			beanBuildSuitMap.put(entry.getKey(), new DefaultDataBuildSuit(entry.getKey(), builder2FieldsMap));
		}
	}
	
	private void scanBuildBeanPackages() {
		if (buildBeanPackages == null) {
			return;
		}

		// 扫描build model
		for (String viewModelPackage : Splitter.on(",").omitEmptyStrings().trimResults().splitToList(buildBeanPackages)) {
			String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
					resolveBasePackage(viewModelPackage) + "/" + "**/*.class";

			try {
				Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
						ClassMetadata cm = metadataReader.getClassMetadata();
						if (!cm.isAbstract() && !cm.isInterface() && cm.isIndependent()) {
							Class<?> clazz = ClassUtils.forName(cm.getClassName(), ClassUtils.getDefaultClassLoader());

							for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(clazz)) {
								if (pd.getWriteMethod() == null) {
									continue;
								}
								Field field = null;
								try {
									field = clazz.getDeclaredField(pd.getName());
								} catch (NoSuchFieldException e) {
									// do nothing
								} catch (Exception e) {
									logger.warn(e.getMessage(), e);
								}
								if (field == null) {
									continue;
								}
								BuildField bo = field.getAnnotation(BuildField.class);
								
								if (bo == null) {
									continue;
								}
								
								fieldDescriptorMap.put(clazz, new BuildFieldDescriptor(
										clazz, 
										pd.getName(), 
										bo.flag(), 
										bo.buildFlag(), 
										bo.idProperty(), 
										bo.type().equals(void.class) ? null : bo.type(), 
										bo.buildMethod()));
								
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
	private boolean checkBuilderSignature(Method m) {
		Class<?> returnType = m.getReturnType();
		Class<?>[] parameterTypes = m.getParameterTypes();
	
		if (returnType == null || !Map.class.isAssignableFrom(returnType)) {
			return false;
		}
		
		if (parameterTypes.length == 1) {
			return Collection.class.isAssignableFrom(parameterTypes[0]);
		} else if (parameterTypes.length == 2) {
			return (Collection.class.isAssignableFrom(parameterTypes[0]) && int.class.equals(parameterTypes[1]));
		}
		
		return false;
	}
	
	private void scanBuilder() {
		String[] beanNames = getApplicationContext().getBeanDefinitionNames();
		for (String beanName : beanNames) {
			Object bean = getApplicationContext().getBean(beanName);
			

			Class<?> serviceClass = ClassUtils.getUserClass(bean);
			
			if (BuilderBean.class.isAssignableFrom(serviceClass)) {
				
				BuilderBean bb = (BuilderBean) bean;
				
				try {
					Class<?> serviceClass2 = ClassUtils.getUserClass( bb.getBean());
					
					for (Method m : serviceClass2.getMethods()) {
						
						if (!m.getName().equals(bb.getMethod())) {
							continue;
						}
						
						if (!checkBuilderSignature(m)) {
							logger.warn("build method signature incorrect:" + m.toString());
							continue;
						}
						
						Class<?> valueType = null;
						if (StringUtils.hasText(bb.getForType())) {
							valueType = ClassUtils.forName(bb.getForType(), ClassUtils.getDefaultClassLoader());
						}
						
						if (valueType == null ) {
							if (m.getGenericReturnType() != null) {
								Type[] keyValueTypes = ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments();
								valueType = (Class<?>) keyValueTypes[1];
							} else {
								logger.warn("forType incorrect:{}" , bb);
								continue;
							}
						}
						
						BuilderDescriptor db = new BuilderDescriptor(bb.getName(), bean, m, valueType, bb.isDefaultBuilder());
						
						if (bb.isDefaultBuilder()) {
							addDefaultBuilderDescrptor(valueType, db);
						}
						
						if (StringUtils.hasText(bb.getName())) {
							addNamedBuilderDescrptor(bb.getName(), db);
						}
					}
					
				} catch (Throwable t) {
					throw Throwables.propagate(t);
				}
			} else if (BuildFieldBean.class.isAssignableFrom(serviceClass)) {
				BuildFieldBean bfb = (BuildFieldBean) bean;
				
				try {
					Class<?> c = ClassUtils.forName(bfb.getClazz(), ClassUtils.getDefaultClassLoader());
					Class<?> typeClass = null;
					if (StringUtils.hasText(bfb.getType())) {
						typeClass = ClassUtils.forName(bfb.getType(), ClassUtils.getDefaultClassLoader());
					}
					
					addBuildFieldDescriptor(c, new BuildFieldDescriptor(c, bfb.getProperty(), bfb.getFlag(), bfb.getBuildFlag(), bfb.getIdProperty(), typeClass, bfb.getBuilder()));
					
				} catch (Throwable t) {
					throw Throwables.propagate(t);
				}
				
			} else {
				// 查找出构建方法
				for (Method m : serviceClass.getMethods()) {
					Builder bm = m.getAnnotation(Builder.class);
					
					// 方法必须是2个参数，第一个是collection，第二个是int
					if (bm != null) {
						if (!checkBuilderSignature(m)) {
							logger.warn("build method signature incorrect:" + m.toString());
						} else {
							try {
								Class<?> valueType = bm.forType();
								if (void.class.equals(valueType) ) {
									if (m.getGenericReturnType() != null && (m.getGenericReturnType() instanceof ParameterizedType)) {
										Type[] keyValueTypes = ((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments();
										if (keyValueTypes[1] instanceof ParameterizedType) {
											valueType = (Class<?>) ((ParameterizedType) keyValueTypes[1]).getRawType();
										} else {
											valueType = (Class<?>) keyValueTypes[1];
										}
										
									} else {
										logger.warn("BuildMethod.forType() incorrect:" + m.toString());
										continue;
									}
								}
								
								BuilderDescriptor db = new BuilderDescriptor(bm.name(), bean, m, valueType, bm.auto());
								if (bm.auto()) {
									addDefaultBuilderDescrptor(valueType, db);
								} else {
									addNamedBuilderDescrptor(bm.name(), db);
								}
							} catch (Exception e) {
								throw Throwables.propagate(e);
							}
						}
					}
				}
			}
			
	
		}
	}
	/* (non-Javadoc)
	 * @see org.springframework.context.Lifecycle#stop()
	 */
	@Override
	public void stop() {
		running = false;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.Lifecycle#isRunning()
	 */
	@Override
	public boolean isRunning() {
		return running;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.Phased#getPhase()
	 */
	@Override
	public int getPhase() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.SmartLifecycle#isAutoStartup()
	 */
	@Override
	public boolean isAutoStartup() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.SmartLifecycle#stop(java.lang.Runnable)
	 */
	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
	}
	
	static interface DataBuildSuit {

		void buildBeans(Collection<?> beans, int flags);
	}
	
	static class DefaultDataBuildSuit implements DataBuildSuit {

		private Class<?> beanClass;
		private SetMultimap<BuilderDescriptor, BuildFieldDescriptor> builder2FieldsMap;
		
		public DefaultDataBuildSuit(Class<?> beanClass, SetMultimap<BuilderDescriptor, BuildFieldDescriptor> builder2FieldsMap) {
			this.beanClass = beanClass;
			this.builder2FieldsMap = builder2FieldsMap;
		}
		
		/* (non-Javadoc)
		 * @see me.chongchong.norway.DataBuildSuit#buildBeans(java.util.Collection, java.lang.Class, int)
		 */
		@Override
		public void buildBeans(Collection<?> beans, int flags) {
			
			for (Entry<BuilderDescriptor, Collection<BuildFieldDescriptor>> entry : builder2FieldsMap.asMap().entrySet()) {
				
				
				Set<Object> ids = new HashSet<Object>();
				
				BuildFieldDescriptor[] fields = entry.getValue().toArray(new BuildFieldDescriptor[0]);
				@SuppressWarnings("unchecked")
				List<Object>[] keyList = new List[fields.length];
				
				// 构建标记使用合集
				int buildFlag = 0;
				for (int i = 0;i < fields.length;i++) {
					keyList[i] = new ArrayList<Object>();
					for (Object bean : beans) {
						Object id = fields[i].getIdObject(bean);
						if (id instanceof Collection<?>) {
							ids.addAll((Collection<?>)id);
						} else if(id != null) {
							ids.add(id);
						}
						keyList[i].add(id);
					}
					buildFlag |= fields[i].getBuildFlag();
				}
				
				if (ids.isEmpty()) {
					continue;
				}
				
				
				Map<Object, Object> result = entry.getKey().getObjects(ids, buildFlag);
				
				Object[] beanArray = beans.toArray();
				
				for (int i = 0;i < fields.length;i++) {
					for (int j = 0; j < beanArray.length; j++) {
						Object key = keyList[i].get(j);
						if (key instanceof Collection<?>) {
							// 这种情况 key = id collection
							try {
								@SuppressWarnings("unchecked")
								Collection<Object> collection = (Collection<Object>) BeanUtils.instantiate(key.getClass());
								for (Object id : (Collection<?>) key ) {
									collection.add(result.get(id));
								}
								fields[i].setObject(beanArray[j], collection);
							} catch (Exception e) {
								
							}
						} else {
							// 这种情况 key = id
							fields[i].setObject(beanArray[j], result.get(key));
						}
						
					}
				}
			}
		}
	}
}
