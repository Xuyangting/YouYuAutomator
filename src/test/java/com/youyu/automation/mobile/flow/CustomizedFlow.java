package com.youyu.automation.mobile.flow;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;

import com.youyu.automation.mobile.page.AutoPage;
import com.youyu.automation.mobile.service.AutoService;
import com.youyu.automation.mobile.util.SwipeUtil;
import com.youyu.automation.mobile.util.Util;

import io.appium.java_client.AppiumDriver;

public class CustomizedFlow {
	private AutoService autoService;
	private AutoPage autoPage;
	@SuppressWarnings("rawtypes")
	private AppiumDriver driver;
	
	public CustomizedFlow( @SuppressWarnings("rawtypes") AppiumDriver driver) throws ClassNotFoundException, SQLException, JSONException{
		this.driver  = driver;
		autoService = new AutoService(driver);
		autoPage = new AutoPage(driver);
	}
	
	
	public void addFavorStocks(String stocks) throws JSONException{
		String[] stockList = null;
		autoService.ClickElement("stock_stock_search");
		if(stocks.contains(",")){
			stockList = stocks.split(",");
			for(String stock: stockList){
				autoService.SetValue("stock_stock_search_input", stock.trim());
				Util.sleep(1000);
				autoService.ClickElement("stock_customized_search_toggleBtn");
				Util.sleep(1000);
			}
		}else{
			autoService.SetValue("stock_stock_search_input", stocks.trim());
			Util.sleep(1000);
			autoService.ClickElement("stock_customized_search_toggleBtn");
			Util.sleep(1000);
		}		
		driver.navigate().back(); 
	}
	
	public void keyboardInput(String chars) throws JSONException{
		for(int i = 0, len = chars.length(); i < len; i++){
			String singleChar = chars.substring(i,i+1);
			ArrayList<Integer> clickElement = new ArrayList<>();
			switch (singleChar) {
			case "1":
				Util.recodeMessage("要点击的数字是：1");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.16&left-0.37");
				break;
			case "2":
				Util.recodeMessage("要点击的数字是：2");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.16");
				break;
			case "3":
				Util.recodeMessage("要点击的数字是：3");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.16&right-0.37");
				break;
			case "4":
				Util.recodeMessage("要点击的数字是：4");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.24&left-0.37");
				break;
			case "5":
				Util.recodeMessage("要点击的数字是：5");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.24");
				break;
			case "6":
				Util.recodeMessage("要点击的数字是：6");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.24&right-0.37");
				break;
			case "7":
				Util.recodeMessage("要点击的数字是：7");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.32&left-0.37");
				break;
			case "8":
				Util.recodeMessage("要点击的数字是：8");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.32");
				break;
			case "9":
				Util.recodeMessage("要点击的数字是：9");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.32&right-0.37");
				break;
			case "0":
				Util.recodeMessage("要点击的数字是：0");
				clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_customized_frame")), "down", "0.40");
				break;
			}
			Util.recodeMessage("点击点坐标：x=" + String.valueOf(clickElement.get(0)) + " y=" + String.valueOf(clickElement.get(1)));
			driver.tap(1, clickElement.get(0), clickElement.get(1), 0);
			Util.sleep(1000);
		}
		
	}
	
}
