package Sync;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Bean.Family;
import Bean.Qualifier;
import Bean.Rowkey;
import Bean.Table;
import Bean.TableInfo;
import Bean.Tables;
import Sync.job.HbaseJob;
import cn.dinfo.cpic.Utils.Base64;
import cn.dinfo.cpic.Utils.HttpClientUtil;
import cn.dinfo.cpic.Utils.PropertyUtil;
import cn.dinfo.cpic.Utils.XMLUtil;
import cn.dinfo.cpic.hbase.interfaces.impl.HBaseInterfaceImpl;
import dbcp.DBHelp;
/**
 * 同步启动
 * @author c_chengdong
 *
 */
public class StartSynchro extends TimerTask{
	private static Log log=LogFactory.getLog(Hbasesynchro.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//    	new StartSynchro().run();
//		  Calendar calendar = Calendar.getInstance();  
//		  calendar.set(Calendar.HOUR_OF_DAY, 10); // 控制时  
//		  calendar.set(Calendar.MINUTE, 9); // 控制分  
//		  calendar.set(Calendar.SECOND, 0); // 控制秒  
//		  Date time = calendar.getTime(); // 得出执行任务的时间
		 try {
//			  Timer timer=new Timer();
//			  String strtime=PropertyUtil.get("hbasetime");
//			  int time=0;
//			  if(strtime!=null&&StringUtils.isNotBlank(strtime)){
//				  time=Integer.parseInt(strtime);
//				  timer.scheduleAtFixedRate(new HbaseJob(), new Date(), time*60*1000);
//			  }else {
//				  log.error("定时时间不正确");
//			  }
			  new HbaseJob().run();
		 }catch (Exception e) {
			 log.error("hbase出错");
		 }
//		 try {
//			  Timer timer1=new Timer();
//		      timer1.scheduleAtFixedRate(new DatabaseJob(), new Date(), 5*60*1000);
//		 } catch (Exception e) {
//			log.error("数据库出错");
//		 }
	      //终止任务
//	      timer.cancel();
//	      timer=new Timer();
	}

