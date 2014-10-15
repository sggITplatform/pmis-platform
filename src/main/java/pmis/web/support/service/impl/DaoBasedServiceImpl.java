/**   
 * @Title: SsdpBaseServiceImpl.java 
 * @Package com.szewec.ssdp.framework.service.impl 
 * @Description: 用一句话描述该文件做什么 
 * Copyright: Copyright (c) 2014 
 * Company:wuwh team by iss
 * @author: wuwh  
 * @date: 2014年8月22日 上午10:07:33 
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
package pmis.web.support.service.impl;

import java.io.Serializable;
import java.util.List;

import pmis.web.support.dao.EntityDao;
import pmis.web.support.service.EntityService;
import pmis.web.support.util.PageUtils;

@SuppressWarnings("all")
public class DaoBasedServiceImpl<T, PK extends Serializable> implements EntityService<T, PK>
{
	private EntityDao<T, PK> _dao;

	public DaoBasedServiceImpl(EntityDao<T, PK> dao)
	{
		super();
		_dao = dao;
	}

	public EntityDao<T, PK> getDao()
	{
		return _dao;
	}

	public void setDao(EntityDao<T, PK> dao)
	{
		_dao = dao;
	}

	@Override
	public void deleteEntity(T entity) throws Exception
	{
		_dao.deleteObject(entity);
	}

	@Override
	public void deleteEntityById(PK id) throws Exception
	{

		_dao.deleteObjectById(id);
	}

	@Override
	public T getEntity(PK id) throws Exception
	{
		return (T) _dao.getObject(id);
	}

	@Override
	public List<T> getObjectsByEntity() throws Exception
	{
		return _dao.getObjects();
	}

	@Override
	public PageUtils<T> getObjectsByEntity(PageUtils<T> util) throws Exception
	{
		return _dao.getObjects(util);
	}

	@Override
	public PK saveEntity(T entity) throws Exception
	{
		return (PK) _dao.saveObject(entity);
	}

	@Override
	public void saveObjects(List<T> list) throws Exception
	{
		_dao.saveObjects(list);
	}

	@Override
	public void saveOrUpdateEntity(T entity) throws Exception
	{
		_dao.saveOrUpdate(entity);
	}

	@Override
	public void updateEntity(T entity) throws Exception
	{
		_dao.updateObject(entity);
	}

	@Override
	public void updateObjects(List<T> list) throws Exception
	{
		_dao.updateObjects(list);
	}

}
