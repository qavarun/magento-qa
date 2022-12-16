package utils;

public class GeneralMethods {
	
	public static String generateRandomEmail() {
		String random=Double.toString(Math.floor(Math.random()*1000));
		return  "testautouser" + random + "@aol.com";
	}

}
