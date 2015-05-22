package com.jiuletech.util;

import java.math.BigDecimal;

public class NumericUtil {
	
	
	
	public static String from16to10(String num) {
		return String.valueOf(Integer.parseInt(num, 16));
	}
	
	
	public static String formatGeoString(double a, double b) {
		BigDecimal bigDecimal = new BigDecimal(a + (b/60));  
		double geo = bigDecimal.setScale(6,   BigDecimal.ROUND_HALF_UP).doubleValue();
		return String.valueOf(geo);
	}

}
