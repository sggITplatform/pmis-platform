package pmis.webflow.mvc;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface FormVariablesFilter
{
	Map<String, Object> filterRequestParameters(HttpServletRequest request);
}
