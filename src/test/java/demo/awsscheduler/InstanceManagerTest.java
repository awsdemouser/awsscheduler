package demo.awsscheduler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.mockito.Mockito;
import org.testng.annotations.Test;


public class InstanceManagerTest {

	private static final int LESS_THAN_TRESHOLD = 14;
	private InstanceManager instanceManager;

	@Test(groups = "unit")
	public void createInstanceManagerAndStartTask() throws Exception {
		final AwsInstance awsInstance = Mockito.mock(AwsInstance.class);
		new InstanceManager(2, awsInstance, "task1", null);
		verify(awsInstance).start("task1");
	}

	@Test(groups = "unit")
	public void addTaskToQueue() throws Exception {
		instanceManager = new InstanceManager(1, new AwsInstance(), "task1", null);
		assertTrue(instanceManager.addTask("task1"));
		assertFalse(instanceManager.addTask("task2"));
	}

	@Test(groups = "unit")
	public void startNextTaskFromQueueWhenInstanceFinishesExecution() throws Exception {
		instanceManager = new InstanceManager(2, new AwsInstance(), "task1", null);
		instanceManager.addTask("task1");
		instanceManager.addTask("task2");
		assertFalse(instanceManager.addTask("task3"));
		instanceManager.taskFinished();
		assertTrue(instanceManager.addTask("task3"));
	}

	@Test(groups = "unit")
	public void killInstanceWhenQueueIsEmptyAndRemainingTimeUnderLimit() throws Exception {
		final AwsInstance mockAwsInstance = Mockito.mock(AwsInstance.class);
		when(mockAwsInstance.getRemainingTime()).thenReturn(LESS_THAN_TRESHOLD);
		final InstanceKiller mockInstanceKiller = Mockito.mock(InstanceKiller.class);
		instanceManager = new InstanceManager(2, mockAwsInstance, "task1", mockInstanceKiller);
		instanceManager.taskFinished();

		verify(mockAwsInstance).destroy();
		verify(mockInstanceKiller).removeInstance(instanceManager);

	}
}
