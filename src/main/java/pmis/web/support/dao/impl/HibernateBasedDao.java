/**   
 * @Title: RdpBaseDaoImpl.java 
 * @Package com.szewec.ssdp.framework.core.dao.impl 
 * @Description: 用一句话描述该文件做什么 
 * Copyright: Copyright (c) 2014 
 * Company:wuwh team by iss
 * @author: wuwh   
 * @date: 2014年6月11日 下午8:19:22 
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
package pmis.web.support.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pmis.commons.lang.StringUtils;
import pmis.web.support.dao.GenericDao;
import pmis.web.support.util.PageUtils;

public class HibernateBasedDao<T, PK extends java.io.Serializable> extends AbstractHibernateBasedDao implements
		GenericDao<T, PK>
{
	private Class<T> entityClass;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String pkName = null;

	private final String STR_HQL;

	private final String STR_HQL_COUNT;// 总记录数据获取HQL

	@SuppressWarnings("unchecked")
	public HibernateBasedDao()
	{
		try
		{
			this.entityClass = getSuperClassGenricType(this.getClass());
		}
		catch (Exception e)
		{
			logger.error("Initiate RdpBaseDaoImpl class error: " + e.getMessage(), e);
			this.entityClass = null;
		}
		Field[] fields = this.entityClass.getDeclaredFields();
		for (Field f : fields)
		{
			logger.info("---" + f.getName());
			if (f.isAnnotationPresent(Id.class))
			{
				logger.info("-----注解Id为: " + f.getName());
				this.pkName = f.getName();
			}
		}
		if (null == this.entityClass)
		{
			this.STR_HQL = null;
			this.STR_HQL_COUNT = null;

		}
		else
		{
			logger.info("---------" + this.pkName);

			Class<T> t = this.entityClass;
			logger.info("===" + t.getName());
			this.STR_HQL = new StringBuffer("from ").append(this.entityClass.getSimpleName())
					.append(" order by " + pkName + " asc").toString();
			this.STR_HQL_COUNT = new StringBuffer("select count(*) from ").append(this.entityClass.getSimpleName())
					.toString();
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T aggregate(final String hql, final Object... paramlist)
	{
		Query query = this.getSession().createQuery(hql);
		this.setParameters(query, paramlist);
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	private <T> T aggregateBySql(final String sql, final Object... paramlist)
	{
		Query query = this.getSession().createSQLQuery(sql);
		this.setParameters(query, paramlist);
		return (T) query.uniqueResult();
	}

	@Override
	public void clear() throws Exception
	{

		this.getSession().clear();
	}

	@Override
	public void deleteObject(T entity) throws Exception
	{
		this.getSession().delete(entity);
	}

	@Override
	public void deleteObjectById(PK id) throws Exception
	{
		this.getSession().delete(this.getObject(id));
	}

	@Override
	public void deletesObject(List<T> list) throws Exception
	{
		Session session = this.getSession();
		for (T t : list)
		{
			session.delete(t);
		}
	}

	@Override
	public boolean exists(PK id) throws Exception
	{

		return this.getObject(id) != null;
	}

	@Override
	public void flush() throws Exception
	{

		this.getSession().flush();
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getObject(PK id) throws Exception
	{
		// 直接从DB检索，么有数据返回null
		return (T) this.getSession().get(this.entityClass, id);
	}

	protected List<?> getObjectByHql(String hqlStr) throws Exception
	{
		return this.getSession().createQuery(hqlStr).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> getObjects() throws Exception
	{
		logger.info(this.entityClass.getSimpleName() + "--------");
		return this.getSession().createQuery(this.STR_HQL).list();
	}

	@Override
	public PageUtils<T> getObjects(PageUtils util) throws Exception
	{
		// 获取总记录数
		int totalCount = this.getObjectsCount();
		util.setTotalCount(totalCount);
		util = this.getRelCurrentPage(util);
		int currentPage = util.getCurrentPage();
		Query query = this.getSession().createQuery(this.STR_HQL);
		query.setMaxResults(util.getPageSize());
		int start = PageUtils.getIndex(util.getPageSize(), util.getCurrentPage());
		if (start != 0)
		{
			query.setFirstResult(start);
		}
		List<T> results = query.list();
		util.setObjectList(results);// 记录集
		return util;
	}

	@Override
	public int getObjectsCount() throws Exception
	{
		Long total = aggregate(this.STR_HQL_COUNT);
		return total.intValue();
	}

	private PageUtils getRelCurrentPage(PageUtils util)
	{
		util.setTotalPage(util.countTotalPage(util.getPageSize(), util.getTotalCount()));
		int currentPage = util.getCurrentPage();
		int toalPage = util.getTotalPage();
		if (currentPage >= toalPage)
		{
			currentPage = toalPage;
		}
		if (currentPage <= 0)
		{
			currentPage = 1;
		}
		util.setCurrentPage(currentPage);
		return util;
	}

	protected List<T> getRsByHql(String hqlStr) throws Exception
	{
		return this.getSession().createQuery(hqlStr).list();
	}

	protected PageUtils<T> getRsByHql(String hqlStr, PageUtils util) throws Exception
	{
		int totalCount = this.getRsCountsByHql(hqlStr);
		util.setTotalCount(totalCount);
		util = this.getRelCurrentPage(util);
		int currentPage = util.getCurrentPage();
		Query query = this.getSession().createQuery(hqlStr);
		query.setMaxResults(util.getPageSize());
		int start = PageUtils.getIndex(util.getPageSize(), util.getCurrentPage());
		if (start != 0)
		{
			query.setFirstResult(start);
		}
		List<T> results = query.list();
		util.setObjectList(results);// 记录集
		return util;
	}

	@SuppressWarnings("unchecked")
	protected List<T> getRsByHqlAndPram(final String hql, final Object... paramlist) throws Exception
	{
		Query query = this.getSession().createQuery(hql);
		this.setParameters(query, paramlist);
		return query.list();

	}

	protected List getRsBySql(String sqlStr) throws Exception
	{
		return this.getSession().createSQLQuery(sqlStr).list();
	}

	protected PageUtils getRsBySql(String sqlStr, PageUtils util) throws Exception
	{

		int totalCount = this.getRsCountsBySql(sqlStr);
		util.setTotalCount(totalCount);
		util = this.getRelCurrentPage(util);
		int currentPage = util.getCurrentPage();
		Query query = this.getSession().createSQLQuery(sqlStr);
		query.setMaxResults(util.getPageSize());
		int start = PageUtils.getIndex(util.getPageSize(), util.getCurrentPage());
		if (start != 0)
		{
			query.setFirstResult(start);
		}
		List<T> results = query.list();
		util.setObjectList(results);// 记录集
		return util;
	}

	@SuppressWarnings("unchecked")
	protected List getRsBySqlAndPram(final String sql, final Object... paramlist) throws Exception
	{
		Query query = this.getSession().createSQLQuery(sql);
		this.setParameters(query, paramlist);
		return query.list();

	}

	protected int getRsCountsByHql(String hqlStr) throws Exception
	{
		StringBuffer str = null;
		if (hqlStr.toLowerCase().indexOf("select") >= 0)
		{
			//			str = new StringBuffer("select count(1) from (").append(hqlStr)
			//					.append(") ");
			String[] hqls = StringUtils.split(hqlStr, "from");
			if (hqls.length > 1)
			{
				hqlStr = StringUtils.remove(hqlStr, hqls[0]);
			}
			else
			{
				throw new Exception("Hql 不正确");
			}
		}
		str = new StringBuffer("select count(*)  ").append(hqlStr);

		Long total = aggregate(str.toString());
		return total.intValue();
	}

	protected int getRsCountsBySql(String sqlStr) throws Exception
	{

		StringBuffer str = new StringBuffer("select count(*) from ( ").append(sqlStr).append(" ) cxg ");
		Integer total = new Integer(this.aggregateBySql(str.toString()) + "");
		return total.intValue();
	}

	private Class getSuperClassGenricType(Class clazz) throws Exception
	{
		return getSuperClassGenricType(clazz, 0);
	}

	private Class getSuperClassGenricType(Class clazz, int index) throws IndexOutOfBoundsException
	{

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType))
		{
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0)
		{
			return Object.class;
		}
		if (!(params[index] instanceof Class))
		{
			return Object.class;
		}
		return (Class) params[index];
	}

	@SuppressWarnings("unchecked")
	@Override
	public PK saveObject(T entity) throws Exception
	{
		return (PK) this.getSession().save(entity);
	}

	@Override
	public void saveObjects(List<T> list) throws Exception
	{
		Session session = this.getSession();
		for (T t : list)
		{
			session.save(t);
		}
	}

	@Override
	public void saveOrUpdate(T entity) throws Exception
	{
		this.getSession().saveOrUpdate(entity);
	}

	/**
	 * 
	 * 
	 * @Title: setParameters
	 * @Description: <p>
	 *               加参数信息
	 *               <p>
	 * 
	 *               <pre>
	 * 		这里描述这个方法的使用方法 – 可选
	 * </pre>
	 * @param: <p>
	 * @param query
	 * @param: <p>
	 * @param paramlist
	 *            <p>
	 * @date: 2014年6月12日
	 * @return: void
	 * @throws
	 * 
	 */
	private void setParameters(Query query, Object[] paramlist)
	{
		if (paramlist != null)
		{
			for (int i = 0; i < paramlist.length; i++)
			{
				if (paramlist[i] instanceof Date)
				{
					// TODO 难道这是bug 使用setParameter不行？？
					query.setTimestamp(i, (Date) paramlist[i]);
				}
				else
				{
					query.setParameter(i, paramlist[i]);
				}
			}
		}
	}

	@Override
	public void updateObject(T entity) throws Exception
	{
		this.getSession().update(entity);
	}

	public void updateObjects(List<T> list) throws Exception
	{
		Session session = this.getSession();
		for (T t : list)
		{
			session.update(t);
		}
	}

	protected int updateOrDeleteHql(String hqlStr) throws Exception
	{
		Query query = this.getSession().createQuery(hqlStr);
		return query.executeUpdate();
	}

	protected int updateOrDeleteSql(String sql) throws Exception
	{
		Query query = this.getSession().createSQLQuery(sql);
		return query.executeUpdate();
	}
}
