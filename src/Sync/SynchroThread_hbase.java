package Sync;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Bean.Family;
import Bean.Qualifier;
import Bean.Rowkey;
import Bean.Rowkeybean;
import Bean.Table;
import Bean.TableInfo;
import Bean.Tables;
import cn.dinfo.cpic.Utils.Base64;
import cn.dinfo.cpic.Utils.HttpClientUtil;
import cn.dinfo.cpic.Utils.JdbcUtils;
import cn.dinfo.cpic.Utils.PropertyUtil;
import cn.dinfo.cpic.Utils.StringUtils;
import cn.dinfo.cpic.Utils.XMLUtil;
import cn.dinfo.cpic.hbase.interfaces.impl.HBaseInterfaceImpl;
import dbcp.DBHelp;

public class SynchroThread_hbase extends Thread {
	private static Log log = LogFactory.getLog(SynchroThread_hbase.class);
	JdbcUtils jdbcUtils = new JdbcUtils(PropertyUtil.get("Username"),PropertyUtil.get("Password"),PropertyUtil.get("DatabaseIP"),PropertyUtil.get("Port"),PropertyUtil.get("DatabaseName"));
	private String tablename;

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	 private HBaseInterfaceImpl hBaseInterfaceImpl = new HBaseInterfaceImpl();
//	private HBaseInterfaceImpl hBaseInterfaceImpl = null;

	@Override
	public void run() {
		try {
			synchro();
		} catch (Exception e) {
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
			log.info(Base64.getFromBase64(tablename)+"出现异常");
			log.error(e.getMessage());
			jdbcUtils.getConnection();
			String sql = "insert into sysexceptioninfo(exceptionname,exceptiontime,exceptioninfo) values(?,?,?)";
			List<Object> params = new ArrayList<Object>();
			params.add("数据交付异常");
			params.add(new Date());
			params.add(tablename + "同步出错：" + e.getMessage());
			jdbcUtils.insert(sql, params);
			e.printStackTrace();
		}
		finally{
			jdbcUtils.releaseConn();
		}
	}

	@SuppressWarnings({ "unused"})
	public void synchro() throws IOException {
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
		int i = 0;
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
//				log.info("++++++++++++++++++++++++++++++++++"+ Base64.getFromBase64(tablename) + "开始同步" + i + "到"+ (i + number) + "条数据");
				log.info("访问数据查询数据接口.....");
				log.info("URL:"+url);
				String str = HttpClientUtil.docProcVsm(url, "");
				log.info("访问数据查询接口成功.....");
//				 log.info("读取文件.....");
//				 String str="";
//				 if(readcount==0){
//					 str =FileHelper.readFile("C:\\Users\\cd\\Desktop\\接口查询结果\\"+Base64.getFromBase64(tablename)+".xml");
//					 str=Base64.getFromBase64(str);
//					 str=str.substring(0,str.indexOf("</tables>")+9);
//					 System.out.println(str);
//				 }
//				log.info(str);
				Tables tables = new Tables();
				Class<Tables> c = (Class<Tables>) Tables.class;// 用于将xml转换成bean
//				log.info(str);
				if(str!=null&&str.trim().length()>0){
					tables = XMLUtil.converyToJavaBean(str, c);
				}else {
					tables=null;
				}
				String deltname = null;// 要删除的表名
				String rowkeys = null;// 所有rowkeys name的xml格式
				String rowurl = "";// 所有rowkeys name处理后的xml格式
				List<Table> tablelist = new ArrayList<Table>();
				Table tmptbale = null;
				/***/
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
				boolean del = false;// 如果不是INDEX结尾的索引表则修改为true
				loop1: for (int j = 0; j < tablelist.size(); j++) {
					Table t = tablelist.get(j);
					String tname = t.getTname();
					/***/
					if (tname.endsWith("index")) {
						deltname = tname;
						del = true;
					}
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
						String err = StringUtils.getStackTrace(e);
					}
					Rowkeybean rkbean = new Rowkeybean();
					Map<String, String> rowmap = new HashMap<String, String>();// 储存Rowkey的name
					Map<String, String> errmap = new HashMap<String, String>();
					List<String> rklist = new ArrayList<String>();// 储存Rowkey的name
					if (null != t.getRowkeyList()&& t.getRowkeyList().size() > 0) {
						log.info("保存数据,"+tname+"共"+t.getRowkeyList().size());
						count += t.getRowkeyList().size();
						for (Rowkey r : t.getRowkeyList()) {
							int rownum=0;
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
						if (tname.endsWith("index")) {
							for (String key : rowmap.keySet()) {
								if (null == errmap.get(key)) {
									rklist.add(key);
								}
							}
							rkbean.setRowkeylist(rklist);
							rowkeys = XMLUtil.convertToXml(rkbean).replaceAll(
									" ", "%20");
							rowkeys = rowkeys.replaceAll("<", "&lt;");
							rowkeys = rowkeys.replaceAll(">", "&gt;");
							rowkeys = rowkeys.replaceAll("\"", "'");
							String[] row = rowkeys.split("\n");
							for (int k = 0; k < row.length; k++) {
								rowurl = rowurl + row[k];
							}
						}
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
				try {
//					Thread.sleep(1*60*1000);
//					for(int tt=180;tt>1;tt--){
//						Thread.sleep(1000);
//						log.info(tt);
//					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		//更新数据状态
		log.info("更新数据状态");
		//统计采集数据
		countCollectData(counturl, tablename_cn);
		//统计同步数据
//		jdbcUtils.getConnection();
		String quertmax = "select MAX(totaldata) from datastate where type=2 and datatype='"+tablename_cn+"'";
//		List<Object> queryparams = new ArrayList<Object>();
//		queryparams.add(tablename_cn);
		int oldTotal = 0;
		try {
			Map<String, Object> map = DBHelp.query(quertmax, null);
			oldTotal = (map.get("MAX(totaldata)") == null || org.apache.commons.lang.StringUtils
					.isBlank(map.get("MAX(totaldata)").toString())) ? 0
					: Integer.parseInt(map.get("MAX(totaldata)").toString());
			log.info("统计" + tablename_cn + "的同步数据");
			SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String nowdate=format.format(new Date());
			String sql = "insert into datastate (LastUpdateTime,TotalData,DayData,type,datatype) values ('"+nowdate+"',"+(oldTotal + count)+","+syncount+",2,'"+tablename_cn+"')";
			log.info("执行"+tablename_cn+"同步数据");
			DBHelp.executeSQL(sql, null);
//			List<Object> params = new ArrayList<Object>();
//			params.add(new Date());
//			params.add(oldTotal + count);
//			params.add(count);
//			params.add(2);
//			params.add(tablename_cn);
//			jdbcUtils.insert(sql, params);
		} catch (Exception e) {
			log.error("更新数据状态出错");
			log.info(e.getMessage());
		}finally{
//			jdbcUtils.releaseConn();
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
//		List<Object> params = new ArrayList<Object>();
//		params.add(new Date());
//		params.add(totalcount);
//		params.add(daycount);
//		params.add(1);
//		params.add(tablename_cn);
//		try {
//			jdbcUtils.insert(sql, params);
//		} catch (Exception e) {
//			log.error(e.getMessage());
//		}finally{
//			jdbcUtils.releaseConn();
//		}
		log.info(tablename_cn+"采集数据统计完毕，今日"+daycount+"，全部"+totalcount);
	}
}
