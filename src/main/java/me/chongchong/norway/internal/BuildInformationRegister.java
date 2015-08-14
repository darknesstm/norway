/**
 * 
 */
package me.chongchong.norway.internal;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import com.google.common.collect.Maps;

import me.chongchong.norway.internal.bean.BuildFieldDescriptor;
import me.chongchong.norway.internal.bean.BuilderDescriptor;

/**
 * @author DarknessTM (askkoy@163.com)
 *
 */
public class BuildInformationRegister {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private BuildInformationRegister() {
		
	}
	
	public static final BuildInformationRegister INSTANCE = new BuildInformationRegister();
	
	private Map<Class<?>, BuildFieldDescriptor[]> fieldDescriptorMap = new HashMap<Class<?>, BuildFieldDescriptor[]>();
	
	private Map<Class<?>, BuilderDescriptor> defaultBuilders = Maps.newHashMap();
	private Map<String, BuilderDescriptor> namedBuilders = Maps.newHashMap();
	
	public void addDefaultBuilderDescrptor(Class<?> clazz, BuilderDescriptor bd) {
		BuilderDescriptor old = defaultBuilders.put(clazz, bd);
		if (old != null) {
			log.warn("{} is replaced by {} for type {}", old, bd, clazz);
		}
	}
	
	public void addNamedBuilderDescrptor(String name, BuilderDescriptor bd) {
		BuilderDescriptor old = namedBuilders.put(name, bd);
		if (old != null) {
			log.warn("{} is replaced by {} for name {}", old, bd, name);
		}
	}
	
	public boolean addFieldDescriptors(Class<?> clazz, BuildFieldDescriptor[] fields) {
		return fieldDescriptorMap.put(clazz, fields) == null;
	}
	
	public BuildFieldDescriptor[] getFieldDescriptors(Class<?> clazz) {
		return fieldDescriptorMap.get(clazz);
	}
	
	protected String resolveBasePackage(ApplicationContext applicationContext, String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(applicationContext.getEnvironment().resolveRequiredPlaceholders(basePackage));
	}
	
//	public void refreshFieldDescriptor(ApplicationContext applicationContext, String viewModelPackage) {
//		// 扫描view model
//		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//
//		MetadataReaderFactory metadataReaderFactory =
//				new CachingMetadataReaderFactory(resourcePatternResolver);
//		
//		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
//				resolveBasePackage(applicationContext, viewModelPackage) + "/" + "**/*.class";
//		
//		try {
//			Resource[] resources =  resourcePatternResolver.getResources(packageSearchPath);
//			for (Resource resource : resources) {
//				if (resource.isReadable()) {
//					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
//					ClassMetadata cm = metadataReader.getClassMetadata();
//					if (!cm.isAbstract() && !cm.isInterface() && cm.isIndependent()) {
//						Class<?> clazz = ClassUtils.forName(cm.getClassName(), ClassUtils.getDefaultClassLoader());
//						
//						if (clazz == null) {
//							continue;
//						}
//						
//						MethodAccess methodAccess = MethodAccess.get(clazz);
//						
//						List<BuildFieldDescriptor> descriptors = Lists.newArrayList();
//						
//						for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(clazz)) {
//							if (pd.getWriteMethod() == null) {
//								continue;
//							}
//							Field field = null;
//							try {
//								field = clazz.getDeclaredField(pd.getName());
//							} catch (NoSuchFieldException e) {
//								// do nothing
//							} catch (Exception e) {
//								log.warn(e.getMessage(), e);
//							} 
//							if (field == null) {
//								continue;
//							}
//							
//							BuildObject bo = field.getAnnotation(BuildObject.class);
//							if (bo != null) {
//								Builder builder = null;
//								if (StringUtils.isNotBlank(bo.buildMethod())) {
//									// 优先使用指定id的
//									builder = idBuilders.get(bo.buildMethod());
//									
//									if (builder == null) {
//										logger.warn("指定id的构建方法不存在[" + bo.buildMethod() +"]");
//									}
//								}
//								
//								if (builder == null) {
//									Class<?> type = field.getType();
//									if (Collection.class.isAssignableFrom(type)) {
//										Type gt = field.getGenericType();
//										if (gt != null) {
//											type = (Class<?>) ((ParameterizedType)gt).getActualTypeArguments()[0];
//										} else {
//											type = bo.type();
//										}
//									}
//									builder = clazzBuilders.get(type);
//									
//									if (builder == null) {
//										logger.warn("无法构建类型[" + type.getName() +"]" );
//									}
//								}
//								
//								if (builder != null) {
//									
//									PropertyDescriptor pdId = BeanUtils.getPropertyDescriptor(clazz, bo.idField());
//									
//									if (pdId != null) {
//										descriptors.add(new BuildObjectDescriptor(methodAccess, methodAccess.getIndex(pdId.getReadMethod().getName()), 
//												methodAccess.getIndex(pd.getWriteMethod().getName()), bo.flag(), bo.buildFlag(), builder));
//									} else {
//										logger.warn("指定的id属性不存在[" + bo.idField() +"]" );
//									}
//									
//								}
//							}
//						}
//						
//						BuildService.buildObjectDescriptors.put(clazz, 
//								descriptors.toArray(new BuildObjectDescriptor[descriptors.size()]));
//					}
//				}
//			}
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
	
}
