package Sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import Table.communityBaiduInfo;
import Table.communityBaiduMap;
import Table.communityBbs;
import Table.communityDianping;
import Table.communityOriginal;
import Table.communityOriginalIn;
import Table.communityXiangqing;
import cn.dinfo.cpic.Utils.Base64;
import cn.dinfo.cpic.Utils.DateUtils;
import cn.dinfo.cpic.Utils.HttpClientUtil;
import cn.dinfo.cpic.Utils.PropertyUtil;
import dbcp.DBHelp;

public class SynchroThread_database extends Thread {
	private static Log log = LogFactory.getLog(SynchroThread_database.class);
	private String tablename;

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	@Override
	public void run() {
		try {
			switch (tablename) {
				case "community_original":
					GlobelFlag.setCommunityOriginal(true);
					break;
				case "community_baidu_info":
					GlobelFlag.setCommunityBaiduInfo(true);
					break;
				case "community_xiangqing":
					GlobelFlag.setCommunityXiangqing(true);
					break;
				case "community_baidu_map":
					GlobelFlag.setCommunityBaiduMap(true);
					break;
				case "community_dianping":
					GlobelFlag.setCommunityDianping(true);
					break;
				case "community_original_in":
					GlobelFlag.setCommunityOriginalIn(true);
					break;
				case "community_bbs":
					GlobelFlag.setCommunityBbs(true);
					break;
			}
			synchro();
		} catch (IOException e) {
			switch (tablename) {
				case "community_original":
					GlobelFlag.setCommunityOriginal(false);
					break;
				case "community_baidu_info":
					GlobelFlag.setCommunityBaiduInfo(false);
					break;
				case "community_xiangqing":
					GlobelFlag.setCommunityXiangqing(false);
					break;
				case "community_baidu_map":
					GlobelFlag.setCommunityBaiduMap(false);
					break;
				case "community_dianping":
					GlobelFlag.setCommunityDianping(false);
					break;
				case "community_original_in":
					GlobelFlag.setCommunityOriginalIn(false);
					break;
				case "community_bbs":
					GlobelFlag.setCommunityBbs(true);
					break;
			}
			String nowdate=DateUtils.DatetoString("yyyy-MM-dd HH:mm:ss", new Date());
			String sql = "insert into sysexceptioninfo(exceptionname,exceptiontime,exceptioninfo) values('数据交付异常','"+nowdate+"','"+tablename + "同步出错：" + e.getMessage()+"')";
			DBHelp.executeSQL(sql, null);
			log.error(tablename+"出错");
			return;
		}
	}
	
	
	
	
	@SuppressWarnings({ "static-access", "unchecked" })
	public void synchro() throws IOException {
		Date startTime=new Date();
		log.info(tablename + "访问数据接口");
		String s=tablename;
		tablename = Base64.getBase64(tablename);
		String number=PropertyUtil.get("dbnumber");
		if(number==null||StringUtils.isBlank(number)){
			log.error("number参数配置错误");
		}
		String url = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
				+ "/CpicDataServer/hbapp?tableName=" + tablename+"&number="+Base64.getBase64(number);
		log.info(url);
		log.info(s + "获取接口数据");
		String str=HttpClientUtil.docProcVsm(url, null);
		if(str==null||str.length()==0){
			log.info(Base64.getFromBase64(tablename)+"没有查询到相关数据");
			switch (s) {
				case "community_original":
					GlobelFlag.setCommunityOriginal(false);
					break;
				case "community_baidu_info":
					GlobelFlag.setCommunityBaiduInfo(false);
					break;
				case "community_xiangqing":
					GlobelFlag.setCommunityXiangqing(false);
					break;
				case "community_baidu_map":
					GlobelFlag.setCommunityBaiduMap(false);
					break;
				case "community_dianping":
					GlobelFlag.setCommunityDianping(false);
					break;
				case "community_original_in":
					GlobelFlag.setCommunityOriginalIn(false);
					break;
				case "community_bbs":
					GlobelFlag.setCommunityBbs(false);
					break;
			}
			return;
		}
		StringBuffer sb=new StringBuffer();
		String paramIds="";
		switch (s) {
			case "community_original":
				log.info("community_original插入");
				JSONArray jsonArray1=JSONArray.fromObject(str);
				List<communityOriginal> infos=(List<communityOriginal>)jsonArray1.toList(jsonArray1, new communityOriginal(), new JsonConfig());
				if(infos!=null&&infos.size()>0){
					String sql="";
					int result=0;
					int count=1;
					String updateurl="";
					String updateResult="";
					log.info(s+"从数据接口获得"+infos.size()+"条数据");
					for(communityOriginal commu:infos){
						sb.append(commu.getId()+",");
						sql="insert into community_original(id,cid,ci_family_address,ci_mobile,city,province,type,usetype,community_name,synTime) values("+commu.getId()+",'"+commu.getCid()+"','"+commu.getCiFamilyAddress().replaceAll("'", "''")+"','"+commu.getCiMobile()+"','"+commu.getCity()+"','"+commu.getProvince()+"','"+commu.getType()+"',"+commu.getUseType()+",'"+commu.getCommunityName().replaceAll("'", "''")+"','"+DateUtils.DatetoString("yyyy-MM-dd HH:mm:ss", new Date())+"')";
						try{
							result=DBHelp.executeSQL(sql, null);
							if(result==0){
								log.info("保存失败");
								log.info(sql);
							}
						}catch (Exception e){
							log.error(s+"保存出错!!!");
							log.error(sql);
							log.error(e.getMessage());
							continue;
						}
						if(count%100==0){
							try {
								paramIds=sb.toString();
								updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
										+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len=100";
								updateResult=HttpClientUtil.docProcVsm(updateurl, null);
								if(updateResult!=null&&updateResult.equals("error")){
									log.info(s+"更新同步状态失败");
									log.info(updateurl);
								}
								sb=new StringBuffer();
								paramIds="";
								log.info(s+"更新"+count);
							}catch (Exception e) {
								log.error(s+"更新同步状态出错");
								log.info(updateurl);
								continue;
							}
						}
						count++;
					}
					try {
						paramIds=sb.toString();
						updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
								+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len="+(count%100);
						updateResult=HttpClientUtil.docProcVsm(updateurl, null);
						if(updateResult!=null&&updateResult.equals("error")){
							log.info(s+"更新同步状态失败");
							log.info(updateurl);
						}
						sb=new StringBuffer();
						log.info(s+"更新"+count);
						paramIds="";
					}catch (Exception e) {
						switch (s) {
							case "community_original":
								GlobelFlag.setCommunityOriginal(false);
								break;
							case "community_baidu_info":
								GlobelFlag.setCommunityBaiduInfo(false);
								break;
							case "community_xiangqing":
								GlobelFlag.setCommunityXiangqing(false);
								break;
							case "community_baidu_map":
								GlobelFlag.setCommunityBaiduMap(false);
								break;
							case "community_dianping":
								GlobelFlag.setCommunityDianping(false);
								break;
							case "community_original_in":
								GlobelFlag.setCommunityOriginalIn(false);
								break;
							case "community_bbs":
								GlobelFlag.setCommunityBbs(false);
								break;
						}
						log.error(s+"更新同步状态出错");
						log.info(updateurl);
					}
					log.info(s+"同步"+count+"条数据");
				}
				break;
			case "community_baidu_map":
				log.info("community_baidu_map插入");
				JSONArray jsonArray2=JSONArray.fromObject(str);
				List<communityBaiduMap> infos1=(List<communityBaiduMap>)jsonArray2.toList(jsonArray2, new communityBaiduMap(), new JsonConfig());
				if(infos1!=null&&infos1.size()>0){
					String sql="";
					int count=1;
					int result=0;
					String updateurl="";
					String updateResult="";
					List<Object> params=new ArrayList<Object>();
					log.info(s+"从数据接口获得"+infos1.size()+"条数据");
					for(communityBaiduMap map:infos1){
						sb.append(map.getId()+",");
						sql="insert into community_baidu_map(id,merchant_name,merchant_url,merchant_address,merchant_tag,store_introduction,longitude,latitude,distance,get_time,type,oldlatandlng,analy_mark,synTime) values("+map.getId()+",'"+map.getMerchantName().replaceAll("'", "''")+"','"+map.getMerchantUrl().replaceAll("'", "''")+"','"+map.getMerchantAddress().replaceAll("'", "''")+"','"+map.getMerchantTag().replaceAll("'", "''")+"','"+map.getStoreIntroduction()+"','"+map.getLongitude()+"','"+map.getLatitude()+"','"+map.getDistance()+"','"+map.getGetTime()+"','"+map.getType()+"','"+map.getOldlatandlng()+"','"+map.getAnalyMark()+"','"+DateUtils.DatetoString("yyyy-MM-dd HH:mm:ss", new Date())+"')";
						try{
							result=DBHelp.executeSQL(sql, null);
							if(result==0){
								log.info(s+"保存失败");
								log.info(sql);
							}
						}catch (Exception e){
							log.error(s+"保存出错!!!");
							log.error(sql);
							log.error(e.getMessage());
							continue;
						}
						if(count%100==0){
							try {
								paramIds=sb.toString();
								updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
										+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len=100";
								updateResult=HttpClientUtil.docProcVsm(updateurl, null);
								if(updateResult!=null&&updateResult.equals("error")){
									log.info(s+"更新同步状态失败");
									log.info(updateurl);
								}
								sb=new StringBuffer();
								log.info(s+"更新"+count);
								paramIds="";
							}catch (Exception e) {
								log.error(s+"更新同步状态出错");
								log.info(updateurl);
								continue;
							}
						}
						count++;
					}
					try {
						paramIds=sb.toString();
						updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
								+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len="+(count%100);
						updateResult=HttpClientUtil.docProcVsm(updateurl, null);
						if(updateResult!=null&&updateResult.equals("error")){
							log.info(s+"更新同步状态失败");
							log.info(updateurl);
						}
						sb=new StringBuffer();
						log.info(s+"更新"+count);
						paramIds="";
					}catch (Exception e) {
						switch (s) {
							case "community_original":
								GlobelFlag.setCommunityOriginal(false);
								break;
							case "community_baidu_info":
								GlobelFlag.setCommunityBaiduInfo(false);
								break;
							case "community_xiangqing":
								GlobelFlag.setCommunityXiangqing(false);
								break;
							case "community_baidu_map":
								GlobelFlag.setCommunityBaiduMap(false);
								break;
							case "community_dianping":
								GlobelFlag.setCommunityDianping(false);
								break;
							case "community_original_in":
								GlobelFlag.setCommunityOriginalIn(false);
								break;
							case "community_bbs":
								GlobelFlag.setCommunityBbs(false);
								break;
						}
						log.error(s+"更新同步状态出错");
						log.info(updateurl);
					}
					log.info(s+"同步"+count+"条数据");
				}
				break;
				case "community_dianping":
					JSONArray jsonArray3=JSONArray.fromObject(str);
					List<communityDianping> infos2=(List<communityDianping>)jsonArray3.toList(jsonArray3, new communityDianping(), new JsonConfig());
					if(infos2!=null&&infos2.size()>0){
						String sql="";
						int count=1;
						int result=0;
						String updateurl="";
						String updateResult="";
						log.info(s+"从数据接口获得"+infos2.size()+"条数据");
						for(communityDianping dp:infos2){
								sb.append(dp.getId()+",");
							sql="insert into community_dianping(id,text,author,star,ptags,post_time,tdtags,source,gradetags,dptags,znum,hnum,url,analy_mark,synTime) values("+dp.getId()+",'"+dp.getText().replaceAll("'", "''")+"','"+((dp.getAuthor()==null||dp.getAuthor().length()==0)?null:dp.getAuthor().replaceAll("'", "''"))+"','"+dp.getStar()+"','"+dp.getPtags().replaceAll("'", "''")+"','"+((dp.getPostTime()==null||dp.getPostTime().length()==0)?null:dp.getPostTime())+"','"+dp.getTdtags()+"','"+dp.getSource().replaceAll("'", "''")+"','"+dp.getGradetags()+"','"+dp.getDptags()+"','"+dp.getZnum()+"','"+dp.getHnum()+"','"+((dp.getUrl()==null||dp.getUrl().length()==0)?null:dp.getUrl().replaceAll("'", "''"))+"','"+dp.getAnalyMark()+"','"+DateUtils.DatetoString("yyyy-MM-dd HH:mm:ss", new Date())+"')";
							try{
								result=DBHelp.executeSQL(sql, null);
								if(result==0){
									log.info("保存失败");
									log.info(sql);
								}
							}catch (Exception e){
								log.error(s+"保存出错!!!");
								log.error(sql);
								log.error(e.getMessage());
								continue;
							}
							if(count%100==0){
								try {
									paramIds=sb.toString();
									updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
											+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len=100";
									updateResult=HttpClientUtil.docProcVsm(updateurl, null);
									if(updateResult!=null&&updateResult.equals("error")){
										log.info(s+"更新同步状态失败");
										log.info(updateurl);
									}
									sb=new StringBuffer();
									log.info(s+"更新"+count);
									paramIds="";
								}catch (Exception e) {
									log.error(s+"更新同步状态出错");
									log.info(updateurl);
									continue;
								}
							}
							count++;
						}
						try {
							paramIds=sb.toString();
							updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
									+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len="+(count%100);
							updateResult=HttpClientUtil.docProcVsm(updateurl, null);
							if(updateResult!=null&&updateResult.equals("error")){
								log.info(s+"更新同步状态失败");
								log.info(updateurl);
							}
							sb=new StringBuffer();
							log.info(s+"更新"+count);
							paramIds="";
						}catch (Exception e) {
							switch (s) {
								case "community_original":
									GlobelFlag.setCommunityOriginal(false);
									break;
								case "community_baidu_info":
									GlobelFlag.setCommunityBaiduInfo(false);
									break;
								case "community_xiangqing":
									GlobelFlag.setCommunityXiangqing(false);
									break;
								case "community_baidu_map":
									GlobelFlag.setCommunityBaiduMap(false);
									break;
								case "community_dianping":
									GlobelFlag.setCommunityDianping(false);
									break;
								case "community_original_in":
									GlobelFlag.setCommunityOriginalIn(false);
									break;
								case "community_bbs":
									GlobelFlag.setCommunityBbs(false);
									break;
							}
							log.error(s+"更新同步状态出错");
							log.info(updateurl);
						}
						log.info(s+"同步"+count+"条数据");
					}
					break;
				case "community_bbs":
					JSONArray jsonArray4=JSONArray.fromObject(str);
					List<communityBbs> infos3=(List<communityBbs>)jsonArray4.toList(jsonArray4, new communityBbs(), new JsonConfig());
					if(infos3!=null&&infos3.size()>0){
						String sql="";
						int count=1;
						int result=0;
						String updateurl="";
						String updateResult="";
							log.info(s+"从数据接口获得"+infos3.size()+"条数据");
							for(communityBbs bbs:infos3){
								sb.append(bbs.getId()+",");
								sql="insert into community_bbs(id,title,rqnum,author,latewiter,get_time,base_url,url,pnum,text,synTime) values("+bbs.getId()+",'"+bbs.getTitle().replaceAll("'", "''")+"','"+bbs.getRqnum()+"','"+bbs.getAuthor().replaceAll("'", "''")+"','"+bbs.getLatewiter().replaceAll("'", "''")+"','"+bbs.getGetTime()+"','"+bbs.getUrl().replaceAll("'", "''")+"','"+bbs.getBaseUrl().replaceAll("'", "''")+"','"+bbs.getPnum()+"','"+bbs.getText().replaceAll("'", "''")+"','"+DateUtils.DatetoString("yyyy-MM-dd HH:mm:ss", new Date())+"')";
								try{
									result=DBHelp.executeSQL(sql, null);
									if(result==0){
										log.info(s+"保存失败");
										log.info(sql);
									}
								}catch (Exception e) {
									log.error(s+"保存出错!!!");
									log.error(sql);
									log.error(e.getMessage());
									continue;
								}
								if(count%100==0){
									try {
										paramIds=sb.toString();
										updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
												+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len=100";
										updateResult=HttpClientUtil.docProcVsm(updateurl, null);
										if(updateResult!=null&&updateResult.equals("error")){
											log.info(s+"更新同步状态失败");
											log.info(updateurl);
										}
										sb=new StringBuffer();
										log.info(s+"更新"+count);
										paramIds="";
									}catch (Exception e) {
										log.error(s+"更新同步状态出错");
										log.info(updateurl);
										continue;
									}
								}
								count++;
							}
							try {
								paramIds=sb.toString();
								updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
										+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len="+(count%100);
								updateResult=HttpClientUtil.docProcVsm(updateurl, null);
								if(updateResult!=null&&updateResult.equals("error")){
									log.info(s+"更新同步状态失败");
									log.info(updateurl);
								}
								sb=new StringBuffer();
								log.info(s+"更新"+count);
								paramIds="";
							}catch (Exception e) {
								switch (s) {
									case "community_original":
										GlobelFlag.setCommunityOriginal(false);
										break;
									case "community_baidu_info":
										GlobelFlag.setCommunityBaiduInfo(false);
										break;
									case "community_xiangqing":
										GlobelFlag.setCommunityXiangqing(false);
										break;
									case "community_baidu_map":
										GlobelFlag.setCommunityBaiduMap(false);
										break;
									case "community_dianping":
										GlobelFlag.setCommunityDianping(false);
										break;
									case "community_original_in":
										GlobelFlag.setCommunityOriginalIn(false);
										break;
									case "community_bbs":
										GlobelFlag.setCommunityBbs(false);
										break;
								}
								log.error(s+"更新同步状态出错");
								log.info(updateurl);
							}
							log.info(s+"同步"+count+"条数据");
					}
					break;
				case "community_original_in":
					JSONArray jsonArray5=JSONArray.fromObject(str);
					List<communityOriginalIn> infos4=(List<communityOriginalIn>)jsonArray5.toList(jsonArray5, new communityOriginalIn(), new JsonConfig());
					if(infos4!=null&&infos4.size()>0){
						String sql="";
						int count=1;
						int result=0;
						String updateurl="";
						String updateResult="";
						log.info(s+"从数据接口获得"+infos4.size()+"条数据");
						for(communityOriginalIn in:infos4){
							sb.append(in.getId()+",");
							sql="insert into community_original_in(id,cid,ci_family_address,ci_mobile,insurance_type,insurance_name,insurance_time,zhufuxian,premium_style,claim_settlement,cancellation_type,cancellation_time,community_name,synTime) values("+in.getId()+",'"+in.getCid()+"','"+in.getCiFamilyAddress()+"','"+in.getCiMobile()+"','"+in.getInsuranceType()+"','"+in.getInsuranceName().replaceAll("'", "''")+"','"+in.getInsuranceTime()+"','"+in.getZhufuxian().replaceAll("'", "''")+"','"+in.getPremiumStyle()+"','"+in.getClaimSettlement()+"','"+in.getCancellationType()+"','"+in.getCancellationTime()+"','"+in.getCommunityName().replaceAll("'", "''")+"','"+DateUtils.DatetoString("yyyy-MM-dd HH:mm:ss", new Date())+"')";
							try{
								result=DBHelp.executeSQL(sql, null);
								if(result==0){
									log.info(s+"保存失败");
									log.info(sql);
								}
							}catch (Exception e) {
								log.error(s+"保存出错!!!");
								log.error(sql);
								log.error(e.getMessage());
								continue;
							}
							if(count%100==0){
								try {
									paramIds=sb.toString();
									updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
											+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len=100";
									updateResult=HttpClientUtil.docProcVsm(updateurl, null);
									if(updateResult!=null&&updateResult.equals("error")){
										log.info(s+"更新同步状态失败");
										log.info(updateurl);
									}
									sb=new StringBuffer();
									log.info(s+"更新"+count);
									paramIds="";
								}catch (Exception e) {
									log.error(s+"更新同步状态出错");
									log.info(updateurl);
									continue;
								}
							}
							count++;
						}
						try {
							paramIds=sb.toString();
							updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
									+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len="+(count%100);
							updateResult=HttpClientUtil.docProcVsm(updateurl, null);
							if(updateResult!=null&&updateResult.equals("error")){
								log.info(s+"更新同步状态失败");
								log.info(updateurl);
							}
							sb=new StringBuffer();
							log.info(s+"更新"+count);
							paramIds="";
						}catch (Exception e) {
							switch (s) {
								case "community_original":
									GlobelFlag.setCommunityOriginal(false);
									break;
								case "community_baidu_info":
									GlobelFlag.setCommunityBaiduInfo(false);
									break;
								case "community_xiangqing":
									GlobelFlag.setCommunityXiangqing(false);
									break;
								case "community_baidu_map":
									GlobelFlag.setCommunityBaiduMap(false);
									break;
								case "community_dianping":
									GlobelFlag.setCommunityDianping(false);
									break;
								case "community_original_in":
									GlobelFlag.setCommunityOriginalIn(false);
									break;
								case "community_bbs":
									GlobelFlag.setCommunityBbs(false);
									break;
							}
							log.error(s+"更新同步状态出错");
							log.info(updateurl);
						}
						log.info(s+"同步"+count+"条数据");
					}
					break;
				case "community_baidu_info":
					JSONArray jsonArray6=JSONArray.fromObject(str);
					List<communityBaiduInfo> infos5=(List<communityBaiduInfo>)jsonArray6.toList(jsonArray6, new communityBaiduInfo(), new JsonConfig());
					if(infos5!=null&&infos5.size()>0){
						String sql="";
						int count=1;
						int result=0;
						String updateurl="";
						String updateResult="";
						log.info(s+"从数据接口获得"+infos5.size()+"条数据");
						for(communityBaiduInfo baiduInfo:infos5){
							sb.append(baiduInfo.getId()+",");
							sql="insert into community_baidu_info(id,merchant_name,community_name,province,city,merchant_url,merchant_address,merchant_tag,store_introduction,feature,longitude,latitude,comment_url,distance,bbs_url,dianping_url,manager_url,manage_type,bbs_type,dianping_type,analy_mark,name_mark,synTime) values("+baiduInfo.getId()+",'"+baiduInfo.getMerchantName()+"','"+baiduInfo.getCommunityName()+"','"+baiduInfo.getProvince()+"','"+baiduInfo.getCity()+"','"+baiduInfo.getMerchantUrl().replaceAll("'", "''")+"','"+baiduInfo.getMerchantAddress().replaceAll("'", "''")+"','"+baiduInfo.getMerchantTag().replaceAll("'", "''")+"','"+baiduInfo.getStoreIntroduction()+"','"+baiduInfo.getFeature()+"','"+baiduInfo.getLongitude()+"','"+baiduInfo.getLatitude()+"','"+baiduInfo.getCommentUrl().replaceAll("'", "''")+"','"+baiduInfo.getDistance()+"','"+baiduInfo.getBbsUrl().replaceAll("'", "''")+"','"+baiduInfo.getDianpingUrl()+"','"+baiduInfo.getManagerUrl().replaceAll("'", "''")+"','"+baiduInfo.getManageType()+"','"+baiduInfo.getBbsType()+"','"+baiduInfo.getDianpingType()+"','"+baiduInfo.getAnalyMark()+"','"+baiduInfo.getNameMark()+"','"+DateUtils.DatetoString("yyyy-MM-dd HH:mm:ss", new Date())+"')";
							try{
								result=DBHelp.executeSQL(sql, null);
								if(result==0){
									log.info(s+"保存失败");
									log.info(sql);
								}
							}catch (Exception e) {
								log.error(s+"保存出错!!!");
								log.error(sql);
								log.error(e.getMessage());
								continue;
							}
							if(count%100==0){
								try {
									paramIds=sb.toString();
									updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
											+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len=100";
									updateResult=HttpClientUtil.docProcVsm(updateurl, null);
									if(updateResult!=null&&updateResult.equals("error")){
										log.info(s+"更新同步状态失败");
										log.info(updateurl);
									}
									sb=new StringBuffer();
									log.info(s+"更新"+count);
									paramIds="";
								}catch (Exception e) {
									switch (s) {
										case "community_original":
											GlobelFlag.setCommunityOriginal(false);
											break;
										case "community_baidu_info":
											GlobelFlag.setCommunityBaiduInfo(false);
											break;
										case "community_xiangqing":
											GlobelFlag.setCommunityXiangqing(false);
											break;
										case "community_baidu_map":
											GlobelFlag.setCommunityBaiduMap(false);
											break;
										case "community_dianping":
											GlobelFlag.setCommunityDianping(false);
											break;
										case "community_original_in":
											GlobelFlag.setCommunityOriginalIn(false);
											break;
										case "community_bbs":
											GlobelFlag.setCommunityBbs(false);
											break;
									}
									log.error(s+"更新同步状态出错");
									log.info(updateurl);
									continue;
								}
							}
							count++;
						}
						try {
							paramIds=sb.toString();
							updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
									+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len="+(count%100);
							updateResult=HttpClientUtil.docProcVsm(updateurl, null);
							if(updateResult!=null&&updateResult.equals("error")){
								log.info(s+"更新同步状态失败");
								log.info(updateurl);
							}
							sb=new StringBuffer();
							log.info(s+"更新"+count);
							paramIds="";
						}catch (Exception e) {
							switch (s) {
								case "community_original":
									GlobelFlag.setCommunityOriginal(false);
									break;
								case "community_baidu_info":
									GlobelFlag.setCommunityBaiduInfo(false);
									break;
								case "community_xiangqing":
									GlobelFlag.setCommunityXiangqing(false);
									break;
								case "community_baidu_map":
									GlobelFlag.setCommunityBaiduMap(false);
									break;
								case "community_dianping":
									GlobelFlag.setCommunityDianping(false);
									break;
								case "community_original_in":
									GlobelFlag.setCommunityOriginalIn(false);
									break;
								case "community_bbs":
									GlobelFlag.setCommunityBbs(false);
									break;
							}
							log.error(s+"更新同步状态出错");
							log.info(updateurl);
						}
						log.info(s+"同步"+count+"条数据");
					}
					break;
				case "community_xiangqing":
					JSONArray jsonArray7=JSONArray.fromObject(str);
					List<communityXiangqing> infos6=(List<communityXiangqing>)jsonArray7.toList(jsonArray7, new communityXiangqing(), new JsonConfig());
					if(infos6!=null&&infos6.size()>0){
						String sql="";
						int count=1;
						int result=0;
						String updateurl="";
						String updateResult="";
						log.info(s+"从数据接口获得"+infos6.size()+"条数据");
						for(communityXiangqing detail:infos6){
							sb.append(detail.getId()+",");
							sql="insert into community_xiangqing(id,name,xqdz,ssqy,yb,cqms,  wylb,jgsj,kfs,jzjg,jzlb,  jzmj,zdmj,dqhs,zhs,lhl,rjl,wyf,fjxx,wybgdd,gs,gn,gd,rq,txsb,wsfw,dtfw,aqgl,tcw,xqjj,jtzk,zbxx,pirce,url,hbsy,tbqn,jjlq,lscjxx,dls,analy_mark,synTime) "
								+"values("+detail.getId()+",'"+detail.getName()+"','"+detail.getXqdz()+"','"+detail.getSsqy()+"','"+detail.getYb()+"','"+detail.getCqms()+"','"+detail.getWylb()+"','"+detail.getJgsj()+"'," 
								+"'"+detail.getKfs()+"','"+detail.getJzjg()+"','"+detail.getJzlb()+"','"+detail.getJzmj()+"','"+detail.getZdmj()+"','"+detail.getDqhs()+"','"+detail.getZhs()+"','"+detail.getLhl()+"','"+detail.getRjl()+"',"
								+"'"+detail.getWyf()+"','"+detail.getFjxx()+"','"+detail.getWybgdd()+"','"+detail.getGs()+"','"+detail.getGn()+"','"+detail.getGd()+"','"+detail.getRq()+"','"+detail.getTxsb()+"','"+detail.getWsfw()+"','"+detail.getDtfw()+"',"
								+"'"+detail.getAqgl()+"','"+detail.getTcw()+"','"+detail.getXqjj()+"','"+detail.getJtzk()+"','"+detail.getZbxx()+"','"+detail.getPirce()+"','"+((detail.getUrl()==null||detail.getUrl().length()==0)?null:detail.getUrl())+"',"
								+"'"+detail.getHbsy()+"','"+detail.getTbqn()+"','"+detail.getJjlq()+"','"+detail.getLscjxx()+"','"+detail.getDls()+"','"+detail.getAnalyMark()+"','"+DateUtils.DatetoString("yyyy-MM-dd HH:mm:ss", new Date())+"')";
							try{
								result=DBHelp.executeSQL(sql, null);
								if(result==0){
									log.info(s+"保存失败");
									log.info(sql);
								}
							}catch (Exception e) {
								log.error(s+"保存出错!!!");
								log.error(sql);
								log.error(e.getMessage());
								continue;
							}
							if(count%100==0){
								try {
									paramIds=sb.toString();
									updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
											+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len=100";
									updateResult=HttpClientUtil.docProcVsm(updateurl, null);
									if(updateResult!=null&&updateResult.equals("error")){
										log.info(s+"更新同步状态失败");
										log.info(updateurl);
									}
									sb=new StringBuffer();
									log.info(s+"更新"+count);
									paramIds="";
								}catch (Exception e) {
									log.error(s+"更新同步状态出错");
									log.info(updateurl);
									continue;
								}
							}
							count++;
						}
						try {
							paramIds=sb.toString();
							updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
									+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len="+(count%100);
							updateResult=HttpClientUtil.docProcVsm(updateurl, null);
							if(updateResult!=null&&updateResult.equals("error")){
								log.info(s+"更新同步状态失败");
								log.info(updateurl);
							}
							sb=new StringBuffer();
							log.info(s+"更新"+count);
							paramIds="";
						}catch (Exception e) {
							switch (s) {
								case "community_original":
									GlobelFlag.setCommunityOriginal(false);
									break;
								case "community_baidu_info":
									GlobelFlag.setCommunityBaiduInfo(false);
									break;
								case "community_xiangqing":
									GlobelFlag.setCommunityXiangqing(false);
									break;
								case "community_baidu_map":
									GlobelFlag.setCommunityBaiduMap(false);
									break;
								case "community_dianping":
									GlobelFlag.setCommunityDianping(false);
									break;
								case "community_original_in":
									GlobelFlag.setCommunityOriginalIn(false);
									break;
								case "community_bbs":
									GlobelFlag.setCommunityBbs(false);
									break;
							}
							log.error(s+"更新同步状态出错");
							log.info(updateurl);
						}
						log.info(s+"同步"+count+"条数据");
					}
					break;
		}
		String ids=sb.toString();
		String updateResult="";
		switch (s) {
			case "community_original":
				GlobelFlag.setCommunityOriginal(false);
				break;
			case "community_baidu_info":
				GlobelFlag.setCommunityBaiduInfo(false);
				break;
			case "community_xiangqing":
				GlobelFlag.setCommunityXiangqing(false);
				break;
			case "community_baidu_map":
				GlobelFlag.setCommunityBaiduMap(false);
				break;
			case "community_dianping":
				GlobelFlag.setCommunityDianping(false);
				break;
			case "community_original_in":
				GlobelFlag.setCommunityOriginalIn(false);
				break;
			case "community_bbs":
				GlobelFlag.setCommunityBbs(false);
				break;
		}
//		if(ids.length()>0){
//			int len=0;
//			String[] a=ids.split(",");
//			String paramIds="";
//			log.info(s+"需要更新"+a.length+"条数据");
//			do {
//				String[] n=Arrays.copyOfRange(a, len, (len+1000));
//				paramIds=StringUtils.join(n,",");
//				len+=1000;
//				String updateurl = PropertyUtil.get("SychroUrl")+":"+PropertyUtil.get("SychroPort")
//						+ "/CpicDataServer/hbupdateapp?tableName=" + tablename+"&ids="+paramIds+"&len="+len;
//				updateResult=HttpClientUtil.docProcVsm(updateurl, null);
//				log.info(paramIds);
//				if(updateResult!=null&&updateResult.equals("error")){
//					log.error(s+"更新同步失败");
//					switch (s) {
//						case "community_original":
//							GlobelFlag.setCommunityOriginal(false);
//							break;
//						case "community_baidu_info":
//							GlobelFlag.setCommunityBaiduInfo(false);
//							break;
//						case "community_xiangqing":
//							GlobelFlag.setCommunityXiangqing(false);
//							break;
//						case "community_baidu_map":
//							GlobelFlag.setCommunityBaiduMap(false);
//							break;
//						case "community_dianping":
//							GlobelFlag.setCommunityDianping(false);
//							break;
//						case "community_original_in":
//							GlobelFlag.setCommunityOriginalIn(false);
//							break;
//						case "community_bbs":
//							GlobelFlag.setCommunityBbs(false);
//							break;
//					}
//					break;
//				}
//			} while ((a.length-len)>0);
//		}
		Date endTime=new Date();
		switch (s) {
			case "community_original":
				GlobelFlag.setCommunityOriginal(false);
				break;
			case "community_baidu_info":
				GlobelFlag.setCommunityBaiduInfo(false);
				break;
			case "community_xiangqing":
				GlobelFlag.setCommunityXiangqing(false);
				break;
			case "community_baidu_map":
				GlobelFlag.setCommunityBaiduMap(false);
				break;
			case "community_dianping":
				GlobelFlag.setCommunityDianping(false);
				break;
			case "community_original_in":
				GlobelFlag.setCommunityOriginalIn(false);
				break;
			case "community_bbs":
				GlobelFlag.setCommunityBbs(false);
				break;
		}
		log.info(s+"同步结束,耗时"+(endTime.getTime()-startTime.getTime())/60000+"分钟");
	}
}
