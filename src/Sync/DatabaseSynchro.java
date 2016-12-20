package Sync;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * 数据库同步
 * @author c_chengdong
 *
 */
public class DatabaseSynchro extends TimerTask{
	private static Log log=LogFactory.getLog(DatabaseSynchro.class);
	@Override
	public void run() {
//		 String[] tablearry={"community_original","community_original_in","community_dianping","community_bbs","community_baidu_map","community_baidu_info","community_xiangqing"};
		  String[] tablearry={"community_dianping"};
		  //每一个表启动一个线程
		  for(int i=0;i<tablearry.length;i++){
			  log.info("启动 "+tablearry[i]+" 线程");
			  SynchroThread_database scthread=new SynchroThread_database();
			  scthread.setTablename(tablearry[i]);
			  Thread thread=new Thread(scthread,tablearry[i]);
			  thread.start();
			  try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	     }
	}

}
