package com.jiuletech.mysql;

import java.sql.Connection;
import java.sql.SQLException;


import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;


import org.apache.log4j.Logger;

public class MySQLClient {


    private MySQLConfig config;
    private String mysqlHost = null;
    private int mysqlPort = -1;
    private String mysqlUser = null;
    private String mysqlPasswd = null;
    private String mysqlDbname = null;

    private BasicDataSource dataSource = new BasicDataSource();
    private QueryRunner run;
    private Connection con ;
    
    private static MySQLClient mySQLClient = null;

    public MySQLClient() throws SQLException {

        String fileName = "mysql-conf.properties";
        try {
            config = new MySQLConfig(fileName,true);
            config.parseProperties();
        } catch (AbstractConfig.ConfigException e) {
            e.printStackTrace();
        }

        mysqlHost = config.getMysqlHost();
        mysqlPort = config.getMysqlPort();
        mysqlUser = config.getMysqlUser();
        mysqlPasswd = config.getMysqlPasswd();
        mysqlDbname = config.getMysqlDbname();


        // setup dataStore
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername(mysqlUser);
        dataSource.setPassword(mysqlPasswd);
        dataSource.setUrl("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDbname
                + "?useUnicode=true&amp;characterEncoding=UTF-8");
        run = new QueryRunner(dataSource);
        con = dataSource.getConnection();
    }

    public static MySQLClient getInstance() throws SQLException {
    	//return new MySQLClient();
        //return mySQLClient;
    	if(null == mySQLClient) {
    		mySQLClient = new MySQLClient();
    	}
    	return mySQLClient;
    }

    /**
     * 保存
     * @param sql
     * @param param
     * @return
     * @throws java.sql.SQLException
     */
    public int insert(String sql, Object[] param) throws SQLException {
        return run.update(con,sql, param);
    }
    
    /**
     * eg: select count(1) from user
     * 
     * @param sql
     * @param params
     * @return
     * @throws java.sql.SQLException
     */
    public int count(String sql, Object[] params) throws SQLException {
        
        int ret = 0;
        
        Object o = getAnAttr(sql, params);
        if (o instanceof Integer) {
            return (Integer) o;
        }
        if (o instanceof Long) {
            Long l = (Long) o;
            return l.intValue();
        }

        String s = (String) o;

        ret =  Integer.parseInt(s);

        return ret;
    }

    /**
     * 获得第一个查询第一行第一列
     * 
     * @param sql
     * @param params
     * @return
     * @throws java.sql.SQLException
     */
    public Object getAnAttr(String sql, Object[] params) throws SQLException {
    	//Connection con = dataSource.getConnection();
        return run.query(sql, new ScalarHandler(1), params);
    }
    
    public void close() {

    	if ( con != null) {
    		try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	
    	if ( dataSource != null) {
    		try {
				dataSource.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}

    }

    public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
