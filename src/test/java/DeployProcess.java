import java.io.FileInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;

/**
 * Activiti 5.10 demo
 * 
 * @author BruceQin
 * 
 */
public class DeployProcess
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

		//清空所有的内容
		for (Deployment d : repositoryService.createDeploymentQuery().list())
		{
			try
			{
				System.out.print(String.format("deleting deployment %s ...", d.getId()));
				repositoryService.deleteDeployment(d.getId(), true);
				System.out.println("OK");
			}
			catch (Exception e)
			{
				System.out.println("failed");
			}
		}

		System.out.println("process count: " + repositoryService.createProcessDefinitionQuery().list().size());
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// 部署流程定义
		repositoryService.createDeployment()
				.addInputStream("vacation.bpmn", new FileInputStream(realPath + "\\vacation.bpmn"))
				.addInputStream("vacation.png", new FileInputStream(realPath + "\\vacation.png")).deploy();
		System.out.println("process count: " + repositoryService.createProcessDefinitionQuery().list().size());
	}
}