	@Override
	public void run() {
		  log.info("启动成功");
		  log.info("database同步开始");
			//database
			String[] dbTable={"community_bbs","community_original_in","community_original","community_baidu_info","community_dianping","community_baidu_map","community_xiangqing"};
//			String[] dbTable={"community_xiangqing"};
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
//		
		
		//habse
		//多线程
//		String[] hbTable={"zx_squrl","zx_hotel_manager","zx_hotel_dianping","zx_food_manager","zx_food_dianping"};
////    	String[] hbTable={"zx_food_dianping"};
//		log.info("hbase同步开始");
//		if(GlobelFlag.isHbase()){
//			log.info("hbase同步正在进行中....");
//		}else{
//			  //每一个表启动一个线程
//			  for(int i=0;i<hbTable.length;i++){
//				  log.info("启动 "+hbTable[i]+" 线程");
//				  switch(hbTable[i]){
//				  	case "zx_squrl":
//				  		if(GlobelFlag.isSqurl()){
//							log.info(hbTable[i]+"线程正在运行");
//							continue;
//						}else {
//							log.info(hbTable[i]+"状态设为true");
//							GlobelFlag.setSqurl(true);
//						}
//				  		break;
//				  	case "zx_hotel_manager":
//				  		if(GlobelFlag.isHotelManager()){
//							log.info(hbTable[i]+"线程正在运行");
//							continue;
//						}else {
//							log.info(hbTable[i]+"状态设为true");
//							GlobelFlag.setHotelManager(true);
//						}
//				  		break;
//				  	case "zx_hotel_dianping":
//				  		if(GlobelFlag.isHotelDianping()){
//							log.info(hbTable[i]+"线程正在运行");
//							continue;
//						}else {
//							log.info(hbTable[i]+"状态设为true");
//							GlobelFlag.setHotelDianping(true);
//						}
//				  		break;
//				  	case "zx_food_manager":
//				  		if(GlobelFlag.isFoodManager()){
//							log.info(hbTable[i]+"线程正在运行");
//							continue;
//						}else {
//							log.info(hbTable[i]+"状态设为true");
//							GlobelFlag.setFoodManager(true);
//						}
//				  		break;
//				  	case "zx_food_dianping":
//				  		if(GlobelFlag.isFoodDianping()){
//							log.info(hbTable[i]+"线程正在运行");
//							continue;
//						}else {
//							log.info(hbTable[i]+"状态设为true");
//							GlobelFlag.setFoodDianping(true);
//						}
//				  		break;
//				  }
//				  try {
////					  if(i==0){
////						  log.info("hbase同步开始");
////						  GlobelFlag.setHbase(true);
////					  }
//					  SynchroThread_hbase scthread=new SynchroThread_hbase();
//					  scthread.setTablename(hbTable[i]);
//					  Thread thread=new Thread(scthread,hbTable[i]);
//					  thread.start();
//					  Thread.sleep(50*1000);
////					  if(i==hbTable.length){
////						  log.info("hbase同步结束");
////						  GlobelFlag.setHbase(false);
////					  }
//				} catch (Exception e) {
//					log.error(hbTable[i]+"同步出错");
//					continue;
//				}
//	         }
//		}
		  
		//顺序跑
		String[] hbTable={"zx_squrl","zx_hotel_manager","zx_hotel_dianping","zx_food_manager","zx_food_dianping"};
////  	String[] hbTable={"zx_food_dianping"};
		log.info("hbase同步开始");
		if(GlobelFlag.isHbase()){
			log.info("hbase同步正在进行中....");
		}else{
			  GlobelFlag.setHbase(true);
			  for(int i=0;i<hbTable.length;i++){
				  String tablename=hbTable[i];
				  try {
					  synchro(tablename);
//					  log.info(tablename);
//					  Thread.sleep(20000);
				  } catch (Exception e) {
						e.printStackTrace();
						continue;
				  }
	         }
			 GlobelFlag.setHbase(false);
		}
	}
	public void synchro(String tablename) throws IOException {
		HBaseInterfaceImpl hBaseInterfaceImpl=new HBaseInterfaceImpl();
		log.info(tablename + "开始");
		int number = (PropertyUtil.get("hbnumber") == null || PropertyUtil
				.get("hbnumber").length() == 0) ? 0 : Integer
				.parseInt(PropertyUtil.get("hbnumber"));
		if (number == 0) {
			log.info("number为0");
			return;
		}
		String[] familys = null;
		List<String> list = new ArrayList<String>();
		String state = Base64.getBase64("0");
		String pageSize = Base64.getBase64(String.valueOf(number));
		tablename = Base64.getBase64(tablename);
		String url = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
				+ "/CpicDataServer/hbaseapp?state=" + state + "&pageSize="
				+ pageSize + "&tableName=" + tablename;
		log.info(url);
		String counturl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
				+ "/CpicDataServer/countapp?tableName=" + tablename;
		int count = 0;// 统计同步数据量
		String tablename_cn = "";
		switch (Base64.getFromBase64(tablename)) {
			case TableInfo.ZX_FOOD_MANAGE:
				tablename_cn = TableInfo.ZX_FOOD_MANAGE_NAME;
				break;
			case TableInfo.ZX_HOTEL_MANAGE:
				tablename_cn = TableInfo.ZX_HOTEL_MANAGE_NAME;
				break;
			case TableInfo.ZX_FOOD_DIANPING:
				tablename_cn = TableInfo.ZX_FOOD_DIANPING_NAME;
				break;
			case TableInfo.ZX_HOTEL_DIANPING:
				tablename_cn = TableInfo.ZX_HOTEL_DIANPING_NAME;
				break;
			case TableInfo.ZX_SQURL:
				tablename_cn = TableInfo.ZX_SQURL_NAME;
				break;
			default:
				break;
		}
		int syncount=0;
		if (hBaseInterfaceImpl != null) {
			loop: while (true) {
				log.info("访问数据查询数据接口.....");
				log.info("URL:"+url);
				String str = HttpClientUtil.docProcVsm(url, "");
				log.info("访问数据查询接口成功.....");
				Tables tables = new Tables();
				Class<Tables> c = (Class<Tables>) Tables.class;// 用于将xml转换成bean
				if(str!=null&&str.trim().length()>0){
					tables = XMLUtil.converyToJavaBean(str, c);
				}else {
					tables=null;
				}
				List<Table> tablelist = new ArrayList<Table>();
				Table tmptbale = null;
				if(tables!=null){
					for (Table t : tables.getTableList()) {
						if(t.getRowkeyList().size()<2){
							log.info("最后一条rowkey，结束循环");
							switch(Base64.getFromBase64(tablename)){
							  	case "zx_squrl":
							  		GlobelFlag.setSqurl(false);
							  		break;
							  	case "zx_hotel_manager":
							  		GlobelFlag.setHotelManager(false);
							  		break;
							  	case "zx_hotel_dianping":
							  		GlobelFlag.setHotelDianping(false);
							  		break;
							  	case "zx_food_manager":
							  		GlobelFlag.setFoodManager(false);
							  		break;
							  	case "zx_food_dianping":
							  		GlobelFlag.setFoodDianping(false);
							  		break;
							}
							break loop;
						}
						if (t.getTname().endsWith("index")) {
							tmptbale = new Table();
							tmptbale = t;
						} else {
							// 非索引表全部加入
							tablelist.add(t);
						}
					}
				}else {
					break loop;
				}
				// 加入最后一个索引表
				if (tmptbale != null) {
					tablelist.add(tmptbale);
				}
				loop1: for (int j = 0; j < tablelist.size(); j++) {
					Table t = tablelist.get(j);
					String tname = t.getTname();
					switch (tname) {
						case TableInfo.ZX_FOOD_MANAGE:
							list = TableInfo.ZX_FOOD_MANAGE_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.ZX_HOTEL_MANAGE:
							list = TableInfo.ZX_HOTEL_MANAGE_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.ZX_FOOD_DIANPING:
							list = TableInfo.ZX_FOOD_DIANPING_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.ZX_HOTEL_DIANPING:
							list = TableInfo.ZX_HOTEL_DIANPING_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.ZX_FOOD_MANAGE_INDEX:
							list = TableInfo.ZX_FOOD_MANAGE_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.ZX_HOTEL_MANAGE_INDEX:
							list = TableInfo.ZX_HOTEL_MANAGE_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.ZX_FOOD_DIANPING_INDEX:
							list = TableInfo.ZX_FOOD_DIANPING_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.ZX_HOTEL_DIANPING_INDEX:
							list = TableInfo.ZX_HOTEL_DIANPING_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.ZX_SQURL:
							list = TableInfo.ZX_SQURL_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.ZX_SQURL_INDEX:
							list = TableInfo.ZX_SQURL_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
					}
					try {
						log.info("创建表"+tablename_cn);
							hBaseInterfaceImpl.createTable(tname, familys);
						log.info("创建表"+tablename_cn+"成功");
					} catch (Exception e) {
					}
					Map<String, String> rowmap = new HashMap<String, String>();// 储存Rowkey的name
					if (null != t.getRowkeyList()&& t.getRowkeyList().size() > 0) {
						log.info("保存数据,"+tname+"共"+t.getRowkeyList().size());
						count += t.getRowkeyList().size();
						for (Rowkey r : t.getRowkeyList()) {
							String rowkey = r.getRname();
							if (tname.endsWith("index")) {
								if (null == rowmap.get(r.getRname())) {
									rowmap.put(r.getRname(), r.getRname());
								}
							}
							if (null != r.getFamilyList()&& r.getFamilyList().size() > 0) {
								for (Family f : r.getFamilyList()) {
									String fname = f.getFname();
									if (null != f.getQualifierList()&& f.getQualifierList().size() > 0) {
										for (Qualifier q : f.getQualifierList()) {
											String qname = q.getQname();
											String value = q.getValueStr();
											hBaseInterfaceImpl.put(tname, fname, rowkey,qname, value);
										}
									}
								}
							}
						}
						hBaseInterfaceImpl.putrest(tablename);
					} else {
						log.info(tablename + "同步结束......");
						break loop;
					}
					log.info("++++++++++++++++++++++++++++++++++" + tname
							+ "已同步" + count + "条数据");
					if(!tname.endsWith("index")){
						syncount+=count;
					}
					count=0;
				}
			}
		}
		//更新数据状态
		log.info("更新数据状态");
		//统计采集数据
		countCollectData(counturl, tablename_cn);
		//统计同步数据
		String quertmax = "select MAX(totaldata) from datastate where type=2 and datatype='"+tablename_cn+"'";
		int oldTotal = 0;
		try {
			Map<String, Object> map = DBHelp.query(quertmax, null);
			oldTotal = (map.get("MAX(totaldata)") == null || org.apache.commons.lang.StringUtils
					.isBlank(map.get("MAX(totaldata)").toString())) ? 0
					: Integer.parseInt(map.get("MAX(totaldata)").toString());
			log.info("统计" + tablename_cn + "的同步数据");
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowdate=format.format(new Date());
			String sql = "insert into datastate (LastUpdateTime,TotalData,DayData,type,datatype) values ('"+nowdate+"',"+(oldTotal + syncount)+","+syncount+",2,'"+tablename_cn+"')";
			log.info("执行"+tablename_cn+"同步数据");
			DBHelp.executeSQL(sql, null);
		} catch (Exception e) {
			log.error("更新数据状态出错");
			log.info(e.getMessage());
		}finally{
		}
		log.info(tablename_cn + "的同步数据统计完毕，共" + syncount + "条信息");
		switch(tablename){
		  	case "zx_squrl":
		  		log.error("zx_squrl出现异常");
		  		GlobelFlag.setSqurl(false);
		  		break;
		  	case "zx_hotel_manager":
		  		log.error("zx_hotel_manager出现异常");
		  		GlobelFlag.setHotelManager(false);
		  		break;
		  	case "zx_hotel_dianping":
		  		log.error("zx_hotel_dianping出现异常");
		  		GlobelFlag.setHotelDianping(false);
		  		break;
		  	case "zx_food_manager":
		  		log.error("zx_food_manager出现异常");
		  		GlobelFlag.setFoodManager(false);
		  		break;
		  	case "zx_food_dianping":
		  		log.error("zx_food_dianping出现异常");
		  		GlobelFlag.setFoodDianping(false);
		  		break;
		}
	}

