package demo.awsscheduler;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.testng.annotations.Test;


public class SqsReaderTest {

	@Test(groups = "unit")
	public void readTasksFromSqsAndDispatchNewOnesToInstanceScheduler() throws Exception {
		final InstanceScheduler mockInstanceScheduler = Mockito.mock(InstanceScheduler.class);
		final TaskHistoryCounter mockTaskHistoryCounter = Mockito.mock(TaskHistoryCounter.class);
		final SqsReader sqsReader = new SqsReader(mockInstanceScheduler, mockTaskHistoryCounter);

		final List<String> result = new ArrayList<String>();
		result.add("task1");
		result.add("task2");

		final SqsCloudService mockSqsCloudService = Mockito.mock(SqsCloudService.class);
		when(mockSqsCloudService.getTasks()).thenReturn(result);

		final List<String> delta = new ArrayList<String>();
		delta.add("task2");
		when(mockTaskHistoryCounter.getUnknownTasks(result)).thenReturn(delta);

		sqsReader.fetchTasks(mockSqsCloudService);

		verify(mockTaskHistoryCounter).getUnknownTasks(result);
		verify(mockInstanceScheduler).dispatchTasks(delta);
	}
}
