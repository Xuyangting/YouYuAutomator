package com.youyu.automation.mobile.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.openqa.selenium.WebElement;

import com.youyu.automation.mobile.page.AutoPage;
import com.youyu.automation.mobile.util.AppiumUtil;
import com.youyu.automation.mobile.util.Util;

import io.appium.java_client.AppiumDriver;

public class AutoService {
	private AutoPage autoPage;
	
	public AutoService(@SuppressWarnings("rawtypes") AppiumDriver driver) throws ClassNotFoundException, SQLException, JSONException{
		autoPage = new AutoPage(driver);
	}
	
	/**
	 * 点击 事件
	 * @param info
	 * @throws JSONException 
	 */
	public void ClickElement(String name) throws JSONException {
		Util.recodeMessage("点击 -> " + autoPage.getNameByEnglishName(name));
		WebElement element = autoPage.FindElement(autoPage.getAddressByEnglishName(name));
		if (element == null) {
			Util.recodeMessage("告警 -> 尚未找到该控件 -> " + name + " -> " + autoPage.getNameByEnglishName(name));
			assert false;
		}else {
			element.click();
		}
	}
	
	/**
	 * 输入 文本 事件
	 * @param info
	 * @param name
	 * @param value
	 * @throws JSONException
	 */
	public void SetValue(String name, String value) throws JSONException {
		Util.recodeMessage("元素 -> " + autoPage.getNameByEnglishName(name) + " -> 输入 -> " + value);
		WebElement element = autoPage.FindElement(autoPage.getAddressByEnglishName(name));
		if (element == null) {
			Util.recodeMessage("告警 -> 尚未找到该控件 -> " + name + " -> " + autoPage.getNameByEnglishName(name));
			assert false;
		}else {
			element.clear();
			element.sendKeys(value);
		}
	}
	
	/**
	 * 获取 元素 文本
	 * @param name
	 * @return
	 * @throws JSONException
	 */
	public String getValue(String name) throws JSONException {
		Util.recodeMessage("获取 -> " + autoPage.getNameByEnglishName(name));
		WebElement element = autoPage.FindElement(autoPage.getAddressByEnglishName(name));
		if (element == null) {
			Util.recodeMessage("告警 -> 尚未找到该控件 -> " + name + " -> " + autoPage.getNameByEnglishName(name));
			return null;
		}else {
			return element.getText().toString();
		}
	}
	
	/**
	 * 对比，预期提示元素提示数据、文字与实际进行对比
	 * @param expect
	 * @param actual
	 */
	public void checkValue(String expect, String actual) {
		Util.recodeMessage("检测 -> 预期结果：" + expect + " -> 实际结果：" + actual);
		assert expect.equals(actual);
	}
	
		
	/**
	 * 对比，预期提示元素提示数据、文字与实际进行对比
	 * @param expect
	 * @param actual
	 * @throws JSONException 
	 */
	public void checkValueNotNull(String name) throws JSONException {
		String result = getValue(name);
		Util.recodeMessage("检测 -> 控件显示数据不为空 -> " + name + " -> " + result);
		if (result.length() == 0 ) {
			assert result.equals("");
		}else {
			Util.recodeMessage("检测通过");
		}
	}
	
	/**
	 * 对比，预期结果不为null
	 * @param expect
	 * @param length
	 * @throws JSONException 
	 */
	public void checkListValueNotNull(String expect, String length) throws JSONException {
		String android_name = autoPage.getNameByEnglishName(expect);
		String android_address = autoPage.getAddressByEnglishName(expect);
		Util.recodeMessage("获取 -> " + android_name);
		List<WebElement> elementsById = autoPage.getAllElementsById(AppiumUtil.getPackageName() + android_address);
		List<WebElement> Elements = (elementsById.size() == 0)? autoPage.getAllElementsByClass(android_address):elementsById;
		if(Elements.size() == 0){
			Util.recodeMessage("告警 -> 找不到该控件 -> " + expect + " -> " + android_name);
			assert false;
		}
		if (Integer.parseInt(length) <= Elements.size()) {
			for (int i = 0; i < Integer.parseInt(length); i++) {
				WebElement element = Elements.get(i);
				if (element == null) {
					Util.recodeMessage("告警 -> 尚未找到该控件 -> " + expect + " -> " + android_name);
					assert false;
				}else {
					Util.recodeMessage("元素显示 -> " + element.getText().toString());
				}
			}
		}else {
			for (int i = 0; i < Elements.size(); i++) {
				WebElement element = Elements.get(i);
				if (element == null) {
					Util.recodeMessage("告警 -> 尚未找到该控件 -> " + expect + " -> " + android_name);
					assert false;
				}else {
					Util.recodeMessage("元素显示 -> " + element.getText().toString());
				}
			}
		}
	}
	
