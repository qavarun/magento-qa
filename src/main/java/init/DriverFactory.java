package init;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import constants.Constants;
import utils.FileOperations;

public class DriverFactory {

	ThreadLocal<RemoteWebDriver> webDriver = new ThreadLocal<RemoteWebDriver>();
	private RemoteWebDriver remoteWebDriver;
	private static Logger log = LogManager.getLogger(DriverFactory.class);
	FileOperations fileOperations = new FileOperations();
	Constants constants = new Constants();
	static Map<String, RemoteWebDriver> map = new HashMap<String, RemoteWebDriver>();

	/**
	 * Create a WebDriver Instance
	 * 
	 * @param browser
	 * @param locale
	 * @throws Exception
	 */
	public DriverFactory() throws Exception {
		String browser = fileOperations.getValueFromPropertyFile(constants.CONFIG_WEB_FILE_PATH, "browserName");
		String osType = getOperatingSystemType();

		switch (browser.toLowerCase()) {
		case "ie":
			log.info("Initializing driver for Internet Explorer...");
			if (osType.equals("Windows")) {
				System.setProperty("webdriver.ie.driver", "");
				InternetExplorerOptions options = new InternetExplorerOptions();
				options.introduceFlakinessByIgnoringSecurityDomains();
				webDriver.set(new InternetExplorerDriver(options));
				log.info("Driver for Internet Explorer initialized.");
				break;
			} else {
				throw new Exception("You are not running the tests on windows operating system");
			}
		case "edge":
			log.info("Initializing driver for Edge...");
			if (osType.equals("Windows")) {
				System.setProperty("webdriver.edge.driver", "");
				EdgeOptions options = new EdgeOptions(); // we can add the options for edge in future as we need
				webDriver.set(new EdgeDriver(options));
				log.info("Driver for Edge initialized.");
				break;
			} else {
				throw new Exception("You are not running the tests on windows operating system");
			}
		case "chrome":
			WebDriverManager.chromedriver().setup();
			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			String downloadLoc = s + File.separator + "testDownloadedItems" + File.separator;
			log.info("Initializing driver for Chrome...");
			List<String> arguments = new ArrayList<String>();
			arguments.add("--lang=en");
			arguments.add("--allow-file-access-from-files");

			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
			chromePrefs.put("download.default_directory", downloadLoc);
			chromePrefs.put("credentials_enable_service", false);
			chromePrefs.put("profile.password_manager_enabled", false);
			chromePrefs.put("plugins.plugins_disabled", new String[] { "Adobe Flash Player", "Chrome PDF Viewer" });

			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			chromeOptions.addArguments("--disable-notifications");
			chromeOptions.setExperimentalOption("prefs", chromePrefs);
			chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			chromeOptions.setExperimentalOption("useAutomationExtension", false);
			chromeOptions.addArguments("--headless");
			chromeOptions.addArguments(arguments);

			if (osType.equals("MacOS")) {
				try {
					remoteWebDriver = new ChromeDriver(chromeOptions);
					remoteWebDriver.manage().window().maximize();
					setDriver(remoteWebDriver);
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("Driver for Chrome initialized.");
				break;
			} else if (osType.equals("Windows")) {
				try {
					remoteWebDriver = new ChromeDriver(chromeOptions);
					remoteWebDriver.manage().window().maximize();
					setDriver(remoteWebDriver);
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("Driver for Chrome initialized.");
				break;
			} else if (osType.equals("Linux")) {
				try {
					remoteWebDriver = new ChromeDriver(chromeOptions);
					remoteWebDriver.manage().window().maximize();
					setDriver(remoteWebDriver);
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.info("Driver for Chrome initialized.");
				break;

			} else {
				throw new Exception("Please check your operating system");
			}
		case "firefox":
			log.info("Initializing driver for Firefox...");
			FirefoxProfile firefoxProfile = new FirefoxProfile();
			firefoxProfile.setPreference("intl.accept_languages", "en");
			firefoxProfile.setPreference("browser.download.folderList", 2);
			firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk",
					"text/plain application/zip application/tar");
			FirefoxOptions firefoxOptions = new FirefoxOptions();
			firefoxOptions.setProfile(firefoxProfile);
			webDriver.set(new FirefoxDriver(firefoxOptions));
			log.info("Driver for Firefox initialized.");
			break;
		default:
			break;
		}
		try {
			getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} catch (NoSuchSessionException e) {
			getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		}

	}

	/**
	 * detect the OS on which tests are running
	 */
	public static String getOperatingSystemType() {
		String OS = System.getProperty("os.name", "generic").toLowerCase();
		String identifiededOS;

		if ((OS.indexOf("mac") >= 0)) {
			identifiededOS = "MacOS";
		} else if (OS.indexOf("win") >= 0) {
			identifiededOS = "Windows";
		} else if (OS.indexOf("nux") >= 0) {
			identifiededOS = "Linux";
		} else {
			identifiededOS = "Other";
		}

		return identifiededOS;
	}

	public static synchronized void setDriver(RemoteWebDriver remote) {
		String id = Thread.currentThread().getName().trim();
		map.put(id, remote);
	}

	public static synchronized RemoteWebDriver getDriver() {

		String id = Thread.currentThread().getName().trim();
		return map.get(id);
	}

	public static synchronized void setDriverFactory(DriverFactory factory) {
		factory = factory;
	}
}
