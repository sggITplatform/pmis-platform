/**   
* @Title: IssdpTestDao.java 
* @Package com.szewec.ssdp.test.dao.impl 
* @Description: 用一句话描述该文件做什么 
* Copyright: Copyright (c) 2014 
* Company:wuwh team by iss
* @author: wuwh  
* @date: 2014年8月22日 上午10:48:57 
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
package pmis.web.support.example.dao;

import java.util.List;

import pmis.web.support.dao.GenericDao;
import pmis.web.support.example.entity.Tree;
import pmis.web.support.util.PageUtils;

public interface IssdpTestDao extends GenericDao<Tree,String>{

	public PageUtils getPageInfoByHql(PageUtils util) throws Exception;
	
	public PageUtils getPageInfoBySql(PageUtils util) throws Exception;
	
	public List getRsHqlPar() throws Exception; 
	public List getRsSqlPar() throws Exception;
	
}
