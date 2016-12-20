package cn.dinfo.cpic.hbase.interfaces.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.TableNotFoundException;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import Sync.GlobelFlag;
import cn.dinfo.cpic.Utils.HBaseConfigurationUtil;
import cn.dinfo.cpic.hbase.interfaces.HBaseInterface;

/**
 * 
 * @author c_sulinbing
 *
 */

public class HBaseInterfaceImpl implements HBaseInterface {

	private final static Logger logger = Logger.getLogger(HBaseInterfaceImpl.class);

	private static Admin admin = null;
	private static Table table = null;
	private HTableDescriptor tableDescriptor;
	private HColumnDescriptor columnDescriptor;
	List<Put> listPuts = new ArrayList<Put>();
	static {
		try {
			admin = HBaseConfigurationUtil.getAdmin();
		} catch (Exception e) {
			GlobelFlag.setSqurl(false);
	  		GlobelFlag.setHotelManager(false);
	  		GlobelFlag.setHotelDianping(false);
	  		GlobelFlag.setFoodManager(false);
	  		GlobelFlag.setFoodDianping(false);
	  		e.printStackTrace();
		}
	}

	@Override
	public synchronized void  createTable(String tableName, String[] familys) {

		try {
			if (!admin.tableExists(TableName.valueOf(tableName))) {

				// Specify the table descriptor.
				tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

				for (String family : familys) {

					columnDescriptor = new HColumnDescriptor(family);

					// Set data encoding methods，HBase provides
					// DIFF，FAST_DIFF，PREFIX
					// and PREFIX_TREE
					columnDescriptor.setDataBlockEncoding(DataBlockEncoding.FAST_DIFF);

					tableDescriptor.addFamily(columnDescriptor);
					
					tableDescriptor.addCoprocessor("org.apache.hadoop.hbase.coprocessor.AggregateImplementation");
					
				}

				logger.info("Createing table " + tableName + " to hbase.");
				admin.createTable(tableDescriptor);
			} else {
				logger.error("The table is already exists");
			}
		} catch (IOException e) {
			logger.error("Connection failed");
			e.printStackTrace();
		} finally {
			if (admin != null) {
				try {
					// Close the HBaseAdmin object.
					admin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Delete user table
	 */
	@Override
	public void dropTable(String tableName) {
		try {
			if (admin.tableExists(TableName.valueOf(tableName))) {
				// Disable the table before deleteing it
				admin.disableTable(TableName.valueOf(tableName));

				// Delete table
				admin.deleteTable(TableName.valueOf(tableName));
				logger.info("Drop table " + tableName + " successful");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (admin != null) {
				try {
					// Close the HBaseAdmin object.
					admin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void modifyTable(String tableName, String familyName) {

		// Specify the column family name.
		byte[] family_Name = Bytes.toBytes(familyName);

		try {
			// Obtain the table descriptor.
			tableDescriptor = admin.getTableDescriptor(TableName.valueOf(tableName));
		} catch (TableNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Check whether the column family is specified before modification.
		if (!tableDescriptor.hasFamily(family_Name)) {

			// Create the column descriptor.
			columnDescriptor = new HColumnDescriptor(family_Name);
			tableDescriptor.addFamily(columnDescriptor);

			try {
				// Disable the table to get the table offline before modifying
				// the table.
				admin.disableTable(TableName.valueOf(tableName));
				// Submit a modifyTable request.
				admin.modifyTable(TableName.valueOf(tableName), tableDescriptor);

				// Enable the table to get the table online after modifying the
				// table
				admin.enableTable(TableName.valueOf(tableName));
				logger.info("modify table " + tableName + " successful");

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (admin != null) {
					try {
						// Close the HBaseAdmin object.
						admin.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	@Override
	public void truncateTable(String tableName, boolean flag) {
		try {
			if (admin.tableExists(TableName.valueOf(tableName))) {
				admin.truncateTable(TableName.valueOf(tableName), flag);
			} else {
				logger.error("The table " + tableName + " is not exists!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void put(String tableName, String familyName, String rowKey, String qualifier, String value) {
		
		try {
			table = HBaseConfigurationUtil.getTable(tableName);

			Put put = new Put(Bytes.toBytes(rowKey));
			
//			logger.info(value);
			put.addImmutable(Bytes.toBytes(familyName), Bytes.toBytes(qualifier), Bytes.toBytes(value));
			listPuts.add(put);
			if(listPuts.size()==20000) { 
				table.put(listPuts); 
				listPuts=new ArrayList<Put>();
//				logger.info(tableName+"插入20000条成功");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null) {
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
public synchronized void putrest(String tablename) {
		
		try {
			if(listPuts!=null&&listPuts.size()>0){
				table.put(listPuts); 
//				logger.info(Base64.getFromBase64(tablename)+"插入剩余数据成功,共"+listPuts.size());
			}
			listPuts=new ArrayList<Put>();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null) {
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void put(List<Put> puts) {
		try {
			table.put(puts);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null) {
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	@Override
	public void put(String tableName, List<Map<String, String>> dataMaps) {
		table = HBaseConfigurationUtil.getTable(tableName);
		
		for(Map<String,String> map : dataMaps){
			List<String> list = new ArrayList<String>();
			for(Entry<String,String> entry : map.entrySet()){
				list.add(entry.getValue());
			}
			System.out.println(list.toString());
			
		}
	}
	
	@Override
	public void put(String tableName, String familyName, String rowKey, String qualifier, long timestamp,String value) {
		List<Put> listPuts = new ArrayList<Put>();
		try {
			table = HBaseConfigurationUtil.getTable(tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.addImmutable(Bytes.toBytes(familyName), Bytes.toBytes(qualifier), timestamp, Bytes.toBytes(value));
			listPuts.add(put);
			
			if(listPuts.size() == 5000) { table.put(put); }
			
			logger.info("put date to " + tableName + " success ");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null) {
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Map<String,String> getDataFromHBaseByRowKey(String tableName, String rowKey) {
		Map<String,String> resultMap = new HashMap<String,String>();
		try {
			table = HBaseConfigurationUtil.getTable(tableName);
			Get singleGet = new Get(Bytes.toBytes(rowKey));
			Result result = table.get(singleGet);
			for (Cell cell : result.rawCells()) {
				resultMap.put("rowkey",Bytes.toString(CellUtil.cloneRow(cell)));
				resultMap.put("family",Bytes.toString(CellUtil.cloneFamily(cell)));
				resultMap.put(Bytes.toString(CellUtil.cloneQualifier(cell)),Bytes.toString(CellUtil.cloneValue(cell)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public Map<String, String> getDataFromHBase(String tableName, String rowKey,String columnFamily, String qualifier) {
		Map<String, String> resultMap = new LinkedHashMap<String, String>();
		try {
			table = HBaseConfigurationUtil.getTable(tableName);
			Get get = new Get(Bytes.toBytes(rowKey));
			get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));

			Result result = table.get(get);
			for (Cell cell : result.rawCells()) {
				resultMap.put("rowkey",Bytes.toString(CellUtil.cloneRow(cell)));
				resultMap.put("family",Bytes.toString(CellUtil.cloneFamily(cell)));
				resultMap.put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	@Override
	public void deleteData(String tableName, String rowKey) {
		table = HBaseConfigurationUtil.getTable(tableName);
		try {
			Delete delete = new Delete(Bytes.toBytes(rowKey));
			table.delete(delete);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null) {
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void deleteBatch(String tableName, List<String> rowkeylList) {
		table = HBaseConfigurationUtil.getTable(tableName);
		try {
			List list = new ArrayList();
			for (String rowkey : rowkeylList) {
				logger.info("delete of rowkey==========="+rowkey);
				Delete delete = new Delete(Bytes.toBytes(rowkey));
				list.add(delete);
			}
			table.delete(list);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null) {
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void updateData(String tableName, String familyName, String rowKey, String qualifier, String value) {
		table = HBaseConfigurationUtil.getTable(tableName);
		
		Put put = new Put(Bytes.toBytes(rowKey));
		put.addImmutable(Bytes.toBytes(familyName), Bytes.toBytes(qualifier), Bytes.toBytes(value));
		try {
			table.checkAndPut(Bytes.toBytes(familyName), Bytes.toBytes(rowKey), Bytes.toBytes(qualifier), Bytes.toBytes(value),put);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (table != null) {
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void listTableNames() {
		try {
			TableName[] listTableNames = admin.listTableNames();
			System.out.println("Total table  : "+listTableNames.length);
			for (TableName tableName : listTableNames) {
				System.out.println("The table name is : "+ tableName);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public int scan(String tableName){
		Configuration config=null;
		try {
			config =HBaseConfigurationUtil.getHBaseConfiguration();
		} catch (IOException e1) {
			e1.printStackTrace();
			logger.info("config生成出错！！"); 
		}
        AggregationClient aggregationClient = new AggregationClient(config);
		Scan scan=new Scan();
		try {
			Long rowCount = aggregationClient.rowCount(TableName.valueOf(tableName), new LongColumnInterpreter(), scan);
			aggregationClient.close();
			return rowCount.intValue();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return 0;
	}
}