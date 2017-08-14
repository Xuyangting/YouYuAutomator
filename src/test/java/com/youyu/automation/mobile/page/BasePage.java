package com.youyu.automation.mobile.page;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.youyu.automation.mobile.util.AppiumUtil;

import io.appium.java_client.AppiumDriver;

public class BasePage {
	@SuppressWarnings("rawtypes")
	protected AppiumDriver driver;
	protected final int WAITFORSECONDS = 30;
	
	@SuppressWarnings("rawtypes")
	public BasePage(AppiumDriver driver) {
		this.driver = driver;
	}
	
	/**
	 * IOS：请求等待超时处理
	 * @param xpathString
	 * @return
	 */
	public WebElement getElementByXPath(String xpathString) {	
		return driver.findElement(By.xpath(xpathString));				
	}
	
	/**
	 * Android：根据id获取相关元素
	 * @param idString
	 * @return
	 */
	public WebElement getElementById(final String idString) {
		return (new WebDriverWait(driver, WAITFORSECONDS)).until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver d) {
				return d.findElement(By.id(idString));
			}
		});
	}
	
	/**
	 * Android：通过class和index进行定位
	 * @param className
	 * @param index
	 * @return
	 */
	public WebElement getElementByClassAndIndex(String className, String index) {
		return driver.findElement(By.xpath("//*[@class='" + className + "' and @index='" + index + "']"));
	}
	
	/**
	 * 根据id获取相关元素集合，默认最长等待时长为30秒
	 * @param idString
	 * @return 返回相关元素的集合
	 */
	public List<WebElement> getAllElementsById(String idString){
		return (new WebDriverWait(driver, WAITFORSECONDS)).until(new ExpectedCondition<List<WebElement>>() {
			@Override
			public List<WebElement> apply(WebDriver d) {
				return d.findElements(By.id(idString));
			}
		});
	}
	
	/**
	 * Android：通过class进行定位
	 * @param className
	 * @return
	 */
	
	public List<WebElement> getAllElementsByClass(String className){
		return (new WebDriverWait(driver, WAITFORSECONDS)).until(new ExpectedCondition<List<WebElement>>() {
			@Override
			public List<WebElement> apply(WebDriver d) {
				return d.findElements(By.className(className));
			}
		});
	}
	
	
	public List<WebElement> getAllElementsByText(String text){
		return (new WebDriverWait(driver, WAITFORSECONDS)).until(new ExpectedCondition<List<WebElement>>() {
			@Override
			public List<WebElement> apply(WebDriver d) {
				return d.findElements(By.xpath("//*[@text='" + text + "']"));
			}
		});
	}
	
	
	
	/**
	 * 选择iOS进行元素查找还是Android进行查找元素
	 */
	public WebElement FindElement(String element) {
		try {
			if (AppiumUtil.getDevice().equals("ios")) {
				// ios 查找 控件
				return getElementByXPath(element);
			}else {
				// android 查找 控件
				if (element.substring(0, 1).equals(":") || element.substring(0, 1).equals("/")) {
					if (element.contains("@")) {
						// 查找控件list中的其中一个
						List<WebElement> Elements = getAllElementsById(AppiumUtil.getPackageName() + element.split("@")[0]);
						return Elements.get(Integer.parseInt(element.split("@")[1]));
					}else {
						// 通过唯一的resourceId寻找控件
						List<WebElement> Elements = getAllElementsById(AppiumUtil.getPackageName() + element);
						return Elements.get(0);
					}
				}else if(element.contains("$")){
					// 通过class name 寻找控件
					String className = element.split("[$]")[0];
					int index = Integer.parseInt(element.split("[$]")[1]);
					List<WebElement> Elements = getAllElementsByClass(className);
					return Elements.get(index);
				}else if(element.contains("-")){
					//通过文本寻找控件
					String text = element.split("-")[0];
					int index = Integer.parseInt(element.split("-")[1]);
					List<WebElement> Elements = getAllElementsByText(text);
					return Elements.get(index);
				}
				else {
					// 通过class和index寻找控件
					String className = element.split("@")[0];
					String index = element.split("@")[1];
					return getElementByClassAndIndex(className, index);
				}
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<WebElement> FindElements(String element){
		List<WebElement> idElements = getAllElementsById(AppiumUtil.getPackageName() + element);
		return  (idElements.size() == 0)? getAllElementsByClass(element): idElements;
		
  	}
	
	
	public boolean isElementExist(String element){
		WebElement el = FindElement(element);
		if(el == null){
			return false;
		}else{
			return true;
		}
	}
	
	public String getAttribute(String element, String attributeName){
		WebElement el = FindElement(element);	
		String attr;
		try {
			attr = el.getAttribute(attributeName);
		} catch (Exception e) {
			attr = null;
		}
		return attr;
	}
}
