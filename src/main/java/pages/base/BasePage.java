package pages.base;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

public class BasePage {

	final int WAIT_FOR_SECONDS = 60;

	private static Logger log = null;

	public BasePage() {
		log = LogManager.getLogger(BasePage.class);
	}

	/**
	 * @param driver
	 * @param elem
	 */
	public void waitForElementVisibility(RemoteWebDriver driver, WebElement elem) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
		webDriverWait.until(ExpectedConditions.visibilityOf(elem));
	}

	/**
	 * @param driver
	 * @param locator
	 */
	public void waitForElementVisibility(RemoteWebDriver driver, By locator) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
		webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(locator)));
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}

	/**
	 * @param driver
	 * @param elem
	 */
	public void waitForElementInVisibility(RemoteWebDriver driver, WebElement elem) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
		webDriverWait.until(ExpectedConditions.invisibilityOf(elem));
	}

	/**
	 * @param driver
	 * @param locator
	 */
	public void waitForElementInVisibility(RemoteWebDriver driver, By locator) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
		webDriverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	/**
	 * @param driver
	 * @param locator
	 */
	public void waitForElementClickability(RemoteWebDriver driver, By locator) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
		webDriverWait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * @param driver
	 * @param locator
	 */
	public void waitForElementClickability(RemoteWebDriver driver, WebElement locator) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * @param driver
	 * @param locator
	 */
	public void waitForAllElementPresenceByElement(RemoteWebDriver driver, By locator) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(locator));
	}

	/**
	 * @param driver
	 * @param locator
	 */
	public void waitForvisibilityOfAllElements(RemoteWebDriver driver, List<WebElement> element) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
		webDriverWait.until(ExpectedConditions.visibilityOfAllElements(element));
	}

	/**
	 * @param driver
	 * @param locator
	 * @throws InterruptedException
	 */
	public void enterData(RemoteWebDriver driver, By locator, String value) {
		waitForElementClickability(driver, locator);
		driver.findElement(locator).click();
		driver.findElement(locator).clear();
		driver.findElement(locator).sendKeys(value);
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @throws InterruptedException
	 */
	public void enterDataBySpaceAndEnterKeys(RemoteWebDriver driver, By locator) throws InterruptedException {
		waitForElementVisibility(driver, locator);
		driver.findElement(locator).sendKeys(Keys.SPACE);
		hardWait(2000);
		driver.findElement(locator).sendKeys(Keys.ENTER);
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @param value
	 */
	public void enterDataAfterDeleteAllDefaultValue(RemoteWebDriver driver, By locator, String value) {
		waitForElementVisibility(driver, locator);
		driver.findElement(locator).sendKeys(Keys.CONTROL + "a");
		driver.findElement(locator).sendKeys(Keys.DELETE);
		driver.findElement(locator).sendKeys(value);
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @param value
	 */
	public void enterDataAfterDeleteAllDefaultValue(RemoteWebDriver driver, WebElement locator, String value) {
		waitForElementVisibility(driver, locator);
		locator.sendKeys(Keys.CONTROL + "a");
		locator.sendKeys(Keys.DELETE);
		locator.sendKeys(value);
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @param value
	 */
	public void enterDataAfterDeleteValue(RemoteWebDriver driver, By locator, String value) {
		waitForElementVisibility(driver, locator);
		driver.findElement(locator).sendKeys(Keys.CONTROL + "a");
		driver.findElement(locator).sendKeys(Keys.DELETE);
		driver.findElement(locator).sendKeys(value);
		driver.findElement(locator).sendKeys(Keys.ENTER);
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @param value
	 */
	public void enterDataAndPressEnter(RemoteWebDriver driver, By locator, String value) {
		waitForElementVisibility(driver, locator);
		driver.findElement(locator).clear();
		driver.findElement(locator).sendKeys(value);
		hardWait(2000);
		driver.findElement(locator).sendKeys(Keys.ENTER);
	}

	/**
	 * 
	 * @param driver
	 */
	public void arrowDownAndPressEnter(RemoteWebDriver driver) {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_DOWN).build().perform();
		action.sendKeys(Keys.TAB).build().perform();
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 * @throws Exception
	 */
	public WebElement returnSecondLastElement(RemoteWebDriver driver, By locator) throws Exception {
		WebElement latestElemnent = null;
		List<WebElement> elements = driver.findElements(locator);
		int currentElement = 1;
		for (WebElement wElement : elements) {
			if (elements.size() != currentElement) {
				latestElemnent = wElement;
			}
			currentElement += 1;
		}
		return latestElemnent;
	}

	/**
	 * @param driver
	 * @param locator
	 * @param value
	 * @throws InterruptedException
	 */
	public void enterDataWithoutClearTextField(RemoteWebDriver driver, By locator, String value) {
		waitForElementVisibility(driver, locator);
		driver.findElement(locator).sendKeys(value);
	}

	/**
	 * 
	 * @param driver
	 * @param element
	 * @param value
	 */
	public void enterDataWithoutClearTextField(RemoteWebDriver driver, WebElement element, String value) {
		waitForElementVisibility(driver, element);
		element.sendKeys(value);
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 */
	public void click(RemoteWebDriver driver, By locator) {
		try {
			waitForElementVisibility(driver, locator);
			waitForElementClickability(driver, locator);
			driver.findElement(locator).click();
			log.info("Click action performed successfully on element " + driver);
		} catch (Exception e) {
			driver.findElement(locator).click();
		}
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @param value
	 */
	public void doubleClickAndEnterData(RemoteWebDriver driver, By locator, String value) {
		waitForElementVisibility(driver, locator);
		waitForElementClickability(driver, locator);
		Actions action = new Actions(driver);
		// Here I used JavascriptExecutor interface to scroll down to the targeted
		// element
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", getWebElement(driver, locator));
		// used doubleClick(element) method to do double click action
		action.doubleClick(getWebElement(driver, locator)).sendKeys(value).build().perform();
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @param value
	 */
	public void enterDataWithdoubleClick(RemoteWebDriver driver, WebElement locator, String value) {
		waitForElementVisibility(driver, locator);
		Actions action = new Actions(driver);
		// Here I used JavascriptExecutor interface to scroll down to the targeted
		// element
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", locator);
		// used doubleClick(element) method to do double click action
		action.doubleClick(locator).sendKeys(value).build().perform();
	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 */
	public void doubleClick(RemoteWebDriver driver, By locator) {
		waitForElementVisibility(driver, locator);
		waitForElementClickability(driver, locator);
		Actions action = new Actions(driver);
		// Here I used JavascriptExecutor interface to scroll down to the targeted
		// element
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", getWebElement(driver, locator));
		// used doubleClick(element) method to do double click action
		action.doubleClick(getWebElement(driver, locator)).build().perform();
	}

	/**
	 * 
	 * @param driver
	 * @param element
	 */
	public void JSClick(RemoteWebDriver driver, WebElement element) {
		JavascriptExecutor jse2 = driver;
		jse2.executeScript("arguments[0].click();", element);
	}

	public int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(10000);
	}

	public int generateDynamicRandomNumber(String size) {
		Random random = new Random();
		int number = 0;
		switch (size) {
		
		case "2":
			number = random.nextInt(100);
			break;
		
		case "3":
			number = random.nextInt(1000);
			break;
		
		case "4":
			number = random.nextInt(10000);
			break;

		case "5":
			number = random.nextInt(100000);
			break;

		case "6":
			number = random.nextInt(1000000);
			break;

		case "7":
			number = random.nextInt(10000000);
			break;
		
		case "8":
			number = random.nextInt(100000000);
			break;
		}

		return number;
	}

	/**
	 * @param driver
	 * @param locator
	 * @param text
	 */
	public void dropdownSelectByText(RemoteWebDriver driver, By locator, String text) {
		try {
			waitForElementVisibility(driver, locator);
			Select dropdown = new Select(driver.findElement(locator));
			dropdown.selectByVisibleText(text);
			log.info("SelectByVisibleText action performed successfully on element " + driver);
		} catch (TimeoutException e) {
			log.error("Timeout occurs while selecting dropdrown option for element " + locator);
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException occurs while selecting dropdrown option " + locator);
		}
	}

	/**
	 * @param driver
	 * @param locator
	 * @param text
	 */
	public void dropdownSelectByText(RemoteWebDriver driver, WebElement element, String text) {
		try {
			Select dropdown = new Select(element);
			dropdown.selectByVisibleText(text);
			log.info("SelectByVisibleText action performed successfully on element " + driver);
		} catch (TimeoutException e) {
			log.error("Timeout occurs while selecting dropdrown option for element " + element);
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException occurs while selecting dropdrown option " + element);
		}
	}

	/**
	 * @param driver
	 * @param locator
	 * @param index
	 */
	public void dropdownSelectByIndex(RemoteWebDriver driver, By locator, int index) {
		try {
			waitForElementVisibility(driver, locator);
			Select dropdown = new Select(driver.findElement(locator));
			dropdown.selectByIndex(index);
			log.info("SelectByIndex action performed successfully on element " + driver);
		} catch (TimeoutException e) {
			log.error("Timeout occurs while selecting dropdrown option for element " + locator);
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException occurs while selecting dropdrown option " + locator);
		}
	}

	public void pageRefresh(RemoteWebDriver driver) {
		driver.navigate().refresh();
	}

	/**
	 * @param driver
	 * @param locator
	 * @param value
	 */
	public void dropdownSelectByValue(RemoteWebDriver driver, By locator, String value) {
		try {
			waitForElementVisibility(driver, locator);
			Select dropdown = new Select(driver.findElement(locator));
			dropdown.selectByValue(value);
			log.info("SelectByValue action performed successfully on element " + driver);
		} catch (TimeoutException e) {
			log.error("Timeout occurs while selecting dropdrown option for element " + locator);
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException occurs while selecting dropdrown option " + locator);
		}
	}

	/**
	 * @param driver
	 * @param locator
	 * @param value
	 */
	public void selectValueFromListUsingSelectClass(RemoteWebDriver driver, By locator, String value) {
		try {
			waitForElementVisibility(driver, locator);
			Select dropdown = new Select(driver.findElement(locator));
			dropdown.selectByValue(value);
			log.info("SelectByValue action performed successfully on element " + driver);
		} catch (TimeoutException e) {
			log.error("Timeout occurs while selecting dropdrown option for element " + locator);
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			log.error("NoSuchElementException occurs while selecting dropdrown option " + locator);
		}
	}

	/**
	 * @param select
	 * @return
	 */
	public String getDropdownSelectedValue(Select select) {
		return select.getFirstSelectedOption().getText();
	}

	public String getCurrentDate() {
		LocalDate dateObj = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String currentDate = dateObj.format(formatter);
		return currentDate;
	}

	public String getCurrentDateMonthInWords() {
		DateFormat dateformat = new SimpleDateFormat("yyyy-MMM-d");
		Date date = new Date();
		String currentDate = dateformat.format(date).toUpperCase();
		return currentDate;
	}

	public String getCurrentDateTime() {
		DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String currentDate = dateformat.format(date).toUpperCase();
		return currentDate;
	}

	public String getThreeDaysDifferenceFromCurrentDate(String curDate, int daysDifference) {
		String nextDate = "";
		try {
			Calendar today = Calendar.getInstance();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = format.parse(curDate);
			today.setTime(date);
			today.add(Calendar.DAY_OF_YEAR, daysDifference);
			nextDate = format.format(today.getTime()).toUpperCase();
		} catch (Exception e) {
			return nextDate;
		}
		return nextDate;
	}

	public String getThreeDaysDifferenceFromCurrentDateWithFormate(String curDate, int daysDifference) {
		String nextDate = "";
		try {
			Calendar today = Calendar.getInstance();
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = format.parse(curDate);
			today.setTime(date);
			today.add(Calendar.DAY_OF_YEAR, daysDifference);
			nextDate = format.format(today.getTime());
		} catch (Exception e) {
			return nextDate;
		}
		return nextDate;
	}

	/**
	 * @param driver
	 * @param locators
	 * @return
	 * @throws InterruptedException
	 */
	public boolean isElementPresent(RemoteWebDriver driver, By locators) {
		boolean status = false;
		try {
			driver.findElement(locators).isDisplayed();
			status = true;
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	public boolean isElementPresent(RemoteWebDriver driver, WebElement element) {
		boolean status = false;
		try {
			element.isDisplayed();
			status = true;
		} catch (NoSuchElementException e) {
			status = false;
		}
		return status;
	}

	public boolean checkIfElementIsClickable(RemoteWebDriver driver, By locator) {
		try {
			WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
			webDriverWait.until(ExpectedConditions.elementToBeClickable(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean checkIfElementIsClickable(RemoteWebDriver driver, By locator, int waitTimeForSeconds) {
		try {
			WebDriverWait webDriverWait = new WebDriverWait(driver, waitTimeForSeconds);
			webDriverWait.until(ExpectedConditions.elementToBeClickable(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean checkIfElementIsClickable(RemoteWebDriver driver, WebElement element) {
		try {
			WebDriverWait webDriverWait = new WebDriverWait(driver, WAIT_FOR_SECONDS);
			webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			return false;

		}
	}

	public boolean checkIfElementIsClickable(RemoteWebDriver driver, WebElement element, int waitTimeForSeconds) {
		try {
			WebDriverWait webDriverWait = new WebDriverWait(driver, waitTimeForSeconds);
			webDriverWait.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		} catch (Exception e) {
			return false;

		}
	}

	public List<WebElement> getWebElements(RemoteWebDriver driver, By locator) {
		return driver.findElements(locator);
	}

	public WebElement getWebElement(RemoteWebDriver driver, By locator) {
		return driver.findElement(locator);
	}

	public WebElement getDynamicWebElementByText(RemoteWebDriver driver, String xpath) {
		return driver.findElement(By.xpath(xpath));
	}

	/**
	 * @param driver
	 * @param locator
	 * @return
	 *
	 */
	public String getText(RemoteWebDriver driver, By locator) {
		waitForElementVisibility(driver, locator);
		try {
			return driver.findElement(locator).getText().trim();
		} catch (StaleElementReferenceException e) {
			return driver.findElement(locator).getText().trim();
		}
	}

	public String getTextFromDefaultSelectedDropdownValue(RemoteWebDriver driver, By locator) {

		Select select = new Select(driver.findElement(locator));
		WebElement option = select.getFirstSelectedOption();
		String defaultItem = option.getText();
		return defaultItem;
	}

	/**
	 * @param driver
	 * @param locator
	 * @return
	 * @throws InterruptedException
	 */
	public boolean getCheckBoxStaus(RemoteWebDriver driver, By locator) throws InterruptedException {
		boolean status = false;
		try {
			status = driver.findElement(locator).isSelected();
		} catch (NoSuchElementException e) {
			status = false;
		}
		return status;
	}

	public boolean getCheckBoxStaus(RemoteWebDriver driver, WebElement element) throws InterruptedException {
		boolean status = false;
		try {
			status = element.isSelected();
		} catch (NoSuchElementException e) {
			status = false;
		}
		return status;
	}

	/**
	 * @param driver
	 * @param index
	 */
	public void switchWindows(RemoteWebDriver driver, Integer index) {
		try {
			Set<String> allWindows = driver.getWindowHandles();
			String[] windows = allWindows.toArray(new String[allWindows.size()]);
			System.out.println(windows.toString());
			driver.switchTo().window(windows[index]);
		} catch (Exception e) {
			System.out.println("Unexpected issue occurred in switching windows: " + e);
		}
	}

	public void closeCurrentWindow(RemoteWebDriver driver) {
		driver.close();
	}

	/**
	 * @param driver
	 * @param locator
	 * @param attributeName
	 * @return
	 */
	public String getValueFromAttribute(RemoteWebDriver driver, By locator, String attributeName) {
		waitForElementVisibility(driver, locator);
		return driver.findElement(locator).getAttribute(attributeName);
	}

	/**
	 * @param driver
	 * @param locator
	 * @return
	 */
	public String getElementTagName(RemoteWebDriver driver, By locator) {
		return driver.findElement(locator).getTagName();
	}

	/**
	 * @param driver
	 * @param locator
	 * @return
	 */
	public String getElementCSSColor(RemoteWebDriver driver, By locator) {
		waitForElementVisibility(driver, locator);
		return driver.findElement(locator).getCssValue("color");
	}

	public void scrollToRightUsingAction(RemoteWebDriver driver, int offset) {
		hardWait(3000);
		WebElement ele = driver.findElement(By.cssSelector("div.ag-body-horizontal-scroll-viewport"));
		waitForElementVisibility(driver, ele);
		hardWait(2000);
		ele.click();
		checkIfElementIsClickable(driver, ele);
		Actions move = new Actions(driver);
		Actions moveToElement = move.moveToElement(ele);
		for (int i = 0; i < offset; i++) {
			moveToElement.sendKeys(Keys.RIGHT).build().perform();
		}
	}

	/**
	 * @param driver
	 * @param locator
	 */
	public void mouseHoverAndClick(RemoteWebDriver driver, By locator) {
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(locator)).click().build().perform();
	}

	/**
	 * @param driver
	 * @param element
	 */
	public void mouseHoverAndClickByElement(RemoteWebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).click().perform();
	}

	/**
	 * @param driver
	 * @param locator
	 */
	public void mouseHover(RemoteWebDriver driver, By locator) {
		Actions action = new Actions(driver);
		WebElement target = driver.findElement(locator);
		action.moveToElement(target).build().perform();
	}

	/**
	 * @param driver
	 * @param locator Limitations:Without help of ActionClass we are not able to
	 *                draw rectangle on map Methods :moveToElement() ,moveByOffset()
	 *                ,doubleClick()
	 */
	public void drawRectangleUsingAction(RemoteWebDriver driver, By locator) {
		Actions builder = new Actions(driver);
		Action drawAction = builder.moveToElement(getWebElement(driver, locator), 15, 10) // start points x axis and y
																							// axis.
				.click().moveByOffset(20, 30) // 1st points (x1,y1)
				.click().moveByOffset(10, 15).click()// 2nd points (x2,y2)
				.pause(java.time.Duration.ofSeconds(2)).moveByOffset(50, 20) // 3rd points (x1,y1)
				.pause(java.time.Duration.ofSeconds(2)).doubleClick().build();
		drawAction.perform();
	}

	/**
	 * @param driver
	 * @param locator Limitations:Without help of ActionClass we are not able to
	 *                draw circle on map Methods:moveToElement() ,moveByOffset()
	 *                ,doubleClick()
	 */
	public void drawCricleUsingAction(RemoteWebDriver driver, By locator) {
		Actions builder = new Actions(driver);
		Action drawAction = builder.moveToElement(getWebElement(driver, locator), 35, 15) // start points x axis and y
																							// axis.
				.click().moveByOffset(10, 20) // 1st points (x1,y1)
				.click().moveByOffset(10, 30).click()// 2nd points (x2,y2)
				.moveByOffset(15, 30) // 3rd points (x1,y1)
				.doubleClick().build();
		drawAction.perform();
	}

	/**
	 * @param driver
	 * @param locator Limitations:Without help of ActionClass we are not able to
	 *                draw line on map Methods :moveToElement() ,moveByOffset()
	 *                ,doubleClick()
	 */
	public void drawLineOnMapUsingAction(RemoteWebDriver driver, By locator) {
		Actions builder = new Actions(driver);
		Action drawAction = builder.moveToElement(getWebElement(driver, locator), 15, 10) // start points x axis and y
				.click().moveByOffset(20, 30) // 1st points (x1,y1)
				.click().moveByOffset(30, 40) // 2rd points (x1,y1)
				.doubleClick().build();
		drawAction.perform();
	}

	public void drawLineOnMapUsingActions(RemoteWebDriver driver, By locator) {
		Actions builder = new Actions(driver);
		Action drawAction = builder.moveToElement(getWebElement(driver, locator), 15, 10) // start points x axis and y
				.click().moveByOffset(20, 30) // 1st points (x1,y1)
				.doubleClick().build();
		drawAction.perform();
	}

	/**
	 * @param driver
	 * @param element
	 */
	public void mouseHover(RemoteWebDriver driver, WebElement element) {
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
	}

	/**
	 * @param driver
	 */
	public void scrollToRightUsingAction(RemoteWebDriver driver) {
		List<WebElement> ele = driver.findElements(By.cssSelector("div.ag-body-horizontal-scroll-viewport"));
		try {
			if (ele.size() > 0) {
				ele.get(0).click();
				Actions move = new Actions(driver);
				move.moveToElement(ele.get(0)).clickAndHold();
				move.moveByOffset(300, 0);
				move.release();
				move.perform();
				hardWait(3000);
			}
		} catch (Exception e) {
		}

	}

	/**
	 * @param milliSeconds
	 * @throws InterruptedException
	 */
	public void hardWait(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void backToNavigate(RemoteWebDriver driver) {
		driver.navigate().back();
	}

	/**
	 * @param driver
	 * @param index
	 * @throws InterruptedException
	 */
	public void switchToFrameByIndex(RemoteWebDriver driver, int index) {
		driver.switchTo().frame(index);
	}

	public void switchOutFromIframe(RemoteWebDriver driver) {
		driver.switchTo().defaultContent();
		hardWait(1000);
	}

	/**
	 * @param driver
	 * @param value
	 */
	public void selectValueInList(RemoteWebDriver driver, String value) {
		By recordList = By.cssSelector("div.menuable__content__active div.v-list-item");
		hardWait(3000);
		waitForAllElementPresenceByElement(driver, recordList);
		List<WebElement> compositeQueryTypeElements = getWebElements(driver, recordList);
		for (WebElement compositeQueryTypeElement : compositeQueryTypeElements) {
			waitForElementVisibility(driver, compositeQueryTypeElement);
			waitForElementClickability(driver, compositeQueryTypeElement);
			if (compositeQueryTypeElement.getText().trim().contains(value)) {
				compositeQueryTypeElement.click();
				break;
			}
		}
	}

	/**
	 * @param driver
	 */
	public void scrollDown(RemoteWebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,1000)");
	}

	public void scrollUpByYaxis(RemoteWebDriver driver, String y) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0," + y + ")");
	}

	public void scrollRightXaxis(RemoteWebDriver driver, String x) {
		((JavascriptExecutor) driver).executeScript("window.scrollBy(" + x + ",0)");
	}

	/**
	 * @param driver
	 * @param locator
	 */
	public void scrollToElement(RemoteWebDriver driver, By locator) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", driver.findElement(locator));
	}

	/**
	 * @param driver
	 * @param element
	 */
	public void scrollToElement(RemoteWebDriver driver, WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
	}

	public void scrollToTop(RemoteWebDriver driver) {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");
	}

	/**
	 * @param driver
	 * @param pdfURL
	 * @param pdfFileName
	 * @return
	 * @throws IOException
	 */
	public String readPDFInURL(RemoteWebDriver driver, String pdfURL, String pdfFileName) throws IOException {
		// page with example pdf document
		driver.get(pdfURL + pdfFileName);
		String output = null;
		URL url = new URL(driver.getCurrentUrl());
		InputStream is = url.openStream();
		BufferedInputStream fileToParse = new BufferedInputStream(is);
		org.apache.pdfbox.pdmodel.PDDocument document = null;
		try {
			document = PDDocument.load(fileToParse);
			output = new PDFTextStripper().getText(document);
		} finally {
			if (document != null) {
				document.close();
			}
			fileToParse.close();
			is.close();
		}
		hardWait(2000);
		driver.navigate().back();
		hardWait(3000);
		return output;
	}

	/**
	 * @param formate
	 * @return
	 */
	public String getCurrentDateWithUserFormate(String format) {
		DateFormat dateformat = new SimpleDateFormat(format);
		Date date = new Date();
		String currentDate = dateformat.format(date);
		return currentDate;
	}

	public void doubleClickOnElement(RemoteWebDriver driver, By elem) {
		Actions actions = new Actions(driver);
		WebElement elementLocator = driver.findElement(elem);
		actions.doubleClick(elementLocator).perform();
	}

	public void clickAllowLocation() {
		Robot robot;
		try {
			robot = new Robot();
			robot.delay(5000);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyPress(KeyEvent.VK_ENTER);
			hardWait(2000);
		} catch (AWTException e) {
			log.error("Not able to click on allow location");
		}
	}

	public void pressEnter() {
		Robot robot;
		try {
			robot = new Robot();
			robot.delay(5000);
			robot.keyPress(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			log.error("Not able press Enter");
		}
	}

	public void pressEnterButton(RemoteWebDriver driver) {
		Actions actions = new Actions(driver);
		By noteContains = By.cssSelector(".fr-element.fr-view");
		actions.moveToElement(driver.findElement(noteContains));
		actions.click().build().perform();
		driver.findElement(noteContains).sendKeys(Keys.chord(Keys.ENTER));
	}

	/**
	 * @param driver
	 * @throws UnsupportedFlavorException
	 */
	public String getCopyMessageFromCopyToClipboard(RemoteWebDriver driver) {
		String clipBoradMsg = null;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Clipboard clipboard = toolkit.getSystemClipboard();
		try {
			clipBoradMsg = (String) clipboard.getData(DataFlavor.stringFlavor);
		} catch (UnsupportedFlavorException | IOException e) {
			e.printStackTrace();
		}
		return clipBoradMsg;

	}

	public void verifyASCAndDESCOrder(RemoteWebDriver driver, SoftAssert softAssert, String header, String listCount,
			String listElements, Boolean sorted) {
		By heading = By.xpath(header);
		if (!sorted) {
			click(driver, heading);
			hardWait(4000);
		}
		hardWait(4000);
		List<WebElement> allSavedViewName = getWebElements(driver, By.xpath(listCount));
		int count = allSavedViewName.size();
		List<String> actualAscNamelist = new ArrayList<String>();
		List<String> actualDescNamelist = new ArrayList<String>();
		List<String> expectAscNamelist = new ArrayList<String>();
		List<String> expectDescNamelist = new ArrayList<String>();
		String[] eleXpath = listElements.split("count");
		for (int i = 1; i < count; i++) {

			expectAscNamelist.add(getText(driver, By.xpath(eleXpath[0] + (i) + eleXpath[1])));

		}
		Collections.sort(expectAscNamelist, String.CASE_INSENSITIVE_ORDER);// Ascending sort

		/************************ expected asc list ******************/
		/*
		 * System.out.println("expected asc list"); for (String str : expectAscNamelist)
		 * { System.out.println(str); }
		 */
		/*************************************************************/
		for (int i = 1; i < count; i++) {
			actualAscNamelist.add(getText(driver, By.xpath(eleXpath[0] + (i) + eleXpath[1])));
		}
		/************************ Actual asc list ******************/
		/*
		 * System.out.println(" Actual asc list"); for (String str : actualAscNamelist)
		 * { System.out.println(str); }
		 */
		/*************************************************************/
		click(driver, heading);
		hardWait(4000);
		for (int i = 1; i < count; i++) {
			expectDescNamelist.add(getText(driver, By.xpath(eleXpath[0] + (i) + eleXpath[1])));

		}
		Collections.sort(expectDescNamelist, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
		/************************ Expected desc list ******************/
		/*
		 * System.out.println("Expected desc list "); for (String str :
		 * expectDescNamelist) { System.out.println(str); }
		 */
		/*************************************************************/
		for (int i = 1; i < count; i++) {
			actualDescNamelist.add(getText(driver, By.xpath(eleXpath[0] + (i) + eleXpath[1])));
		}
		/************************ Actual desc list ******************/
		/*
		 * System.out.println("Actual desc list"); for (String str : actualDescNamelist)
		 * { System.out.println(str); }
		 */
		/*************************************************************/
		softAssert.assertEquals(actualAscNamelist, expectAscNamelist, "Ascending order is not working");
		softAssert.assertEquals(actualDescNamelist, expectDescNamelist, "Descending order is not working");
	}

	public String getHexaColorCodeFromRGB(RemoteWebDriver driver, By locator) {
		waitForElementVisibility(driver, locator);
		String value = getValueFromAttribute(driver, locator, "style");
		String rgbColor[] = value.split(":|;");
		String hexaColor = Color.fromString(rgbColor[1]).asHex();
		return hexaColor.toUpperCase();
	}

	public void clickOnMapImage(RemoteWebDriver driver, By locator) {
		try {
			Actions builder = new Actions(driver);
			Action drawAction = builder.moveToElement(getWebElement(driver, locator), 15, 10) // start points x axis and
																								// y axis.
					.click().moveByOffset(10, 20) // 1st points (x1,y1)
					.click().moveByOffset(10, 15).click()// 2nd points (x2,y2)
					.moveByOffset(10, 15) // 3rd points (x1,y1)
					.doubleClick().build();
			drawAction.perform();
		} catch (Exception e) {
			System.out.println("Map image is not clicked");
		}
	}

	public void closeExtraTab(RemoteWebDriver driver) {
		ArrayList<String> handles = new ArrayList<String>(driver.getWindowHandles());
		if (handles.size() > 1) {
			driver.switchTo().window(handles.get(1));
			driver.close();
			driver.switchTo().window(handles.get(0));
		}
	}

	public void openCopiedLinkInNewWindow(RemoteWebDriver driver) throws IOException, UnsupportedFlavorException {
		driver.executeScript("window.open('','_blank');");
		switchWindows(driver, 1);
		driver.get(getCopyMessageFromCopyToClipboard(driver));
	}

	public void openNewTab(RemoteWebDriver driver) {
		driver.executeScript("window.open('','_blank');");
		switchWindows(driver, 1);
	}

	/**
	 * 
	 * @param driver
	 */
	public void openSecondChildWindow(RemoteWebDriver driver) {
		driver.executeScript("window.open('','_blank');");
		switchWindows(driver, 2);
	}

	/**
	 * 
	 * @param driver
	 */
	public void minimizeWindow(RemoteWebDriver driver) {
		driver.manage().window().setPosition(new Point(-2000, 0));
	}

	/**
	 * 
	 * @return
	 */
	public RemoteWebDriver openNewIncognitoTab() {
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_setting_values.geolocation", 1);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		capabilities = capabilities.merge(DesiredCapabilities.chrome());
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		RemoteWebDriver driverIncognito = new ChromeDriver(options);
		driverIncognito.manage().window().maximize();
		return driverIncognito;
	}

	/**
	 * 
	 * @param driver
	 */
	public void handleConfirmAlertOnPage(RemoteWebDriver driver) {
		driver.switchTo().alert().accept();
	}

	/**
	 * 
	 * @param webdriver
	 * @param fileWithPath
	 * @throws Exception
	 */
	public static void takeSnapShot(RemoteWebDriver webdriver, String fileWithPath) throws Exception {

		// Convert web driver object to TakeScreenshot

		TakesScreenshot scrShot = ((TakesScreenshot) webdriver);
		// Call getScreenshotAs method to create image file
		File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

		// Move image file to new destination
		File DestFile = new File(fileWithPath);

		// Copy file at destination
		FileUtils.copyFile(SrcFile, DestFile);

	}

	/**
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 */
	public List<String> getListOfAllOptionsFromDropdown(RemoteWebDriver driver, By locator) {
		
		List<String> allOptionsList = new ArrayList<>();
		Select select_obj = new Select(driver.findElement(locator));
		List<WebElement> op = select_obj.getOptions();
		int size = op.size();
		for (int i = 0; i < size; i++) {
			String options = op.get(i).getText();
			allOptionsList.add(options);
			System.out.println(allOptionsList.get(i));
		}
		return allOptionsList;
	}

	/**
	 * 
	 * @param sdate
	 * @return
	 * @throws ParseException
	 */
	public LocalDate convertStringToDateThenToLocalDate(String sdate) throws ParseException {

		Date date = new SimpleDateFormat("yyyy-mm-dd").parse(sdate);
		LocalDate localdate = LocalDate.parse(new SimpleDateFormat("yyyy-mm-dd").format(date));
		return localdate;
	}

	/**
	 * 
	 * @param first_date
	 * @param second_date
	 * @return
	 */
	public int getDifferenceInDatePeriod(LocalDate first_date, LocalDate second_date) {
		Period difference = Period.between(first_date, second_date);
		return difference.getDays();
	}
	
	public String getLoadIdFromCurrentUrl(String current_url) {
		String[] arrOfStr = current_url.split("=", -2);
		String url = arrOfStr[1];
		String[] arrOfStr1 = url.split("&", -2);
		return arrOfStr1[0];
	}
}