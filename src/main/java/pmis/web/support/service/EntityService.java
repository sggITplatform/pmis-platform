/**   
 * @Title: IssdpBaseService.java 
 * @Package com.szewec.ssdp.framework.service 
 * @Description: 定义基本的service泛型接口
 * Copyright: Copyright (c) 2014 
 * Company:wuwh team by iss
 * @author: wuwh  
 * @date: 2014年8月22日 上午10:06:55 
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
package pmis.web.support.service;

import java.io.Serializable;
import java.util.List;

import pmis.web.support.util.PageUtils;

public interface EntityService<T, PK extends Serializable>
{

	/**
	 * 删除指定的实体
	 * 
	 * @param entity
	 *            实体对象
	 */
	public abstract void deleteEntity(T entity) throws Exception;

	/**
	 * 删除指定的实体,根据主键PK
	 * 
	 * @param PK
	 */
	public abstract void deleteEntityById(PK id) throws Exception;

	/**
	 * 按主键取记录
	 * 
	 * @param id
	 *            主键值
	 * @return 记录实体对象，如果没有符合主键条件的记录，则返回null
	 */
	public abstract T getEntity(PK id) throws Exception;

	/**
	 * 获取全部实体
	 * 
	 * @return 返回一个list集合数据
	 */
	public abstract List<T> getObjectsByEntity() throws Exception;

	/**
	 * 获取带有分页的实体集合对象
	 * 
	 * @param entity
	 *            实体对象
	 */
	public abstract PageUtils<T> getObjectsByEntity(PageUtils<T> util) throws Exception;

	/**
	 * 插入一个实体（在数据库INSERT一条记录）
	 * 
	 * @param entity
	 *            实体对象
	 */
	public abstract PK saveEntity(T entity) throws Exception;

	/**
	 * 批量增加
	 * 
	 * @param list
	 *            数据集
	 * @throws Exception
	 *             异常信息
	 */
	public abstract void saveObjects(List<T> list) throws Exception;

	/**
	 * 增加或更新实体
	 * 
	 * @param entity
	 *            实体对象
	 */
	public abstract void saveOrUpdateEntity(T entity) throws Exception;

	/**
	 * 修改一个实体对象（UPDATE一条记录）
	 * 
	 * @param entity
	 *            实体对象
	 */
	public abstract void updateEntity(T entity) throws Exception;

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
