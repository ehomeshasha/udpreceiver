package com.jiuletech.mysql;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.jiuletech.common.MsgBean;
import com.jiuletech.mysql.MySQLClient;

/**
 * Created by hadoop on 2014/12/29.
 */
public class MySQLRunner {
    
	private QueryRunner runner;
	
	public MySQLRunner(QueryRunner runner) {
		this.runner = runner;
	}
	
	
	
    public int getCount(String tableName) {
        String sql = "select count(1) from " + tableName;


        int count = 0;

        try {
            count = MySQLClient.getInstance().count(sql, null);
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
        return count;
    }

    

    public void insertMysql(String tbname,Object obj) throws UnsupportedEncodingException, SQLException {
    	
    	
    	
    	
    	
    	//String sql = "insert into "+tbname+" (gu_aid,gu_url,gu_is_caiji,gu_page,gu_hash,gu_tao_id,gu_ref,gu_add_time,gu_start_time,gu_end_time) values(?,?,?,?,?,?,?,?,?,?)  ";
    	//Object[] params = {goods.getAid(), goods.getSalesUrls(), 0, page, hash, goods.getTid(), 9,(int)(System.currentTimeMillis()/1000), goods.getStart_time(), goods.getEnd_time()};
    	//runner.update(sql, params);
    }

    


	
}
