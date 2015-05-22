package com.jiuletech;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


/**
 * Unit test for simple App.
 */
public class AppTest  {
//	@Test
//	public void convert() {
//		String cols =  "mid,userid,spo2,heartrate,breath,skin,healthindex,ecg_ppt,ecg_mrt,ecg_p1,ecg_p0,ecg_b,ecg_a,ecg_area,activity,sleep_total_time,sleep_effective_time,sleep_rover_time,longitude,latitude,gsp_type,type,wear_type,time,year,month,day";
//		System.out.println(cols.split(",").length);
//		String wenhao = "";
//		for(String col : cols.split(",")) {
//			wenhao += ",?";
//		}
//		System.out.println(wenhao);
//	}
	
	
	@Test
	public void testReg() {
		Pattern geoPattern = Pattern.compile("^([0-9]+)([0-9]{2}\\.[0-9]{4})([0-9]+)([0-9]{2}\\.[0-9]{4})$");
		//Pattern geoPattern = Pattern.compile("\\d+");
		String geoString = "11423.23563030.8330";
		System.out.println(geoString);
		Matcher m = geoPattern.matcher(geoString);
		if(m.find()) { 
			System.out.println(m.group(0));
			System.out.println(m.group(1));
			System.out.println(m.group(2));
			System.out.println(m.group(3));
			System.out.println(m.group(4));
		} 
//		System.out.println(geoMatcher.group(0));
//		System.out.println(geoMatcher.group(1));
//		System.out.println(geoMatcher.group(2));
//		System.out.println(geoMatcher.group(3));
//		System.out.println(geoMatcher.group(4));
	}
}