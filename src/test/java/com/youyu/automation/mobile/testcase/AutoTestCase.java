package com.youyu.automation.mobile.testcase;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.youyu.automation.mobile.flow.AutoFlow;
import com.youyu.automation.mobile.flow.CustomizedFlow;
import com.youyu.automation.mobile.flow.StockTradeFlow;
import com.youyu.automation.mobile.page.AutoPage;
import com.youyu.automation.mobile.service.AutoService;
import com.youyu.automation.mobile.util.AppiumUtil;
import com.youyu.automation.mobile.util.MySqlUtil;
import com.youyu.automation.mobile.util.SwipeUtil;
import com.youyu.automation.mobile.util.Util;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class AutoTestCase {
	protected String URL;
	protected String PORT;
	protected String APP;
	protected String UDID;
	protected String ENV;
	protected String DEVICE;
	protected String JOBID;
	protected String APPADDRESS;
	protected static String METHOD;
	protected static String TESTCASENAME;
	protected static String EXECUTEID;
	
	@SuppressWarnings("rawtypes")
	protected static AppiumDriver driver;
	private AutoPage autoPage;
	private AutoService autoService;
	private AutoFlow autoFlow;
	private StockTradeFlow stockTradeFlow;
	private CustomizedFlow customFlow;
	
	/**
	 * 测试前获取driver
	 * @throws JSONException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings("rawtypes")
	@BeforeMethod
	public void setUpMethod() throws MalformedURLException, ClassNotFoundException, SQLException, JSONException{
		if (DEVICE.equals("ios")) {
			driver = (IOSDriver) AppiumUtil.getDriver(URL, PORT, APP, DEVICE, UDID, ENV, APPADDRESS);
		}else if (DEVICE.equals("android")) {
			driver = (AndroidDriver) AppiumUtil.getDriver(URL, PORT, APP, DEVICE, UDID, ENV, APPADDRESS);
		}
		autoPage = new AutoPage(driver);
		autoService = new AutoService(driver);
		autoFlow = new AutoFlow(driver);
		stockTradeFlow = new StockTradeFlow(driver);
		customFlow = new CustomizedFlow(driver);
	}
	
	/**
	 * 执行�进行数据 初始�
	 */
	@BeforeTest
	@Parameters({"url", "port", "app", "device", "udid", "env", "test_job", "app_address", "method", "test_execute_id"})
	public void setUp(String url, String port, String app, String device, String udid, String env, String test_job, String app_address,String method,String test_execute_id) throws MalformedURLException, ClassNotFoundException, SQLException, JSONException {
		ENV = env;
		UDID = udid;
		JOBID = test_job;
		DEVICE = device;
		URL = url;
		PORT = port;
		APP = app;
		APPADDRESS = app_address;
		METHOD = method;
		EXECUTEID = test_execute_id;
		Util.clearScreenshotsDir();
	}
	
	/**
	 * 执行�进行数据 清理
	 */
	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
	
	/**
	 * 提供测试用例
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws JSONException
	 */
	@DataProvider(name = "getTestCase")
	public Object[][] getTestCase() throws ClassNotFoundException, SQLException, JSONException{
		// 获取测试集合编号
		String suite_id = null;
		JSONArray test_job = MySqlUtil.getData("select * from app_testjob");
		for (int i = 0; i < test_job.length(); i++) {
			if (((JSONObject)test_job.get(i)).get("id").toString().equals(JOBID.toString())) {
				suite_id = ((JSONObject)test_job.get(i)).get("suite_id").toString();
				break;
			}
		}
		// 获取测试用例编号
		ArrayList<String> case_id = new ArrayList<>();
		JSONArray test_case = MySqlUtil.getData("select * from app_suitecase");
		for (int i = 0; i < test_case.length(); i++) {
			if (((JSONObject)test_case.get(i)).get("suite_id").toString().equals(suite_id)) {
				case_id.add(((JSONObject)test_case.get(i)).get("case_id").toString());
			}
		}
		// 将测试用例编号传给run test case，进行执行case
		Object[][] result=new Object[case_id.size()][];
		for (int i = 0; i < case_id.size(); i++) {
			JSONArray case_info = MySqlUtil.getData("select * from app_testcase where id = " + case_id.get(i));
			result[i] = new Object[]{case_id.get(i), ((JSONObject)case_info.get(0)).get("ename"), ((JSONObject)case_info.get(0)).get("description")};
		}
		return result;
	}
	
	/**
	 * 执行测试用例
	 * @param case_id
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws JSONException
	 * @throws NumberFormatException
	 * @throws InterruptedException
	 */
	@Test(dataProvider = "getTestCase")
	public void runTestCase(String case_id, String ename, String description) throws ClassNotFoundException, SQLException, JSONException, NumberFormatException, InterruptedException{
		TESTCASENAME = ename;
		Util.startTC(ename, description);
		// 根据测试用例编号查询用例的执行步�
		JSONArray testCaseStep = MySqlUtil.getData("select * from app_casestep where step_type='" + DEVICE + "' and case_id='" + case_id + "' order by step_run_id");
		ArrayList<String> methods = new ArrayList<>();
		ArrayList<String> parameter_1 = new ArrayList<>();
		ArrayList<String> parameter_2 = new ArrayList<>();
		for (int i = 0; i < testCaseStep.length(); i++) {
			methods.add((String) ((JSONObject)testCaseStep.get(i)).get("step_method"));
			parameter_1.add((String) ((JSONObject)testCaseStep.get(i)).get("parameter_1"));
			parameter_2.add((String) ((JSONObject)testCaseStep.get(i)).get("parameter_2"));
		}
		// 获取当前路径
		File currentPath = new File(System.getProperty("user.dir"));
		for (int i = 0; i < methods.size(); i++) {
			// 跳过登录流程
			if (methods.get(i).equals("WithUnLogin")) {
				autoFlow.gotoAppWithNoLogin(APP);
				continue;
			}
			
			// 跳过登录流程
			if (methods.get(i).equals("WithLogin")) {
				if (parameter_1.get(i).equals("") && parameter_2.get(i).equals("")) {
					autoFlow.gotoAppWithLogin("13133847086", "xyt123456",APP);
				}else {
					String account = autoPage.getAccountPwd(parameter_1.get(i)).get(0);
					String pwd = autoPage.getAccountPwd(parameter_1.get(i)).get(1);
					autoFlow.gotoAppWithLogin(account,pwd,APP);
				}
				continue;
			}
			
			// 等待事件
			if (methods.get(i).equals("sleep")) {
				Util.sleep(Integer.parseInt(parameter_1.get(i)) * 1000);
				continue;
			}
			
			// 滑动事件
			if (methods.get(i).equals("swipe")) {
				switch (parameter_1.get(i)) {
				case "left":
					if (parameter_2.get(i).toString().matches("[0-9]{1,}")) {
						SwipeUtil.swipeToLeft(driver, Integer.parseInt(parameter_2.get(i)));
						break;
					}else {
						Util.recodeMessage("滑动控件 -> " + parameter_2.get(i));
						SwipeUtil.swipeToLeft(driver, autoPage.FindElement(autoPage.getAddressByEnglishName(parameter_2.get(i))));
						break;
					}
				case "right":
					if (parameter_2.get(i).toString().matches("[0-9]{1,}")) {
						SwipeUtil.swipeToRight(driver, Integer.parseInt(parameter_2.get(i)));
						break;
					}else {
						Util.recodeMessage("滑动控件 -> " + parameter_2.get(i));
						SwipeUtil.swipeToRight(driver, autoPage.FindElement(autoPage.getAddressByEnglishName(parameter_2.get(i))));
						break;
					}
				case "up":
					if (parameter_2.get(i).toString().matches("[0-9]{1,}")) {
						SwipeUtil.swipeToUp(driver, Integer.parseInt(parameter_2.get(i)));
						break;
					}else {
						Util.recodeMessage("滑动控件 -> " + parameter_2.get(i));
						SwipeUtil.swipeToUp(driver, autoPage.FindElement(autoPage.getAddressByEnglishName(parameter_2.get(i))));
						break;
					}
				case "down":
					if (parameter_2.get(i).toString().matches("[0-9]{1,}")) {
						SwipeUtil.swipeToDown(driver, Integer.parseInt(parameter_2.get(i)));
						break;
					}else {
						Util.recodeMessage("滑动控件 -> " + parameter_2.get(i));
						SwipeUtil.swipeToDown(driver, autoPage.FindElement(autoPage.getAddressByEnglishName(parameter_2.get(i))));
						break;
					}
				}
				continue;
			}
			
			// 点击事件
			if (methods.get(i).equals("click")) {
				autoService.ClickElement(parameter_1.get(i));
				continue;
			}
			
			// 点击事件 - 通过某个控件定位，相对于这个控件查找需要点击的�x, y)
			if (methods.get(i).equals("clickByElementRelative")) {
				String[] result = parameter_2.get(i).split("@");	
				ArrayList<Integer> clickElement = SwipeUtil.getElementRelative(driver, autoPage.FindElement(autoPage.getAddressByEnglishName(parameter_1.get(i))), result[0], result[1]);
				Util.recodeMessage("点击点坐标：x=" + String.valueOf(clickElement.get(0)) + " y=" + String.valueOf(clickElement.get(1)));
				driver.tap(1, clickElement.get(0), clickElement.get(1), 0);
				continue;
			}
			
			// 输入文本事件
			if (methods.get(i).equals("setValue")) {
				autoService.SetValue(parameter_1.get(i), parameter_2.get(i));
				continue;
			}
			
			// 检测事�- 绝对等于
			if (methods.get(i).equals("check")) {
				autoService.checkValue(parameter_2.get(i), autoService.getValue(parameter_1.get(i)));
				continue;
			}
			
			// 检测事�- 有鱼股票 - 交易 - 最小价�
			if (methods.get(i).equals("checkTradeMininumPrice")) {
				if (parameter_2.get(i).equals("")) {
					stockTradeFlow.ckeckStockTradeMininumPrice(parameter_1.get(i));
				}else {
					stockTradeFlow.ckeckStockTradeMininumPrice(parameter_1.get(i), parameter_2.get(i));
				}
				continue;
			}
			
			// 检测事�- 包含
			if (methods.get(i).equals("contain")) {
				autoService.contain(autoService.getValue(parameter_1.get(i)),parameter_2.get(i));
				continue;
			}
			
			// 检测事�- 绝对等于
			if (methods.get(i).equals("checkValueNotNull")) {
				autoService.checkValueNotNull(parameter_1.get(i));
				continue;
			}
			
			// 检测事�- 元素列表数据不为空，并显示出�
			if (methods.get(i).equals("checkListNotNull")) {
				autoService.checkListValueNotNull(parameter_1.get(i), parameter_2.get(i));
				continue;
			}
			
			// 检测事�- 元素列表数据排序是否正确，并显示出来
			if (methods.get(i).equals("checkListOrder")) {
				autoService.checkListValueOrder(parameter_1.get(i), parameter_2.get(i));
				continue;
			}
			
			// 提示�
			if (methods.get(i).equals("tip")) {
				Util.recodeMessage(parameter_1.get(i) + parameter_2.get(i));
				continue;
			}
			
			//安卓系统返回命令
			if(methods.get(i).equals("backMenu")){
				autoFlow.backMenu();
				continue;
			}
			
			//对比 不相 等
			if(methods.get(i).equals("checkValueNotEqual")){
				autoService.checkValueNotEqual(parameter_2.get(i),autoService.getValue(parameter_1.get(i)));
				continue;
			}
			
			//对比 列表所有元素都不 等与某一值
			if(methods.get(i).equals("checkAllValuesNotEqual")){
				autoService.checkAllValuesNotEqual(parameter_1.get(i),parameter_2.get(i));
				continue;
			}
						
			
			// 判断元素是否存在
			if(methods.get(i).equals("checkElementIfExist")){
				autoService.checkElementIfExist(parameter_1.get(i), parameter_2.get(i));
				continue;
			}
			
			// 注册账号
			if (methods.get(i).equals("registerAccountByAPI")) {
				Util.execCommand("python " + currentPath + "/python/register_account.py " + parameter_1.get(i) + " " + ENV + " " + parameter_2.get(i));
				continue;
			}
			
			// 删除账号
			if (methods.get(i).equals("deleteAccountByAPI")) {
				Util.execCommand("python " + currentPath + "/python/delete_account.py " + parameter_1.get(i) + " " + ENV);
				continue;
			}
			
			// 重置密码
			if (methods.get(i).equals("resetPasswdByAPI")) {
				Util.execCommand("python " + currentPath + "/python/reset_password.py " + parameter_1.get(i) + " " + ENV + " " + parameter_2.get(i));
				continue;
			}
			
			// 检测元素是否可以点�
			if(methods.get(i).equals("checkElementEnable")){
				autoService.checkElementEnable(parameter_1.get(i), parameter_2.get(i));
				continue;
			}
			
			if(methods.get(i).equals("addFavorStocks")){
				customFlow.addFavorStocks(parameter_1.get(i));
				continue;
			}
			
			// 有鱼股票 -> 交易 -> 输入交易密码流程
			if (methods.get(i).equals("inputTradePassword")) {
				stockTradeFlow.inputStockTradePassword(parameter_1.get(i));
				continue;
			}
			
			// 有鱼股票 -> 交易 -> 输入交易密码流程
			if (methods.get(i).equals("setTradePrice")) {
				stockTradeFlow.setTradePrice(parameter_1.get(i));
				continue;
			}
						
			// 有鱼股票 -> 交易 -> 订单金额检�
			if (methods.get(i).equals("checkOrderMoney")) {
				stockTradeFlow.checkOrderMoney();
				continue;
			}
			
			// 有鱼股票 -> 交易 -> 下单 -> 最大可�
			if (methods.get(i).equals("checkMaxBuy")) {
				stockTradeFlow.checkMaxBuy(parameter_1.get(i));
				continue;
			}
			
			// 有鱼股票 -> 交易 -> 下单 -> 购买�
			if (methods.get(i).equals("checkOwnMoney")) {
				stockTradeFlow.checkOwnMoney(parameter_1.get(i));
				continue;
			}
			
			// 有鱼股票 -> 交易 -> 下单 -> 最大可�
			if (methods.get(i).equals("checkMaxSale")) {
				stockTradeFlow.checkMaxSale(parameter_1.get(i), parameter_2.get(i));
				continue;
			}
			
			// 有鱼股票 -> 交易 -> 下单 -> 购买股票后检测下单页是否保留数据，和订单页记录数据是否正�
			if (methods.get(i).equals("checkBuyStock")) {
				stockTradeFlow.checkBuyStock(parameter_1.get(i));
				continue;
			}
			
			// 有鱼股票 -> 交易 -> 下单 -> 买卖股票
			if (methods.get(i).equals("buyOrSaleStock")) {
				stockTradeFlow.buyOrSaleStock(parameter_1.get(i), parameter_2.get(i));
				continue;
			}
			
			// 有鱼股票 -> 交易 -> 订单 -> 检测委托价和成交价保留小数位
			if (methods.get(i).equals("checkOrderPriceDecimal")) {
				stockTradeFlow.checkOrderPriceDecimal();
				continue;
			}
						
			// 元素数量对比，第二个参数输入对比符_数量，如 max_4, <_2, equals_1 等
			if(methods.get(i).equals("compareLength")){
				autoService.compareLength(parameter_1.get(i), parameter_2.get(i));
				continue;
			}
			
			// 有鱼股票 -> 个人 -> 账号安全->修改账号和交易密码
			if (methods.get(i).equals("keyboardInput")) {
				customFlow.keyboardInput(parameter_1.get(i));
				continue;
			}
		}
		Util.endTC();
	}
	
	/**
	 * 传递driver给监听器,全量变量driver,有好有坏，后期再进行优化
	 */
	public static AppiumDriver getDriver() {
		return driver;
	}
	
	/**
	 * 获取测试用例英文名称
	 */
	public static String getTestCaseEnglishName() {
		return TESTCASENAME;
	}
	
	/**
	 * 调试模式 还是 生产模式
	 */
	public static String getMethod() {
		return METHOD;
	}
	
	/**
	 * 返回质量中心执行测试时候创建的编号，方便讲测试过程中出现的失败截图进行转移到图片服务器进行统一管理
	 */
	public static String getExecuteId() {
		return EXECUTEID;
	}
}
