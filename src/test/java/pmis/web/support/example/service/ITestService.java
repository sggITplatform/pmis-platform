/**   
* @Title: ITestService.java 
* @Package com.szewec.ssdp.test.service 
* @Description: 用一句话描述该文件做什么 
* Copyright: Copyright (c) 2014 
* Company:wuwh team by iss
* @author: wuwh  
* @date: 2014年8月22日 上午10:44:28 
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
package pmis.web.support.example.service;

import java.util.List;

import pmis.web.support.example.entity.Tree;
import pmis.web.support.service.GenericService;
import pmis.web.support.util.PageUtils;

public interface ITestService extends GenericService<Tree,String> {

	public PageUtils getPageInfoByHql(PageUtils util) throws Exception;
	
	public PageUtils getPageInfoBySql(PageUtils util) throws Exception;
	
	/**
	 * 
	*
	* @Title: saveInfo
	* @Description: <p>测试事物问题<p>
	* <pre>
		这里描述这个方法的使用方法 – 可选
	* </pre>
	* @param: <p>@throws Exception<p>
	* @date: 2014年8月22日
	* @return: void
	* @throws 
	*
	 */
	public void saveInfo() throws Exception;
	
	public List getRsHqlPar() throws Exception;
	
	public List getRsSqlPar() throws Exception;
}
