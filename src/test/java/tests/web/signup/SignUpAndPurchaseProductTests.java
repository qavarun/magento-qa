package tests.web.signup;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import constants.Constants;
import init.DriverFactory;
import listner.ReportListener;
import pages.base.BasePage;
import pages.operations.OperationsPage;
import pages.signup.SignUpPage;
import utils.FileOperations;

public class SignUpAndPurchaseProductTests {

	SoftAssert softAssert;
	Logger log = null;
	DriverFactory driverFactory;

	Path currentRelativePath = Paths.get("");
	String s = currentRelativePath.toAbsolutePath().toString();
	String testDataFile = s + File.separator + "testdata" + File.separator;
	String webTDataFile = testDataFile + "web" + File.separator + "testData.properties";
	String firstName;
	String lastName;
	String email;
	String password;

	Constants constants = new Constants();
	FileOperations fileOperations = new FileOperations();
	RemoteWebDriver driver;

	SignUpPage loginSignupHeaderFooterPageObj = new SignUpPage();
	BasePage basePage = new BasePage();
	OperationsPage operationsPage = new OperationsPage();

	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		log = LogManager.getLogger(SignUpAndPurchaseProductTests.class);
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		context.setConfigLocation(new File(constants.CONFIG_LOG4J_FILE_PATH).toURI());
		this.driverFactory = new DriverFactory();
		this.driver = DriverFactory.getDriver();
		DriverFactory.setDriverFactory(this.driverFactory);
		this.driver.get(fileOperations.getValueFromPropertyFile(constants.CONFIG_WEB_FILE_PATH, "appUrl"));
		log.info("URL is entered in broswer");
		firstName = fileOperations.getValueFromPropertyFile(constants.CONFIG_WEB_FILE_PATH, "firstName");
		lastName = fileOperations.getValueFromPropertyFile(constants.CONFIG_WEB_FILE_PATH, "lastName");
		password = fileOperations.getValueFromPropertyFile(webTDataFile, "password");
		email = fileOperations.getValueFromPropertyFile(constants.CONFIG_WEB_FILE_PATH, "email");
	}

	@Test(groups = { "smoke" }, priority = 1)
	public void validateSignUpWithNewUser() throws Exception {
		log.info("validateLoginWithDispatcher() Test Started");
		SoftAssert softAssert = new SoftAssert();
		loginSignupHeaderFooterPageObj.clickOnCreateAccountLink(driver);
		log.info("Signing Up a User Initiated");
		loginSignupHeaderFooterPageObj.signUpUser(driver, email, password, firstName, lastName);
		log.info("Signing Up a User Completed");
		Assert.assertTrue(driver.getCurrentUrl().contains("customer/account/"));
		log.info("Verified Customer Is Successfully Registered");
		loginSignupHeaderFooterPageObj.addressUpdate(driver);
		operationsPage.clickOntrendingLink(driver);
		operationsPage.clickOnFirstProductAndAddToCart(driver);
		log.info("Product is Added");
		operationsPage.redirectUserToCheckOutPage(driver,
				fileOperations.getValueFromPropertyFile(constants.CONFIG_WEB_FILE_PATH, "appUrl") + "/checkout/cart/");
		log.info("Checkout Is Initiated");
		operationsPage.clickOnProceedToCheckOutButton(driver);
		operationsPage.checkOutAndPlaceOrderBtn(driver);
		log.info("order Is Successfully Placed");
		Assert.assertTrue(driver.getCurrentUrl().contains("checkout/onepage/success/"));
		softAssert.assertAll();
		ReportListener.logToReport("Test successful.");
		log.info("validateLoginWithDispatcher() Test Completed");
	}

}
