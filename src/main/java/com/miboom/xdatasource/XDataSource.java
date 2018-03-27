package com.miboom.xdatasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * 
 * @author zzf
 *
 */
public abstract class XDataSource implements DataSource, AutoCloseable {
	protected DataSource ds;

	protected XDataSource(DataSource ds) {
		this.ds = ds;
	}

	public abstract String getUrl();

	public abstract void setUrl(String url);

	public abstract String getUsername();

	public abstract void setUsername(final String username);

	public abstract String getPassword();

	public abstract void setPassword(final String password);

	public abstract void clearUsernameAndPassword();

	public DataSource getDataSource() {
		return ds;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return ds.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		ds.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		ds.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return ds.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return ds.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return ds.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return ds.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return ds.getConnection(username, password);
	}

}
