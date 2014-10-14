/**   
* @Title: TestServiceImpl.java 
* @Package com.szewec.ssdp.test.service.impl 
* @Description: 用一句话描述该文件做什么 
* Copyright: Copyright (c) 2014 
* Company:wuwh team by iss
* @author: wuwh  
* @date: 2014年8月22日 上午10:46:19 
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
package pmis.web.support.example.service.impl;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pmis.web.support.example.dao.IssdpTestDao;
import pmis.web.support.example.entity.Tree;
import pmis.web.support.example.service.ITestService;
import pmis.web.support.service.impl.DaoBasedServiceImpl;
import pmis.web.support.util.PageUtils;

@Service
public class TestServiceImpl extends DaoBasedServiceImpl<Tree, String>
		implements ITestService {
	private Logger logger=LoggerFactory.getLogger(TestServiceImpl.class);

	private IssdpTestDao dao;
	@Autowired
	public TestServiceImpl(IssdpTestDao dao) {
		super(dao);
		this.dao=dao;
		logger.info(dao.getClass()+"---");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public PageUtils getPageInfoByHql(PageUtils util) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPageInfoByHql(util);
	}
	@Override
	public PageUtils getPageInfoBySql(PageUtils util) throws Exception {
		// TODO Auto-generated method stub
		return dao.getPageInfoBySql(util);
	}
	@Override
	public void saveInfo() throws Exception {
		// TODO Auto-generated method stub
		Tree tree=new Tree();
		tree.setId("11111111111111");
		tree.setName("xxxxxxxxxxx");
		dao.saveObject(tree);
		tree.setId(null);
		dao.saveObject(tree);
	}
	@Override
	public List getRsHqlPar() throws Exception {
		// TODO Auto-generated method stub
		return dao.getRsHqlPar();
	}
	@Override
	public List getRsSqlPar() throws Exception {
		logger.info("========"+dao.getClass());
		// TODO Auto-generated method stub
		return dao.getRsSqlPar();
	}
	
}
