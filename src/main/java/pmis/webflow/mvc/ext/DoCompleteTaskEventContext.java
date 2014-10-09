package pmis.webflow.mvc.ext;

import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public interface DoCompleteTaskEventContext extends EventContext
{
	public abstract String getTaskId();

	public abstract Task getTask();

	public abstract Map<String, Object> getProcessVariableMap();

	public abstract ProcessInstance getProcessInstance();
}
