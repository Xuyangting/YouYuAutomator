package com.youyu.automation.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class AppiumUtil {
	private static String PACKAGE;
	private static String ACTIVITY;
	private static String Device;
	private static String Env;
	
	
	/**
	 * 设备和appium进行连接，并获取设备的driver
	 * @param url
	 * @param port
	 * @param device
	 * @param udid
	 * @param env
	 * @return
	 * @throws MalformedURLException
	 */
	@SuppressWarnings("rawtypes")
	public static AppiumDriver getDriver(String url, String port, String app, String device, String udid, String env, String app_address) throws MalformedURLException{
		Device = device;
		Env = env;
		File classpathRoot = new File(System.getProperty("user.dir"));
		DesiredCapabilities capabilities = null;	
		// 第一层过滤，判断是Android还是iOS
		if (device.equals("ios")) {
			// 第二层过来，判断是有鱼股票还是有鱼智投
			File propertyFile = new File(classpathRoot.getAbsolutePath() + "//resources/Base_IOS.properties");
			capabilities = getBasedCapabilities(propertyFile);
			capabilities.setCapability("app", app_address);
//			if (app.equals("stock")) {
//				// iOS 有鱼股票 
//				// 第三层过滤，判断是qa还是live
//				if(env.equalsIgnoreCase("qa")) {
//					capabilities.setCapability("app", app_address);
//				}
//				if(env.equalsIgnoreCase("live")) {
//					capabilities.setCapability("app", app_address);
//				}
//			}else {
//				// iOS 有鱼智投
//				// 第三层过滤，判断是qa还是live
//				if(env.equalsIgnoreCase("qa")) {
//					capabilities.setCapability("app", app_address);
//				}
//				if(env.equalsIgnoreCase("live")) {
//					capabilities.setCapability("app", app_address);
//				}
//			}
			return new IOSDriver(new URL("http://" + url + ":" + port + "/wd/hub"), capabilities);
		}else if (device.equals("android")) {
			capabilities = getBasedCapabilities(new File(classpathRoot, "//resources//Base_Android.properties"));
			if(app.equals("stock")){
				// Android 有鱼股票
				// 第三层过滤，判断是qa还是live
				if(env.equalsIgnoreCase("qa")) {
					PACKAGE = "com.ruifusoft.finance.app.debug";
					ACTIVITY = "com.ruifusoft.ui.SplashActivity";
					capabilities.setCapability("appPackage", PACKAGE);
					capabilities.setCapability("appActivity", ACTIVITY);
					capabilities.setCapability("app", app_address);
				}
				if(env.equalsIgnoreCase("live")) {
					PACKAGE = "com.ruifusoft.finance.app";
					ACTIVITY = "com.ruifusoft.ui.SplashActivity";
					capabilities.setCapability("appPackage", PACKAGE);
					capabilities.setCapability("appActivity", ACTIVITY);
					capabilities.setCapability("app", app_address);
				}
			}else {
				// Android 有鱼智投
				// 第三层过滤，判断是qa还是live
				if(env.equalsIgnoreCase("qa")) {
					PACKAGE = "com.ruifusoft.wm.dev";
					ACTIVITY = "com.ruifusoft.wm.controller.launch.SplashActivity";
					capabilities.setCapability("appPackage", PACKAGE);
					capabilities.setCapability("appActivity", ACTIVITY);
					capabilities.setCapability("app", app_address);
				}
				if(env.equalsIgnoreCase("live")) {
					PACKAGE = "com.ruifusoft.wm";
					ACTIVITY = "com.ruifusoft.wm.controller.launch.SplashActivity";
					capabilities.setCapability("appPackage", PACKAGE);
					capabilities.setCapability("appActivity", ACTIVITY);
					capabilities.setCapability("app", app_address);
				}
			}
			capabilities.setCapability("udid", udid);	
			return new AndroidDriver(new URL("http://" + url + ":" + port + "/wd/hub"), capabilities);
		}else {
			return null;
		}	
	}
	
	/**
	 * properties参数进行整理
	 * 
	 * @param property
	 * @return
	 */
	private static DesiredCapabilities getBasedCapabilities(File property) {
		DesiredCapabilities capabilities = new DesiredCapabilities();		
		try {
			InputStream is = new FileInputStream(property);
			Properties props = new Properties();
			props.load(is);
			for(Object key : props.keySet()) {
				capabilities.setCapability(key.toString(), props.getProperty(key.toString()));
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return capabilities;
	}
	
	/**
	 * 获取Android APK package name eg:com.ruifusoft.finance.app
	 * @return
	 */
	public static String getPackageName() {
		return PACKAGE;
	}
	
	/**
	 * 获取执行环境是Android还是IOS
	 */
	public static String getDevice() {
		return Device;
	}
	
	/**
	 * 获取执行环境是qa还是Live
	 */
	public static String getEnv() {
		return Env;
	}
}
