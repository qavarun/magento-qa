package pages.signup;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

import pages.base.BasePage;

public class SignUpPage extends BasePage {

	public By createAnAccountLink = By.xpath("//a[text()='Create an Account']");
	public By firstnameField = By.id("firstname");
	public By lastnameField = By.id("lastname");
	public By emailAddressField = By.id("email_address");
	public By passwordField = By.id("password");
	public By confPassWordField = By.id("password-confirmation");
	public By createAccountBtn = By.xpath("//button[@title='Create an Account']");
	public By selectAddressBookLink = By.xpath("//a[text()='Address Book']");
	public By selectCountrySelector = By.id("country");
	public By selectRegionSelector = By.id("region_id");
	public By streetOneFld = By.id("street_1");
	public By cityFld = By.id("city");
	public By zipFld = By.id("zip");
	public By telephoneFld = By.id("telephone");
	public By saveAddressBtn = By.xpath("//button[@title='Save Address']");

	/**
	 * 
	 * @param driver
	 * @param email
	 * @param password
	 * @param firstname
	 * @param lastname
	 */
	public void signUpUser(RemoteWebDriver driver, String email, String password, String firstname, String lastname) {
		enterData(driver, firstnameField, firstname);
		enterData(driver, lastnameField, lastname);
		enterData(driver, emailAddressField, email);
		enterData(driver, passwordField, password);
		enterData(driver, confPassWordField, password);
		click(driver, createAccountBtn);
		hardWait(1000);
	}

	/**
	 * 
	 * @param driver
	 */
	public void clickOnCreateAccountLink(RemoteWebDriver driver) {
		click(driver, createAnAccountLink);
	}

	/**
	 * 
	 * @param driver
	 */
	public void addressUpdate(RemoteWebDriver driver) {
		click(driver, selectAddressBookLink);
		enterData(driver, streetOneFld, "Test Street Address");
		enterData(driver, cityFld, "Noida");
		enterData(driver, telephoneFld, "8800180729");
		enterData(driver, zipFld, "201301");
		scrollToElement(driver, selectCountrySelector);
		dropdownSelectByText(driver, selectCountrySelector, "India");
		hardWait(1000);
		scrollUpByYaxis(driver, "200");
		dropdownSelectByText(driver, selectRegionSelector, "Uttar Pradesh");
		click(driver, saveAddressBtn);
	}

}
