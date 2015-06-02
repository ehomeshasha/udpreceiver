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

import com.jiuletech.common.Constants;
import com.jiuletech.common.GroupDataBean;
import com.jiuletech.common.MsgBean;
import com.jiuletech.util.DateUtil;
import com.jiuletech.util.GsonUtils;

/**
 * Created by hadoop on 2014/12/29.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MySQLRunner {
	
	private static final Logger LOG = Logger.getLogger(MySQLRunner.class);
    
	private QueryRunner runner;
	
	public MySQLRunner(QueryRunner runner) {
		this.runner = runner;
	}
	
	public boolean isDataExists(MsgBean msgBean) throws SQLException {
		
		String queryString = "SELECT count(1) AS count FROM "+msgBean.getInsertTableName()+" WHERE mid=? AND userid=? AND time=?";
    	List<GroupDataBean> groupDataBeanList = msgBean.getGroupDataBeanList();
    	String time = groupDataBeanList.get(groupDataBeanList.size()-1).getTime();
    	Object[] params = { msgBean.getMid(), msgBean.getUserid(), time};
		LOG.info("查询字符串="+queryString);
    	LOG.info("查询参数="+GsonUtils.toJson(params));
    	long count = getCount(queryString, params);
    	LOG.info("在"+msgBean.getInsertTableName()+"表中找到"+count+"条记录");
    	if(count >= 1) {
    		return true;
    	}
    	return false;	
    }
	
	public String getSoftVersion(String hardVersion) throws SQLException {
		ResultSetHandler h = new KeyedHandler("hard_version");
    	String queryString = "SELECT soft_version,hard_version FROM "+Constants.JL_VERSION+" WHERE hard_version='"+hardVersion+"'";
    	LOG.info("QueryString="+queryString);
    	Map resultMap = (Map) runner.query(queryString, h);
    	Map version = (Map) resultMap.get(hardVersion);
    	return (String) version.get("soft_version");
	}
	
    public Map getMemberInfo(String userid) throws SQLException {
    	userid = StringUtils.stripStart(userid,"0");
    	ResultSetHandler h = new KeyedHandler("id");
    	String queryString = "SELECT * FROM "+Constants.JL_MEMBER+" WHERE id='"+userid+"'";
    	LOG.info("查询字符串="+queryString);
    	Map resultMap = (Map) runner.query(queryString, h);
    	Map member = (Map) resultMap.get(new Long(userid));
    	return member;
    }

    public void insertMsg2Mysql(MsgBean msgBean) throws UnsupportedEncodingException, SQLException {
    	
    	String updateWarnTableName = Constants.JL_DATA_WARN;
    	
    	String insertTablename = msgBean.getInsertTableName();
    	
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
    	
    	//如果要插入的表里面已经有重复记录就不插入
    	if(isDataExists(msgBean)) {
    		LOG.info("在"+msgBean.getInsertTableName()+"表中已经有记录，因此不进行插入操作");
    	} else {
    	
	    	for(GroupDataBean data : groupDataBeanList) {
	    		
	    		String spo2 = data.getSpo2();
				String heartrate = data.getHeartrate();
				String breath = data.getBreath();
				String skin = data.getSkin();
				String healthindex = data.getHealthindex();
				String ecg_ppt = data.getECG_PPT();
				String ecg_mrt = data.getECG_MRT();
				String ecg_p1 = data.getECG_P1();
				String ecg_p0 = data.getECG_P0();
				String ecg_b = data.getECG_B();
				String ecg_a = data.getECG_A();
				String ecg_area = data.getECG_AREA();
				String wear_type = "0";
				String time = data.getTime();
				DateUtil dateUtil = new DateUtil(Long.parseLong(time)*1000);
				String year = String.valueOf(dateUtil.getYear());
				String month = String.valueOf(dateUtil.getMonth());
				String day = String.valueOf(dateUtil.getDay());
				
	    		String sql = "INSERT INTO "+insertTablename+" (mid,userid,spo2,heartrate,breath,skin,healthindex,ecg_ppt,ecg_mrt,ecg_p1,ecg_p0,ecg_b,ecg_a,ecg_area,activity,sleep_total_time,sleep_effective_time,sleep_rover_time,longitude,latitude,gps_type,type,wear_type,time,year,month,day) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	    		Object[] params = {mid,userid,spo2,heartrate,breath,skin,healthindex,ecg_ppt,ecg_mrt,ecg_p1,ecg_p0,ecg_b,ecg_a,ecg_area,activity,sleep_total_time,sleep_effective_time,sleep_rover_time,longitude,latitude,gps_type,type,wear_type,time,year,month,day};
	    		LOG.info("查询字符串="+sql);
	    		LOG.info("查询参数="+GsonUtils.toJson(params));
	    		runner.update(sql, params);
    	}
    	}
    	
    	if(msgBean.isUpdateWarnTable()) {
    		GroupDataBean data = groupDataBeanList.get(groupDataBeanList.size()-1);
    		String spo2 = data.getSpo2();
			String heartrate = data.getHeartrate();
			String breath = data.getBreath();
			String skin = data.getSkin();
			String healthindex = data.getHealthindex();
			String ecg_ppt = data.getECG_PPT();
			String ecg_mrt = data.getECG_MRT();
			String ecg_p1 = data.getECG_P1();
			String ecg_p0 = data.getECG_P0();
			String ecg_b = data.getECG_B();
			String ecg_a = data.getECG_A();
			String ecg_area = data.getECG_AREA();
			String wear_type = "0";
			String time = data.getTime();
			DateUtil dateUtil = new DateUtil(Long.parseLong(time)*1000);
			String year = String.valueOf(dateUtil.getYear());
			String month = String.valueOf(dateUtil.getMonth());
			String day = String.valueOf(dateUtil.getDay());
			String sql = "UPDATE "+updateWarnTableName+" SET mid=?,spo2=?,heartrate=?,breath=?,skin=?,healthindex=?,ecg_ppt=?,ecg_mrt=?,ecg_p1=?,ecg_p0=?,ecg_b=?,ecg_a=?,ecg_area=?,activity=?,sleep_total_time=?,sleep_effective_time=?,sleep_rover_time=?,longitude=?,latitude=?,gps_type=?,type=?,wear_type=?,time=?,year=?,month=?,day=? WHERE userid=?";
    		Object[] params = {mid,spo2,heartrate,breath,skin,healthindex,ecg_ppt,ecg_mrt,ecg_p1,ecg_p0,ecg_b,ecg_a,ecg_area,activity,sleep_total_time,sleep_effective_time,sleep_rover_time,longitude,latitude,gps_type,type,wear_type,time,year,month,day,userid};
    		LOG.info("查询字符串="+sql);
    		LOG.info("查询参数="+GsonUtils.toJson(params));
    		runner.update(sql, params);
    	}
    	
    	
     }
    
    public long getCount(String queryString, Object[] params, String keyName) throws SQLException {
    	ResultSetHandler h = new KeyedHandler(keyName);
    	Map resultMap = (Map) runner.query(queryString, h, params);
    	long count = 0;
    	for(Object key : resultMap.keySet()) {
    		count = (Long) key;
    		break;
    	}
    	return count;
    }
    

    public long getCount(String queryString, Object[] params) throws SQLException {
    	return getCount(queryString, params, "count");
    }
    
    
	public boolean isMidExists(String mid) throws SQLException {
		String queryString = "SELECT count(1) AS count FROM "+Constants.JL_MEMBER+" WHERE pid=?";
    	Object[] params = {mid};
    	LOG.info("查询字符串="+queryString);
    	LOG.info("查询参数="+GsonUtils.toJson(params));
    	long count = getCount(queryString, params);
    	LOG.info("在jl_member表中找到"+count+"条记录");
    	if(count >= 1) {
    		return true;
    	}
    	return false;
	}
}
