package com.miboom.xdatasource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DriverConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * 可动态修改用户密码和url的连接池 for dbcp2.0
 * 
 * hack DriverConnectionFactory.createConnection
 * 
 * @author zzf
 *
 */
class DBCPDataSource extends XDataSource {

	private BasicDataSource ds;

	DBCPDataSource(BasicDataSource ds) {
		super(ds);
		this.ds = ds;
	}

	@Override
	public String getUsername() {
		return ds.getUsername();
	}

	/**
	 * 说明：setUsername和setPassword优先于url中的配置(url=...&user=xxx&password=xxx),
	 * 若user或password未设置则尝试取url中的值。
	 */
	public void setUsername(final String username) {
		Properties ps = getDriverConnectionFactoryProperties();
		if (ps != null) {
			// 这里的key是user，与url中同。(dbcp2的配置用username)
			ps.put("user", username);
		}
		ds.setUsername(username);
	}

	@Override
	public String getPassword() {
		return ds.getPassword();
	}

	@Override
	public void setPassword(final String password) {
		Properties ps = getDriverConnectionFactoryProperties();
		if (ps != null) {
			ps.put("password", password);
		}
		ds.setPassword(password);
	}

	@Override
	public String getUrl() {
		return ds.getUrl();
	}

	@Override
	public void setUrl(String url) {
		try {
			DriverConnectionFactory dcf = getDriverConnectionFactory();
			if (dcf != null) {
				Field field = DriverConnectionFactory.class.getDeclaredField("_connectUri");
				field.setAccessible(true);
				field.set(dcf, url);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ds.setUrl(url);
	}

	private Properties getDriverConnectionFactoryProperties() {
		try {
			DriverConnectionFactory dcf = getDriverConnectionFactory();
			if (dcf != null) {
				Field field = DriverConnectionFactory.class.getDeclaredField("_props");
				field.setAccessible(true);
				return (Properties) field.get(dcf);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private DriverConnectionFactory getDriverConnectionFactory() throws Exception {
		Method getConnectionPool = BasicDataSource.class.getDeclaredMethod("getConnectionPool");
		getConnectionPool.setAccessible(true);
		GenericObjectPool<PoolableConnection> pool = (GenericObjectPool<PoolableConnection>) getConnectionPool
				.invoke(ds);
		if (pool != null) {
			PoolableConnectionFactory pcf = (PoolableConnectionFactory) pool.getFactory();
			Method getConnectionFactory = PoolableConnectionFactory.class.getDeclaredMethod("getConnectionFactory");
			getConnectionFactory.setAccessible(true);
			return (DriverConnectionFactory) getConnectionFactory.invoke(pcf);
		}
		return null;
	}

	@Override
	public void close() throws Exception {
		ds.close();
	}

}
