package com.youyu.automation.mobile.listener;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.youyu.automation.mobile.testcase.AutoTestCase;

import io.appium.java_client.AppiumDriver;

public class FailureScreenshotsListener extends TestListenerAdapter{
	@Override
    public void onTestFailure(ITestResult tr){
        @SuppressWarnings("rawtypes")
		AppiumDriver driver = AutoTestCase.getDriver();
        File location = new File("screenshots");
        String screenShotName = location.getAbsolutePath()+File.separator+AutoTestCase.getTestCaseEnglishName() +".png";
        File screenShot = driver.getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(screenShot, new File(screenShotName));    
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
