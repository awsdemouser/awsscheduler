package demo.awsscheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SqsCloudService {

	Random random = new Random();

	public List<String> getTasks() {
		final ArrayList<String> result = new ArrayList<String>();
		final int nextInt = random.nextInt(100);
		for (int i = 0; i < nextInt; i++) {
			result.add("Alma" + random.nextInt(200));
		}
		return result;
	}

}
