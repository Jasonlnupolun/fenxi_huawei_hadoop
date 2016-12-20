package cn.dinfo.cpic.hbase.interfaces;

import java.util.List;
import java.util.Map;


public interface HBaseInterface {
	
	/**
	 * create table
	 * @param tableName
	 * @param familys
	 */
	void createTable(String tableName,String[] familys);
	
	/**
	 * delete table
	 * @param tableName
	 */
	void dropTable(String tableName);
	
	/**
	 * modify table
	 * @param tableName
	 * @param familyName
	 */
	void listTableNames();
	void modifyTable(String tableName,String familyName);
	
	/**
	 * clear the table,but not delete the table
	 * @param tableName
	 * @param flag
	 */
	void truncateTable(String tableName,boolean flag);
	
	
	/**
	 * put data to hbase 
	 * @param tableName
	 * @param rowKey
	 * @param familyName
	 * @param qualifier
	 * @param value
	 */
	void put(String tableName,String familyName, String rowKey,String qualifier,String value);
	void put(String tableName,List<Map<String,String>> dataMaps);
	void put(String tableName, String familyName,String rowKey,String qualifier,long timestamp,String value);
	
	/**
	 * get data from hbase table by row key
	 * @param tableName
	 * @param rowKey
	 * @return
	 */
	Map<String,String> getDataFromHBaseByRowKey(String tableName,String rowKey);
	Map<String,String> getDataFromHBase(String tableName,String rowKey,String columnFamily,String qualifier);
	
	/**
	 *  update date
	 * @param tableName
	 * @param familyName
	 * @param rowKey
	 * @param qualifier
	 * @param value
	 */
	void updateData(String tableName,String familyName,String rowKey,String qualifier,String value);
	
	
	
	/**
	 * delete data 
	 * @param tableName
	 * @param rowKey
	 */
	void deleteData(String tableName,String rowKey);
	/**
	 * scan data 
	 * @param tableName
	 */
	int scan(String tableName);
}
