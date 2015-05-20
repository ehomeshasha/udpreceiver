package com.jiuletech.mysql;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.jiuletech.common.GroupDataBean;
import com.jiuletech.common.MsgBean;
import com.jiuletech.util.DateUtil;
import com.jiuletech.util.GsonUtils;
import com.jiuletech.util.RadixUtil;

/**
 * Created by hadoop on 2014/12/29.
 */
public class MySQLRunner {
	
	private static final Logger LOG = Logger.getLogger(MySQLRunner.class);
    
	private QueryRunner runner;
	
	public MySQLRunner(QueryRunner runner) {
		this.runner = runner;
	}
	
	
	
    public Map getMemberInfo(String userid) throws SQLException {
    	userid = StringUtils.stripStart(userid,"0");
    	ResultSetHandler h = new KeyedHandler("id");
    	String queryString = "SELECT * FROM jl_member WHERE id='"+userid+"'";
    	LOG.info("QueryString="+queryString);
    	Map resultMap = (Map) runner.query(queryString, h);
    	Map member = (Map) resultMap.get(new Long(userid));
    	return member;
    }

    public void insertMsg2Mysql(MsgBean msgBean) throws UnsupportedEncodingException, SQLException {
    	
    	String tablename = "jl_data";
    	
    	String gps_type = "";
    	if(msgBean.getGpsFlagA().equals("1")) {
    		gps_type = "2";
    	} else if(msgBean.getGpsFlagA().equals("0")) {
    		gps_type = "1";
    	}
    	String type = msgBean.getGpsFlagB();
    	String mid = msgBean.getMid();
    	String userid = msgBean.getUserid();
    	String activity = msgBean.getActivity();
		String sleep_total_time = msgBean.getSleep_total_time();
    	String sleep_effective_time = msgBean.getSleep_effective_time();
    	String sleep_rover_time = msgBean.getSleep_rover_time();
    	String longitude = msgBean.getLongitude();
    	String latitude = msgBean.getLatitude();
    	
    	
    	List<GroupDataBean> groupDataBeanList = msgBean.getGroupDataBeanList();
    	for(GroupDataBean data : groupDataBeanList) {
    		
    		String spo2 = data.getSpo2();
			String heartrate = data.getHeartrate();
			String breath = data.getBreath();
			String skin = data.getSkin();
			String healthindex = data.getHealthindex();
			String ecg_ppt = RadixUtil.from16to10(data.getECG_PPT());
			String ecg_mrt = RadixUtil.from16to10(data.getECG_MRT());
			String ecg_p1 = RadixUtil.from16to10(data.getECG_P1());
			String ecg_p0 = RadixUtil.from16to10(data.getECG_P0());
			String ecg_b = RadixUtil.from16to10(data.getECG_B());
			String ecg_a = RadixUtil.from16to10(data.getECG_A());
			String ecg_area = RadixUtil.from16to10(data.getECG_AREA());
			String wear_type = "0";
			String time = data.getTime();
			time = RadixUtil.from16to10(time);
			DateUtil dateUtil = new DateUtil(Long.parseLong(time)*1000);
			String year = String.valueOf(dateUtil.getYear());
			String month = String.valueOf(dateUtil.getMonth());
			String day = String.valueOf(dateUtil.getDay());
			
    		String sql = "INSERT INTO "+tablename+" (mid,userid,spo2,heartrate,breath,skin,healthindex,ecg_ppt,ecg_mrt,ecg_p1,ecg_p0,ecg_b,ecg_a,ecg_area,activity,sleep_total_time,sleep_effective_time,sleep_rover_time,longitude,latitude,gps_type,type,wear_type,time,year,month,day) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    		Object[] params = {mid,userid,spo2,heartrate,breath,skin,healthindex,ecg_ppt,ecg_mrt,ecg_p1,ecg_p0,ecg_b,ecg_a,ecg_area,activity,sleep_total_time,sleep_effective_time,sleep_rover_time,longitude,latitude,gps_type,type,wear_type,time,year,month,day};
    		LOG.info("sql="+sql);
    		LOG.info("params="+GsonUtils.toJson(params));
    		runner.update(sql, params);
    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	//String sql = "insert into "+tbname+" (gu_aid,gu_url,gu_is_caiji,gu_page,gu_hash,gu_tao_id,gu_ref,gu_add_time,gu_start_time,gu_end_time) values(?,?,?,?,?,?,?,?,?,?)  ";
    	//Object[] params = {goods.getAid(), goods.getSalesUrls(), 0, page, hash, goods.getTid(), 9,(int)(System.currentTimeMillis()/1000), goods.getStart_time(), goods.getEnd_time()};
    	//runner.update(sql, params);
    }

    


	
}
