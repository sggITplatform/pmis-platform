package pmis.webflow.mvc.support;

public class HttpRequestParameterRequiredException extends RuntimeException
{
	private String _name;

	public HttpRequestParameterRequiredException(String name)
	{
		_name = name;
	}

	@Override
	public String getMessage()
	{
		return String.format("HTTP request parameter required: %s", _name);
	}
}
