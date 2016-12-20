package dbcp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowdate=format.format(new Date());
		String sql = "insert into datastate (LastUpdateTime,TotalData,DayData,type,datatype) values ('"+nowdate+"',11,634,1,'商圈')";
		DBHelp.executeSQL(sql, null);
	}

}
