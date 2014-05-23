package demo.awsscheduler;

import java.util.concurrent.ConcurrentLinkedQueue;


public class InstanceManager {

	private static final int MAX_REMAINING_TIME = 16;
	private final ConcurrentLinkedQueue<String> localQueue;
	private final int queueLimit;
	private final AwsInstance awsInstance;
	private final InstanceKiller instanceKiller;

	public InstanceManager(final int queueLimit, final AwsInstance awsInstance, final String taskName,
			final InstanceKiller instanceKiller) {
		localQueue = new ConcurrentLinkedQueue<String>();
		this.queueLimit = queueLimit;
		this.awsInstance = awsInstance;
		this.instanceKiller = instanceKiller;
		awsInstance.start(taskName);
	}

	public boolean addTask(final String taskName) {
		if (localQueue.size() < queueLimit) {
			localQueue.add(taskName);
			return true;
		} else {
			return false;
		}
	}

	public void taskFinished() {
		final String nextTask = localQueue.poll();
		if (nextTask == null) {
			destroyInstance();
		} else {
			awsInstance.start(nextTask);
		}
	}

	private void destroyInstance() {
		if (awsInstance.getRemainingTime() < MAX_REMAINING_TIME) {
			awsInstance.destroy();
			instanceKiller.removeInstance(this);
		}

	}
}
