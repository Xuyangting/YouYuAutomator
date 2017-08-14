package com.youyu.automation.mobile.flow;

import java.sql.SQLException;

import org.json.JSONException;

import com.youyu.automation.mobile.page.AutoPage;
import com.youyu.automation.mobile.service.AutoService;
import com.youyu.automation.mobile.util.SwipeUtil;
import com.youyu.automation.mobile.util.Util;

import io.appium.java_client.AppiumDriver;


public class AutoFlow {
	private AutoPage autoPage;
	private AutoService autoService;
	@SuppressWarnings("rawtypes")
	private AppiumDriver myDriver;
	
	public AutoFlow(@SuppressWarnings("rawtypes") AppiumDriver driver) throws ClassNotFoundException, SQLException, JSONException{
		autoPage = new AutoPage(driver);
		autoService = new AutoService(driver);
		myDriver = driver;
	}
	
	/**
	 * 进入APP，不进行登录操作
	 * @throws JSONException
	 */
	public void gotoAppWithNoLogin(String app) throws JSONException {
		if (app==null ||app.equals("stock") || app.equals("")){
			Util.sleep(5000);
			SwipeUtil.swipeToLeft(myDriver, 200);
			Util.sleep(2000);
			SwipeUtil.swipeToLeft(myDriver, 200);
			Util.sleep(2000);
			autoService.ClickElement("stock_regLogin_guide_experience");
			Util.sleep(2000);
		}else if(app.equals("wealth")){
			Util.sleep(5000);
			autoService.ClickElement("wealth_splash_start_overseas_invest");
			Util.sleep(2000);
			autoService.ClickElement("wealth_risk_evaluation_exit");
			Util.sleep(2000);
		}
	}
	
	/**
	 * 进入APP，进行登录操作
	 * 备注： 你进行reset app操作，减少app运行时间上的开支
	 * @throws JSONException
	 */
	public void gotoAppWithLogin(String account,String password,String app) throws JSONException {
		if (app==null ||app.equals("stock") || app.equals("")){
			// 判断是否已经处于APP登录状态，如果已经登录，并且登录账号是需要登录的账号，则pass，如果不是，则进行登录操作，或重新登录需要登录的账号登录
			if (autoPage.isElementExist(autoPage.getAddressByEnglishName("stock_person"))) {
				Util.sleep(2000);
				autoService.ClickElement("stock_person");
				Util.sleep(2000);
				// 在APP中，但是未登录，进行登录操作
				if (autoPage.isElementExist(autoPage.getAddressByEnglishName("stock_personal_unlogin_loginRegBtn"))) {
					autoService.ClickElement("stock_personal_unlogin_loginRegBtn");
					Util.sleep(2000);
					autoService.SetValue("stock_regLogin_login_inputAccount", account);
					Util.sleep(2000);
					autoService.SetValue("stock_regLogin_login_sendPasswd", password);
					Util.sleep(2000);
					autoService.ClickElement("stock_regLogin_login_loginButton");
					Util.sleep(3000);
				}
				// 在APP中，已登录，但是登录的账号不是需要登录的账号，进行账号切换
				else if (autoPage.isElementExist(autoPage.getAddressByEnglishName("stock_personal_login_userName"))) {
					if (!autoService.getValue("stock_personal_login_userName").equals(Util.changeAccountNickname(account))) {
						autoService.ClickElement("stock_person_nologin_set");
						Util.sleep(2000);
						autoService.ClickElement("stock_personal_setting_existBtn");
						Util.sleep(2000);
						autoService.ClickElement("stock_personal_setting_exist");
						Util.sleep(2000);
						autoService.ClickElement("stock_personal_unlogin_loginRegBtn");
						Util.sleep(2000);
						autoService.SetValue("stock_regLogin_login_inputAccount", account);
						Util.sleep(2000);
						autoService.SetValue("stock_regLogin_login_sendPasswd", password);
						Util.sleep(2000);
						autoService.ClickElement("stock_regLogin_login_loginButton");
						Util.sleep(3000);
					}else {
						Util.sleep(3000);
					}
				} 
				// 在APP中，已登录，而且登录的账号是需要登录的账号，则不进行操作，等待3秒作为缓冲
				else {
					Util.sleep(3000);
				}
			}else {
				Util.sleep(5000);
				SwipeUtil.swipeToLeft(myDriver, 200);
				Util.sleep(2000);
				SwipeUtil.swipeToLeft(myDriver, 200);
				Util.sleep(2000);
				autoService.ClickElement("stock_regLogin_guide_loginOrReg");
				Util.sleep(2000);
				autoService.SetValue("stock_regLogin_login_inputAccount", account);
				Util.sleep(2000);
				autoService.SetValue("stock_regLogin_login_sendPasswd", password);
				Util.sleep(2000);
				autoService.ClickElement("stock_regLogin_login_loginButton");
				Util.sleep(3000);
			}
		}else if(app.equals("wealth")){
			Util.sleep(5000);
			autoService.ClickElement("wealth_splash_loginRegBtn");
			Util.sleep(2000);
			autoService.SetValue("wealth_login_account", account);
			Util.sleep(1000);
			autoService.SetValue("wealth_login_password", password);
			Util.sleep(2000);
			autoService.ClickElement("wealth_login_btn");
			Util.sleep(2000);
		}
	}
	
	/**
	 * 返回上一级
	 */
	public void backMenu(){
		 myDriver.navigate().back(); 
	}
}
