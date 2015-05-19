package com.jiuletech.mysql;
import java.util.Map.Entry;

public class MySQLConfig extends AbstractConfig {
	

	private static String CONFIG_FILENAME;
	
	
	public MySQLConfig(String configFile) throws ConfigException {
        super(configFile);
    }
	
    public MySQLConfig() throws ConfigException {
		super(CONFIG_FILENAME);
	}
	
	public MySQLConfig(boolean isResources) throws ConfigException {
		super(CONFIG_FILENAME, isResources);
	}


	public MySQLConfig(String configFile, boolean isResources) throws ConfigException {
		super(configFile, isResources);
	}

	private String mysqlHost = null;
	
	private int mysqlPort = -1;
	
	private String mysqlUser = null;
	
	private String mysqlPasswd = null;
	
	private String mysqlDbname = null;

	@Override
	public void parseProperties() throws ConfigException {

		for(Entry<Object, Object> entry : prop.entrySet()) {
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if(key.equals("mysql.host")) {
				mysqlHost = value.trim();
			} else if(key.equals("mysql.port")) {
				mysqlPort = Integer.parseInt(value.trim());
			} else if(key.equals("mysql.user")) {
				mysqlUser = value.trim();
			} else if(key.equals("mysql.passwd")) {
				mysqlPasswd = value.trim();
			} else if(key.equals("mysql.dbname")) {
				mysqlDbname = value.trim();
			} else {
				
			}
			
		}
		
		if(mysqlHost == null) {
		    //LOG.error("mysql host is not set");
			throw new ConfigException("mysql host is not set");
		}
		if(mysqlPort == -1) {
		    //LOG.error("mysql port is not set");
			throw new ConfigException("mysql port is not set");
		}
		if(mysqlUser == null) {
		    //LOG.error("mysql user is not set");
			throw new ConfigException("mysql user is not set");
		}
		if(mysqlPasswd == null) {
		    //LOG.error("mysql password is not set");
			throw new ConfigException("mysql password is not set");
		}
		if(mysqlDbname == null) {
		    //LOG.error("mysql dbname is not set");
			throw new ConfigException("mysql dbname is not set");
		}

	}

	public String getMysqlHost() {
		return mysqlHost;
	}

	public int getMysqlPort() {
		return mysqlPort;
	}

	public String getMysqlUser() {
		return mysqlUser;
	}

	public String getMysqlPasswd() {
		return mysqlPasswd;
	}

	public String getMysqlDbname() {
		return mysqlDbname;
	}
	
}
