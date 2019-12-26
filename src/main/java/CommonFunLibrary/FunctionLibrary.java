package CommonFunLibrary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import Utilities.PropertyFileUtil;

public class FunctionLibrary {
public static WebDriver driver;
public static ExtentReports report;
public static ExtentTest test;
//method for launching browser
public static WebDriver startBrowser() throws Throwable
{
	if(PropertyFileUtil.getValueFor("Browser").equalsIgnoreCase("chrome"))
	{
		System.setProperty("webdriver.chrome.driver", "D:\\VINOD.VINNU\\ERP_Maven\\CommonDriver\\chromedriver.exe");
		driver=new ChromeDriver();
	}
	else if(PropertyFileUtil.getValueFor("Browser").equalsIgnoreCase("Firefox"))
	{
		driver=new FirefoxDriver();
	}
	else if(PropertyFileUtil.getValueFor("Browser").equalsIgnoreCase("ie"))
	{
		driver=new InternetExplorerDriver();
	}
	else
	{
		System.out.println("Browser is not matching");
	}
	return driver;
}
//method for Open application
public static void openApplication(WebDriver driver) throws Throwable
{
	driver.get(PropertyFileUtil.getValueFor("Url"));
	driver.manage().window().maximize();
}
//method for wait for element
public static void waitForElement(WebDriver driver,String locatortype,String locatorvalue,String waittime)
{
	WebDriverWait mywait=new WebDriverWait(driver, Integer.parseInt(waittime));
	if(locatortype.equalsIgnoreCase("id"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorvalue)));
	}
	else if(locatortype.equalsIgnoreCase("name"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorvalue)));
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
	{
		mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorvalue)));
	}
	else
	{
		System.out.println("Unable to execute waitfor element method");
	}
}
//method for typeAction
public static void typeAction(WebDriver driver,String locatortype,String locatorvalue,String testdata)
{
	if(locatortype.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(locatorvalue)).clear();
		driver.findElement(By.id(locatorvalue)).sendKeys(testdata);
	}
	else if(locatortype.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(locatorvalue)).clear();
		driver.findElement(By.name(locatorvalue)).sendKeys(testdata);
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(locatorvalue)).clear();
		driver.findElement(By.xpath(locatorvalue)).sendKeys(testdata);
	}
	else
	{
		System.out.println("Unable to execute typeAction method");
	}
}
//method for clickAction
public static void clickAction(WebDriver driver,String locatortype,String locatorvalue)
{
	if(locatortype.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(locatorvalue)).sendKeys(Keys.ENTER);
	}
	else if(locatortype.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(locatorvalue)).click();
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(locatorvalue)).click();
	}
	else
	{
		System.out.println("Unable to execute clickAction method");
	}
}
//method for closeBrowser
public static void closeBrowser(WebDriver driver)
{
	driver.close();
}
public static String generateDate(){
	Date date=new Date();
	SimpleDateFormat sdf=new SimpleDateFormat("YYYY_MM_dd_ss");
	return sdf.format(date);
}
 //method for capturing supplier number
public static void captureData(WebDriver driver,String locatortype,String locatorvalue) throws Throwable
{
	String snumber="";
	if(locatortype.equalsIgnoreCase("id"))
	{
		snumber=driver.findElement(By.id(locatorvalue)).getAttribute("value");
	}
	else if(locatortype.equalsIgnoreCase("name"))
	{
		snumber=driver.findElement(By.name(locatorvalue)).getAttribute("value");
	}
	else if(locatortype.equalsIgnoreCase("xpath"))
	{
		snumber=driver.findElement(By.xpath(locatorvalue)).getAttribute("value");
	}

//writing into notepad
FileWriter fw=new FileWriter("D:\\VINOD.VINNU\\ERP_Maven\\CaptureData\\supplier.txt");
BufferedWriter bw=new BufferedWriter(fw);
bw.write(snumber);
bw.flush();
bw.close();
}
//table validation for supplier
public static void tableValidation(WebDriver driver,String column) throws Throwable
{
	FileReader fr=new FileReader("D:\\VINOD.VINNU\\ERP_Maven\\CaptureData\\supplier.txt");
	BufferedReader br=new BufferedReader(fr);
	String exp_data=br.readLine();
	//convert columndata into integer
	int columndata=Integer.parseInt(column);
	if(!driver.findElement(By.xpath(PropertyFileUtil.getValueFor("search-text"))).isDisplayed())
	{
		driver.findElement(By.xpath(PropertyFileUtil.getValueFor("search-panel"))).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueFor("search-text"))).clear();
		Thread.sleep(5000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueFor("search-text"))).sendKeys(exp_data);
		Thread.sleep(5000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueFor("search-button"))).click();
		WebElement table=driver.findElement(By.xpath(PropertyFileUtil.getValueFor("supplier-table")));
	   //count no of rows
	 List<WebElement>rows=table.findElements(By.tagName("tr"));
	for(int i=1;i<rows.size();i++)
	{
		String act_data=driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr["+i+"]/td["+columndata+"]/div/span/span")).getText();		
		Thread.sleep(5000);
		System.out.println(exp_data+" "+act_data);
		Thread.sleep(5000);
		Assert.assertEquals(exp_data, act_data,"snumber is not matching");
		break;
	}
		
	}
}
}
