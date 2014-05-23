package demo.awsscheduler;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;


public class TaskhistoryCounterTest {

	@Test(groups = "unit")
	public void returnAllDataWhenNoHistoryAvailable() throws Exception {
		final List<String> tasks = new ArrayList<String>();
		tasks.add("task1");
		tasks.add("task2");
		final List<String> result = new TaskHistoryCounter().getUnknownTasks(tasks);
		assertEquals(result.get(0), "task1");
		assertEquals(result.get(1), "task2");
	}

	@Test(groups = "unit")
	public void returOnlyNewTasksWhenHistoryAvailable() throws Exception {
		final List<String> tasks = new ArrayList<String>();
		final TaskHistoryCounter deltaCalculator = new TaskHistoryCounter();
		tasks.add("task1");
		tasks.add("task2");

		List<String> result = deltaCalculator.getUnknownTasks(tasks);

		assertEquals(result.get(0), "task1");
		assertEquals(result.get(1), "task2");

		final List<String> newTasks = new ArrayList<String>();
		newTasks.add("task1");
		newTasks.add("task2");
		newTasks.add("task3");

		result = deltaCalculator.getUnknownTasks(newTasks);

		assertEquals(result.size(), 1);
		assertEquals(result.get(0), "task3");

	}
}
