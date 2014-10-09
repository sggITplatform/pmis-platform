package pmis.commons.lang;

import java.util.Collection;

import org.apache.commons.collections.ListUtils;

/**
 * 
 * @ClassName: CollectionUtils
 * @author: youlun@126.com
 * @Description: 集合工具
 * @version: V1.0
 */
public abstract class CollectionUtils extends ListUtils
{
	/**
	 * 
	 * @Title: isEmpty
	 *         <p>
	 *         check collection is empty
	 *         </p>
	 * 
	 *         <pre>
	 * CollectionUtil.isEmpty(null)   = true
	 * Map.put("ddd","ddd");
	 * CollectionUtil.isEmpty(Map)=false
	 * </pre>
	 * 
	 */
	public static boolean isEmpty(Collection collection)
	{
		return null != collection && !collection.isEmpty() ? false : true;
	}

	/**
	 * 
	 * @Title: isNotEmpty
	 * 
	 *         <p>
	 *         check collection is empty
	 *         </p>
	 * 
	 *         <pre>
	 * CollectionUtil.isNotEmpty(null)   = false
	 * Map.put("ddd","ddd");
	 * CollectionUtil.isNotEmpty(Map)=true
	 * </pre>
	 * 
	 */
	public static boolean isNotEmpty(Collection collection)
	{
		return null != collection && !collection.isEmpty() ? true : false;
	}

}
