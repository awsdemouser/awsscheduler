package demo.awsscheduler;

import java.util.Random;


public class AwsInstance {

	public void start(final String nextTask) {

	}

	public int getRemainingTime() {
		final Random random = new Random();
		return random.nextInt(30);
	}

	public boolean destroy() {
		return true;

	}

}
