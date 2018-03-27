package com.miboom.xdatasource;

import java.lang.reflect.Field;

/**
 * 可动态修改用户密码和url的连接池 for druid1.1.9
 * 
 * hack DruidAbstractDataSource.createPhysicalConnection
 * 
 * @author zzf
 *
 */
class DruidDataSource extends XDataSource {
	private final static long default_maxWait = 5 * 1000;
	private com.alibaba.druid.pool.DruidDataSource ds;

	DruidDataSource(com.alibaba.druid.pool.DruidDataSource ds) {
		super(ds);
		// 如果用户密码不正确，缺省maxWait=-1，会造成无限等待(内部创建连接CreateConnectionThread会无限重试)，而dbcp(maxWaitMillis)则直接抛对应SQL异常。
		// 设置后，超时则抛异常：com.alibaba.druid.pool.GetConnectionTimeoutException
		if (ds.getMaxWait() == -1) {
			ds.setMaxWait(default_maxWait);
		}
		/**
		 * <pre>
		 * fix bug:
		 * 
		 * DruidDataSource#getConnectionDirect在GetConnectionTimeoutException时，
		 * 用notFullTimeoutRetryCnt <= this.notFullTimeoutRetryCount判断重试。
		 * 
		 * <=应改为=，否则notFullTimeoutRetryCount=0(默认)还是会重试一次。
		 * 
		 * 已提issues: https://github.com/alibaba/druid/issues/2437
		 * 
		 * 在此通过设置参数去除重试。
		 */
		if (ds.getNotFullTimeoutRetryCount() == 0) {
			ds.setNotFullTimeoutRetryCount(-1);
		}
		this.ds = ds;
	}

	@Override
	public String getUrl() {
		return ds.getUrl();
	}

	@Override
	public void setUrl(String url) {
		setField("jdbcUrl", url);
	}

	@Override
	public String getPassword() {
		return ds.getPassword();
	}

	@Override
	public void setPassword(String password) {
		setField("password", password);
	}

	@Override
	public String getUsername() {
		return ds.getUsername();
	}

	@Override
	public void setUsername(String username) {
		setField("username", username);
	}

	private void setField(String name, String value) {
		try {
			Field field = com.alibaba.druid.pool.DruidAbstractDataSource.class.getDeclaredField(name);
			field.setAccessible(true);
			field.set(ds, value);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws Exception {
		ds.close();
	}
}
