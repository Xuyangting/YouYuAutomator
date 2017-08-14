package com.youyu.automation.mobile.util;

import java.util.ArrayList;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import io.appium.java_client.AppiumDriver;

public class SwipeUtil {
	private final static int speed = 300;
	/**
	 * 获取手机屏幕的宽度
	 * @param driver
	 * @return
	 */
	public static int getWindowWidth(@SuppressWarnings("rawtypes") AppiumDriver driver) {
		return driver.manage().window().getSize().getWidth();
	}
	
	/**
	 * 获取手机屏幕的高度
	 * @param driver
	 * @return
	 */
	public static int getWindowHeight(@SuppressWarnings("rawtypes") AppiumDriver driver) {
		return driver.manage().window().getSize().getHeight();
	}
	
	// 从元素的右边向左滑动
	public static void swipeToLeft(@SuppressWarnings("rawtypes") AppiumDriver driver, WebElement element) {
		Util.recodeMessage("向左滑动元素的宽度");
		Point point = element.getLocation();
		Dimension dim = element.getSize();
		int startX = point.getX() + dim.getWidth() -50;
		int startY = point.getY() + dim.getHeight() / 2;
		int endX = point.getX() + 50;
		int endY = startY;
		driver.swipe(startX, startY, endX, endY, 1000);
	}

	// 从元素的左边向右滑动
	public static void swipeToRight(@SuppressWarnings("rawtypes") AppiumDriver driver, WebElement element) {
		Util.recodeMessage("向右滑动元素的宽度");
		Point point = element.getLocation();
		Dimension dim = element.getSize();
		int startX = point.getX() + 50;
		int startY = point.getY() + dim.getHeight() / 2;
		int endX = point.getX() + dim.getWidth() - 50;
		int endY = startY;
		driver.swipe(startX, startY, endX, endY, 1000);
	}

	// 从元素的下边向上滑动
	public static void swipeToUp(@SuppressWarnings("rawtypes") AppiumDriver driver, WebElement element) {
		Util.recodeMessage("向上滑动元素的高度");
		Point point = element.getLocation();
		Dimension dim = element.getSize();
		int startX = dim.getWidth() / 2;
		int startY = dim.getHeight() + point.getY();
		int endX = startX;
		int endY = point.getY();
		driver.swipe(startX, startY, endX, endY, 2000);
	}

	// 从元素的上边向下滑动
	public static void swipeToDown(@SuppressWarnings("rawtypes") AppiumDriver driver, WebElement element) {
		Util.recodeMessage("向下滑动元素的高度");
		Point point = element.getLocation();
		Dimension dim = element.getSize();
		int startX = dim.getWidth() / 2;
		int startY = point.getY();
		int endX = startX;
		int endY = dim.getHeight() + point.getY();
		driver.swipe(startX, startY, endX, endY, 2000);
	}

	// 屏幕向上滑动指定间距
	public static void swipeToUp(@SuppressWarnings("rawtypes") AppiumDriver driver, int distance) {
		Util.recodeMessage("屏幕向上滑动指定间距 -> " + String.valueOf(distance));
		Dimension dim = driver.manage().window().getSize();
		int startX = dim.getWidth() / 2;
		int startY = dim.getHeight() * 9 / 10;
		int endX = startX;
		int endY = (startY - distance) <= 10 ? 10 : (startY - distance);
//		System.out.println(startX + "--->" + startY + "--->" + endX + "--->" + endY);
		int duration = (startY - endY)/speed * 1000;
		driver.swipe(startX, startY, endX, endY, duration);
	}

	// 屏幕向下滑动指定间距
	public static void swipeToDown(@SuppressWarnings("rawtypes") AppiumDriver driver, int distance) {
		Util.recodeMessage("屏幕向下滑动指定间距 -> " + String.valueOf(distance));
		Dimension dim = driver.manage().window().getSize();
		int startX = dim.getWidth() / 2;
		int startY = dim.getHeight() / 2;
		int endX = startX;
		int endY = (startY + distance) >= dim.getHeight() ? (dim.getHeight() - 10) : (startY + distance);
//		System.out.println(startX + "--->" + startY + "--->" + endX + "--->" + endY);
		int duration = (endY - startY)/speed * 1000;
		driver.swipe(startX, startY, endX, endY, duration);
	}

