package Sync.job;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Sync.GlobelFlag;
import Sync.SynchroThread_database;

public class DatabaseJob extends TimerTask {
	private static Log log=LogFactory.getLog(DatabaseJob.class);
	@Override
	public void run() {
		log.info("database同步开始");
		String[] dbTable={"community_bbs","community_original_in","community_original","community_baidu_info","community_dianping","community_baidu_map","community_xiangqing"};
		  //每一个表启动一个线程
		  for(int i=0;i<dbTable.length;i++){
			  log.info("启动 "+dbTable[i]+" 线程");
			  switch (dbTable[i]) {
				case "community_original":
					if(GlobelFlag.isCommunityOriginal()){
						log.info(dbTable[i]+"线程正在运行");
						continue;
					}
					break;
				case "community_baidu_info":
					if(GlobelFlag.isCommunityBaiduInfo()){
						log.info(dbTable[i]+"线程正在运行");
						continue;
					}
					break;
				case "community_xiangqing":
					if(GlobelFlag.isCommunityXiangqing()){
						log.info(dbTable[i]+"线程正在运行");
						continue;
					}
					break;
				case "community_baidu_map":
					if(GlobelFlag.isCommunityBaiduMap()){
						log.info(dbTable[i]+"线程正在运行");
						continue;
					}
					break;
				case "community_dianping":
					if(GlobelFlag.isCommunityDianping()){
						log.info(dbTable[i]+"线程正在运行");
						continue;
					}
					break;
				case "community_original_in":
					if(GlobelFlag.isCommunityOriginalIn()){
						log.info(dbTable[i]+"线程正在运行");
						continue;
					}
					break;
				case "community_bbs":
					if(GlobelFlag.isCommunityBbs()){
						log.info(dbTable[i]+"线程正在运行");
						continue;
					}
					break;
			}
			  SynchroThread_database scthread=new SynchroThread_database();
			  scthread.setTablename(dbTable[i]);
			  Thread thread=new Thread(scthread,dbTable[i]);
			  thread.start();
			  try {
				Thread.sleep(3000);
			} catch (Exception e) {
			}
	     }
	}

}
