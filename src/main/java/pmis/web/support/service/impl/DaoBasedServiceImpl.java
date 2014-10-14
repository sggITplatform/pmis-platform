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
import pmis.web.support.service.GenericService;
import pmis.web.support.util.PageUtils;

@SuppressWarnings("all")
public class DaoBasedServiceImpl<T, PK extends Serializable> implements GenericService<T, PK>
{

	//	@Resource
	private EntityDao<T, PK> basedao;

	public DaoBasedServiceImpl(EntityDao<T, PK> dao)
	{
		this.basedao = dao;
	}

	@Override
	public void deleteEntity(T entity) throws Exception
	{

		basedao.deleteObject(entity);
	}

	@Override
	public void deleteEntityById(PK id) throws Exception
	{

		basedao.deleteObjectById(id);
	}

	@Override
	public T getEntity(PK id) throws Exception
	{

		return (T) basedao.getObject(id);
	}

	@Override
	public List<T> getObjectsByEntity() throws Exception
	{

		return basedao.getObjects();
	}

	@Override
	public PageUtils<T> getObjectsByEntity(PageUtils<T> util) throws Exception
	{

		return basedao.getObjects(util);
	}

	@Override
	public PK saveEntity(T entity) throws Exception
	{

		return (PK) basedao.saveObject(entity);
	}

	@Override
	public void saveObjects(List<T> list) throws Exception
	{

		basedao.saveObjects(list);
	}

	@Override
	public void saveOrUpdateEntity(T entity) throws Exception
	{

		basedao.saveOrUpdate(entity);
	}

	@Override
	public void updateEntity(T entity) throws Exception
	{

		basedao.updateObject(entity);
	}

	@Override
	public void updateObjects(List<T> list) throws Exception
	{

		basedao.updateObjects(list);
	}

}