	/**
	 * 对比，预期结果是否排序正确
	 * @param expect
	 * @param actual
	 * @throws JSONException 
	 */
	public void checkListValueOrder(String expect, String length) throws JSONException {
		String android_name = autoPage.getNameByEnglishName(expect);
		String android_address = autoPage.getAddressByEnglishName(expect);
		ArrayList<String> result = new ArrayList<>();
		List<WebElement> Elements = autoPage.getAllElementsById(AppiumUtil.getPackageName() + android_address);
		if (Integer.parseInt(length.split("@")[1]) <= Elements.size()) {
			for (int i = Integer.parseInt(length.split("@")[0]); i < Integer.parseInt(length.split("@")[1]); i++) {
				WebElement element = Elements.get(i);
				if (element == null) {
					Util.recodeMessage("告警 -> 尚未找到该控件 -> " + expect + " -> " + android_name);
					assert false;
				}else {
					String temp = element.getText().toString();
					result.add(temp.substring(0, temp.length()-1));
				}
			}
		}else {
			for (int i = Integer.parseInt(length.split("@")[0]); i < Elements.size(); i++) {
				WebElement element = Elements.get(i);
				if (element == null) {
					Util.recodeMessage("告警 -> 尚未找到该控件 -> " + expect + " -> " + android_name);
					assert false;
				}else {
					String temp = element.getText().toString();
					result.add(temp.substring(0, temp.length()-1));
				}
			}
		}
		
		String orderResult = "0";
		// 检测升序
		if (length.split("@")[2].equals("asc")) {
			Util.recodeMessage("开始检测升序排序：" + result);
			for (int i = 1; i < result.size(); i++) {
				if (Float.parseFloat(result.get(i)) < Float.parseFloat(result.get(i-1))) {
					orderResult = "1";
				}
			}
		}
		// 检测降序
		if (length.split("@")[2].equals("desc")) {
			Util.recodeMessage("开始检测降序排序：" + result);
			for (int i = 1; i < result.size(); i++) {
				if (Float.parseFloat(result.get(i)) > Float.parseFloat(result.get(i-1))) {
					orderResult = "1";
				}
			}
		}
		if (orderResult.equals("0")) {
			Util.recodeMessage("排序正确");
		}else {
			Util.recodeMessage("排序错误，请检测");
			assert orderResult.equals("0");
		}
	}
	
	/**
	 * 对比，预期结果不相等
	 * @param expect
	 * @param actual
	 */
	public void checkValueNotEqual(String expect, String actual) {
		Util.recodeMessage("检测不相等 -> 预期结果：" + expect + " -> 实际结果：" + actual);
		assert !expect.equals(actual);
	}
	
	public void checkAllValuesNotEqual(String englishName, String expect) throws JSONException {
		String elementAddress = autoPage.getAddressByEnglishName(englishName);
		String name = autoPage.getNameByEnglishName(englishName);
		List<WebElement> autual = autoPage.FindElements(elementAddress);
		if(autual.size() == 0){
			Util.recodeMessage("告警 -> 找不到该控件 ->" + name + " : " + elementAddress);
			assert false;
		}
		for(int i=0, len=autual.size(); i < len; i++){
			Util.recodeMessage("检测所有 " + name + " 都不等于" + expect + ". 实际结果:" + autual.get(i).getText().toString() + " -> 预期不为：" + expect);
			assert !autual.get(i).equals(expect);
		}
	}
	
