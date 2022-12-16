package apiOperations;

import org.apache.logging.log4j.Logger;
import org.testng.Reporter;

import constants.Config;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class UserGeneration {
	
	public Response getRandomUser(Logger log) {
		RestAssured.baseURI = Config.BASE_URL;
		Reporter.log("Base URL " + RestAssured.baseURI + " to the RESTful web service called; \n");
		RestAssured.useRelaxedHTTPSValidation();
		RequestSpecification request = RestAssured.given();
		Response response = request.get("/api");
		Reporter.log("Response recieved successfully for the GET call; \n");
		return response;
	}

}
