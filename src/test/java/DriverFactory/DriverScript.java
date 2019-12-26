package DriverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunLibrary.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {
public static WebDriver driver;
public static ExtentReports report;
public static ExtentTest test;

public void ERP_Test() throws Throwable
{
	//creating reference object for excel util methods
	ExcelFileUtil xl=new ExcelFileUtil();
	//iterating all row in MasterTestCases
	for(int i=1;i<=xl.rowCount("MasterTestCases");i++)
	{
	if(xl.getCellData("MasterTestCases", i,2).equalsIgnoreCase("Y"))
	{
		//store module name into TCModule
		String TCModule=xl.getCellData("MasterTestCases", i, 1);
		//generate user define html
		report=new ExtentReports("./Reports//"+TCModule+FunctionLibrary.generateDate()+".html");
		//iterate all rows in TCModule sheet
		for(int j=1;j<=xl.rowCount(TCModule);j++)
		{
			test=report.startTest(TCModule);
			//read all columns from TC module
			String Description=xl.getCellData(TCModule, j, 0);
			String Function_Name=xl.getCellData(TCModule, j, 1);
			String Locator_Type=xl.getCellData(TCModule, j, 2);
			String Locator_Value=xl.getCellData(TCModule, j, 3);
			String Test_Data=xl.getCellData(TCModule, j, 4);
			try{
				if(Function_Name.equalsIgnoreCase("startBrowser"))
				{
					driver=FunctionLibrary.startBrowser();
					System.out.println("Executing start browser");
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("openApplication"))
				{
					FunctionLibrary.openApplication(driver);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("waitForElement"))
				{
					FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("typeAction"))
				{
					FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("clickAction"))
				{
					FunctionLibrary.clickAction(driver, Locator_Type, Locator_Value);
					test.log(LogStatus.INFO, Description);
				}
				else if(Function_Name.equalsIgnoreCase("closeBrowser"))
				{
					FunctionLibrary.closeBrowser(driver);
					test.log(LogStatus.INFO, Description);
				}
				if(Function_Name.equalsIgnoreCase("captureData"))
				{
					FunctionLibrary.captureData(driver, Locator_Type, Locator_Value);
					test.log(LogStatus.INFO,Description);
				}
				else if(Function_Name.equalsIgnoreCase("tableValidation"))
				{
					FunctionLibrary.tableValidation(driver, Test_Data);
					test.log(LogStatus.INFO,Description);
				}
				//write as pass into status column
				xl.setCellData(TCModule, j, 5, "PASS");
				test.log(LogStatus.PASS, Description);
				xl.setCellData("MasterTestCases", i, 3, "PASS");
			}catch(Exception e)
			{
				System.out.println("exception handled");
				xl.setCellData(TCModule, j, 5, "Fail");
				test.log(LogStatus.FAIL, Description);
				xl.setCellData("MasterTestCases", i, 3, "Fail");
//take screen shot and store
File screen=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
FileUtils.copyFile(screen, new File("./Screens//"+TCModule+FunctionLibrary.generateDate()+"MYScreen.png"));
break;
				
			}
			report.endTest(test);
			report.flush();
		}
	}
	else
	{
		//write as note executed into status column
		xl.setCellData("MasterTestCases", i, 3, "Not Executed");
	}
	}
}

}
