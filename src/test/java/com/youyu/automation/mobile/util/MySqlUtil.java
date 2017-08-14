package com.youyu.automation.mobile.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.cj.jdbc.PreparedStatement;
import com.mysql.cj.jdbc.ResultSetMetaData;

public class MySqlUtil {
    public static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";  
    public static final String DBURL = "jdbc:mysql://10.9.8.20:3306/app?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC";  
    public static final String DBUSER = "monitor";  
    public static final String DBPASS = "monitor123"; 
    
	/**
	 * 连接数据库
	 * @return 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public static Connection getConn() throws SQLException, ClassNotFoundException {
		Class.forName(DBDRIVER);
		Connection con = DriverManager.getConnection(DBURL,DBUSER,DBPASS);    
        return con;
	}
	
	/**
	 * 关闭数据库连接
	 */
	public static void closeMySQL(Connection conn) {
		try {
			conn.close();
		} catch (Exception e) {
			Util.recodeMessage("关闭 -> 数据库异常");
		}
	}
	
	/**
	 * 查询数据，并返回json格式的查询结果数据
	 * @param sql
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws JSONException
	 */
	public static  JSONArray getData(String sql) throws ClassNotFoundException, SQLException, JSONException {
		// json数组  
		JSONArray array = new JSONArray(); 
		
		Connection connection = getConn();
		ResultSet rs = null;
		Statement stmt = connection.createStatement();
		rs = stmt.executeQuery(sql);
		// 获取列数  
	    ResultSetMetaData metaData = (ResultSetMetaData) rs.getMetaData();  
	    int columnCount = metaData.getColumnCount(); 
	    
		while (rs.next()) {
			JSONObject jsonObj = new JSONObject(); 
			for (int i = 1; i <= columnCount; i++) {
				String columnName =metaData.getColumnLabel(i);  
	            String value = rs.getString(columnName);  
	            jsonObj.put(columnName, value); 
			}
			array.put(jsonObj); 
		}
		closeMySQL(connection);
		return array;
	}
	
	
	/**
	 * 更新数据
	 * @param updateSql
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void update(String updateSql) throws ClassNotFoundException, SQLException {
	    Connection conn = getConn();
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(updateSql);
	        pstmt.executeUpdate();
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	/**
	 * 主方法 - 调试用
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws JSONException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, SQLException, JSONException, IOException {
		JSONArray test_job = MySqlUtil.getData("select * from app_casestep where step_type='android' and case_id='1' order by step_run_id");
		System.out.println(test_job);
	}
}
