package edu.sdsc.dao;

import java.sql.*;
import java.util.Properties;
import com.vertica.jdbc.VerticaConnection;


public class WellConn {

	private static Connection conn = null;
	private static final String DRIVER = "com.vertica.jdbc.Driver";
	private static final String URL = "jdbc:vertica://stsi1.sdsc.edu:5433/wellderly";
	

	public static Connection getConn() throws Exception {
		
		Properties myProp = new Properties();
		//myProp.put("LogLevel", "DEBUG");
		myProp.put("AutoCommit", "false");
		myProp.put("user", "dbadmin");
		myProp.put("password", "3Nathan$");
		
		try {
			Class.forName(DRIVER);
			conn = (VerticaConnection) DriverManager.getConnection(URL, myProp);
			((VerticaConnection) conn).setProperty("ResultBufferSize", "0");
		} catch (Exception e) {
			System.out.println(e.toString());

		} finally {

		}
		return conn;
	}

}