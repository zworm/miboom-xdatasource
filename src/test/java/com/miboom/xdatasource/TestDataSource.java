package com.miboom.xdatasource;

import java.sql.Connection;
import java.util.Properties;

import com.miboom.xdatasource.XDataSource;
import com.miboom.xdatasource.XDataSourceFactory;

import junit.framework.TestCase;

public class TestDataSource extends TestCase {

	public void aatestDBCPGetConnection() throws Exception {
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
		String url = "jdbc:mysql://localhost:3306/mysql?serverTimezone=UTC&useCursorFetch=true";
		// url+="&user=root&password=111";
		Properties pp = new Properties();
		pp.setProperty("url", url);
		pp.setProperty("username", "root");
		pp.setProperty("password", "111");
		pp.setProperty("initialSize", "0");
		return pp;
	}

	private void doGetConnection(XDataSource ds) throws Exception {
		Connection con1 = ds.getConnection();
		System.out.println(con1);

		// 修改：url、用户、密码
		ds.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC&useCursorFetch=true");
		 ds.setUsername("admin");
		 ds.setPassword("222");

		Connection con2 = ds.getConnection();
		System.out.println(con2);
		con1.close();
		con2.close();
	}

}
