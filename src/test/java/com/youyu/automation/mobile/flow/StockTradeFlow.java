package com.youyu.automation.mobile.flow;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.openqa.selenium.WebElement;

import com.youyu.automation.mobile.page.AutoPage;
import com.youyu.automation.mobile.service.AutoService;
import com.youyu.automation.mobile.util.AppiumUtil;
import com.youyu.automation.mobile.util.SwipeUtil;
import com.youyu.automation.mobile.util.Util;

import io.appium.java_client.AppiumDriver;

public class StockTradeFlow {
	private AutoPage autoPage;
	private AutoService autoService;
	@SuppressWarnings("rawtypes")
	private AppiumDriver myDriver;
	
	public StockTradeFlow(@SuppressWarnings("rawtypes") AppiumDriver driver) throws ClassNotFoundException, SQLException, JSONException{
		autoPage = new AutoPage(driver);
		autoService = new AutoService(driver);
		myDriver = driver;
	}
	
	/**
	 * 检测 -> 有鱼股票->交易->下单->最小价差
	 * @throws JSONException 
	 */
	public void ckeckStockTradeMininumPrice(String stockMarket) throws JSONException {
		Util.recodeMessage("检测 -> 有鱼股票 -> 交易 -> 最小价差");
		String beforePrice = autoService.getValue("stock_trade_buy_value");
		Util.sleep(2000);
		autoService.ClickElement("stock_trade_buy_value_add");
		Util.sleep(2000);
		String afterPrice = autoService.getValue("stock_trade_buy_value");
		Double mininumPrice = Util.getStockMinimunPrive(Double.parseDouble(beforePrice), stockMarket);
		Util.recodeMessage("起始价格 -> " + String.valueOf(Double.parseDouble(beforePrice)) + " 增加后价格 -> " + String.valueOf(Double.parseDouble(afterPrice)));
		if (beforePrice.equals("0")) {
			assert afterPrice.equals("0.010");
		}else {
			assert mininumPrice + Double.parseDouble(beforePrice) == Double.parseDouble(afterPrice):mininumPrice + Double.parseDouble(beforePrice);
			autoService.ClickElement("stock_trade_buy_value_reduce");
			Util.sleep(2000);
			String afterReducePrice = autoService.getValue("stock_trade_buy_value");
			Util.recodeMessage("起始价格 -> " + afterPrice + " 减少后价格 -> " + String.valueOf(Double.parseDouble(afterReducePrice)));
			assert Double.parseDouble(afterReducePrice) == Double.parseDouble(beforePrice);
			Util.recodeMessage("检测 -> 最小差价 -> 通过");
		}
	}
	
	/**
	 * 检测 -> 有鱼股票->交易->下单->最小价差 -> 边界值 -> 点击 + 与 -
	 * @throws JSONException
	 */
	public void ckeckStockTradeMininumPrice(String stockMarket,String choose) throws JSONException {
		if (choose.equals("+")) {
			Util.recodeMessage("检测 -> 有鱼股票 -> 交易 -> 最小价差 -> 边界值 点击 +");
			String beforePrice = autoService.getValue("stock_trade_buy_value");
			Util.sleep(2000);
			autoService.ClickElement("stock_trade_buy_value_add");
			Util.sleep(2000);
			String afterPrice = autoService.getValue("stock_trade_buy_value");
			Double mininumPrice = Util.getStockMinimunPrive(Double.parseDouble(beforePrice) + 0.01, stockMarket);
			Util.recodeMessage("起始价格 -> " + String.valueOf(Double.parseDouble(beforePrice)) + " 增加后价格 -> " + String.valueOf(Double.parseDouble(afterPrice)));
			if (beforePrice.equals("0")) {
				assert afterPrice.equals("0.010");
			}else {
				assert mininumPrice + Double.parseDouble(beforePrice) == Double.parseDouble(afterPrice):mininumPrice + Double.parseDouble(beforePrice);
				}
		}else if (choose.equals("-")) {
			Util.recodeMessage("检测 -> 有鱼股票 -> 交易 -> 最小价差 -> 边界值 点击 -");
			String beforePrice = autoService.getValue("stock_trade_buy_value");
			Util.sleep(2000);
			autoService.ClickElement("stock_trade_buy_value_reduce");
			Util.sleep(2000);
			String afterPrice = autoService.getValue("stock_trade_buy_value");
			Double mininumPrice = Util.getStockMinimunPrive(Double.parseDouble(beforePrice) - 0.01, stockMarket);
			Util.recodeMessage("起始价格 -> " + String.valueOf(Double.parseDouble(beforePrice)) + " 增加后价格 -> " + String.valueOf(Double.parseDouble(afterPrice)));
			if (beforePrice.equals("0")) {
				assert afterPrice.equals("0.010");
			}else {
				assert Double.parseDouble(beforePrice) - mininumPrice == Double.parseDouble(afterPrice):mininumPrice + Double.parseDouble(beforePrice);
				}
			
		}else {
			Util.recodeMessage("传入参数有误 ^..^ !");
		}
	}
	
