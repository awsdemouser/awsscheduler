package demo.awsscheduler;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class InstanceScheduler implements InstanceKiller {

	private static final int FIRST_ELEMENT = 0;
	private final List<InstanceManager> instanceManagerStore;
	private final InstanceManagerFactory instanceManagerFactory;
	private final int queueLimit;

	public InstanceScheduler(final InstanceManagerFactory instanceManagerFactory, final int queueLimit) {
		this.instanceManagerFactory = instanceManagerFactory;
		this.queueLimit = queueLimit;
		this.instanceManagerStore = new LinkedList<InstanceManager>();

	}

	public void dispatchTasks(final List<String> tasks) {

		final ListIterator<InstanceManager> instanceManagerIterator = instanceManagerStore.listIterator();

		while (!tasks.isEmpty()) {

			if (instanceManagerIterator.hasNext()) {
				final InstanceManager instanceManager = instanceManagerIterator.next();
				while (!tasks.isEmpty() && instanceManager.addTask(tasks.get(FIRST_ELEMENT))) {
					tasks.remove(0);
				}

			} else {
				final InstanceManager instanceManager = instanceManagerFactory.create(queueLimit, new AwsInstance(),
						tasks.get(FIRST_ELEMENT), this);
				instanceManagerIterator.add(instanceManager);
				instanceManagerIterator.previous();
				tasks.remove(FIRST_ELEMENT);
			}
		}

	}

	@Override
	public void removeInstance(final InstanceManager instanceManager) {
		instanceManagerStore.remove(instanceManager);
	}

}
