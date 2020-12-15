package bankingapp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.google.common.io.Files;


public class Utilities {
	public WebElement elem;
	public WebDriver driver;
	ExtentReports report;
	ExtentTest test;
	String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
	String drivPath = System.getProperty("user.dir") + "\\drivers\\";
	String srcPath = System.getProperty("user.dir") + "\\test-results\\";
	String browser = "firefox";
	String url = "http://www.way2automation.com/angularjs-protractor/banking/#/login";

	public void getReport() {
		System.out.println("****Preparing report****");
		String myhtmlRep = srcPath + "report" + dateName + ".html";
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(myhtmlRep);
		report = new ExtentReports();
		report.attachReporter(htmlReporter);
		test = report.createTest("Verify deposit and withdrawal functionalities");
		test.assignAuthor("Lebo Radebe");;
		test.info("Report generated");
	}

	public String getScreenshot(WebDriver driver, String screenshotName) throws Exception {
		System.out.println("****Preparing for screenshots****");
		String fileDirectory = srcPath + screenshotName + dateName + ".png";
		File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Files.copy(srcFile, new File(fileDirectory));
		test.info("Screenshot taken");
		return fileDirectory;
	}

	public void getResult(ITestResult result) throws Exception {
		String screenShotPath = getScreenshot(driver, "Screenshot");
		test.addScreenCaptureFromPath(screenShotPath);
	}

	public void loadDriver() {
		System.out.println("****Loading webdriver****");
		if (browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", drivPath + "chromedriver.exe");
			driver = new ChromeDriver();
		} else if(browser.equalsIgnoreCase("firefox")){
			System.setProperty("webdriver.gecko.driver", drivPath + "geckodriver.exe");
			driver = new FirefoxDriver();
		} else {
			test.info("Sorry we dont support your browser at this point.");
		}
		test.info("Webdriver for " + browser.toLowerCase() + " is loaded successully!!");
	}

	public void launchPage() {
		System.out.println("****Launching banking webpage****");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(5000, TimeUnit.SECONDS);
		test.info("Webpage is launched successfully!!" );
	}

	public void login() {
		System.out.println("****Login as Customer****");
		getElemByXPath("//button[.='Customer Login']").click();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		getElemByName("userSelect").click();
		getElemByXPath("//option[@value='3']").click();
		getElemByXPath("//button[@type='submit']").click();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		test.info("Customer logged in successfully");
	}

	public void deposit(String amount) {
		System.out.println("****Customer making a deposit****");
		getElemByXPath("//button[@ng-class='btnClass2']").click();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		getElemByXPath("//input[@ng-model='amount']").sendKeys(amount);
		getElemByXPath("//button[@type='submit']").click();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		String actualMsgd = getElemByXPath("//span[@ng-show='message']").getText();
		Assert.assertEquals(actualMsgd, "Deposit Successful");
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.SECONDS);
		test.info("Customer deposited " + amount + " successfully");
	}

	public void withdrawal(String amount) {
		System.out.println("****Customer making a withdrawal****");
		getElemByXPath("//button[@ng-class='btnClass3']").click();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		getElemByXPath("//input[@ng-model='amount']").sendKeys(amount);
		getElemByXPath("//button[@type='submit']").click();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		String actualMsgw = getElemByXPath("//span[@ng-show='message']").getText();
		Assert.assertEquals(actualMsgw, "Transaction successful");
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.SECONDS);
		test.info("Customer withdrew " + amount + " successfully");
	}

	public void transaction(String amount) {
		System.out.println("****Customer viewing transaction****");
		getElemByXPath("//button[@ng-class='btnClass1']").click();
		driver.manage().timeouts().implicitlyWait(20000, TimeUnit.SECONDS);
		//		driver.navigate().refresh();
		String actualMsgt = getElemByXPath("//tr[@id='anchor0']/td[2]").getText();
		Assert.assertEquals(actualMsgt, amount);
		getElemByXPath("//button[@ng-click='back()']").click();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		test.info("Transaction of " + amount + " is recorded successfully");
	}

	public void logout() {
		System.out.println("****Customer logout****");
		getElemByXPath("//button[@ng-click='byebye()']").click();
		getElemByXPath("//button[@ng-click='home()']").click();
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		test.info("Customer logged out successfully");
	}

	public void closePage() {
		System.out.println("****Banking is done!!****");
		driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		driver.quit();
		report.flush();
		test.info(browser + " webdriver and webpage is closed successfully!!");
	}

	public WebElement getElemByName(String Name) {
		elem = driver.findElement(By.name(Name));
		return elem;
	}

	public WebElement getElemByXPath(String XPath) {
		elem = driver.findElement(By.xpath(XPath));
		return elem;
	}
}