	/**
	 * 有鱼股票 -> 交易输入密码，密码固定
	 * @throws JSONException 
	 */
	public void inputStockTradePassword(String pwd) throws JSONException {
		Util.recodeMessage("输入 -> 交易密码 -> " + pwd);
		for (int i = 0; i < pwd.length(); i++) {
			String password_single = pwd.substring(i, i+1);
			ArrayList<Integer> clickElement = new ArrayList<>();
			switch (password_single) {
			case "1":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "up", "0.37&left-0.3");
				break;
			case "2":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "up", "0.37");
				break;
			case "3":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "up", "0.37&right-0.3");
				break;
			case "4":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "up", "0.12&left-0.3");
				break;
			case "5":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "up", "0.12");
				break;
			case "6":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "up", "0.12&right-0.3");
				break;
			case "7":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "down", "0.12&left-0.3");
				break;
			case "8":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "down", "0.12");
				break;
			case "9":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "down", "0.12&right-0.3");
				break;
			case "0":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_keyboard")), "down", "0.37");
				break;
			}
			Util.recodeMessage("点击点坐标：x=" + String.valueOf(clickElement.get(0)) + " y=" + String.valueOf(clickElement.get(1)));
			myDriver.tap(1, clickElement.get(0), clickElement.get(1), 0);
			Util.sleep(2000);
		}
	}
	
	/**
	 * 有鱼股票 -> 交易 -> 输入价格，密码固定
	 * @throws JSONException 
	 */
	public void setTradePrice(String price) throws JSONException {
		Util.recodeMessage("输入 -> 价格 -> " + price);
		for (int i = 0; i < price.length(); i++) {
			String password_single = price.substring(i, i+1);
			ArrayList<Integer> clickElement = new ArrayList<>();
			switch (password_single) {
			case "1":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "2");
				break;
			case "2":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "2&right-0.5");
				break;
			case "3":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "2&right-1");
				break;
			case "4":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "3");
				break;
			case "5":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "3&right-0.5");
				break;
			case "6":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "3&right-1");
				break;
			case "7":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "4");
				break;
			case "8":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "4&right-0.5");
				break;
			case "9":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "4&right-1");
				break;
			case "0":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "5&right-0.5");
				break;
			case ".":
				clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_buy_btn")), "down", "5");
				break;
			}
			Util.recodeMessage("点击点坐标：x=" + String.valueOf(clickElement.get(0)) + " y=" + String.valueOf(clickElement.get(1)));
			myDriver.tap(1, clickElement.get(0), clickElement.get(1), 0);
			Util.sleep(2000);
		}
	}
	
	
	/**
	 * 检测 有鱼股票 -> 交易 -> 下单 -> 订单金额
	 * @throws JSONException 
	 */
	public void checkOrderMoney() throws JSONException {
		Util.recodeMessage("检测 -> 订单金额");
		String stockPrice = autoService.getValue("stock_trade_buy_value");
		String stockNum = autoService.getValue("stock_trade_buy_num");
		String orderMoney = autoService.getValue("stock_trade_buy_amount");
		Util.recodeMessage(String.valueOf(stockPrice));
		Util.recodeMessage(String.valueOf(stockNum));
		Util.recodeMessage(String.valueOf(orderMoney));
		if (orderMoney.contains(",")) {
			orderMoney = orderMoney.split(",")[0] + orderMoney.split(",")[1];
		}
		orderMoney = orderMoney.substring(4,orderMoney.length());
		BigDecimal bigStockPrice = new BigDecimal(stockPrice);
		BigDecimal bigStockNum = new BigDecimal(stockNum);
		BigDecimal bigOrderMoney = new BigDecimal(orderMoney);
		BigDecimal actual = bigStockPrice.multiply(bigStockNum);
		Util.recodeMessage("预期结果：" + String.valueOf(bigOrderMoney) + " 实际结果：" + String.valueOf(actual));
		assert actual.compareTo(bigOrderMoney) == 0;
		Util.recodeMessage("检测通过");
	}
	
	/**
	 * 检测 有鱼股票 -> 交易 -> 下单 -> 最大可买
	 * @throws JSONException 
	 */
	public void checkMaxBuy(String stockMarket) throws JSONException {
		Util.recodeMessage("检测 -> 最大可买");
		String stockPrice = autoService.getValue("stock_trade_buy_value");
		String stockNum = autoService.getValue("stock_trade_buy_num");
		Util.recodeMessage("获取价格：" + stockPrice + " 一手数量：" + stockNum);
		autoService.ClickElement("stock_trade_own");
		String HKD = autoService.getValue("stock_trade_own_available_HKD");
		String USD = autoService.getValue("stock_trade_own_available_USD");
		String CNH = autoService.getValue("stock_trade_own_available_CNY");
		Util.recodeMessage("可用港币：" + HKD + " 可用美元：" + USD + " 可用人民币：" + CNH);
		autoService.ClickElement("stock_trade_buy");
		String available_buy = autoService.getValue("stock_trade_buy_available_buy");
		Util.recodeMessage("最大可买：" + available_buy);
		Util.recodeMessage("检测其正确性： 最大可买 = （统一购买力/输入的价格）取整手");
		BigDecimal myMoney = null;
		switch (stockMarket) {
		case "us":
			myMoney = Util.dealMoney(USD);
			break;
		case "hk":
			myMoney = Util.dealMoney(HKD);
			break;
		case "hs":
			myMoney = Util.dealMoney(CNH);
			break;
		}
		BigDecimal result = myMoney.divide(new BigDecimal(stockPrice),0,BigDecimal.ROUND_HALF_EVEN).divideToIntegralValue(new BigDecimal(stockNum)).multiply(new BigDecimal(stockNum));
		Util.recodeMessage("统一购买力：" + String.valueOf(myMoney) + " /输入的价格：" + stockPrice + " = 结果：" + String.valueOf(result));
		assert Util.dealMoney(available_buy.substring(0, available_buy.length()-1)).compareTo(result) == 0;
		Util.recodeMessage("检测通过 ^_^ !");
	}
	
	/**
	 * 检测 有鱼股票 -> 交易 -> 下单 -> 购买力
	 * @throws JSONException 
	 */
	public void checkOwnMoney(String stockMarket) throws JSONException {
		Util.recodeMessage("检测 -> 购买力");
		String orderOwnMoney = autoService.getValue("stock_trade_buy_enable");
		autoService.ClickElement("stock_trade_own");
		String HKD = autoService.getValue("stock_trade_own_available_HKD");
		String USD = autoService.getValue("stock_trade_own_available_USD");
		String CNH = autoService.getValue("stock_trade_own_available_CNY");
		Util.recodeMessage("可用港币：" + HKD + " 可用美元：" + USD + " 可用人民币：" + CNH);
		autoService.ClickElement("stock_trade_buy");
		String ownOwnMoney = null;
		switch (stockMarket) {
		case "us":
			ownOwnMoney = "USD " + USD;
			break;
		case "hk":
			ownOwnMoney = "HKD " + HKD;
			break;
		case "hs":
			ownOwnMoney = "CNH " + CNH;
			break;
		}
		Util.recodeMessage("持仓金额：" + ownOwnMoney + " 下单购买力：" + orderOwnMoney);
		assert orderOwnMoney.endsWith(ownOwnMoney);
		Util.recodeMessage("检测通过 ^_^ !");
	}
	
	/**
	 * 检测 有鱼股票 -> 交易 -> 下单 -> 最大可卖
	 * @throws JSONException 
	 */
	public void checkMaxSale(String stockMarket, String stock) throws JSONException {
		Util.recodeMessage("检测 -> 最大可卖");
		String orderMaxSale = autoService.getValue("stock_trade_buy_position_num");
		Util.recodeMessage("下单页面 -> 最大可卖：" + orderMaxSale);
		autoService.ClickElement("stock_trade_own");
		Util.sleep(2000);
		switch (stockMarket) {
		case "us":
			autoService.ClickElement("stock_trade_own_indicator_1");
			Util.sleep(2000);
			SwipeUtil.swipeToUp(myDriver, SwipeUtil.getWindowHeight(myDriver) / 2);
			Util.sleep(2000);
			break;
		case "hs":
			autoService.ClickElement("stock_trade_own_indicator_1");
			Util.sleep(2000);
			autoService.ClickElement("stock_trade_own_indicator_2");
			Util.sleep(2000);
			SwipeUtil.swipeToUp(myDriver, SwipeUtil.getWindowHeight(myDriver) * 2 / 3);
			Util.sleep(2000);
			break;
		case "hk":
			SwipeUtil.swipeToUp(myDriver, SwipeUtil.getWindowHeight(myDriver) * 2 / 5);
			Util.sleep(2000);
			break;
		}
		String orderOwnStockNum = null;
		ArrayList<String> stockCodeList = getData("stock_trade_own_stockCode");
		ArrayList<String> stockOwnCountList = getData("stock_trade_own_count");
		for (int i = 0; i < stockCodeList.size(); i++) {
			if (stockCodeList.get(i).equals(stock)) {
				orderOwnStockNum = stockOwnCountList.get(i);
			}
		}
		Util.recodeMessage("持仓里显示持有股票：" + stock + " 有：" + orderOwnStockNum + "股");
		autoService.ClickElement("stock_trade_order");
		Util.sleep(2000);
		if (autoPage.isElementExist("您今天没有订单记录")) {
			Util.recodeMessage("下单页面 -> 最大可卖 -> " + orderMaxSale + " 持仓页面 -> 持股 -> " + orderOwnStockNum + " 订单页面 -> 交易股数 -> 0");
			assert orderMaxSale.equals(orderOwnStockNum + "股");
		}else {
			ArrayList<String> orderStockCode = getData("stock_trade_order_stock_code");
			ArrayList<String> orderStockState = getData("stock_trade_order_state");
			ArrayList<String> orderStockTradeNum = getData("stock_trade_order_stock_amount");
			ArrayList<String> aboutOrderStockCode = new ArrayList<>();
			ArrayList<String> aboutOrderStockState = new ArrayList<>();
			ArrayList<String> aboutOrderStockTradeNum = new ArrayList<>();
			for (int i = 0; i < orderStockCode.size(); i++) {
				if (orderStockCode.get(i).equals(stock)) {
					aboutOrderStockCode.add(orderStockCode.get(i));
					aboutOrderStockState.add(orderStockState.get(i));
					aboutOrderStockTradeNum.add(orderStockTradeNum.get(i));
				}
			}
			if (aboutOrderStockCode.size() == 0) {
				Util.recodeMessage("下单页面 -> 最大可卖 -> " + orderMaxSale + " 持仓页面 -> 持股 -> " + orderOwnStockNum + "股 订单页面 -> 交易股数 -> 0");
				assert orderMaxSale.equals(orderOwnStockNum + "股");
			}else {
				Util.recodeMessage("下单页面 -> 最大可卖 -> " + orderMaxSale + " 持仓页面 -> 持股 -> " + orderOwnStockNum + "股 订单页面 -> 交易股数 -> " + aboutOrderStockTradeNum.get(0));
			}
		}
	}
	
	/**
	 * 购买股票 测试场景，购买完股票检测下单界面是否还保留显示购买股票的数据，并且去订单页面检测今日订单的第一条记录数据是否和刚下单的是否一致
	 * @throws JSONException 
	 */
	public void checkBuyStock(String stockCode) throws JSONException {
		Util.recodeMessage("检测 -> 购买股票 -> " + stockCode + " 后的数据是否正确");
		String stockPrice = autoService.getValue("stock_trade_buy_value");
		String stockNum = autoService.getValue("stock_trade_buy_num");
		if (stockPrice.equals("0.00")) {
			Util.recodeMessage("股票价格显示：0.00");
			assert 1==0;
		}
		if (stockNum.equals("0")) {
			Util.recodeMessage("股票数量显示：0");
			assert 1==0;
		}
		autoService.ClickElement("stock_trade_order");
		Util.sleep(2000);
		ArrayList<String> orderStockCode = getData("stock_trade_order_stock_code");
		ArrayList<String> orderStockPrice = getData("stock_trade_order_order_price");
		ArrayList<String> orderStockTradeNum = getData("stock_trade_order_entrustNum");
		Util.recodeMessage("-- 判断订单界面显示的第一条记录数据是否正确 --");
		Util.recodeMessage("检测 -> 股票代码");
		assert orderStockCode.get(0).equals(stockCode);
		Util.recodeMessage("检测 -> 委托价格");
		assert orderStockPrice.get(0).equals(stockPrice);
		Util.recodeMessage("检测 -> 委托数量");
		assert stockNum.equals(Util.dealMoneyString(orderStockTradeNum.get(0)));
		Util.recodeMessage("-- 检测通过 --");
	}
	
	/**
	 * 买入或者卖出股票，以正确的价格
	 * @throws JSONException 
	 */
	public void buyOrSaleStock(String buyOrSale,String stockData) throws JSONException {
		String stockMarket = stockData.substring(0,2);
		String stockCode = stockData.substring(2, stockData.length());
		if (buyOrSale.equals("buy")) {
			Util.recodeMessage("买入股票");
		}else {
			Util.recodeMessage("卖出股票");
		}
		autoService.SetValue("stock_trade_buy_search_et_input", stockCode);
		Util.sleep(5000);
		ArrayList<Integer> clickElement = SwipeUtil.getElementRelative(myDriver, autoPage.FindElement(autoPage.getAddressByEnglishName("stock_trade_buy_search_et_input")), "down", "1");
		Util.recodeMessage("点击点坐标：x=" + String.valueOf(clickElement.get(0)) + " y=" + String.valueOf(clickElement.get(1)));
		myDriver.tap(1, clickElement.get(0), clickElement.get(1), 0);
		Util.sleep(3000);
		autoService.ClickElement("stock_trade_own");
		Util.sleep(2000);
		switch (stockMarket) {
		case "us":
			autoService.ClickElement("stock_trade_own_indicator_1");
			Util.sleep(2000);
			SwipeUtil.swipeToUp(myDriver, SwipeUtil.getWindowHeight(myDriver) / 2);
			Util.sleep(2000);
			break;
		case "hs":
			autoService.ClickElement("stock_trade_own_indicator_1");
			Util.sleep(2000);
			autoService.ClickElement("stock_trade_own_indicator_2");
			Util.sleep(2000);
			SwipeUtil.swipeToUp(myDriver, SwipeUtil.getWindowHeight(myDriver) * 2 / 3);
			Util.sleep(2000);
			break;
		case "hk":
			SwipeUtil.swipeToUp(myDriver, SwipeUtil.getWindowHeight(myDriver) * 2 / 5);
			Util.sleep(2000);
			break;
		}
		ArrayList<String> orderStockCode = getData("stock_trade_own_stockCode");
		ArrayList<String> orderStockPrice = getData("stock_trade_own_currentPrice");
		String actualPrice = null;
		for (int i = 0; i < orderStockCode.size(); i++) {
			if (orderStockCode.get(i).equals(stockCode)) {
				actualPrice = orderStockPrice.get(i);
			}
		}
		autoService.ClickElement("stock_trade_buy");
		Util.sleep(2000);
		if (actualPrice != null) {
			Util.recodeMessage("获取真实价格 -> " + actualPrice);
			autoService.SetValue("stock_trade_buy_value", "0");
			setTradePrice(actualPrice);
		}
		if (buyOrSale.equals("buy")) {
			autoService.ClickElement("stock_trade_buy_buy_btn");
		}else {
			autoService.ClickElement("stock_trade_buy_sale_btn");
		}
		Util.sleep(3000);
		inputStockTradePassword("123456");
	}
	
	/**
	 * 检测订单记录中委托价和成交价保留小数位的个数
	 * 规则如下：
	 * A股 & 港股： 保留3位小数
	 * 美股： 保留4位小数
	 * @throws JSONException 
	 */
	public void checkOrderPriceDecimal() throws JSONException {
		Util.recodeMessage("检测 -> 订单记录中委托价和成交价保留小数位的个数");
		Util.recodeMessage("A股 & 港股： 保留3位小数     美股： 保留4位小数");
		ArrayList<String> orderStockCode = getData("stock_trade_order_stock_code");
		ArrayList<String> orderOrderPrice = getData("stock_trade_order_order_price");
		ArrayList<String> orderStockPrice = getData("stock_trade_order_stock_price");
		for (int i = 0; i < orderStockCode.size(); i++) {
			// 港股 或者 A股 保留3位小数
			if (orderStockCode.get(i).equals("00700") || orderStockCode.get(i).equals("600626")) {
				assert orderOrderPrice.get(i).split("\\.")[1].length() == 3;
				assert orderStockPrice.get(i).split("\\.")[1].length() == 3;
				Util.recodeMessage("港股 和 A股 检测通过");
			}
			// 美股 保留4位小数
			if (orderStockCode.get(i).equals("BABA")) {
				assert orderOrderPrice.get(i).split("\\.")[1].length() == 4;
				assert orderStockPrice.get(i).split("\\.")[1].length() == 4;
				Util.recodeMessage("美股 检测通过");
			}
		}
	}
	
	/**
	 * 公共方法 - 获取列表数据
	 * @throws JSONException 
	 */
	public ArrayList<String> getData(String elementEnglishName) throws JSONException {
		ArrayList<String> stockCode = new ArrayList<>();
		String stockCodeAddress = autoPage.getAddressByEnglishName(elementEnglishName);
		Util.recodeMessage("获取 -> 股票代码");
		List<WebElement> elementsById = autoPage.getAllElementsById(AppiumUtil.getPackageName() + stockCodeAddress);
		List<WebElement> Elements = (elementsById.size() == 0)? autoPage.getAllElementsByClass(stockCodeAddress):elementsById;
		if(Elements.size() == 0){
			Util.recodeMessage("告警 -> 找不到该控件 -> " + elementEnglishName);
			assert false;
		}else {
			for (int i = 0; i < Elements.size(); i++) {
				WebElement element = Elements.get(i);
				if (element == null) {
					Util.recodeMessage("告警 -> 尚未找到该控件 -> " + elementEnglishName);
					assert false;
				}else {
					stockCode.add(element.getText().toString());
				}
			}
		}
		return stockCode;
	}
}
