package pmis.webflow.mvc.ext;

public interface EventHandlerFactory
{
	EventHandler getEventHandler(EventType eventType, String formKey);
}