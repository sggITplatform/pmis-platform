package pmis.commons.lang;

public abstract class ObjectUtils extends org.apache.commons.lang.ObjectUtils
{
	/**
	 * <p>
	 * Returns a default value if the object passed is {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * ObjectUtil.isNOtNull(null)= false
	 * ObjectUtils.isNOtNull("")= false
	 * ObjectUtils.isNOtNull("ddd")= true
	 * </pre>
	 * 
	 * @param object
	 *            the {@code Object} to test, may be {@code null}
	 */
	public static boolean isNotNull(Object obj)
	{
		if (null == obj)
		{
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Returns a default value if the object passed is {@code null}.
	 * </p>
	 * 
	 * <pre>
	 * ObjectUtil.isNull(null)= true
	 * ObjectUtils.isNull("")= true
	 * ObjectUtils.isNull("ddd")= false
	 * </pre>
	 * 
	 * @param object
	 *            the {@code Object} to test, may be {@code null}
	 */
	public static boolean isNull(Object obj)
	{
		if (null == obj)
		{
			return true;
		}
		return false;
	}
}
