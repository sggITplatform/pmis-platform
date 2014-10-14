package pmis.web.support.util;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 
 * @ClassName: SpringUtil
 * @author: wuwh
 * @CreateDate: 2014-4-2 下午1:56:20
 * @UpdateUser: wuwh
 * @UpdateDate: 2014-4-2 下午1:56:20
 * @UpdateRemark: 说明本次修改内容
 * @Description: 或者spring容器中的bean
 * @version: V1.0
 */
public class SpringUtils implements ApplicationContextAware
{

	private static ApplicationContext applicationContext;

	public static Object getBean(String beanName)
	{
		return applicationContext.getBean(beanName);
	}

	public static <T> T getBeanByType(Class<T> beanType)
	{
		Map<String, T> beans = SpringUtils.applicationContext.getBeansOfType(beanType);
		if (beans != null && beans.size() > 0)
			return beans.values().iterator().next();
		return null;
	}

	public static Map getBeansByType(Class beanType)
	{
		return applicationContext.getBeansOfType(beanType);
	}

	public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}
}
