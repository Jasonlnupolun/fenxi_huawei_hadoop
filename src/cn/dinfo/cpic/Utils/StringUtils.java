package cn.dinfo.cpic.Utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

public class StringUtils {
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	public static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
}
