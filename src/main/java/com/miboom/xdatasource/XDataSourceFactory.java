package com.miboom.xdatasource;

import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 
 * @author zzf
 *
 */
public class XDataSourceFactory {

	public static XDataSource dbcpDataSource(Properties properties) throws Exception {
		return new DBCPDataSource(BasicDataSourceFactory.createDataSource(properties));
	}

	public static XDataSource druidDataSource(Properties properties) throws Exception {
		return new DruidDataSource(
				(com.alibaba.druid.pool.DruidDataSource) DruidDataSourceFactory.createDataSource(properties));
	}
}
