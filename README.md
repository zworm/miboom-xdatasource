# XDataSource
	可动态修改用户、密码的数据源代理，无需重新初始化，支持dbcp2.0、druid1.1.9。
	支持修改url，新老数据连接可共存。

使用说明

```java
String url = "jdbc:mysql://localhost:3306/mysql?serverTimezone=UTC&useCursorFetch=true";

Properties pp = new Properties();
pp.setProperty("url", url);
pp.setProperty("username", "root");
pp.setProperty("password", "111");
// initialSize<=1，确保本例中的con2是用修改过后的用户密码创建。
pp.setProperty("initialSize", "0");

XDataSource ds = XDataSourceFactory.dbcpDataSource(pp);
// XDataSource ds=XDataSourceFactory.druidDataSource(pp);

// 用root/111获取的连接
Connection con1 = ds.getConnection();

ds.setUsername("xxx");
ds.setPassword("xxx");

// 用xxx/xxx创建的连接
Connection con2 = ds.getConnection();

con1.close();
con2.close();
```

注：url中可带用户密码，优先使用properties中设置的。
