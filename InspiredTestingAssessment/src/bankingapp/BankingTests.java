package bankingapp;

import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;


public class BankingTests extends Utilities {
	@BeforeTest
	public void beforeTest() {
		getReport();
		loadDriver();
		launchPage();
	}

	@BeforeMethod
	public void beforeMethod() {
		login();
	}

	@Test (priority = 1)
	public void singleDeposit() {
		System.out.println("****SingleDeposit****");
		deposit("1500");
		test.info("Test Case for SingleDeposit finished");
	}

	@Test (priority = 2)
	public void multiDeposits() {
		System.out.println("****MultiDeposits****");
		WebElement dropdown = getElemByName("accountSelect");
		List<WebElement> options = dropdown.findElements(By.tagName("option"));
		Iterator<WebElement> it=options.iterator();
		while(it.hasNext())
		{
			it.next().click();
			deposit("1500");
		}	
		test.info("Test Case for MultiDeposits finished");
	}

	@Test (priority = 3)
	public void depositAndWithdraw() {
		System.out.println("****DepositandWithdraw****");
		//clear transaction log before making a deposit
		getElemByXPath("//button[@ng-class='btnClass1']").click();
		driver.manage().timeouts().implicitlyWait(20000, TimeUnit.SECONDS);
		//	driver.navigate().refresh();
		try {
			getElemByXPath("//button[@ng-click='reset()']").click();
		} catch (Exception e) {
			System.out.println("No previous transactions available");
		} finally {
			getElemByXPath("//button[@ng-click='back()']").click();
			driver.manage().timeouts().implicitlyWait(3000, TimeUnit.SECONDS);
		}
		deposit("31459");
		transaction("31459");
		withdrawal("31459");
		transaction("31459");
		test.info("Test Case for DepositandWithdrawal finished");
	}

	@AfterMethod
	public void getTestResults(ITestResult result) {
		try {
			getResult(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logout();
	}

	@AfterTest
	public void afterTest() {
		closePage();
	}

}
