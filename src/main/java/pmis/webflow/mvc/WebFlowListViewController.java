package pmis.webflow.mvc;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/workflow/")
public class WebFlowListViewController
{
	@Resource(name = "processEngine")
	private ProcessEngine _processEngine;

	@RequestMapping("listActiveProcesses.action")
	public String listActiveProcesses(ModelMap model)
	{
		model.put("processes", _processEngine.getHistoryService().createHistoricProcessInstanceQuery().unfinished()
				.orderByProcessInstanceStartTime().desc().list());
		model.put("listActiveProcesses", true);
		return "/listHistoricProcesses";
	}

	@RequestMapping("listAssignedTasks.action")
	public String listAssignedTasks(ModelMap model)
	{
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		model.put("tasks", _processEngine.getTaskService().createTaskQuery().taskAssignee(userId)
				.orderByTaskCreateTime().desc().list());
		return "/listTasks";
	}

	@RequestMapping("listHistoricProcesses.action")
	public String listHistoricProcesses(ModelMap model)
	{
		model.put("processes", _processEngine.getHistoryService().createHistoricProcessInstanceQuery().finished()
				.orderByProcessInstanceEndTime().desc().list());
		return "/listHistoricProcesses";
	}

	@RequestMapping("listProcessVariables.action")
	public String listProcessVariables(String processId, boolean historic, ModelMap model)
	{
		if (historic)
		{
			model.put("vars", _processEngine.getHistoryService().createHistoricProcessInstanceQuery()
					.processInstanceId(processId).singleResult().getProcessVariables());
		}
		else
		{
			model.put("vars", _processEngine.getRuntimeService().getVariables(processId));
		}

		return "/listVariables";
	}

	@RequestMapping("listTaskQueue.action")
	public String listTaskQueue(ModelMap model)
	{
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		model.put("tasks", _processEngine.getTaskService().createTaskQuery().taskCandidateUser(userId)
				.orderByTaskCreateTime().desc().list());
		return "/listTasks";
	}
}
