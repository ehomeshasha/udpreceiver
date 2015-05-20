package com.jiuletech.common;

import java.util.ArrayList;
import java.util.List;

public class MsgParser {
	
	private String body = null;
	
	public MsgParser(String body) {
		this.body = body;
	}
	
	public MsgBean parse() throws ParserException {
		
		
		
		int tmp = 0;
		
		String packetHead = body.substring(tmp, tmp+4);
		tmp = tmp+4;
		if(!packetHead.equals("55DD")) {
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
		
		
		
		int uploadCount = Integer.parseInt(body.substring(tmp, tmp+2));
		tmp = tmp+2;
		
		String mid = body.substring(tmp, tmp+13);
		tmp = tmp+13;
		String userid = body.substring(tmp, tmp+8);
		tmp = tmp+8;
		
		List<GroupDataBean> groupDataBeanList = new ArrayList<GroupDataBean>();

		for(int i=0;i<uploadCount;i++) {
			GroupDataBean groupDataBean = new GroupDataBean();
			String spo2 = body.substring(tmp, tmp+2);
			tmp = tmp+2;
			String heartrate = body.substring(tmp, tmp+2);
			tmp = tmp+2;
			String breath = body.substring(tmp, tmp+2);
			tmp = tmp+2;
			String skin = null;
			tmp = tmp+8;
			String healthindex = body.substring(tmp, tmp+2);
			tmp = tmp+2;
			String ECG_PPT = body.substring(tmp, tmp+4);
			tmp = tmp+4;
			String ECG_MRT = body.substring(tmp, tmp+4);
			tmp = tmp+4;
			String ECG_P1 = body.substring(tmp, tmp+4);
			tmp = tmp+4;
			String ECG_P0 = body.substring(tmp, tmp+4);
			tmp = tmp+4;
			String ECG_B = body.substring(tmp, tmp+4);
			tmp = tmp+4;
			String ECG_A = body.substring(tmp, tmp+4);
			tmp = tmp+4;
			String ECG_AREA = body.substring(tmp, tmp+8);
			tmp = tmp+8;
			String time = body.substring(tmp, tmp+8);
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
		
		String activity = body.substring(tmp, tmp+4);
		tmp = tmp+4;
		String sleep_total_time = body.substring(tmp, tmp+4);
		tmp = tmp+4;
		String sleep_effective_time = body.substring(tmp, tmp+4);
		tmp = tmp+4;
		String sleep_rove_time = body.substring(tmp, tmp+2);
		tmp = tmp+2;
		String longitude = body.substring(tmp, tmp+10);
		tmp = tmp+10;
		String latitude = body.substring(tmp, tmp+9);
		tmp = tmp+9;
		
		MsgBean msgBean = new MsgBean();
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
