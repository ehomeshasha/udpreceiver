package com.jiuletech;

import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest  {
	@Test
	public void convert() {
		String cols =  "mid,userid,spo2,heartrate,breath,skin,healthindex,ecg_ppt,ecg_mrt,ecg_p1,ecg_p0,ecg_b,ecg_a,ecg_area,activity,sleep_total_time,sleep_effective_time,sleep_rover_time,longitude,latitude,gsp_type,type,wear_type,time,year,month,day";
		System.out.println(cols.split(",").length);
		String wenhao = "";
		for(String col : cols.split(",")) {
			wenhao += ",?";
		}
		System.out.println(wenhao);
	}
}