package demo.awsscheduler;

import java.util.ArrayList;
import java.util.List;


public class TaskHistoryCounter {

	private final List<String> knownTasks = new ArrayList<String>();

	public List<String> getUnknownTasks(final List<String> tasks) {
		final List<String> unknownTasks = new ArrayList<String>(tasks);
		unknownTasks.removeAll(knownTasks);
		knownTasks.addAll(unknownTasks);
		return unknownTasks;
	}
}
