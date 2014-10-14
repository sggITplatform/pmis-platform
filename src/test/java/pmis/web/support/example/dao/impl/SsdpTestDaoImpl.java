/**   
* @Title: TestDao.java 
* @Package com.szewec.ssdp.test.dao 
* @Description: 用一句话描述该文件做什么 
* Copyright: Copyright (c) 2014 
* Company:wuwh team by iss
* @author: wuwh   
* @date: 2014年6月11日 下午9:32:53 
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
package pmis.web.support.example.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import pmis.web.support.dao.impl.HibernateBasedDao;
import pmis.web.support.example.dao.IssdpTestDao;
import pmis.web.support.example.entity.Tree;
import pmis.web.support.util.PageUtils;
@Repository
public class SsdpTestDaoImpl extends HibernateBasedDao<Tree,String> implements IssdpTestDao{

	public PageUtils getPageInfoByHql(PageUtils util) throws Exception{
		StringBuffer bufer=new StringBuffer(" from Tree ");
		return super.getRsByHql(bufer.toString(), util);
	}
	
	public PageUtils getPageInfoBySql(PageUtils util) throws Exception{
		StringBuffer bufer=new StringBuffer(" select * from tree ");
		return super.getRsBySql(bufer.toString(), util);
	}
	
	public List getRsHqlPar() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer bufer=new StringBuffer(" from Tree where name=? and id=?");
		return super.getRsByHqlAndPram(bufer.toString(),"深圳交通局","000147FBAF4A373FEE60A8C76518C63FEAC7");
	}
	@Override
	public List getRsSqlPar() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer bufer=new StringBuffer(" select * from tree where name=?");
		return super.getRsBySqlAndPram(bufer.toString(),"中国地震局");
	}
}
