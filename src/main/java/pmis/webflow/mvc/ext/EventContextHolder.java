package pmis.webflow.mvc.ext;

public interface EventContextHolder
{
	DoCompleteTaskEventContext getDoCompleteTaskEventContext();

	DoStartProcessEventContext getDoStartProcessEventContext();

	CompleteTaskFormEventContext getCompleteTaskFormEventContext();

	StartProcessFormEvent getStartProcessFormEventContext();
}
