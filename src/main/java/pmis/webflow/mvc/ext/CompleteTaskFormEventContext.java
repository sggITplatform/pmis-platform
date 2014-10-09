package pmis.webflow.mvc.ext;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public interface CompleteTaskFormEventContext extends EventContext
{
	public abstract String getTaskId();

	public abstract Task getTask();

	public abstract ProcessInstance getProcessInstance();

}