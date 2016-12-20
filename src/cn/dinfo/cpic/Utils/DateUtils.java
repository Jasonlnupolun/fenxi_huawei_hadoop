package cn.dinfo.cpic.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String DatetoString(String type,Date date){
		SimpleDateFormat format=new SimpleDateFormat(type);
		return format.format(date);
	}
}
