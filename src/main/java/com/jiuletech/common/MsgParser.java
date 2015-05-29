package com.jiuletech.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import com.jiuletech.mysql.DbHelper;
import com.jiuletech.mysql.MySQLRunner;
import com.jiuletech.util.NumericUtil;

public class MsgParser {
	
	private MySQLRunner mysqlRunner = new MySQLRunner(DbHelper.getQueryRunner());
	
	private String body = null;
	
	public MsgParser(String body) {
		this.body = body;
	}
	
	public MsgBean parse() throws ParserException, SQLException {
		
		String insertTableName = Constants.JL_DATA;
		
		boolean isUpdateWarnTable = false;
		
		int tmp = 0;
		
		String packetHead = body.substring(tmp, tmp+4);
		tmp = tmp+4;
		if(!packetHead.equals(Constants.PACKET_HEAD)) {
			throw new ParserException("包头不正确");
		}
		
		String updateFlagA = body.substring(tmp, tmp+1);
		tmp = tmp+1;
		String updateFlagB = body.substring(tmp, tmp+1);
		tmp = tmp+1;
		String gpsFlagA = body.substring(tmp, tmp+1);
		tmp = tmp+1;
		String gpsFlagB = body.substring(tmp, tmp+1);
		tmp = tmp+1;
		//如果低位为1那么就是报警类型要更新warn表
		if(gpsFlagB.equals("1")) {
			isUpdateWarnTable = true;
		}
		
		if((gpsFlagA+gpsFlagB).equals(Constants.ADJUST_TIME_FLAG)) {
			//表明上传的为时间校准数据
			insertTableName = Constants.JL_DATA_RESET1;
		}
		
		int uploadCount = Integer.parseInt(body.substring(tmp, tmp+2), 16);
		tmp = tmp+2;
		
		String mid = body.substring(tmp, tmp+13);
		if(!mysqlRunner.isMidExists(mid)) {
			mid = body.substring(tmp, tmp+20);
			if(!mysqlRunner.isMidExists(mid)) {
				//13位和20位都找不到，就丢弃不下发数据
				throw new ParserException("13位和20位都找不到，就丢弃不下发数据");
			} else {
				//20位能够找到
				tmp = tmp + 20;
			}
		} else {
			//13位能够找到
			tmp = tmp+13;
		}
		
		
		String userid = String.valueOf(Integer.parseInt(body.substring(tmp, tmp+8)));
		tmp = tmp+8;
		
		List<GroupDataBean> groupDataBeanList = new ArrayList<GroupDataBean>();

		for(int i=0;i<uploadCount;i++) {
			GroupDataBean groupDataBean = new GroupDataBean();
			String spo2 = NumericUtil.from16to10(body.substring(tmp, tmp+2));
			tmp = tmp+2;
			String heartrate = NumericUtil.from16to10(body.substring(tmp, tmp+2));
			tmp = tmp+2;
			String breath = NumericUtil.from16to10(body.substring(tmp, tmp+2));
			tmp = tmp+2;
			//skin = sqrt(shibu^2 + xubu^2)
			double shibu = Math.pow(Integer.parseInt(body.substring(tmp, tmp+4), 16), 2);
			tmp = tmp+4;
			double xubu = Math.pow(Integer.parseInt(body.substring(tmp, tmp+4), 16), 2);
			String skin = String.valueOf(Math.sqrt(shibu + xubu));
			tmp = tmp+4;
			String healthindex = NumericUtil.from16to10(body.substring(tmp, tmp+2));
			tmp = tmp+2;
			String ECG_PPT = NumericUtil.from16to10(body.substring(tmp, tmp+4));
			tmp = tmp+4;
			String ECG_MRT = NumericUtil.from16to10(body.substring(tmp, tmp+4));
			tmp = tmp+4;
			String ECG_P1 = NumericUtil.from16to10(body.substring(tmp, tmp+4));
			tmp = tmp+4;
			String ECG_P0 = NumericUtil.from16to10(body.substring(tmp, tmp+4));
			tmp = tmp+4;
			String ECG_B = NumericUtil.from16to10(body.substring(tmp, tmp+4));
			tmp = tmp+4;
			String ECG_A = null;
			String ECG_AREA = null;
			if((gpsFlagA+gpsFlagB).equals(Constants.ADJUST_TIME_FLAG)) {
				ECG_A = body.substring(tmp, tmp+4);
				tmp = tmp+4;
				ECG_AREA = body.substring(tmp, tmp+8);
				tmp = tmp+8;
			} else {
				ECG_A = NumericUtil.from16to10(body.substring(tmp, tmp+4));
				tmp = tmp+4;
				ECG_AREA = NumericUtil.from16to10(body.substring(tmp, tmp+8));
				tmp = tmp+8;
			}
			String time = NumericUtil.from16to10(body.substring(tmp, tmp+8));
			tmp = tmp+8;
			
			
			groupDataBean.setSpo2(spo2);
			groupDataBean.setHeartrate(heartrate);
			groupDataBean.setBreath(breath);
			groupDataBean.setSkin(skin);
			groupDataBean.setHealthindex(healthindex);
			groupDataBean.setECG_PPT(ECG_PPT);
			groupDataBean.setECG_MRT(ECG_MRT);
			groupDataBean.setECG_P1(ECG_P1);
			groupDataBean.setECG_P0(ECG_P0);
			groupDataBean.setECG_B(ECG_B);
			groupDataBean.setECG_A(ECG_A);
			groupDataBean.setECG_AREA(ECG_AREA);
			groupDataBean.setTime(time);
			
			
			groupDataBeanList.add(groupDataBean);
		}
		
		String activity = NumericUtil.from16to10(body.substring(tmp, tmp+4));
		tmp = tmp+4;
		String sleep_total_time = NumericUtil.from16to10(body.substring(tmp, tmp+4));
		tmp = tmp+4;
		String sleep_effective_time = NumericUtil.from16to10(body.substring(tmp, tmp+4));
		tmp = tmp+4;
		String sleep_rove_time = NumericUtil.from16to10(body.substring(tmp, tmp+2));
		tmp = tmp+2;
		
		String longitude = null;
		String latitude = null;
		if((gpsFlagA+gpsFlagB).equals(Constants.ADJUST_TIME_FLAG)) {
			longitude = "0";
			latitude = "0";
		} else {
		
			//11423.23563030.8330
			String geoString = body.substring(tmp);
			//System.out.println(geoString);
			Matcher geoMatcher = Constants.geoPattern.matcher(geoString);
			if(geoMatcher.find()) {
				longitude = NumericUtil.formatGeoString(Double.parseDouble(geoMatcher.group(1)), 
						Double.parseDouble(geoMatcher.group(2)));
				latitude = NumericUtil.formatGeoString(Double.parseDouble(geoMatcher.group(3)), 
						Double.parseDouble(geoMatcher.group(4)));		
			} else {
				throw new ParserException("Cannot parse geo location");
			}
		}
		
		MsgBean msgBean = new MsgBean();
		msgBean.setInsertTableName(insertTableName);
		msgBean.setUpdateWarnTable(isUpdateWarnTable);
		msgBean.setUpdateFlagA(updateFlagA);
		msgBean.setUpdateFlagB(updateFlagB);
		msgBean.setGpsFlagA(gpsFlagA);
		msgBean.setGpsFlagB(gpsFlagB);
		msgBean.setUploadCount(uploadCount);
		msgBean.setMid(mid);
		msgBean.setUserid(userid);
		msgBean.setGroupDataBeanList(groupDataBeanList);
		msgBean.setActivity(activity);
		msgBean.setSleep_total_time(sleep_total_time);
		msgBean.setSleep_effective_time(sleep_effective_time);
		msgBean.setSleep_rover_time(sleep_rove_time);
		msgBean.setLongitude(longitude);
		msgBean.setLatitude(latitude);
		
		
		
		
		
		
		
		
		
		
		return msgBean;
		
		
	}
	

}
