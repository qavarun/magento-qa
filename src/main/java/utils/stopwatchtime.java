package utils;

import org.apache.commons.lang.time.StopWatch;
import org.testng.Reporter;

public class stopwatchtime extends StopWatch {

	static StopWatch stopwatch = new StopWatch();

	public static String timetaken() {

		long x = stopwatch.getTime();
		@SuppressWarnings("removal")
		Long l= new Long(x);
		int y = l.intValue()/1000;
		String numberAsString = String.valueOf(y);
		System.out.println("Execution time for this method: " + numberAsString + " seconds \n");
		Reporter.log("time taken to execute this method:" + numberAsString + " seconds \n");
		return numberAsString;
	}

	public static void start(String methodname) {
		stopwatch.start();
		Reporter.log(methodname + "; \n");

	}

	public static void stop(String methodname) {
		stopwatch.stop();
		Reporter.log(methodname + "; \n");
	}

	public static void reset(String methodname) {
		stopwatch.reset();
		Reporter.log(methodname + "; \n");
	}

}