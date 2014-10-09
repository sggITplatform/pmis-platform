/**   
 * @Title: RdpSqlServer2008Dialect.java 
 * @Package com.szewec.ssdp.util.qq 
 * @Description: 用一句话描述该文件做什么 
 * Copyright: Copyright (c) 2014 
 * Company:wuwh team by iss
 * @author: wuwh   
 * @date: 2014年6月25日 下午11:40:51 
 * @version: V1.0
 * update Release(文件修正记录)
 * <pre>
 * author--updateDate--description----------------------Flag————
 * wuwh    2014-5-1    测试codesyle                      #wuwh001
 *
 *
 *
 * </pre>
 *
 */
package pmis.commons.sql;

import org.hibernate.dialect.SQLServer2008Dialect;

/**
 * 
 * @ClassName: RdpSqlServer2008Dialect
 * @description: SQl2008方言转换
 * @author: hp
 * @date: 2014年8月21日 上午9:41:45
 * @version: V1.0
 * 
 */
public class RdpSqlServer2008Dialect extends SQLServer2008Dialect
{

	public RdpSqlServer2008Dialect()
	{
		super();
		registerHibernateType(1, "string");
		registerHibernateType(-9, "string");
		registerHibernateType(-16, "string");
		registerHibernateType(3, "double");
	}
}