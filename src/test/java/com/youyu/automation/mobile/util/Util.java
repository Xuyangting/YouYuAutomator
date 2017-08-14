package com.youyu.automation.mobile.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Reporter;

public class Util {
	/**
	 * console输出执行步骤和信息;report记录执行步骤和信息，包括错误信息，将这些写入测试报告中
	 * 
	 * @param message
	 */
	public static void recodeMessage(String message) {
		// 控制台输出测试结果
		SimpleDateFormat df = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]: ");
		System.out.println(df.format(new Date()) + message);
		// 测试报告中显示测试结果
		Reporter.log(df.format(new Date()) + message);
	}
	
	/**
	 * 启动Test Case提示语
	 */
	public static void startTC(String name, String description) {
		recodeMessage("================== Start Test Case ==================");
		recodeMessage("= 用例名称: " + name);
		recodeMessage("= 用例描述: " + description);
		recodeMessage("=====================================================");
	}
	
	/**
	 * 结束Test Case提示语
	 */
	public static void endTC() {
		recodeMessage("=================== End Test Case ===================");
		recodeMessage("=          执行到这一步了，说明该用例执行通过            =");
		recodeMessage("=====================================================");
	}
	
	/**
	 * 请求等待，单位：毫秒
	 * 
	 * @param millisecond
	 */
	public static void sleep(long millisecond) {
		try {
			recodeMessage("等待 -> " + millisecond/1000 + "秒");
			Thread.sleep(millisecond);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取文件内容
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readFileByChars(String fileName) throws IOException {
		String result = "";
		@SuppressWarnings("resource")
		BufferedReader myRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
		while(myRead.readLine() != null){
			result += myRead.readLine();
		}
		return result;
	}
	
	/**
	 * 判断test case是否执行，即enabled为true还是false
	 * @param testJob
	 * @param testCaseName
	 * @return
	 * @throws JSONException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static boolean runTestCase(String testJob, String testCaseName) throws ClassNotFoundException, SQLException, JSONException {
		String message = "not_match";
		JSONArray suite_id = MySqlUtil.getData("select suite_id from app_testjob where id=" + testJob);
		String testSuiteId = null;
		for (int i = 0; i < suite_id.length(); i++) {
			testSuiteId = ((JSONObject)suite_id.get(i)).get("suite_id").toString();
		}
		JSONArray case_id = MySqlUtil.getData("select case_id from app_suitecase where suite_id=" + testSuiteId);
		List<String> testCaseId = new ArrayList<String>();
		for (int i = 0; i < case_id.length(); i++) {
			testCaseId.add(((JSONObject)case_id.get(i)).get("case_id").toString());
		}
		for (int i = 0; i < testCaseId.size(); i++) {
			JSONArray testCaseEnglishName = MySqlUtil.getData("select ename from app_testcase where id=" + testCaseId.get(i));
			for (int j = 0; j < testCaseEnglishName.length(); j++) {
				if(((JSONObject)testCaseEnglishName.get(j)).get("ename").toString().equals(testCaseName)){
					message = "match";
				}
			}
		}
		if (message.equals("match")) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 执行CMD命令/Linux命令
	 * @param command
	 */
	public static void execCommand(String command) {
		Runtime runTime = Runtime.getRuntime();
		try {
			runTime.exec(command);
		} catch (Exception e) {
			recodeMessage("执行CMD命令异常，请手动检测");
		}
	}
	
	/**
	 * 根据港股的股票价格，获取该股票最小价差
	 * @param stockPrice
	 * @return
	 */
	public static double getStockMinimunPrive(double stockPrice,String stockMarket) {
		if (stockMarket.equals("hk")) {
			if (stockPrice >= 0 && stockPrice <= 0.25) {
				return 0.001;
			}else if (stockPrice > 0.25 && stockPrice <= 0.5) {
				return 0.005;
			}else if (stockPrice > 0.5 && stockPrice <= 10) {
				return 0.01;
			}else if (stockPrice > 10 && stockPrice <= 20) {
				return 0.02;
			}else if (stockPrice > 20 && stockPrice <= 100) {
				return 0.05;
			}else if (stockPrice > 100 && stockPrice <= 200) {
				return 0.1;
			}else if (stockPrice > 200 && stockPrice <= 500) {
				return 0.2;
			}else if (stockPrice > 500 && stockPrice <= 1000) {
				return 0.5;
			}else if (stockPrice > 1000 && stockPrice <= 2000) {
				return 1;
			}else if (stockPrice > 2000 && stockPrice <= 5000) {
				return 2;
			}else if (stockPrice > 5000 && stockPrice <= 9995) {
				return 5;
			}else {
				return 0;
			}
		}else if (stockMarket.equals("us")) {
			if (stockPrice >= 1) {
				return 0.01;
			}else {
				return 0.001;
			}
		}else {
			return 0.01;
		}
		
	}
	
	/**
	 * 数据进行处理 
	 */
	public static BigDecimal dealMoney(String myMoney) {
		String myMoneyDecimal = "";
		if (myMoney.contains(",")) {
			String[] myMoneyList = myMoney.split(",");
			for (int i = 0; i < myMoneyList.length; i++) {
				myMoneyDecimal += myMoneyList[i];
			}
		}else {
			myMoneyDecimal += myMoney;
		}
		BigDecimal myMoneyResult = new BigDecimal(myMoneyDecimal);
		return myMoneyResult;
	}
	
	/**
	 * 数据进行处理 
	 */
	public static String dealMoneyString(String myMoney) {
		String myMoneyDecimal = "";
		if (myMoney.contains(",")) {
			String[] myMoneyList = myMoney.split(",");
			for (int i = 0; i < myMoneyList.length; i++) {
				myMoneyDecimal += myMoneyList[i];
			}
		}else {
			myMoneyDecimal += myMoney;
		}
		return myMoneyDecimal;
	}
	
	/**
	 * 手机号码格式变化显示，规则如下：
	 * 大陆-12345678901，123***8901；
	 * 香港12345678，12**78；
	 * 邮箱地址，@前的前两位和最后一位展示，其余无论有几位数字都要用三个星号代替展示
	 * 例如12345@163.com，12*5@163.com；如果是123@163.com，同样和大于三位的一样展示为12*3@163.com; 如果是两位12@163.com，1*2@163.com；如果是一位1@163.com，1**@163.com
	 */
	public static String changeAccountNickname(String Account) {
		String result = null;
		// +8610110000021 -> 101 **** 0021
		if (Account.contains("+86") && !Account.contains(".com")) {
			result = Account.substring(3, 6) + " **** " + Account.substring(10, 14);
		}
		// +85212345678 -> 12 ** 78
		else if (Account.contains("+852") && !Account.contains(".com")) {
			result = Account.substring(4, 6) + "****" + Account.substring(10, 12);
		}
		// 10110000021 -> 101 **** 0021
		else if (Account.length() == 11 && !Account.contains(".com")) {
			result = Account.substring(0, 3) + " **** " + Account.substring(7, 11);
		}
		// 12345678 -> 12****78
		else if (Account.length() == 8 && !Account.contains(".com")) {
			result = Account.substring(0, 2) + "****" + Account.substring(6, 8);
		}
		// 1@test.com -> 1***@test.com
		// 12@test.com -> 1***2@test.com
		// 123@test.com -> 12***3@test.com
		else if (Account.contains(".com")) {
			String emailName = Account.split("@")[0];
			String emailHost = Account.split("@")[1];
			if (emailName.length()==1) {
				result = emailName + "***@" + emailHost;
			}else if (emailName.length()==2) {
				result = emailName.substring(0, 1) + "***" + emailName.substring(1, 2) + "@" + emailHost;
			}else {
				result = emailName.substring(0, 2) + "***" + emailName.substring(emailName.length()-1, emailName.length()) + "@" + emailHost;
			}
		}
		return result;
	}
	
	/**
	 * 清空screenshots文件夹下面的所有图片
	 */
	public static void clearScreenshotsDir() {
		File classpathRoot = new File(System.getProperty("user.dir"));
		String picPath = classpathRoot.getAbsolutePath() + "//screenshots//";
		File file = new File(picPath);
        if (!file.exists()) {
            recodeMessage("文件夹不存在");
        	}
        if (!file.isDirectory()) {
        	recodeMessage("文件夹不存在");
	       }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
           if (picPath.endsWith(File.separator)) {
              temp = new File(picPath + tempList[i]);
           } else {
               temp = new File(picPath + File.separator + tempList[i]);
           }
           if (temp.isFile()) {
              temp.delete();
           }
        }
        recodeMessage("= 初始化 = 删除文件夹：screenshots内的图片成功 ^_^");
	}
	
	/**
	 * 主方法 - 调试用
	 * @param args
	 * @throws IOException
	 * @throws JSONException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, JSONException {
		clearScreenshotsDir();
	}
}
