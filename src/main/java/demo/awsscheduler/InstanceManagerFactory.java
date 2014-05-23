package demo.awsscheduler;

public class InstanceManagerFactory {

	public InstanceManager create(final int queueLimit, final AwsInstance awsInstance, final String taskName,
			final InstanceKiller instanceKiller) {

		return new InstanceManager(queueLimit, awsInstance, taskName, instanceKiller);
	}
}
