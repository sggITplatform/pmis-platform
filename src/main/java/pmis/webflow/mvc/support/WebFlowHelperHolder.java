package pmis.webflow.mvc.support;

public interface WebFlowHelperHolder
{
	ProcessDefinitionHelper getProcessDefinitionHelper();

	ProcessInstanceHelper getProcessInstanceHelper();

	TaskHelper getTaskHelper();
}
