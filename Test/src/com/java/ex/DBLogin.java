package com.java.ex;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DBLogin {
	
	
	String ID = null;
	String PW = null;

	Statement stmt = null;
	ResultSet rs = null;
	String url = "jdbc:mariadb://localhost:3307/datadb"; 
	String sql = null;
	Properties info = null;
	Connection cnn = null;
	//로그인 기능
	public void checkIDPW(String ID, String PW) {
		this.ID = ID;
		this.PW = PW;

		try {
		Class.forName("org.mariadb.jdbc.Driver");
		info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "skrkcb");
		cnn = DriverManager.getConnection(url, info);
		stmt = cnn.createStatement();

		sql = "select * from joindb where ID='" + ID + "' and PW='" + PW + "'" ;
		rs = stmt.executeQuery(sql);



		} catch (Exception ee) {
		System.out.println("문제있음s");
		ee.printStackTrace();
		}
		}
}