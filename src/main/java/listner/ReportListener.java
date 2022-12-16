package listner;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.ScreenCapture;
import com.aventstack.extentreports.model.Test;

import constants.Constants;
import init.DriverFactory;
import utils.EncryptionDecryption;
import utils.FileOperations;
import utils.ReadingEmails;
import utils.stopwatchtime;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IClassListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportListener implements ITestListener, IClassListener, ISuiteListener {

	Path currentRelativePath = Paths.get("");
	String s = currentRelativePath.toAbsolutePath().toString();
	String basePath = s + File.separator;

	private static ExtentReports extent;
	private ExtentSparkReporter htmlReporter;
	private static ScreenCapture med;
	private static ThreadLocal<Integer> testMethodCount = new ThreadLocal<Integer>();
	String testRunStatus = null;

	private static String filePathex = System.getProperty("user.dir") + "/Extent Reports/extentreport.html";
	private static Map<String, RemoteWebDriver> driverMap = new HashMap<String, RemoteWebDriver>();
	private static Map<String, ExtentTest> extentTestMap = new HashMap<String, ExtentTest>();
	private static final String SMTP_SERVER_HOST = "email-smtp.us-east-1.amazonaws.com";
	private static final String SMTP_SERVER_PORT = "587";
	private static final String SUBJECT_WEB = "Web Test Automation Report";
	private static final String SUBJECT_API = "API Test Automation Report";
	private static final String BODY = "Hi Team, \n This is a programmatic email so please find the attached Report.";
	String totalTime;
	LocalDateTime startDateTime, endDateTime;
	boolean executionFinished;
	int totalPassed = 0;
	int totalFailed = 0;
	String packageName;

	Constants constants = new Constants();
	FileOperations fileOperations = new FileOperations();
	EncryptionDecryption encryptionDecryptionObj = new EncryptionDecryption();
	static ReadingEmails readingEmailsObj = new ReadingEmails();

	public synchronized ExtentTest startTest(String testName) {
		ExtentTest classNode = getExtent().createTest(testName);
		extentTestMap.put(testName, classNode);
		return classNode;
	}

	public static synchronized ExtentTest getTest(String testName) {
		return extentTestMap.get(testName);
	}

	public void onStart(ISuite suite) {
		startDateTime = LocalDateTime.now();
		stopwatchtime.start(suite.getName());
		this.extent = getExtent();
		suite.setAttribute("extent", this.extent);
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		String testDownloadedItems = s + File.separator + "testDownloadedItems" + File.separator;
		FileOperations fileOpObj = new FileOperations();
		try {
			System.out.println("CleanUp Directory Started");
			fileOpObj.cleanDir(s + File.separator + "Extent Reports/Failure Screenshots/");
			fileOpObj.cleanDir(s + File.separator + "Extent Reports/Passed Screenshots/");
			fileOpObj.cleanDir(s + File.separator + "Extent Reports/Skipped Screenshots/");
			fileOpObj.cleanDir(testDownloadedItems);
		} catch (Exception e) {
			System.out.println("CleanUp Directory Not Required");
		}
		testRunStatus = "pass";
	}

	public void onStart(ITestContext context) {
	}

	public void onBeforeClass(ITestClass testclass) {
		startTest(testclass.getRealClass().getSimpleName());
		testMethodCount.set(-1);
	}

	public void onTestStart(ITestResult result) {
		String testClassName = result.getTestClass().getRealClass().getSimpleName();
		packageName = result.getTestClass().getRealClass().getPackage().getName();
		System.out.println("class name: " + testClassName + " with instance id :" + DriverFactory.getDriver());
		driverMap.put(testClassName, DriverFactory.getDriver());
		getTest(result.getInstanceName().replace(".", " ")
				.split(" ")[result.getInstanceName().replace(".", " ").split(" ").length - 1].trim())
						.createNode(result.getMethod().getMethodName(), result.getMethod().getDescription())
						.log(Status.INFO, "Test method " + result.getMethod().getMethodName() + " started");

		Integer testMethodCountValue = testMethodCount.get();
		testMethodCountValue = testMethodCountValue + 1;
		testMethodCount.set(testMethodCountValue);
	}

	public void onTestSuccess(ITestResult result) {
		totalPassed = totalPassed + 1;
		String methodName = result.getName().trim();
		String filePath = System.getProperty("user.dir") + "/Extent Reports/Passed Screenshots/";
		if (!packageName.contains("api"))
			takeScreenShot(filePath, methodName, driverMap.get(result.getTestClass().getRealClass().getSimpleName()));

		Log logObj = new Log();
		logObj.setDetails("Test method " + result.getMethod().getMethodName() + " completed successfully");
		logObj.setStatus(Status.PASS);

		med = new ScreenCapture("", "PassedScreenshot", "", "Passed Screenshots/" + methodName + ".png");
		List<Test> tests = getTest(Reporter.getCurrentTestResult().getInstanceName().replace(".", " ")
				.split(" ")[Reporter.getCurrentTestResult().getInstanceName().replace(".", " ").split(" ").length - 1]
						.trim()).getModel().getChildren();

		for (Test test : tests) {
			if (test.getName().trim().equals(methodName)) {
				test.addLog(logObj);
				test.addMedia(med);
				break;
			}
		}
	}

	public void onTestFailure(ITestResult result) {
		totalFailed = totalFailed + 1;
		testRunStatus = "fail";
		String methodName = result.getName().trim();
		String filePath = System.getProperty("user.dir") + "/Extent Reports/Failure Screenshots/";
		if (!packageName.contains("api"))
			takeScreenShot(filePath, methodName, driverMap.get(result.getTestClass().getRealClass().getSimpleName()));

		Log logObj = new Log();
		logObj.setDetails("Test method " + result.getMethod().getMethodName() + " failed due to Exception -----> "
				+ Reporter.getCurrentTestResult().getThrowable().toString());
		logObj.setStatus(Status.FAIL);

		med = new ScreenCapture("", "FailedScreenshot", "", "Failure Screenshots/" + methodName + ".png");
		List<Test> tests = getTest(Reporter.getCurrentTestResult().getInstanceName().replace(".", " ")
				.split(" ")[Reporter.getCurrentTestResult().getInstanceName().replace(".", " ").split(" ").length - 1]
						.trim()).getModel().getChildren();

		for (Test test : tests) {
			if (test.getName().trim().equals(methodName)) {
				test.addLog(logObj);
				test.addMedia(med);
				break;
			}
		}
	}

	public void onTestSkipped(ITestResult result) {
		testRunStatus = "fail";
		try {
			String testClassName = result.getTestClass().getRealClass().getSimpleName();
			System.out.println("class name: " + testClassName + " with instance id :" + DriverFactory.getDriver());
			driverMap.put(testClassName, DriverFactory.getDriver());
			getTest(result.getInstanceName().replace(".", " ")
					.split(" ")[result.getInstanceName().replace(".", " ").split(" ").length - 1].trim())
							.createNode(result.getMethod().getMethodName(), result.getMethod().getDescription())
							.log(Status.SKIP, "Test method " + result.getMethod().getMethodName() + " skipped");

			String methodName = result.getName().trim();
			String filePath = System.getProperty("user.dir") + "/Extent Reports/Skipped Screenshots/";
			if (!packageName.contains("api"))
				takeScreenShot(filePath, methodName,
						driverMap.get(result.getTestClass().getRealClass().getSimpleName()));

			Log logObj = new Log();
			logObj.setDetails("Test method " + result.getMethod().getMethodName() + " skpped due to Exception -----> "
					+ Reporter.getCurrentTestResult().getThrowable().toString());
			logObj.setStatus(Status.SKIP);

			med = new ScreenCapture("", "SkippedScreenshot", "", "Skipped Screenshots/" + methodName + ".png");
			List<Test> tests = getTest(Reporter.getCurrentTestResult().getInstanceName().replace(".", " ").split(
					" ")[Reporter.getCurrentTestResult().getInstanceName().replace(".", " ").split(" ").length - 1]
							.trim()).getModel().getChildren();

			for (Test test : tests) {
				if (test.getName().trim().equals(methodName)) {
					test.addLog(logObj);
					test.addMedia(med);
					break;
				}
			}

			System.out.println("Screenshot will be captured");
		} catch (Exception e) {
			String testClassName = result.getTestClass().getRealClass().getSimpleName();
			System.out.println("class name: " + testClassName + " with instance id :" + DriverFactory.getDriver());
			driverMap.put(testClassName, DriverFactory.getDriver());
			getTest(result.getInstanceName().replace(".", " ")
					.split(" ")[result.getInstanceName().replace(".", " ").split(" ").length - 1].trim())
							.createNode(result.getMethod().getMethodName(), result.getMethod().getDescription())
							.log(Status.SKIP, "Test method " + result.getMethod().getMethodName() + " skipped");

			String methodName = result.getName().trim();

			Log logObj = new Log();
			logObj.setDetails("Test method " + result.getMethod().getMethodName() + " skipped due to Exception -----> "
					+ Reporter.getCurrentTestResult().getThrowable().toString());
			logObj.setStatus(Status.SKIP);

			List<Test> tests = getTest(Reporter.getCurrentTestResult().getInstanceName().replace(".", " ").split(
					" ")[Reporter.getCurrentTestResult().getInstanceName().replace(".", " ").split(" ").length - 1]
							.trim()).getModel().getChildren();

			for (Test test : tests) {
				if (test.getName().trim().equals(methodName)) {
					test.addLog(logObj);
					break;
				}
			}
		}
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
	}

	public void onAfterClass(ITestClass testclass) {
	}

	public void onFinish(ITestContext context) {
	}

	public void onFinish(ISuite suite) {
		stopwatchtime.stop(suite.getName());
		executionFinished = true;
		endDateTime = LocalDateTime.now();
		this.extent = getExtent();

		if (testRunStatus.equalsIgnoreCase("fail")) {
			try {
				fileOperations.updateValueToPropertyFile(constants.CONFIG_WEB_FILE_PATH, "testRunStatus", "fail");
			} catch (ConfigurationException | IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				fileOperations.updateValueToPropertyFile(constants.CONFIG_WEB_FILE_PATH, "testRunStatus", "pass");
			} catch (ConfigurationException | IOException e) {
				e.printStackTrace();
			}
		}
		this.extent.flush();
//		try {
//			if (packageName.contains("api"))
//				readingEmailsObj.sendMailUsingSES(SMTP_SERVER_HOST, SMTP_SERVER_PORT, constants.SMTP_USER_NAME,
//						constants.SMTP_USER_PASSWORD, constants.FROM_USER_EMAIL, constants.FROM_USER_FULLNAME,
//						constants.TO_USER_EMAIL,
//						" Runtime " + stopwatchtime.timetaken() + "s [" + testRunStatus.toUpperCase() + "] - "
//								+ SUBJECT_API,
//						BODY + "\n Total tests passed = " + totalPassed + "\n Total tests failed = " + totalFailed);
//			else
//				readingEmailsObj.sendMailUsingSES(SMTP_SERVER_HOST, SMTP_SERVER_PORT, constants.SMTP_USER_NAME,
//						constants.SMTP_USER_PASSWORD, constants.FROM_USER_EMAIL, constants.FROM_USER_FULLNAME,
//						constants.TO_USER_EMAIL,
//						" Runtime " + stopwatchtime.timetaken() + "s [" + testRunStatus.toUpperCase() + "] - "
//								+ SUBJECT_WEB,
//						BODY + "\n Total tests passed = " + totalPassed + "\n Total tests failed = " + totalFailed);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void takeScreenShot(String filePath, String methodName, RemoteWebDriver driver) {
		try {
			TakesScreenshot d = ((TakesScreenshot) driver);
			File scrFile = d.getScreenshotAs(OutputType.FILE);
			File scrFile2 = new File(filePath + methodName + ".png");
			System.setProperty("org.uncommons.reportng.escape-output", "false");
			FileUtils.copyFile(scrFile, scrFile2);
			System.out.println("***Placed screen shot in " + filePath + "***");
			Reporter.setEscapeHtml(false);
			Reporter.log("<a href='" + scrFile2.toString() + "'><img src ='" + scrFile2.toString()
					+ "' width='200' height='200'></a>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ExtentReports getExtent() {
		if (extent == null) {
			extent = new ExtentReports();
			String name = "TM3";
			try {
				name = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			extent.setSystemInfo("User Name", name);
			extent.setSystemInfo("Browser",
					fileOperations.getValueFromPropertyFile(constants.CONFIG_WEB_FILE_PATH, "browserName"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("OS", System.getProperty("os.name", "generic").toLowerCase());
			extent.setSystemInfo("Environment", "Stage");
			extent.attachReporter(getHtmlReporter());
		}
		Duration diff = null;
		if (executionFinished) {
			diff = Duration.between(startDateTime, endDateTime);
			String hm = String.format("%d:%02d", diff.toHours(), diff.minusHours(diff.toHours()).toMinutes());
			System.out
					.println("====================== " + startDateTime + endDateTime + hm + " ======================");
			extent.setSystemInfo("Total Execution Time", hm);
		}
		return extent;
	}

	private ExtentSparkReporter getHtmlReporter() {
		htmlReporter = new ExtentSparkReporter(filePathex);
		htmlReporter.config().setTheme(Theme.DARK);
		htmlReporter.config().setDocumentTitle("Automation Report");
		htmlReporter.config().setReportName("Test Automation Report");
		return htmlReporter;
	}

	public static void logToReport(String msg) {
		String methodName = Reporter.getCurrentTestResult().getMethod().getMethodName().trim();
		Log logObj = new Log();
		logObj.setDetails(msg);
		logObj.setStatus(Status.INFO);

		List<Test> tests = getTest(Reporter.getCurrentTestResult().getInstanceName().replace(".", " ")
				.split(" ")[Reporter.getCurrentTestResult().getInstanceName().replace(".", " ").split(" ").length - 1]
						.trim()).getModel().getChildren();

		for (Test test : tests) {
			if (test.getName().trim().equals(methodName)) {
				test.addLog(logObj);
				break;
			}
		}
	}
}