	// 屏幕向左滑动指定间距
	public static void swipeToLeft(@SuppressWarnings("rawtypes") AppiumDriver driver, int distance) {
		Util.recodeMessage("屏幕向左滑动指定间距 -> " + String.valueOf(distance));
		Dimension dim = driver.manage().window().getSize();
		int startX = dim.getWidth() * 9 / 10;
		int startY = dim.getHeight() / 2;
		int endX = (startX - distance) <= 0 ? 10 : (dim.getWidth() / 2 - distance);
		int endY = startY;
//		System.out.println(startX + "--->" + startY + "--->" + endX + "--->" + endY);
		driver.swipe(startX, startY, endX, endY, 1000);
	}

	// 屏幕向右滑动指定间距
	public static void swipeToRight(@SuppressWarnings("rawtypes") AppiumDriver driver, int distance) {
		Util.recodeMessage("屏幕向右滑动指定间距 -> " + String.valueOf(distance));
		Dimension dim = driver.manage().window().getSize();
		int startX = dim.getWidth() / 10;
		int startY = dim.getHeight() / 2;
		int endX = (startX + distance) >= dim.getWidth() ? (dim.getWidth() - 10) : (startX + distance);
		int endY = startY;
//		System.out.println(startX + "--->" + startY + "--->" + endX + "--->" + endY);
		driver.swipe(startX, startY, endX, endY, 1000);
	}
	
	// 获取元素的边界的中心位置，元素肯定是以矩形的方式存在，如：left 则返回左边中心点位置；top 则返回上边中心点位置
	public static ArrayList<Integer> getElementRelative(@SuppressWarnings("rawtypes") AppiumDriver driver, WebElement element, String choose, String distance) {
		ArrayList<Integer> result = new ArrayList<>();
		Point point = element.getLocation();
		Dimension dim = element.getSize();
		int x = dim.getWidth();
		int y = dim.getHeight();
		int half_x = x / 2;
		int half_y = y / 2;
		int centerX = point.getX() + half_x;
		int centerY = point.getY() + half_y;
		if (distance.contains("&")) {
			String[] doubleDistance = distance.split("&");
			Double dist = Double.valueOf(doubleDistance[0]);
			double result_x = dist * x;
			double result_y = dist * y;
			switch (choose) {
			case "left":
				centerX = centerX - (int)result_x;
				break;
			case "right":
				centerX = centerX + (int)result_x;
				break;
			case "up":
				centerY = centerY - (int)result_y;
				break;
			case "down":
				centerY = centerY + (int)result_y;
				break;
			}
			String secondDistance = doubleDistance[1];
			String secondChoose = secondDistance.split("-")[0];
			Double secondDist = Double.valueOf(secondDistance.split("-")[1]);
			double second_result_x = secondDist * x;
			double second_result_y = secondDist * y;
			switch (secondChoose) {
			case "left":
				centerX = centerX - (int)second_result_x;
				break;
			case "right":
				centerX = centerX + (int)second_result_x;
				break;
			case "up":
				centerY = centerY - (int)second_result_y;
				break;
			case "down":
				centerY = centerY + (int)second_result_y;
				break;
			}
		}else {
			Double dist = Double.valueOf(distance);
			double result_x = dist * x;
			double result_y = dist * y;
			switch (choose) {
			case "left":
				centerX = centerX - (int)result_x;
				break;
			case "right":
				centerX = centerX + (int)result_x;
				break;
			case "up":
				centerY = centerY - (int)result_y;
				break;
			case "down":
				centerY = centerY + (int)result_y;
				break;
			}
		}
		result.add(centerX);
		result.add(centerY);
		return result;
	}
}