	/**
	 * 统计采集数据
	 * 
	 * @param counturl
	 * @param tablename_cn
	 * @author c_chengdong
	 */
	public void countCollectData(String counturl, String tablename_cn) {
		log.info("统计"+tablename_cn+"采集数量.....");
		String countStr = HttpClientUtil.docProcVsm(counturl, "");
		log.info("访问数据统计接口成功.....");
		countStr = countStr.substring(countStr.indexOf("collectcount") + 13,
				countStr.indexOf("endcollectcount"));
		String[] countarray = countStr.split(",");
		int totalcount = Integer.parseInt((countarray[0]==null||countarray[0].trim().length()==0)?"0":countarray[0]);
		int daycount = Integer.parseInt((countarray[1]==null||countarray[1].trim().length()==0)?"0":countarray[1]);
		log.info(tablename_cn+"totalcount:"+totalcount);
		log.info(tablename_cn+"daycount:"+daycount);
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowdate=format.format(new Date());
		String sql = "insert into datastate (LastUpdateTime,TotalData,DayData,type,datatype) values ('"+nowdate+"',"+totalcount+","+daycount+",1,'"+tablename_cn+"')";
		DBHelp.executeSQL(sql, null);
		log.info("执行"+tablename_cn+"采集统计");
		log.info(tablename_cn+"采集数据统计完毕，今日"+daycount+"，全部"+totalcount);
	}
}
