package pmis.webflow.mvc.ext;

import java.util.Map;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

public interface DoStartProcessEventContext extends EventContext
{
	String getBussinessKey();

	public abstract Map<String, Object> getProcessVariableMap();

	void setBussinessKey(String bussinessKey);
	
	ProcessDefinition getProcessDefinition();

	String getProcessDefinitionId();

	ProcessInstance getProcessInstance();
}
