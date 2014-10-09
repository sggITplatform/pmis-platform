package pmis.commons.config;

import java.io.InputStream;
import java.net.URL;

import pmis.commons.Constants;

public abstract class ConfigUtils
{
	/**
	 * 
	 * @Title: getUserResourceAsStream
	 * @author: youlun@126.com
	 * @Description: 动态加载xml文件,并且获得流文件
	 * @version: V1.0
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	public static InputStream getUserResourceAsStream(String resource) throws Exception
	{
		boolean hasLeadingSlash = resource.startsWith("/");
		String stripped = hasLeadingSlash ? resource.substring(1) : resource;

		InputStream stream = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null)
		{
			stream = classLoader.getResourceAsStream(resource);
			if (stream == null && hasLeadingSlash)
			{
				stream = classLoader.getResourceAsStream(stripped);
			}
		}

		if (stream == null)
		{
			stream = Constants.class.getClassLoader().getResourceAsStream(resource);
		}
		if (stream == null && hasLeadingSlash)
		{
			stream = Constants.class.getClassLoader().getResourceAsStream(stripped);
		}

		if (stream == null)
		{
			throw new Exception(resource + " not found");
		}
		return stream;
	}

	/**
	 * 
	 * @Title: getUserResourcePath
	 * @author: youlun@126.com
	 * @Description: 动态加载xml文件,并且获得xml绝对文件路径 <per> String
	 *               file=ConfigUtil.getUserResourcePath("com/rdp_Servlet.xml");
	 *               out:
	 *               /F:/rdp_workSpace/rdp-portal/rdp-portal-platform/target
	 *               /classes/com/rdp_Servlet.xml </per>
	 * @version: V1.0
	 * @param resource
	 * @return
	 * @throws Exception
	 */
	public static String getUserResourcePath(String resource) throws Exception
	{
		boolean hasLeadingSlash = resource.startsWith("/");
		String stripped = hasLeadingSlash ? resource.substring(1) : resource;
		URL url = null;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null)
		{
			url = classLoader.getResource(resource);
			if (url == null && hasLeadingSlash)
			{
				url = classLoader.getResource(stripped);
			}
		}

		if (url == null)
		{
			url = Constants.class.getClassLoader().getResource(resource);
		}
		if (url == null && hasLeadingSlash)
		{
			url = Constants.class.getClassLoader().getResource(stripped);
		}

		if (url == null)
		{
			throw new Exception(resource + " not found");
		}
		return url.getFile();
	}

	public static void main(String[] args)
	{
		try
		{
			InputStream file = ConfigUtils.getUserResourceAsStream("com/rdp_Servlet.xml");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
