package cn.dinfo.cpic.Utils;

import java.io.File;

import Bean.Propert;


public class PropertiesUtils{
	private static final String DEFAULT_PROPERTIES=System.getProperty("user.dir")+File.separator+"conf"+File.separator+"config.properties";
	public static  String get(String propPath,String key) {
		return getPropert(propPath).get(key);
	}
	public static  String get(String key) {
		return getPropert(DEFAULT_PROPERTIES).get(key);
	}
	private static Propert getPropert(String propPath){
		Propert prop=new Propert(propPath);
		return prop;
	}
}
