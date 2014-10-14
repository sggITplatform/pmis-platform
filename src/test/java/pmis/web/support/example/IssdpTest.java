/**   
 * @Title: TestService.java 
 * @Package com.szewec.ssdp.test.service 
 * @Description: 用一句话描述该文件做什么 
 * Copyright: Copyright (c) 2014 
 * Company:wuwh team by iss
 * @author: wuwh   
 * @date: 2014年6月11日 下午9:33:40 
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
package pmis.web.support.example;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pmis.web.support.example.entity.Tree;
import pmis.web.support.example.service.ITestService;
import pmis.web.support.util.PageUtils;
public class IssdpTest {
//	private Logger logger = LoggerFactory.getLogger(IssdpTest.class);
//
//	@Resource
//	private ITestService service;
//
//	@Test
//	public void test() {
//		try {
//			List<Tree> xlist=service.getObjectsByEntity();
//			logger.info("总数据为: "+xlist.size());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void getRsHqlPar() throws Exception {
//		// TODO Auto-generated method stub
//		List list=service.getRsHqlPar();
//		logger.info(list.size()+"---------");
//	}
//	@Test
//	public void getRsSqlPar() throws Exception {
//		// TODO Auto-generated method stub
//		List list=service.getRsSqlPar();
//		logger.info(list.size()+"---------");
//	}
//	//测试事物
//	@Test
//	public void callInfo() throws Exception {
//		try {
//			List<Tree> xlist=service.getObjectsByEntity();
//			logger.info("执行前总数据为: "+xlist.size());
//			service.saveInfo();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			List<Tree> xlist1=service.getObjectsByEntity();
//			logger.info("执行后总数据为: "+xlist1.size());
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void getPageInfoByHql() {
//		PageUtil<Tree> util = new PageUtil<Tree>();
//		util.setPageSize(10);
//		util.setCurrentPage(0);
//
//		try {
//			util = service.getPageInfoByHql(util);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		logger.info("------------totpage------" + util.getTotalPage());
//		logger.info("---------curpage---------" + util.getCurrentPage());
//		logger.info("-------pagesize-----------" + util.getPageSize());
//		logger.info("-------totcount-----------" + util.getTotalCount());
//
//		if(null!=util.getObjectList()){
//			logger.info("-------objecsieze-----------"
//					+ util.getObjectList().size());
//		}
//	}
//	@Test
//	public void addInfo(){
//		Tree tree=new Tree();
//		tree.setId(GenerationUUID.getGenerationUUID());
//		tree.setName("深圳交通局");
//		tree.setOrderId(3);
//		tree.setPid(null);
//		try {
//			service.saveEntity(tree);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void getTreeInfo(){
//		try {
//			Tree tree=service.getEntity("000147FB58C0963FC7E253F8B94E503FD311");
//			logger.info("---"+tree.getId()+"---"+tree.getName()+"----"+tree.getOrderId());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	@Test
//	public void getPagInfo() {
//		try {
//			PageUtil<Tree> util = new PageUtil<Tree>();
//			util.setPageSize(10);
//			util.setCurrentPage(0);
//
//			util = service.getObjectsByEntity(util);
//			logger.info("------------totpage------" + util.getTotalPage());
//			logger.info("---------curpage---------" + util.getCurrentPage());
//			logger.info("-------pagesize-----------" + util.getPageSize());
//			logger.info("-------totcount-----------" + util.getTotalCount());
//
//			logger.info("-------objecsieze-----------"
//					+ util.getObjectList().size());
//			// logger.info("-------totcount-----------"+util.getTotalCount());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Test
//	public void getPagInfoBySql() {
//		try {
//			PageUtil<Object[]> util = new PageUtil<Object[]>();
//			util.setPageSize(10);
//			util.setCurrentPage(0);
//
//			util = service.getPageInfoBySql(util);
//			logger.info("------------totpage------" + util.getTotalPage());
//			logger.info("---------curpage---------" + util.getCurrentPage());
//			logger.info("-------pagesize-----------" + util.getPageSize());
//			logger.info("-------totcount-----------" + util.getTotalCount());
//
//			logger.info("-------objecsieze-----------"
//					+ util.getObjectList().size());
//			// logger.info("-------totcount-----------"+util.getTotalCount());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
