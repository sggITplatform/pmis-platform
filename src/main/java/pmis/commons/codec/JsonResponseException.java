package pmis.commons.codec;

/**
 * 
 * @ClassName: JsonResponseException
 * @author: [wuwh]
 * @CreateDate: [2014-3-26 下午4:53:47]
 * @UpdateUser: [wuwh]
 * @UpdateDate: [2014-3-26 下午4:53:47]
 * @UpdateRemark: [说明本次修改内容]
 * @Description: [TODO(用一句话描述该文件做什么)]
 * @version: [V1.0]
 */
public class JsonResponseException extends RuntimeException
{
	private String message = "unknown exception";

	private int status = 404;

	public JsonResponseException()
	{
	}

	public JsonResponseException(int status, String message)
	{
		this.status = status;
		this.message = message;
	}

	public JsonResponseException(int status, String message, Throwable cause)
	{
		super(message, cause);
		this.message = message;
		this.status = status;
	}

	public JsonResponseException(int status, Throwable cause)
	{
		super(cause);
		this.message = cause.getMessage();
		this.status = status;
	}

	public JsonResponseException(String message)
	{
		this.message = message;
	}

	public JsonResponseException(String message, Throwable cause)
	{
		super(message, cause);
		this.message = message;
	}

	public JsonResponseException(Throwable cause)
	{
		super(cause);
		this.message = cause.getMessage();
	}

	public String getMessage()
	{
		return this.message;
	}

	public int getStatus()
	{
		return this.status;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void setStatus(int status)
	{
		this.status = status;
	}
}
