package demo.awsscheduler;



public class AwsScheduler {

	public static void main(final String[] args) {
		final SqsReader sqsReader = new SqsReader(new InstanceScheduler(new InstanceManagerFactory(), 5),
				new TaskHistoryCounter());
		final SqsCloudService sqsCloudService = new SqsCloudService();
		while (true) {
			sqsReader.fetchTasks(sqsCloudService);
			try {
				Thread.sleep(5000);
			} catch (final InterruptedException e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
	}
}
