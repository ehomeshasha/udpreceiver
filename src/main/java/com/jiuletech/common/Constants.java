package com.jiuletech.common;

import java.util.regex.Pattern;

public class Constants {
	
	
	public static final Pattern geoPattern = Pattern.compile("^([0-9]+)([0-9]{2}\\.[0-9]{4})([0-9]+)([0-9]{2}\\.[0-9]{4})$");
	
	public static final String PACKET_HEAD = "55DD";
	
	public static final String JL_DATA = "jl_data";
	
	public static final String JL_DATA_WARN = "jl_data_warn";
	
	public static final String JL_DATA_RESET1 = "jl_data_reset1";
	
	public static final String ADJUST_TIME_FLAG = "07";

}