	public void checkElementIfExist(String englishName, String trueOrFlase) throws JSONException{
		String elementAddress = autoPage.getAddressByEnglishName(englishName);
		String name = autoPage.getNameByEnglishName(englishName);
		boolean elementExist = autoPage.isElementExist(elementAddress);
		Util.recodeMessage("判断元素是否存在: '" + name + "' -> 预期结果：" + trueOrFlase + " -> 实际结果：" + String.valueOf(elementExist).toLowerCase());
		assert String.valueOf(elementExist).toLowerCase().equals(trueOrFlase.toLowerCase());
	}
	
	public void contain(String text, String subText){
		boolean result = text.contains(subText);
		Util.recodeMessage("检测 -> " + text + " 包含：" + subText + " -> 结果为： " + String.valueOf(result));
		assert result;
	}
	
	public void checkElementEnable(String englishName, String trueOrFalse) throws JSONException{
		String elementAddress = autoPage.getAddressByEnglishName(englishName);
		String name = autoPage.getNameByEnglishName(englishName);
		String enabled = autoPage.getAttribute(elementAddress, "enabled");
		boolean result = enabled.toLowerCase().equals(trueOrFalse);
		Util.recodeMessage("检测 -> '" + name + "' 是否激活的。预期结果 -> " + trueOrFalse 
				+ "，实际结果->： " + enabled);
		assert result;
	}
	
	public void compareLength(String englishName, String maxOrMin_length) throws JSONException{
		String elementAddress = autoPage.getAddressByEnglishName(englishName);
		String name = autoPage.getNameByEnglishName(englishName);
		List<WebElement> elements = autoPage.FindElements(elementAddress);
		if(elements.size() == 0){
			Util.recodeMessage("告警 -> 找不到该控件 ->" + name + " : " + elementAddress);
			assert false;
		}
		if(!maxOrMin_length.contains("_")){
			Util.recodeMessage("告警 -> 第二个参数输入有误 ->" + maxOrMin_length + ": 必需包含 '_' 分隔符 " );
			assert false;
		}else{
			String maxOrMin = maxOrMin_length.split("_")[0];
			try {
				int len = Integer.parseInt(maxOrMin_length.split("_")[1]);
				if(maxOrMin.toLowerCase().trim().equals("max") || maxOrMin.trim().equals(">")){
					boolean flg = elements.size() > len;
					Util.recodeMessage("检测 -> 控件 " + name + " 的数量 大于 " + len + "。实际结果-> " + String.valueOf(flg));
					assert flg;
				}
				else if(maxOrMin.toLowerCase().trim().equals("min") || maxOrMin.trim().equals("<")){
					boolean flg = elements.size() < len;
					Util.recodeMessage("检测 -> 控件 " + name + " 的数量 小于 " + len + "。实际结果-> " + String.valueOf(flg));
					assert flg;
				}
				else if(maxOrMin.toLowerCase().trim().contains("eq") || maxOrMin.trim().equals("=")){
					boolean flg = elements.size() == len;
					Util.recodeMessage("检测 -> 控件 " + name + " 的数量 等于 " + len + "。实际结果-> " + String.valueOf(flg));
					assert flg;
				}
				else if(maxOrMin.toLowerCase().trim().contains("maxeq") || maxOrMin.trim().equals(">=")){
					boolean flg =  elements.size() >= len;
					Util.recodeMessage("检测 -> 控件 " + name + " 的数量大于 等于 " + len + "。实际结果-> " + String.valueOf(flg));
					assert flg;
				}
				else if(maxOrMin.toLowerCase().trim().contains("mineq") || maxOrMin.trim().equals("<=")){
					boolean flg =  elements.size() <= len;
					Util.recodeMessage("检测 -> 控件 " + name + " 的数量小于 等于 " + len + "。实际结果-> " + String.valueOf(flg));
					assert flg;
				}
				else{
					Util.recodeMessage("告警 -> 第二个参数输入有误 ->" + maxOrMin_length + ": 分隔符 _ 前必须是对比符(>,<,=,>=,<=)或者(max,min,equals,maxEq,minEq)中的一个" );
					assert false;
				}
			} catch (Exception e) {
				Util.recodeMessage("告警 -> 第二个参数输入有误 ->" + maxOrMin_length + ": 分隔符 _ 后必须是数字 " );
				assert false;
			}
		}
	}
}
