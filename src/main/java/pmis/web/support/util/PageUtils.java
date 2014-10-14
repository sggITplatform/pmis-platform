/**
 * @Title:   [PageBean.java]
 * @Package: [com.cc.michelle.platform.pagination]
 * @author:  Mk.Yuan
 * @CreateDate: 2013-6-23 
 * @Description:  [TODO(用一句话描述该文件做什么)]
 */
package pmis.web.support.util;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: PageBean
 * @author: [Administrator]
 * @CreateDate: [2013-6-23 下午6:23:34]
 * @UpdateUser: [Administrator]
 * @UpdateDate: [2013-6-23 下午6:23:34]
 * @UpdateRemark: [说明本次修改内容]
 * @Description: [TODO(用一句话描述该文件做什么)]
 * @version: [V1.0]
 */
public class PageUtils<T> implements Serializable
{

	protected final static Logger logger = LoggerFactory.getLogger(PageUtils.class);

	/**
	 * 计算当前页,若为0或者请求的URL中没有"?page=",则用1代替
	 * 
	 * @param page
	 *            传入的参数(可能为空,即0,则返回1)
	 * @return 当前页
	 */
	public static int countCurrentPage(int page)
	{
		final int curPage = (page == 0 ? 1 : page);
		return curPage;
	}

	/**
	 * 计算总页数
	 * 
	 * @param pageSize
	 *            每页记录数
	 * @param totalCount
	 *            总记录数
	 * @return 总页数
	 */
	public static int countTotalPage(final int pageSize, final int totalCount)
	{
		int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
		return totalPage;
	}

	/**
	 * 计算当前页开始记录
	 * 
	 * @param pageSize
	 *            每页记录数
	 * @param currentPage
	 *            当前第几页
	 * @return 当前页开始记录号
	 */
	public static int getIndex(final int pageSize, final int currentPage)
	{
		//		logger.info("pageSize=====" + pageSize + " currentPage===="
		//				+ currentPage);
		final int startCount = pageSize * (currentPage - 1);
		return startCount;
	}

	//当前第几页
	private int currentPage = 0;

	//返回结果集数据,可以是对应的实体,也可以是数组集合数据
	private List<T> objectList;

	//每页显示数据条数
	private int pageSize = 15;

	//总记录数
	private int totalCount = 0;

	//总页数
	private int totalPage = 0;

	private String url;

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (!(obj instanceof PageUtils))
		{
			return false;
		}
		PageUtils other = (PageUtils) obj;
		if (currentPage != other.currentPage)
		{
			return false;
		}
		if (objectList == null)
		{
			if (other.objectList != null)
			{
				return false;
			}
		}
		else if (!objectList.equals(other.objectList))
		{
			return false;
		}
		if (pageSize != other.pageSize)
		{
			return false;
		}
		if (totalCount != other.totalCount)
		{
			return false;
		}
		if (totalPage != other.totalPage)
		{
			return false;
		}
		if (url == null)
		{
			if (other.url != null)
			{
				return false;
			}
		}
		else if (!url.equals(other.url))
		{
			return false;
		}
		return true;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public List<T> getObjectList()
	{
		return objectList;
	}

	public int getPageSize()
	{
		if (pageSize < 0)
		{
			pageSize = 1;
		}
		return pageSize;
	}

	public int getTotalCount()
	{
		return totalCount;
	}

	public int getTotalPage()
	{
		return totalPage;
	}

	public String getUrl()
	{
		return url;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + currentPage;
		result = prime * result + ((objectList == null) ? 0 : objectList.hashCode());
		result = prime * result + pageSize;
		result = prime * result + totalCount;
		result = prime * result + totalPage;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public void setObjectList(List<T> objectList)
	{
		this.objectList = objectList;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public void setTotalCount(int totalCount)
	{
		this.totalCount = totalCount;
	}

	public void setTotalPage(int totalPage)
	{
		this.totalPage = totalPage;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
}
