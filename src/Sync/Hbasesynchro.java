package Sync;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.dinfo.cpic.Utils.XMLUtil;

/**
 * hbase同步
 * @author c_chengdong
 *
 */
public class Hbasesynchro extends TimerTask{
	XMLUtil xmlutil=new XMLUtil();
	static Timer timer=new Timer();
	private static Log log=LogFactory.getLog(Hbasesynchro.class);
    public static void main(String[] args) throws IOException{
//    	System.out.println("开始");
    	log.info("开始");
    	new Hbasesynchro().run();
//		  Calendar calendar = Calendar.getInstance();  
//		  calendar.set(Calendar.HOUR_OF_DAY, 10); // 控制时  
//		  calendar.set(Calendar.MINUTE, 9); // 控制分  
//		  calendar.set(Calendar.SECOND, 0); // 控制秒  
//		  Date time = calendar.getTime(); // 得出执行任务的时间
//	      timer.scheduleAtFixedRate(new Hbasesynchro(), time, 30*60*1000);
	      //终止任务
//	      timer.cancel();
//	      timer=new Timer();
  }
	    @Override
	    public void run(){
			  String[] tablearry={"zx_squrl","zx_hotel_manager","zx_hotel_dianping","zx_food_manager","zx_food_dianping"};
//	    	String[] tablearry={"zx_squrl"};
			  //每一个表启动一个线程
			  for(int i=0;i<tablearry.length;i++){
				  System.out.println("启动 "+tablearry[i]+" 线程");
				  SynchroThread_hbase scthread=new SynchroThread_hbase();
				  scthread.setTablename(tablearry[i]);
				  Thread thread=new Thread(scthread,tablearry[i]);
				  thread.start();
	         }
	     }
	    
}
