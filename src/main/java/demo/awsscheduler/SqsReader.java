package demo.awsscheduler;

import java.util.List;


public class SqsReader {

	private final InstanceScheduler instanceScheduler;
	private final TaskHistoryCounter taskHistoryCalculator;

	public SqsReader(final InstanceScheduler instanceScheduler, final TaskHistoryCounter taskHistoryReader) {
		this.instanceScheduler = instanceScheduler;
		this.taskHistoryCalculator = taskHistoryReader;
	}

	public void fetchTasks(final SqsCloudService sqsCloudService) {
		final List<String> delta = taskHistoryCalculator.getUnknownTasks(sqsCloudService.getTasks());
		instanceScheduler.dispatchTasks(delta);
	}

}
