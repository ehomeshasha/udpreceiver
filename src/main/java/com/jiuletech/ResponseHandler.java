package com.jiuletech;

import java.sql.SQLException;
import java.util.Map;

import com.jiuletech.common.MsgBean;
import com.jiuletech.mysql.DbHelper;
import com.jiuletech.mysql.MySQLRunner;
import com.jiuletech.util.DateUtil;
import com.jiuletech.util.GsonUtils;

import org.apache.commons.lang3.StringUtils;

public class ResponseHandler {

	private MsgBean msgBean = null;
	
	public ResponseHandler(MsgBean msgBean) {
		this.msgBean = msgBean;
	}


	public String getResponse() throws SQLException {
		String response = "";
		
		MySQLRunner mysqlRunner = new MySQLRunner(DbHelper.getQueryRunner());
		Map member = mysqlRunner.getMemberInfo(msgBean.getUserid());
		GsonUtils.print(member);
		String pid = (String) member.get("pid");
		String tphone = (String) member.get("tphone");
		String status = (Boolean) member.get("status") ? "1" : "0";
		String age = String.valueOf(member.get("age"));
		String height = String.valueOf(member.get("height"));
		String weight = String.valueOf(member.get("weight"));
		String rphone1 = (String) member.get("rphone1");
		String rphone2 = (String) member.get("rphone2");
		String id = String.valueOf(member.get("id"));
		
		long curTimestamp = System.currentTimeMillis();
		DateUtil dateUtil = new DateUtil(curTimestamp);
		dateUtil.setTimeStringFormat("HHmmss");
		dateUtil.setDateStringFormat("yyyyMMdd");
		String HMS = dateUtil.getTimeString();
		String date = dateUtil.getDateString();
		String timestamp = String.valueOf(Math.round(curTimestamp/1000));
		
		response = pid + tphone + status + 
				StringUtils.leftPad(age, 2, "0") + 
				height + weight + rphone1 + rphone2 + 
				StringUtils.leftPad(id, 8, "0") + 
				HMS + date + timestamp;
		
		return response;
	}
	
	
	
	
	

}
