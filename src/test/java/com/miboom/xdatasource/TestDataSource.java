package com.miboom.xdatasource;

import java.sql.Connection;
import java.util.Properties;

import junit.framework.TestCase;

public class TestDataSource extends TestCase {

	private String baseUrl = "jdbc:mysql://localhost:3306/mysql?serverTimezone=Asia/Shanghai&useCursorFetch=true";

	public void testDBCPGetConnection() throws Exception {
		XDataSource ds = XDataSourceFactory.dbcpDataSource(createProperties());
		try {
			doGetConnection(ds);
		} finally {
			ds.close();
		}
	}

	public void testDruidGetConnection() throws Exception {
		XDataSource ds = XDataSourceFactory.druidDataSource(createProperties());
		try {
			doGetConnection(ds);
		} finally {
			ds.close();
		}
	}

	private Properties createProperties() {
		Properties pp = new Properties();
		pp.setProperty("url", baseUrl);
		pp.setProperty("username", "root");
		pp.setProperty("password", "111");
		pp.setProperty("initialSize", "0");
		return pp;
	}

	private void doGetConnection(XDataSource ds) throws Exception {
		Connection con1 = ds.getConnection();
		System.out.println(con1);

		// 修改：url、用户、密码
		String url = baseUrl + "&user=root&password=111";
		ds.setUrl(url);
		// 清除属性中的用户密码，使用url中的。
		ds.clearUsernameAndPassword();

		Connection con2 = ds.getConnection();
		System.out.println(con2);
		con1.close();
		con2.close();
	}

}
