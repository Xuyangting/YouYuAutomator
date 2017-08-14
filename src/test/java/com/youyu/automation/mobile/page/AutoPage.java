package com.youyu.automation.mobile.page;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.youyu.automation.mobile.util.AppiumUtil;
import com.youyu.automation.mobile.util.MySqlUtil;

import io.appium.java_client.AppiumDriver;

public class AutoPage extends BasePage{
	// 提取所有的元素控件  
	private JSONArray elements = new JSONArray();
	private JSONArray accounts = new JSONArray();
	
	public AutoPage(@SuppressWarnings("rawtypes") AppiumDriver driver) throws ClassNotFoundException, SQLException, JSONException {
		super(driver);
		elements = MySqlUtil.getData("select * from app_elements");
		accounts = MySqlUtil.getData("select * from app_testaccount");
	}
	
	/**
	 * 通过英文名称查找存储在MySQL中的元素控件信息
	 * @param EnglishName
	 * @return
	 * @throws JSONException 
	 */
	public String getAddressByEnglishName(String EnglishName) throws JSONException {
		String result = null;
		int len = elements.length();
		for (int i = 0; i < len; i++) {
			if (((JSONObject)elements.get(i)).get("english").toString().equals(EnglishName)) {
				if (AppiumUtil.getDevice().equals("android")) {
					result = ((JSONObject)elements.get(i)).get("android_address").toString();
					break;
				}
				if (AppiumUtil.getDevice().equals("ios")) {
					result = ((JSONObject)elements.get(i)).get("ios_address").toString();
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 通过英文名称查找存储在MySQL中的元素控件信息
	 * @param EnglishName
	 * @return
	 * @throws JSONException 
	 */
	public String getNameByEnglishName(String EnglishName) throws JSONException {
		String result = null;
		int len = elements.length();
		for (int i = 0; i < len;  i++) {
			if (((JSONObject)elements.get(i)).get("english").toString().equals(EnglishName)) {
				result = ((JSONObject)elements.get(i)).get("name").toString();
				break;
			}
		}
		return result;
	}
	
	/**
	 * 通过数据库id查找储在MySQL中的登陆账号密码
	 * @param EnglishName
	 * @return
	 * @throws JSONException 
	 */
	public List<String> getAccountPwd(String id) throws JSONException{
		List<String> result = new ArrayList<>();
		int len = accounts.length();
		for (int i = 0; i < len; i++) {
			JSONObject el = (JSONObject) accounts.get(i);
			if (el.get("id").toString().equals(id)) {
				switch (AppiumUtil.getEnv().toLowerCase()) {
				case "qa":
					result.add((String) el.get("qa_account"));
					result.add((String) el.get("qa_password"));
					break;
				case "stage":
					result.add((String) el.get("stage_account"));
					result.add((String) el.get("stage_password"));
					break;
				case "live":
					result.add((String) el.get("live_account"));
					result.add((String) el.get("live_password"));
					break;
				default:
					break;
				}
				break;
			}
		}
		return result;
	}
}
