package demo.awsscheduler;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class InstanceSchedulerTest {

	private static final int QUEUE_LIMIT = 2;
	private InstanceScheduler instanceScheduler;
	@Mock
	InstanceManagerFactory mockInstanceManagerFactory;

	@BeforeMethod(alwaysRun = true)
	public void initTest() {
		MockitoAnnotations.initMocks(this);
		instanceScheduler = new InstanceScheduler(mockInstanceManagerFactory, QUEUE_LIMIT);
		when(
				mockInstanceManagerFactory.create(eq(QUEUE_LIMIT), any(AwsInstance.class), eq("task1"),
						any(InstanceKiller.class))).thenReturn(
				new InstanceManager(QUEUE_LIMIT, new AwsInstance(), "task1", instanceScheduler));
	}

	@Test(groups = "unit")
	public void addTaskWhenNoManagerInstanceExists() throws Exception {
		final List<String> tasks = new ArrayList<String>();
		tasks.add("task1");
		instanceScheduler.dispatchTasks(tasks);
		verify(mockInstanceManagerFactory).create(eq(QUEUE_LIMIT), any(AwsInstance.class), eq("task1"),
				any(InstanceKiller.class));
	}

	@Test(groups = "unit")
	public void addTaskToExistingManagerInstance() throws Exception {
		final List<String> tasks = new ArrayList<String>();
		tasks.add("task1");
		tasks.add("task2");
		tasks.add("task3");

		instanceScheduler.dispatchTasks(tasks);

		verify(mockInstanceManagerFactory).create(eq(QUEUE_LIMIT), any(AwsInstance.class), eq("task1"),
				any(InstanceKiller.class));
		verify(mockInstanceManagerFactory, never()).create(eq(QUEUE_LIMIT), any(AwsInstance.class), eq("task2"),
				any(InstanceKiller.class));
		verify(mockInstanceManagerFactory, never()).create(eq(QUEUE_LIMIT), any(AwsInstance.class), eq("task3"),
				any(InstanceKiller.class));
	}

	@Test(groups = "unit")
	public void createNewInstanceManagerWhenAllPriorManagersAreFull() throws Exception {
		final List<String> tasks = new ArrayList<String>();
		tasks.add("task1");
		tasks.add("task2");
		tasks.add("task3");

		instanceScheduler.dispatchTasks(tasks);

		final List<String> otherTasks = new ArrayList<String>();
		otherTasks.add("task5");

		instanceScheduler.dispatchTasks(otherTasks);

		verify(mockInstanceManagerFactory).create(eq(QUEUE_LIMIT), any(AwsInstance.class), eq("task5"),
				any(InstanceKiller.class));

	}
}
