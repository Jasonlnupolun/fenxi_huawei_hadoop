package Sync.job;

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
import Sync.GlobelFlag;
import cn.dinfo.cpic.Utils.Base64;
import cn.dinfo.cpic.Utils.HttpClientUtil;
import cn.dinfo.cpic.Utils.PropertyUtil;
import cn.dinfo.cpic.Utils.XMLUtil;
import cn.dinfo.cpic.hbase.interfaces.impl.HBaseInterfaceImpl;
import dbcp.DBHelp;

public class HbaseJob extends TimerTask {
	private static Log log=LogFactory.getLog(HbaseJob.class);
	@Override
	public void run() {
//		String[] hbTable={"zx_squrl","zx_hotel_manager","zx_hotel_dianping","zx_food_manager","zx_food_dianping"};
		String tables=PropertyUtil.get("tables");
		String[] hbTable=null;
		if(tables!=null)
			hbTable=tables.split(",");
		log.info("hbase同步开始");
		if(GlobelFlag.isHbase()){
			log.info("hbase同步正在进行中....");
		}else{
			  GlobelFlag.setHbase(true);
			  if(hbTable!=null){
				  for(int i=0;i<hbTable.length;i++){
					  Date start=new Date();
					  String tablename=hbTable[i];
					  try {
						  synchro(tablename);
					  } catch (Exception e) {
							e.printStackTrace();
							continue;
					  }
					  Date end=new Date();
					  log.info(tablename+"耗时"+(end.getTime()-start.getTime())/(1000*60)+"分钟");
		         }
			  }else {
				log.error("获取hbase表名失败");
			  }
			 log.info("hbase同步结束,状态置为false");
//			 GlobelFlag.setHbase(false);
		}
		return;
	}
	@SuppressWarnings("unused")
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
//		log.info(url);
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
			case TableInfo.COMMUNITY_BBS:
				tablename_cn = TableInfo.COMMUNITY_BBS_NAME;
				break;
			case TableInfo.COMMUNITY_DIANPING:
				tablename_cn = TableInfo.COMMUNITY_DIANPING_NAME;
				break;
			case TableInfo.COMMUNITY_XIANGQING:
				tablename_cn = TableInfo.COMMUNITY_XIANGQING_NAME;
				break;
			case TableInfo.COMMUNITY_XIANGQING1:
				tablename_cn = TableInfo.COMMUNITY_XIANGQING_NAME1;
				break;
			case TableInfo.PREMIUM_INCOME:
				tablename_cn = TableInfo.PREMIUM_INCOME_NAME;
				break;
			case TableInfo.STOCK_MARKET:
				tablename_cn = TableInfo.STOCK_MARKET_NAME;
				break;
			case TableInfo.INSURANCE_MANAGE:
				tablename_cn = TableInfo.INSURANCE_MANAGE_NAME;
				break;
			case TableInfo.COMMUNITY_URLS:
				tablename_cn = TableInfo.COMMUNITY_URLS_NAME;
				break;
			case TableInfo.COMMUNITY_BAIDU_MAP:
				tablename_cn = TableInfo.COMMUNITY_BAIDU_MAP_NAME;
				break;
			case TableInfo.HOTNEWS:
				tablename_cn = TableInfo.HOTNEWS_NAME;
				break;
			case TableInfo.COMPETE_SHOP:
				tablename_cn = TableInfo.COMPETE_SHOP_NAME;
				break;
			case TableInfo.KMPRO_DATA:
				tablename_cn = TableInfo.KMPRO_DATA_NAME;
				break;
			default:
				break;
		}
		int syncount=0;
		int countindex=1;
		if (hBaseInterfaceImpl != null) {
			loop: while (true) {
				log.info(tablename_cn+"访问查询数据接口.....");
				log.info("URL:"+url);
				String str = HttpClientUtil.docProcVsm(url, "");
				log.info("访问数据查询接口成功.....");
				Tables tables = new Tables();
				Class<Tables> c = (Class<Tables>) Tables.class;// 用于将xml转换成bean
				if(str!=null&&str.trim().length()>0){
					log.info("数据转换为xml格式");
					tables = XMLUtil.converyToJavaBean(str, c);
				}else {
					log.info("未查询到数据");
					tables=null;
					continue;
				}
				log.info("查询结果");
				if(str.length()>100)
					log.info(str.substring(0,99));
				else
					log.info(str);
				List<Table> tablelist = new ArrayList<Table>();
				Table tmptbale = null;
				if(tables!=null){
					for (Table t : tables.getTableList()) {
						if(t.getRowkeyList().size()<2){
							log.info("最后一条rowkey，结束循环");
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
					log.info(tablename_cn+"tables没有数据");
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
						case TableInfo.COMMUNITY_BBS:
							list = TableInfo.COMMUNITY_BBS_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMMUNITY_DIANPING:
							list = TableInfo.COMMUNITY_DIANPING_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMMUNITY_XIANGQING:
							list = TableInfo.COMMUNITY_XIANGQING_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;	
						case TableInfo.COMMUNITY_XIANGQING1:
							list = TableInfo.COMMUNITY_XIANGQING1_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;	
						case TableInfo.COMMUNITY_BBS_INDEX:
							list = TableInfo.COMMUNITY_BBS_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMMUNITY_DIANPING_INDEX:
							list = TableInfo.COMMUNITY_DIANPING_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMMUNITY_XIANGQING_INDEX:
							list = TableInfo.COMMUNITY_XIANGQING_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;	
						case TableInfo.COMMUNITY_XIANGQING_INDEX1:
							list = TableInfo.COMMUNITY_XIANGQING1_FAMILYNAME_INDEX_LIST;
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
						case TableInfo.PREMIUM_INCOME:
							list = TableInfo.PREMIUM_INCOME_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.PREMIUM_INCOME_INDEX:
							list = TableInfo.PREMIUM_INCOME_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.STOCK_MARKET:
							list = TableInfo.STOCK_MARKET_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.STOCK_MARKET_INDEX:
							list = TableInfo.STOCK_MARKET_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.INSURANCE_MANAGE:
							list = TableInfo.INSURANCE_MANAGE_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.INSURANCE_MANAGE_INDEX:
							list = TableInfo.INSURANCE_MANAGE_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMMUNITY_URLS:
							list = TableInfo.COMMUNITY_URLS_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMMUNITY_URLS_INDEX:
							list = TableInfo.COMMUNITY_URLS_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMMUNITY_BAIDU_MAP:
							list = TableInfo.COMMUNITY_BAIDU_MAP_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMMUNITY_BAIDU_MAP_INDEX:
							list = TableInfo.COMMUNITY_BAIDU_MAP_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.HOTNEWS:
							list = TableInfo.HOTNEWS_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.HOTNEWS_INDEX:
							list = TableInfo.HOTNEWS_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMPETE_SHOP:
							list = TableInfo.COMPETE_SHOP_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.COMPETE_SHOP_INDEX:
							list = TableInfo.COMPETE_SHOP_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.KMPRO_DATA:
							list = TableInfo.KMPRO_DATA_FAMILYNAME_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
						case TableInfo.KMPRO_DATA_INDEX:
							list = TableInfo.KMPRO_DATA_FAMILYNAME_INDEX_LIST;
							familys = new String[list.size()];
							list.toArray(familys);
							break;
					}
					try {
						log.info("创建表"+tablename_cn);
							hBaseInterfaceImpl.createTable(tname, familys);
						log.info("创建表"+tablename_cn+"成功");
					} catch (Exception e) {
						log.error(tablename_cn+"创建表失败");
					}
					int realcount=0;//实际存入数量
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
										realcount++;
									}
								}
							}
						}
						hBaseInterfaceImpl.putrest(tablename);
					} else {
						log.info("rowkeylist为空,"+tablename + "同步结束......");
						break loop;
					}
					if(!tname.endsWith("index")){
						syncount+=count;
						log.info("++++++++++++++" + tname+"第"+(countindex)+"次同步，"
								+ "已同步" + realcount + "条数据");
						countindex++;
						int totalcount=hBaseInterfaceImpl.scan(tname);
						log.info(tname+"共"+totalcount+"条数据");
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
//		switch(tablename){
//		  	case "zx_squrl":
//		  		log.error("zx_squrl出现异常");
//		  		GlobelFlag.setSqurl(false);
//		  		break;
//		  	case "zx_hotel_manager":
//		  		log.error("zx_hotel_manager出现异常");
//		  		GlobelFlag.setHotelManager(false);
//		  		break;
//		  	case "zx_hotel_dianping":
//		  		log.error("zx_hotel_dianping出现异常");
//		  		GlobelFlag.setHotelDianping(false);
//		  		break;
//		  	case "zx_food_manager":
//		  		log.error("zx_food_manager出现异常");
//		  		GlobelFlag.setFoodManager(false);
//		  		break;
//		  	case "zx_food_dianping":
//		  		log.error("zx_food_dianping出现异常");
//		  		GlobelFlag.setFoodDianping(false);
//		  		break;
//		}
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
		log.info(counturl);
		String countStr = HttpClientUtil.docProcVsm(counturl, "");
		log.info("访问数据统计接口成功.....");
		log.info(countStr);
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
		log.info("执行"+tablename_cn+"采集统计");
		DBHelp.executeSQL(sql, null);
		log.info(sql);
		log.info(tablename_cn+"采集数据统计完毕，今日"+daycount+"，全部"+totalcount);
	}

}
