
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * Activiti 5.10 demo
 * 
 * @author BruceQin
 * 
 */
public class DemoProcessTest
{
	// diagrams实际路径
	private static String realPath = "E:\\workspace\\bpm-test\\src\\main\\resources\\diagrams";

	public static void main(String[] args) throws Exception
	{
		// 创建 Activiti流程引擎
		ProcessEngine processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(
			"activiti.cfg.xml").buildProcessEngine();

		// 取得 Activiti 服务
		RepositoryService repositoryService = processEngine.getRepositoryService();

		System.out.println("process count: " + repositoryService.createProcessDefinitionQuery().list().size());
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// 部署流程定义
		repositoryService.createDeployment()
				.addInputStream("vacation.bpmn", new FileInputStream(realPath + "\\MyProcess.bpmn"))
				.addInputStream("vacation.png", new FileInputStream(realPath + "\\MyProcess.png")).deploy();

		// 启动流程实例
		ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey("myProcess");
		String procId = instance.getId();
		System.out.println("procId:" + procId);

		// 获得第一个任务
		TaskService taskService = processEngine.getTaskService();
		System.out.println(taskService.createTaskQuery().taskCandidateGroup("user").list());
		System.out.println(taskService.createTaskQuery().taskDefinitionKey("usertask2").list());
		System.out.println(taskService.createTaskQuery().taskAssignee("testUser").list());

		List<Task> tasks = taskService.createTaskQuery().taskDefinitionKey("usertask1").list();
		for (Task task : tasks)
		{
			System.out.println("Following task is: taskID -" + task.getId() + " taskName -" + task.getName());
			// 认领任务
			taskService.claim(task.getId(), "testUser");
		}

		// 查看testUser 现在是否能够获取到该任务
		tasks = taskService.createTaskQuery().taskAssignee("testUser").list();
		for (Task task : tasks)
		{
			System.out.println("Task for testUser: " + task.getName());
			// 完成任务
			taskService.complete(task.getId());
		}

		System.out.println("Number of tasks for testUser: "
				+ taskService.createTaskQuery().taskAssignee("testUser").count());

		// 获取并认领第二个任务
		tasks = taskService.createTaskQuery().taskDefinitionKey("usertask2").list();
		for (Task task : tasks)
		{
			System.out.println("Following task is : taskID -" + task.getId() + " taskName -" + task.getName());
			taskService.claim(task.getId(), "testUser");
		}

		System.out.println(taskService.createTaskQuery().list());

		//完成第二个任务结束结束流程
		for (Task task : tasks)
		{
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put("approve", true);
			taskService.complete(task.getId(), variables);
		}

		// 核实流程是否结束
		HistoryService historyService = processEngine.getHistoryService();
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(procId).singleResult();
		System.out.println("Process instance end time: " + historicProcessInstance.getEndTime());
	}
}
