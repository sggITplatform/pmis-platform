package pmis.webflow.mvc;

public interface WebFlowConfiguration
{
	String getDefaultClaimTaskActionView();

	String getDefaultCompleteTaskActionView();

	String getDefaultCompleteTaskFormView();

	String getDefaultStartProcessActionView();

	String getDefaultStartProcessFormView();

	FormVariablesFilter getFormVariablesFilter();
}