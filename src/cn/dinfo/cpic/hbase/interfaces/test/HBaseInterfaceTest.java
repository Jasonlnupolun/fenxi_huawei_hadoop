package cn.dinfo.cpic.hbase.interfaces.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import Bean.Rowkeybean;
import cn.dinfo.cpic.Utils.FileHelper;
import cn.dinfo.cpic.Utils.XMLUtil;
import cn.dinfo.cpic.hbase.interfaces.impl.HBaseInterfaceImpl;

public class HBaseInterfaceTest {
	
	private HBaseInterfaceImpl hBaseInterfaceImpl = new HBaseInterfaceImpl();
	String tablename = "zx_food_manager";
	
	@Test
	public  void testCreateTable(){
		String tableName = "myTestUser2";
		String[] familys = {"info","friends","others"};
		hBaseInterfaceImpl.createTable(tableName, familys);
	}
	
	@Test
	public void testDropTable(){
		hBaseInterfaceImpl.dropTable("zx_squrl_index");
	}
	
	@Test
	public void testPutData(){
		String columnFamily = "manager_base";
		String rowkey = "43545";
		String qualifier = "name";
		String value = "Tom";
		
		hBaseInterfaceImpl.put(tablename, columnFamily, rowkey, qualifier, value);
		System.out.println("single put data success!");
	}
	
	
	

	@Test
	public void getdata(){
		@SuppressWarnings("unused")
		String rowkey = "row-7";
		
	}
	
	@Test
	public void updatedata(){
		String columnFamily = "info";
		String rowkey = "row-9";
		String qualifier = "name";
		String value = "Mark";
		hBaseInterfaceImpl.updateData(tablename, columnFamily, rowkey, qualifier, value);
	}
	
	@Test
	public void putDataBatch(){
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
		map.put("rowkey","row-1111");
		map.put("family","info");
		map.put("name","Anna");
		map.put("age","22");
		map.put("phone","15000235963");
		listMap.add(map);
		hBaseInterfaceImpl.put(tablename,listMap);
	}
	
	@Test
	public void deleteBatch() throws IOException{
		String infos=FileHelper.readFile("C:\\Users\\cd\\Desktop\\删除zx_squrl.xml");
		infos=infos.substring(0, infos.lastIndexOf("<rowkey>"));
		infos="<?xml version='1.0' encoding='UTF-8' standalone='yes'?><rowkeybean><rowkey>2015-09-14 15:07:31:565c58ed325-d802-4c00-867f-4baa1c6cd8db</rowkey><rowkey>2015-09-14 15:07:31:562e483c5e2-bd95-4749-84c3-bdf73e33e32f</rowkey></rowkeybean>";
		System.out.println(infos);
		Rowkeybean rowKeyBean = XMLUtil.converyToJavaBean(infos, Rowkeybean.class);
		List<String> rowkeylList = rowKeyBean.getRowkeylist();
		hBaseInterfaceImpl.deleteBatch("zx_squrl_index", rowkeylList);
	}
	
	@Test
	public void listtables(){
		hBaseInterfaceImpl.listTableNames();
	}
	@Test
	public void truncate(){
		hBaseInterfaceImpl.truncateTable("zx_food_dianping", true);
	}
	
	@Test
	public void testscan(){
		hBaseInterfaceImpl.scan("zx_food_dianping");
	}
	@Test
	public void os(){
		System.out.println(System.getProperties().getProperty("os.name"));
	}
}
