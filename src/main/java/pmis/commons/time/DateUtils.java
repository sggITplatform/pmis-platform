package pmis.commons.time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @ClassName: DateUtils
 * @description: 日期获取类
 * @author: youlun@126.com
 * @version: V1.0
 * 
 */
public abstract class DateUtils extends org.apache.commons.lang.time.DateUtils
{

	public static String format(String fmt, Date dt)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			return sdf.format(dt);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}
	}

}
