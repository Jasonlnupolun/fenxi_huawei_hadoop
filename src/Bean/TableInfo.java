package Bean;





import java.util.ArrayList;
import java.util.List;


public class TableInfo {
	
public static final String[] TABLENAMES = {"ZX_FOOD_MANAGE","ZX_HOTEL_MANAGE","ZX_DIANPING","ZX_SQURL","COMMUNITY_XIANGQING","COMMUNITY_BBS","COMMUNITY_DIANPING"};
	//美食基本信息表
	public static final String ZX_FOOD_MANAGE = "zx_food_manager";
	public static final int ZX_FOOD_MANAGE_TYPE = 1;
	public static final String ZX_FOOD_MANAGE_NAME = "美食";
	//美食索引表
	public static final String ZX_FOOD_MANAGE_INDEX = "zx_food_manager_index";
	//美食点评信息表
	public static final String ZX_FOOD_DIANPING = "zx_food_dianping";
	public static final String ZX_FOOD_DIANPING_NAME = "美食点评";
	//美食点评索引表
	public static final String ZX_FOOD_DIANPING_INDEX = "zx_food_dianping_index";
	//酒店基本信息表
	public static final String ZX_HOTEL_MANAGE = "zx_hotel_manager";
	public static final String ZX_HOTEL_MANAGE_NAME = "酒店";
	//酒店索引表
	public static final String ZX_HOTEL_MANAGE_INDEX = "zx_hotel_manager_index";
	//酒店点评信息表
	public static final String ZX_HOTEL_DIANPING = "zx_hotel_dianping";
	public static final String ZX_HOTEL_DIANPING_NAME = "酒店点评";
	//酒店点评索引表
	public static final String ZX_HOTEL_DIANPING_INDEX = "zx_hotel_dianping_index";
	//商圈信息表
	public static final String ZX_SQURL = "zx_squrl";
	public static final String ZX_SQURL_NAME = "商圈";
	//商圈信息索引表
	public static final String ZX_SQURL_INDEX = "zx_squrl_index";
	//社区基本信息表
	public static final String COMMUNITY_XIANGQING = "community_xiangqing";
	public static final String COMMUNITY_XIANGQING_NAME = "社区基本信息";
	//社区基本信息索引表
	public static final String COMMUNITY_XIANGQING_INDEX = "community_xiangqing_index";
	//社区基本信息表1
	public static final String COMMUNITY_XIANGQING1 = "community_xiangqing1";
	public static final String COMMUNITY_XIANGQING_NAME1 = "社区基本信息1";
	//社区基本信息索引表1
	public static final String COMMUNITY_XIANGQING_INDEX1 = "community_xiangqing_index1";
	//社区论坛信息表
	public static final String COMMUNITY_BBS = "community_bbs";
	public static final String COMMUNITY_BBS_NAME = "社区论坛信息";
	//社区论坛信息索引表
	public static final String COMMUNITY_BBS_INDEX = "community_bbs_index";
	//社区点评信息表
	public static final String COMMUNITY_DIANPING = "community_dianping";
	public static final String COMMUNITY_DIANPING_NAME = "社区点评信息";
	//社区点评信息索引表
	public static final String COMMUNITY_DIANPING_INDEX = "community_dianping_index";
	//保监会采集信息表
	public static final String PREMIUM_INCOME="premium_income";
	public static final String PREMIUM_INCOME_NAME="保监会采集信息";
	//保监会采集索引表
	public static final String PREMIUM_INCOME_INDEX="premium_income_index";
	//上市保险公司行情信息表
	public static final String STOCK_MARKET="stock_market";
	public static final String STOCK_MARKET_NAME="上市保险公司行情信息";
	//上市保险公司行情信息索引表
	public static final String STOCK_MARKET_INDEX="stock_market_index";
	//国内上市保险公司信息表
	public static final String INSURANCE_MANAGE="insurance_manage";
	public static final String INSURANCE_MANAGE_NAME="国内上市保险公司信息";
	//国内上市保险公司信息索引表
	public static final String INSURANCE_MANAGE_INDEX="insurance_manage_index";
	//社区名称与url
	public static final String COMMUNITY_URLS="community_urls";
	public static final String COMMUNITY_URLS_NAME="社区名称与url";
	//社区名称与url索引表
	public static final String COMMUNITY_URLS_INDEX="community_urls_index";
	//社区周围景点表
	public static final String COMMUNITY_BAIDU_MAP="community_baidu_map";
	public static final String COMMUNITY_BAIDU_MAP_NAME="社区周围景点";
	//社区周围景点索引表
	public static final String COMMUNITY_BAIDU_MAP_INDEX="community_baidu_map_index";
	//新闻采集信息表
	public static final String HOTNEWS="hotnews";
	public static final String HOTNEWS_NAME="新闻采集信息";
	//新闻采集信息索引表
	public static final String HOTNEWS_INDEX="hotnews_index";
	//竞争对手产品信息表
	public static final String COMPETE_SHOP="compete_shop";
	public static final String COMPETE_SHOP_NAME="竞争对手产品信息";
	//竞争对手产品信息索引表
	public static final String COMPETE_SHOP_INDEX="compete_shop_index";
	//
	public static final String KMPRO_DATA="kmpro_data";
	public static final String KMPRO_DATA_NAME="kmpro_data";
	//
	public static final String KMPRO_DATA_INDEX="kmpro_data_index";
	
	
	/*********************************************family*****************************************************/
	@SuppressWarnings("serial")
	public static final List<String> ZX_SQURL_FAMILYNAME_LIST = new ArrayList<String>(){{add("squrl_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> ZX_SQURL_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("squrl_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> ZX_FOOD_DIANPING_FAMILYNAME_LIST = new ArrayList<String>(){{add("dianping_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> ZX_FOOD_DIANPING_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("dianping_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> ZX_HOTEL_DIANPING_FAMILYNAME_LIST = new ArrayList<String>(){{add("dianping_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> ZX_HOTEL_DIANPING_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("dianping_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> ZX_FOOD_MANAGE_FAMILYNAME_LIST = new ArrayList<String>(){{add("manager_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> ZX_FOOD_MANAGE_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("manager_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> ZX_HOTEL_MANAGE_FAMILYNAME_LIST = new ArrayList<String>(){{add("manager_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> ZX_HOTEL_MANAGE_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("manager_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_XIANGQING_FAMILYNAME_LIST = new ArrayList<String>(){{add("xiangqing_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_XIANGQING1_FAMILYNAME_LIST = new ArrayList<String>(){{add("xiangqing_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_XIANGQING_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("xiangqing_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_XIANGQING1_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("xiangqing_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_BBS_FAMILYNAME_LIST = new ArrayList<String>(){{add("bbs_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_BBS_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("bbs_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_DIANPING_FAMILYNAME_LIST = new ArrayList<String>(){{add("dianping_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_DIANPING_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("dianping_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> PREMIUM_INCOME_FAMILYNAME_LIST = new ArrayList<String>(){{add("income_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> PREMIUM_INCOME_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("income_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> STOCK_MARKET_FAMILYNAME_LIST = new ArrayList<String>(){{add("market_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> STOCK_MARKET_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("market_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> INSURANCE_MANAGE_FAMILYNAME_LIST = new ArrayList<String>(){{add("manage_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> INSURANCE_MANAGE_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("manage_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_URLS_FAMILYNAME_LIST = new ArrayList<String>(){{add("url_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_URLS_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("url_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_BAIDU_MAP_FAMILYNAME_LIST = new ArrayList<String>(){{add("baidu_map_base");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_BAIDU_MAP_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("baidu_map_base_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> HOTNEWS_FAMILYNAME_LIST = new ArrayList<String>(){{add("new");}};
	
	@SuppressWarnings("serial")
	public static final List<String> HOTNEWS_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("new_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMPETE_SHOP_FAMILYNAME_LIST = new ArrayList<String>(){{add("shop");}};
	
	@SuppressWarnings("serial")
	public static final List<String> COMPETE_SHOP_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("shop_index");}};
	
	@SuppressWarnings("serial")
	public static final List<String> KMPRO_DATA_FAMILYNAME_LIST = new ArrayList<String>(){{add("kmpro");}};
	@SuppressWarnings("serial")
	public static final List<String> KMPRO_DATA_FAMILYNAME_INDEX_LIST = new ArrayList<String>(){{add("kmpro_index");}};
	/*********************************************cell*****************************************************/
	@SuppressWarnings("serial")
	public static final List<String> ZX_SQURL_CELL_LIST = 
			new ArrayList<String>(){{add("circle");add("url");add("city");add("urltype");add("type");}};
			
	@SuppressWarnings("serial")
	public static final List<String> ZX_FOOD_DIANPING_CELL_LIST = 
			new ArrayList<String>(){{add("url");add("koubei");add("start");add("text");add("zannum");add("time");add("name");}};
			
	@SuppressWarnings("serial")
	public static final List<String> ZX_HOTEL_DIANPING_CELL_LIST = 
			new ArrayList<String>(){{add("url");add("koubei");add("start");add("text");add("zannum");
			add("time");add("name");}};

	@SuppressWarnings("serial")
	public static final List<String> ZX_FOOD_MANAGE_CELL_LIST = 
			new ArrayList<String>(){{
				add("name");add("type");add("labels");add("province");add("city");add("address");
				add("tel");add("discount");add("hours");add("park");add("othername");add("pre_kobei");
				add("url");add("urltype");add("circle");add("district");add("foodtype");add("start");
				add("impression");add("dpnum");add("othershop");add("qualification");add("lat");add("lng");
				add("comments_star5_num");add("comments_star4_num");add("comments_star3_num");add("comments_star2_num");add("comments_star1_num");add("source");
				add("get_time");
				}};
			
	@SuppressWarnings("serial")
	public static final List<String> ZX_HOTEL_MANAGE_CELL_LIST = 
			new ArrayList<String>(){{
				add("hotel_name");add("traffic_routes");add("price");add("evaluation_ranking");add("address");add("groupon_ok");
				add("reserve_ok");add("url");add("tuancontain");add("label");add("city");add("type");
				add("urltype");add("hotelintro");add("hoteltransport");add("star");add("comments_star_num");add("netfriend_label");
				add("comments_star5_num");add("comments_star4_num");add("comments_star3_num");add("comments_star2_num");add("comments_star1_num");add("comments_car_num");
				add("circle");add("district");add("hoteltype");add("ambitus_hotel");add("ambitus_food");add("ambitus_shopping");
				add("lat");add("lng");add("source");add("get_time");}};

	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_XIANGQING_CELL_LIST = 
			new ArrayList<String>(){{add("name");add("url");add("xqdz");add("ssqy");add("yb");
			add("cqms");add("wylb");add("jgsj");add("kfs");add("jzjg");add("jzlb");add("jzmj");
			add("zdmj");add("dqhs");add("zhs");add("lhl");add("rjl");add("wyf");add("fjxx");
			add("wybgdd");add("gs");add("gn");add("gd");add("rq");add("txsb");add("wsfw");
			add("dtfw");add("aqgl");add("tcw");add("xqjj");add("jtzk");add("zbxx");add("price");
			add("hbsy");add("tbqn");add("jjlq");add("lscjxx");add("dls");add("get_time");add("source");
			add("city");add("labels");add("school");add("hospital");add("business");
			add("lat");add("lng");add("analy_mark");add("analy_num");add("analy_ok");add("wygs");add("type");}};
			
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_XIANGQING1_CELL_LIST = 
			new ArrayList<String>(){{add("name");add("url");add("xqdz");add("ssqy");add("yb");
			add("cqms");add("wylb");add("jgsj");add("kfs");add("jzjg");add("jzlb");add("jzmj");
			add("zdmj");add("dqhs");add("zhs");add("lhl");add("rjl");add("wyf");add("fjxx");
			add("wybgdd");add("gs");add("gn");add("gd");add("rq");add("txsb");add("wsfw");
			add("dtfw");add("aqgl");add("tcw");add("xqjj");add("jtzk");add("zbxx");add("price");
			add("hbsy");add("tbqn");add("jjlq");add("lscjxx");add("dls");add("get_time");add("source");
			add("city");add("labels");add("school");add("hospital");add("business");
			add("lat");add("lng");add("analy_mark");add("analy_num");add("analy_ok");add("wygs");add("type");}};
			
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_BBS_CELL_LIST = 
			new ArrayList<String>(){{add("base_url");add("url");add("title");add("author");add("rqnum");
			add("latewiter");add("text");add("get_time");}};
			
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_DIANPING_CELL_LIST = 
			new ArrayList<String>(){{add("text");add("author");add("star");add("ptags");add("post_time");add("tdtags");add("source");
			add("gradetags");add("dptags");add("znum");add("hnum");add("url");add("get_time");}};
			
	@SuppressWarnings("serial")
	public static final List<String> PREMIUM_INCOME_CELL_LIST = 
			new ArrayList<String>(){{add("url");add("title");add("region");add("total");add("property_insurance");add("life_insurance");
			add("accident_insurance");add("health_insurance");add("get_time");add("source");add("post_time");add("unit");
			add("no");add("company_name");add("insurance");add("rows");add("new_loan");add("new_accounts");add("payment_year");add("entrust_assets");add("invest_assets");}};
			
	@SuppressWarnings("serial")
	public static final List<String> STOCK_MARKET_CELL_LIST = 
			new ArrayList<String>(){{add("no");add("nocode");add("name");add("date_close");add("market_cap");add("profit_rate");
			add("net_rate");add("time");add("get_time");add("source");}};	
			
	@SuppressWarnings("serial")
	public static final List<String> INSURANCE_MANAGE_CELL_LIST = 
			new ArrayList<String>(){{add("subject");add("money");add("nocade");add("name");add("unit");add("get_time");
			add("type");add("year");add("quarter");add("url");}};		
			
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_URLS_CELL_LIST = 
			new ArrayList<String>(){{add("houseurl");add("name");add("xiangqing_url");add("xiangqing_type");add("bbs_url");add("bbs_type");
			add("dianping_url");add("dianping_type");add("get_time");}};
			
	@SuppressWarnings("serial")
	public static final List<String> COMMUNITY_BAIDU_MAP_CELL_LIST = 
			new ArrayList<String>(){{add("merchant_name");add("merchant_url");add("merchant_address");add("merchant_tag");add("store_introduction");add("longitude");
			add("latitude");add("distance");add("get_time");add("oldlatandlng");}};
			
	@SuppressWarnings("serial")
	public static final List<String> HOTNEWS_CELL_LIST = 
			new ArrayList<String>(){{add("module");add("title");add("source");add("author");add("post_time");add("text");
			add("url");add("get_time");}};
			
	@SuppressWarnings("serial")
	public static final List<String> COMPETE_SHOP_CELL_LIST = 
			new ArrayList<String>(){{add("url");add("title");add("price");add("ptittag");add("source");add("get_time");}};
		
	@SuppressWarnings("serial")
	public static final List<String> KMPRO_DATA_CELL_LIST = 
			new ArrayList<String>(){{add("website");add("url");add("title");add("content");add("author");add("src");add("publishtime");add("gettime");}};
	/*********************************************索引表rowkey*****************************************************/		
	public static final String getIndexTableQualifier(String dataTableName){
		switch(dataTableName){
			case "zx_food_manager_index" : return "manager_base_rowkey";
			case "zx_hotel_manager_index" : return "manager_base_rowkey";
			case "zx_food_dianping_index" : return "dianping_base_rowkey";
			case "zx_hotel_dianping_index" : return "dianping_base_rowkey";
			case "zx_squrl_index" : return "squrl_base_rowkey";
			case "community_xiangqing_index" : return "xiangqing_base_rowkey";
			case "community_xiangqing_index1" : return "xiangqing_base_rowkey";
			case "community_bbs_index" : return "bbs_base_rowkey";
			case "community_dianping_index" : return "dianping_base_rowkey";
			case "premium_income_index" : return "income_base_rowkey";
			case "stock_market_index" : return "market_base_rowkey";
			case "insurance_manage_index" : return "manage_base_rowkey";
			case "community_urls_index" : return "url_base_rowkey";
			case "community_baidu_map_index" : return "xiangqing_base_rowkey";
			case "hotnews_index" : return "new_rowkey";
			case "compete_shop_index" : return "shop_rowkey";
			case "kmpro_data_index" : return "kmpro_rowkey";
		}
		return null;
	}
}
