package tests.api.user;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import apiOperations.UserGeneration;
import constants.Constants;
import io.restassured.response.Response;
import listner.ReportListener;
import utils.FileOperations;

public class GenerateRandomUserTests {

	Logger log = null;
	Constants constants = new Constants();
	UserGeneration userGenerationObj = new UserGeneration();
	FileOperations fileOperations = new FileOperations();

	@BeforeClass(alwaysRun = true)
	public void setUp() throws Exception {
		log = LogManager.getLogger(GenerateRandomUserTests.class);
		LoggerContext context = (LoggerContext) LogManager.getContext(false);
		context.setConfigLocation(new File(constants.CONFIG_LOG4J_FILE_PATH).toURI());
	}

	@Test(priority = 1, groups = { "smoke" })
	public void verifyGenerateRandomUser() throws Exception, IOException {
		log.info("verifyGenerateRandomUser() Test Started");
		SoftAssert softAssert = new SoftAssert();
		Response response = userGenerationObj.getRandomUser(log);
		softAssert.assertTrue(response.getStatusCode() == 200);
		softAssert.assertAll();
		JSONObject jsonObject = new JSONObject(response.getBody().asString());
		JSONArray resultsArray = jsonObject.getJSONArray("results");
		JSONObject nameObject = resultsArray.getJSONObject(0).getJSONObject("name");
		String firstName = nameObject.getString("first");
		String lastName = nameObject.getString("last");
		String email = resultsArray.getJSONObject(0).getString("email");
		fileOperations.updateValueToPropertyFile(constants.CONFIG_WEB_FILE_PATH, "firstName", firstName);
		fileOperations.updateValueToPropertyFile(constants.CONFIG_WEB_FILE_PATH, "lastName", lastName);
		fileOperations.updateValueToPropertyFile(constants.CONFIG_WEB_FILE_PATH, "email", email);
		ReportListener.logToReport("verifyGenerateRandomUser Test successful.");
		log.info("verifyGenerateRandomUser() Test Finished");
	}
}
