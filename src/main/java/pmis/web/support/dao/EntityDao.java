/**   
 * @Title: ISsdpBaseDao.java 
 * @Package com.szewec.ssdp.framework.core.dao 
 * @Description: 用一句话描述该文件做什么 
 * Copyright: Copyright (c) 2014 
 * Company:wuwh team by iss
 * @author: wuwh   
 * @date: 2014年6月11日 下午8:07:37 
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
package pmis.web.support.dao;

import java.util.List;

import pmis.web.support.util.PageUtils;

public interface EntityDao<T, PK extends java.io.Serializable>
{
	public void clear() throws Exception;

	/**
	 * 删除数据
	 * 
	 * @param object
	 *            实体对象
	 */
	public void deleteObject(T entity) throws Exception;

	/**
	 * 
	 * 
	 * @Title: deleteObjectById
	 * @Description: <p>
	 *               根据主键删除
	 *               <p>
	 * 
	 *               <pre>
	 *     	这里描述这个方法的使用方法 – 可选
	 * </pre>
	 * @param: <p>
	 * @param id
	 * @param: <p>
	 * @throws Exception
	 *             <p>
	 * @date: 2014年6月12日
	 * @return: void
	 * @throws
	 * 
	 */
	public void deleteObjectById(PK id) throws Exception;

	/**
	 * 批量删除数据
	 * 
	 * @param list
	 *            实体集
	 * @throws Exception
	 */
	public void deletesObject(List<T> list) throws Exception;

	/**
	 * 
	 * 
	 * @Title: exists
	 * @Description: <p>
	 *               判断该Id的实体是否存在
	 *               <p>
	 * 
	 *               <pre>
	 *     	这里描述这个方法的使用方法 – 可选
	 * </pre>
	 * @param: <p>
	 * @param id
	 * @param: <p>
	 * @return
	 * @param: <p>
	 * @throws Exception
	 *             <p>
	 * @date: 2014年6月12日
	 * @return: boolean
	 * @throws
	 * 
	 */

	public boolean exists(PK id) throws Exception;

	/**
	 * 
	 * 
	 * @Title: getRsBySqlAndPram
	 * @Description: <p>
	 *               根据参数获取sql
	 *               <p>
	 * 
	 *               <pre>
	 *     	这里描述这个方法的使用方法 – 可选
	 * </pre>
	 * @param: <p>
	 * @param sql
	 * @param: <p>
	 * @param paramlist
	 * @param: <p>
	 * @return
	 * @param: <p>
	 * @throws Exception
	 *             <p>
	 * @date: 2014年8月25日
	 * @return: List<T>
	 * @throws
	 * 
	 */
	// public  List<T> getRsBySqlAndPram(final String sql, final Object... paramlist) throws Exception;

	public void flush() throws Exception;

	/**
	 * 获取单一对象
	 * 
	 * @param clazz
	 *            实体类
	 * @param id
	 *            主键
	 * @return
	 */
	public T getObject(PK id) throws Exception;

	/**
	 * 获取对象集
	 * 
	 * @param clazz
	 *            实体类
	 * @return 对象集
	 */
	public List<T> getObjects() throws Exception;

	/**
	 * 
	 * 
	 * @Title: getObjects
	 * @Description: <p>
	 *               单实体分页获取
	 *               <p>
	 * 
	 *               <pre>
	 *     	这里描述这个方法的使用方法 – 可选
	 * </pre>
	 * @param: <p>
	 * @param currentPage
	 * @param: <p>
	 * @param pageSize
	 * @param: <p>
	 * @return
	 * @param: <p>
	 * @throws Exception
	 *             <p>
	 * @date: 2014年6月12日
	 * @return: List<T>
	 * @throws
	 * 
	 */
	public PageUtils<T> getObjects(PageUtils<T> util) throws Exception;

	/**
	 * 
	 * 
	 * @Title: countAll
	 * @Description: <p>
	 *               实体总记录数获取
	 *               <p>
	 * 
	 *               <pre>
	 *     	这里描述这个方法的使用方法 – 可选
	 * </pre>
	 * @param: <p>
	 * @return
	 * @param: <p>
	 * @throws Exception
	 *             <p>
	 * @date: 2014年6月12日
	 * @return: int
	 * @throws
	 * 
	 */
	public int getObjectsCount() throws Exception;

	/**
	 * 添加对象
	 * 
	 * @param object
	 *            实体类
	 * @throws Exception
	 */
	public PK saveObject(T entity) throws Exception;

	/**
	 * 批量保存或更新数据
	 * 
	 * @param list
	 *            数据集
	 * @throws Exception
	 *             异常信息
	 */
	public void saveObjects(List<T> list) throws Exception;

	/**
	 * 保存或更新一个实体对象
	 * 
	 * @param object
	 *            实体对象
	 * @throws Exception
	 *             异常信息
	 */
	public void saveOrUpdate(T entity) throws Exception;

	/**
	 * 
	 * 
	 * @Title: getRsByHqlAndPram
	 * @Description: <p>
	 *               带参数的查询
	 *               <p>
	 * 
	 *               <pre>
	 *     	这里描述这个方法的使用方法 – 可选
	 * </pre>
	 * @param: <p>
	 * @param hql
	 * @param: <p>
	 * @param paramlist
	 * @param: <p>
	 * @return
	 * @param: <p>
	 * @throws Exception
	 *             <p>
	 * @date: 2014年8月25日
	 * @return: List<T>
	 * @throws
	 * 
	 */
	//public  List<T> getRsByHqlAndPram(final String hql, final Object... paramlist) throws Exception;

	/**
	 * 更新对象
	 * 
	 * @param object
	 *            实体对象
	 * @throws Exception
	 */
	public void updateObject(T entity) throws Exception;

	/**
	 * 批量保存或更新数据
	 * 
	 * @param list
	 *            数据集
	 * @throws Exception
	 *             异常信息
	 */
	public void updateObjects(List<T> list) throws Exception;
}
