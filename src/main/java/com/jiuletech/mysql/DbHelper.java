package com.jiuletech.mysql;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;


public class DbHelper {
	private static DataSource dataSource;
	private DbHelper(){
	}
	
	public static QueryRunner getQueryRunner(){
		if(DbHelper.dataSource==null){
			
			synchronized (BasicDataSource.class) {
			
				String fileName = "mysql-conf.properties";
				MySQLConfig config = null;
		        try {
		            config = new MySQLConfig(fileName,true);
		            config.parseProperties();
		        } catch (AbstractConfig.ConfigException e) {
		            e.printStackTrace();
		        }
	
		        String mysqlHost = config.getMysqlHost();
		        int mysqlPort = config.getMysqlPort();
		        String mysqlUser = config.getMysqlUser();
		        String mysqlPasswd = config.getMysqlPasswd();
		        String mysqlDbname = config.getMysqlDbname();
	
	
		        
				
				
				
				//配置dbcp数据源
				BasicDataSource dbcpDataSource = new BasicDataSource();
				dbcpDataSource.setUrl("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDbname
		                + "?useUnicode=true&amp;characterEncoding=UTF-8");
				dbcpDataSource.setDriverClassName("com.mysql.jdbc.Driver");
				dbcpDataSource.setUsername(mysqlUser);
				dbcpDataSource.setPassword(mysqlPasswd);
				dbcpDataSource.setDefaultAutoCommit(true);
				dbcpDataSource.setMaxActive(100);
				dbcpDataSource.setMaxIdle(30);
				dbcpDataSource.setMaxWait(500);
				DbHelper.dataSource = (DataSource)dbcpDataSource;
				System.out.println("Initialize dbcp...");
			}
		}
		return new QueryRunner(DbHelper.dataSource);
	}
}