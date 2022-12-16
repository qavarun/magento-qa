package pages.operations;

import static org.testng.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import pages.base.BasePage;

public class OperationsPage extends BasePage {

	// Operations
	public By whatIsNewLink = By.xpath("//a[contains(@href,'what-is-new')]");
	public By firstProductLink = By.xpath("(//li[@class='product-item']/div/a)[1]");
	public By selectSizeS = By.xpath("//div[@option-label=\"S\"]");
	public By selectColorWhite = By.cssSelector(".swatch-option.color[option-tooltip-value=\"#ffffff\"]");
	public By addToCartBtn = By.id("product-addtocart-button");
	public By proceedToCheckOutBtn = By.xpath("//button[contains(@data-role,'proceed-to-checkout')]");
	public By nextButton = By.xpath("//div[@id='shipping-method-buttons-container']/div/button[@type='submit']");
	public By placeOrderBtn = By.xpath("//span[text()='Place Order']");
	public By successMessageHead = By.xpath("//main[@id='maincontent']/div/h1/span");
	public By goToShopCartLink = By.xpath("//div[contains(text(),\"You\")]/a[contains(@href,'checkout/cart')]");

	/**
	 * 
	 * @param driver
	 */
	public void clickOntrendingLink(RemoteWebDriver driver) {
		click(driver, whatIsNewLink);
	}

	public void clickOnFirstProductAndAddToCart(RemoteWebDriver driver) {
		// TODO Auto-generated method stub
		scrollDown(driver);
		click(driver, firstProductLink);
		hardWait(1000);
		click(driver, selectSizeS);
		click(driver, selectColorWhite);
		click(driver, addToCartBtn);
		click(driver, goToShopCartLink);
		hardWait(1000);
	}

	/**
	 * 
	 * @param driver
	 * @param checkoutURI
	 */
	public void redirectUserToCheckOutPage(RemoteWebDriver driver, String checkoutURI) {
		driver.get(checkoutURI);
		hardWait(1000);
	}

	/**
	 * 
	 * @param driver
	 */
	public void clickOnProceedToCheckOutButton(RemoteWebDriver driver) {
		click(driver, proceedToCheckOutBtn);
	}

	/**
	 * 
	 * @param driver
	 */
	public void checkOutAndPlaceOrderBtn(RemoteWebDriver driver) {
		click(driver, nextButton);
		hardWait(1000);
		click(driver, placeOrderBtn);
		hardWait(3000);
		System.out.println(getText(driver, successMessageHead));
		assertTrue(getText(driver, successMessageHead).contains("Thank you for your purchase!"));
	}

}
