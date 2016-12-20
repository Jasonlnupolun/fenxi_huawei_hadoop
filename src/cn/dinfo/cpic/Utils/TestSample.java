package cn.dinfo.cpic.Utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.index.ColumnQualifier.ValueType;
import org.apache.hadoop.hbase.index.Constants;
import org.apache.hadoop.hbase.index.IndexSpecification;
import org.apache.hadoop.hbase.index.client.IndexAdmin;
import org.apache.hadoop.hbase.index.coprocessor.master.IndexMasterObserver;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.io.encoding.DataBlockEncoding;
import org.apache.hadoop.hbase.ipc.CoprocessorRpcChannel;
import org.apache.hadoop.hbase.protobuf.ProtobufUtil;
import org.apache.hadoop.hbase.protobuf.generated.AccessControlProtos;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.hbase.security.access.AccessControlLists;
import org.apache.hadoop.hbase.security.access.Permission;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.zookeeper.ZKUtil;

import com.google.protobuf.ServiceException;

/**
 * HBase Development Instruction Sample Code The sample code uses user information as source data,it introduces how to
 * implement businesss process development using HBase API
 */
public class TestSample {

  private static String tableName = "hbase_simple_table";
  public void testSample() throws Exception {
    testCreateTable();
    testMultiSplit();
    testPut();
    createIndex();
    testScanDataByIndex();
    testModifyTable();
    testGet();
    testScanData();
    testSingleColumnValueFilter();
    testFilterList();
    testDelete();
    dropIndex();
    dropTable();
    testCreateMOBTable();
    testMOBDataInsertion();
    testMOBDataRead();
    dropTable();
  }

  public void testMOBDataRead() {
    try {
      Configuration config = getConfiguration();
      Connection conn = ConnectionFactory.createConnection(config);
      // get table object representing table tableName
      Table table = conn.getTable(TableName.valueOf(tableName));
      Admin admin = conn.getAdmin();
      admin.flush(table.getName());
      Scan scan = new Scan();
      // get table scanner
      ResultScanner scanner = table.getScanner(scan);
      for (Result result : scanner) {
        byte[] value = result.getValue(Bytes.toBytes("mobcf"),
            Bytes.toBytes("cf1"));
        String string = Bytes.toString(value);
        System.out.println("value:" + string);
      }
      System.out.println("MOB data read successfully....");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testMOBDataInsertion() {
    try {
      Configuration config = getConfiguration();
      Connection conn = ConnectionFactory.createConnection(config);
      // set row name to "row"
      Put p = new Put(Bytes.toBytes("row"));
      byte[] value = new byte[1000];
      // set the column value of column family mobcf with the value of "cf1"
      p.addColumn(Bytes.toBytes("mobcf"), Bytes.toBytes("cf1"), value);
      // get the table object represent table tableName
      Table table = conn.getTable(TableName.valueOf(tableName));
      // put data
      table.put(p);
      // close table object
      table.close();
      System.out.println("mob data inserted successfully....");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("deprecation")
  public void testCreateMOBTable() {
    try {
      Configuration config = getConfiguration();
      // Create HBaseAdmin instance
      HBaseAdmin admin = new HBaseAdmin(config);
      HTableDescriptor tabDescriptor = new HTableDescriptor(
          TableName.valueOf(tableName));
      HColumnDescriptor mob = new HColumnDescriptor("mobcf");
      // Open mob function
      mob.setMobEnabled(true);
      // Set mob threshold
      mob.setMobThreshold(10L);
      tabDescriptor.addFamily(mob);
      admin.createTable(tabDescriptor);
      System.out.println("MOB Table is created successfully....");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void testMultiSplit() {
    
    Configuration conf = getConfiguration();
    
    HTable table = null;
    HBaseAdmin admin = null;
    try {
      admin = new HBaseAdmin(conf);
      // initilize a HTable object
      table = new HTable(conf, tableName);
      Map<HRegionInfo, ServerName> regionMap = table.getRegionLocations();
      Set<HRegionInfo> regionSet = regionMap.keySet();
      byte[][] sk = new byte[4][];
      sk[0] = "A".getBytes();
      sk[1] = "D".getBytes();
      sk[2] = "F".getBytes();
      sk[3] = "H".getBytes();
      for (HRegionInfo regionInfo : regionSet) {
        admin.multiSplit(regionInfo.getRegionName(), sk);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      if (table != null) {
        try {
          // Close table object
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public Configuration getConfiguration() {
    Configuration conf = HBaseConfiguration.create();
    if (User.isHBaseSecurityEnabled(conf)) {
      String confDirPath = System.getProperty("user.dir") + File.separator + "conf" + File.separator;

      // set zookeeper server pricipal
      System.setProperty("zookeeper.server.principal", "zookeeper/hadoop.hadoop.com");
      // jaas.conf file, it is included in the client pakcage file
      System.setProperty("java.security.auth.login.config", confDirPath + "jaas.conf");

      // set the kerberos server info,point to the kerberosclient
      System.setProperty("java.security.krb5.conf", confDirPath + "krb5.conf");
      // set the keytab file name
      conf.set("username.client.keytab.file", confDirPath + "user.keytab");
      // set the user's principal
      conf.set("username.client.kerberos.principal", "hbaseuser1");
      try {
        User.login(conf, "username.client.keytab.file", "username.client.kerberos.principal", InetAddress
            .getLocalHost().getCanonicalHostName());

        ZKUtil.loginClient(conf, "username.client.keytab.file", "username.client.kerberos.principal", InetAddress
            .getLocalHost().getCanonicalHostName());
      } catch (UnknownHostException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return conf;
  }

  /**
   * Create user info table
   */
  public void testCreateTable() {
    
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    // Specify the table descriptor.
    HTableDescriptor htd = new HTableDescriptor(TableName.valueOf(tableName));

    // Set the column family name to info.
    HColumnDescriptor hcd = new HColumnDescriptor("info");

    // Set data encoding methods，HBase provides DIFF，FAST_DIFF，PREFIX
    // and PREFIX_TREE
    hcd.setDataBlockEncoding(DataBlockEncoding.FAST_DIFF);

    // Set compression methods, HBase provides two default compression
    // methods:GZ and SNAPPY
    // GZ has the highest compression rate,but low compression and
    // decompression effeciency,fit for cold data
    // SNAPPY has low compression rate, but high compression and
    // decompression effeciency,fit for hot data.
    // it is advised to use SANPPY
    hcd.setCompressionType(Compression.Algorithm.SNAPPY);

    htd.addFamily(hcd);
    //System.setProperty("zookeeper.server.principal", "zookeeper/hadoop");

    HBaseAdmin admin = null;
    try {
      // Instantiate an HBaseAdmin object.
      admin = new HBaseAdmin(conf);
      System.out.println("created admin  successfully");
      if (!admin.tableExists(tableName)) {
        System.out.println("creating table");
        admin.createTable(htd);
        System.out.println(admin.getClusterStatus());
        System.out.println(admin.listNamespaceDescriptors());
      } else {
        System.out.println("table already exists");
      }
    } catch (IOException e) {
      System.out.println("Connection failed");
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
  public void dropTable() {
   
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    HBaseAdmin admin = null;
    try {
      admin = new HBaseAdmin(conf);
      if (admin.tableExists(tableName)) {
        // Disable the table before deleting it.
        admin.disableTable(tableName);

        // Delete table.
        admin.deleteTable(tableName);
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

  /**
   * Modify a Table
   */
  public void testModifyTable() {
   
    // Specify the column family name.
    byte[] familyName = Bytes.toBytes("education");
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    HBaseAdmin admin = null;
    try {
      // Instantiate an HBaseAdmin object.
      admin = new HBaseAdmin(conf);

      // Obtain the table descriptor.
      HTableDescriptor htd = admin.getTableDescriptor(Bytes.toBytes(tableName));

      // Check whether the column family is specified before modification.
      if (!htd.hasFamily(familyName)) {
        // Create the column descriptor.
        HColumnDescriptor hcd = new HColumnDescriptor(familyName);
        htd.addFamily(hcd);

        // Disable the table to get the table offline before modifying
        // the table.
        admin.disableTable(tableName);
        // Submit a modifyTable request.
        admin.modifyTable(Bytes.toBytes(tableName), htd);
        // Enable the table to get the table online after modifying the
        // table.
        admin.enableTable(tableName);
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

  /**
   * Insert data
   */
  public void testPut() {

    // Specify the column family name.
    byte[] familyName = Bytes.toBytes("info");
    // Specify the column name.
    byte[][] qualifiers = { Bytes.toBytes("name"), Bytes.toBytes("gender"),
        Bytes.toBytes("age"), Bytes.toBytes("address") };
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    
    HTable table = null;
    try {
      // Instantiate an HTable object.
      table = new HTable(conf, tableName);
      List<Put> puts = new ArrayList<Put>();
      // Instantiate a Put object.
      Put put = new Put(Bytes.toBytes("012005000201"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Zhang San"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Male"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(19)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Shenzhen, Guangdong"));
      puts.add(put);

      put = new Put(Bytes.toBytes("012005000202"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Li Wanting"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Female"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(23)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Shijiazhuang, Hebei"));
      puts.add(put);

      put = new Put(Bytes.toBytes("012005000203"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Wang Ming"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Male"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(26)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Ningbo, Zhejiang"));
      puts.add(put);

      put = new Put(Bytes.toBytes("012005000204"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Li Gang"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Male"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(18)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Xiangyang, Hubei"));
      puts.add(put);

      put = new Put(Bytes.toBytes("012005000205"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Zhao Enru"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Female"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(21)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Shangrao, Jiangxi"));
      puts.add(put);

      put = new Put(Bytes.toBytes("012005000206"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Chen Long"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Male"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(32)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Zhuzhou, Hunan"));
      puts.add(put);

      put = new Put(Bytes.toBytes("012005000207"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Zhou Wei"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Female"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(29)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Nanyang, Henan"));
     

      put = new Put(Bytes.toBytes("012005000208"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Yang Yiwen"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Female"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(30)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Kaixian, Chongqing"));
      puts.add(put);
      

      put = new Put(Bytes.toBytes("012005000209"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Xu Bing"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Male"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(26)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Weinan, Shaanxi"));
      puts.add(put);

      put = new Put(Bytes.toBytes("012005000210"));
      put.add(familyName, qualifiers[0], Bytes.toBytes("Xiao Kai"));
      put.add(familyName, qualifiers[1], Bytes.toBytes("Male"));
      put.add(familyName, qualifiers[2], Bytes.toBytes(new Long(25)));
      put.add(familyName, qualifiers[3], Bytes.toBytes("Dalian, Liaoning"));
      puts.add(put);

      
      
      // Submit a put request.
      table.put(puts);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (table != null) {
        try {
          // Close the HTable object.
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * deleting data
   */
  public void testDelete() {

    // Set RowKey to 012005000201.
    byte[] rowKey = Bytes.toBytes("012005000201");
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    HTable table = null;
    try {
      // Instantiate an HTable object.
      table = new HTable(conf, tableName);

      // Instantiate an Delete object.
      Delete delete = new Delete(rowKey);

      // Submit a delete request.
      table.delete(delete);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (table != null) {
        try {
          // Close the HTable object.
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Get Data
   */
  public void testGet() {

    // Specify the column family name.
    byte[] familyName = Bytes.toBytes("info");
    // Specify the column name.
    byte[][] qualifier = { Bytes.toBytes("name"), Bytes.toBytes("address") };
    // Specify RowKey.
    byte[] rowKey = Bytes.toBytes("012005000201");
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    HTable table = null;
    try {
      // Create the Configuration instance.
      table = new HTable(conf, tableName);

      // Instantiate a Get object.
      Get get = new Get(rowKey);

      // Set the column family name and column name.
      get.addColumn(familyName, qualifier[0]);
      get.addColumn(familyName, qualifier[1]);

      // Submit a get request.
      Result result = table.get(get);

      // Print query results.
      for (Cell cell : result.rawCells()) {
        System.out.println(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
            + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
            + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
            + Bytes.toString(CellUtil.cloneValue(cell)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (table != null) {
        try {
          // Close the HTable object.
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void testScanData() {

    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    HTable table = null; 
    // Instantiate a ResultScanner object.
    ResultScanner rScanner = null;
    try {
      // Create the Configuration instance.
      table = new HTable(conf, tableName);

      // Instantiate a Get object.
      Scan scan = new Scan();
      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"));

      // Set the cache size.
      scan.setCaching(5000);
      scan.setBatch(2);

      // Submit a scan request.
      rScanner = table.getScanner(scan);

      // Print query results.
      for (Result r = rScanner.next(); r != null; r = rScanner.next()) {
        for (Cell cell : r.rawCells()) {
          System.out.println(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
              + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
              + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
              + Bytes.toString(CellUtil.cloneValue(cell)));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (rScanner != null) {
        // Close the scanner object.
        rScanner.close();
      }
      if (table != null) {
        try {
          // Close the HTable object.
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void testSingleColumnValueFilter() {

    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    HTable table = null;
    
    // Instantiate a ResultScanner object.
    ResultScanner rScanner = null;
    
    try {
      // Create the Configuration instance.
      table = new HTable(conf, tableName);

      // Instantiate a Get object.
      Scan scan = new Scan();
      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"));

      // Set the filter criteria.
      SingleColumnValueFilter filter = new SingleColumnValueFilter(
          Bytes.toBytes("info"), Bytes.toBytes("name"), CompareOp.EQUAL,
          Bytes.toBytes("Xu Bing"));

      scan.setFilter(filter);

      // Submit a scan request.
      rScanner = table.getScanner(scan);

      // Print query results.
      for (Result r = rScanner.next(); r != null; r = rScanner.next()) {
        for (Cell cell : r.rawCells()) {
          System.out.println(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
              + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
              + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
              + Bytes.toString(CellUtil.cloneValue(cell)));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
        if (rScanner != null) {
            // Close the scanner object.
            rScanner.close();
          }
      if (table != null) {
        try {
          // Close the HTable object.
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void testFilterList() {

    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    HTable table = null;
    
    // Instantiate a ResultScanner object.
    ResultScanner rScanner = null;
    
    try {
      // Create the Configuration instance.
      table = new HTable(conf, tableName);

      // Instantiate a Get object.
      Scan scan = new Scan();
      scan.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"));

      // Instantiate a FilterList object in which filters have "and"
      // relationship with each other.
      FilterList list = new FilterList(Operator.MUST_PASS_ALL);
      // Obtain data with age of greater than or equal to 20.
      list.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("age"), CompareOp.GREATER_OR_EQUAL, Bytes.toBytes(new Long(20))));
      // Obtain data with age of less than or equal to 29.
      list.addFilter(new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("age"), CompareOp.LESS_OR_EQUAL, Bytes.toBytes(new Long(29))));

      scan.setFilter(list);


      // Submit a scan request.
      rScanner = table.getScanner(scan);
      // Print query results.
      for (Result r = rScanner.next(); r != null; r = rScanner.next()) {
        for (Cell cell : r.rawCells()) {
          System.out.println(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
              + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
              + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
              + Bytes.toString(CellUtil.cloneValue(cell)));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
        if (rScanner != null) {
            // Close the scanner object.
            rScanner.close();
          }
      if (table != null) {
        try {
          // Close the HTable object.
          table.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void testAggregate() {

    byte[] table_Name = Bytes.toBytes(tableName);
    // Specify the column family name.
    byte[] family = Bytes.toBytes("info");
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    AggregationClient aggregationClient = new AggregationClient(conf);

    // Instantiate a Get object.
    Scan scan = new Scan();
    scan.addFamily(family);
    scan.addColumn(family, Bytes.toBytes("age"));
    try {
      // Count the number of rows.
      long rowCount = aggregationClient.rowCount(TableName.valueOf(table_Name),
          null, scan);

      System.out.println("row count is " + rowCount);

      // Count the maximum value.
      long max = aggregationClient.max(TableName.valueOf(table_Name),
          new LongColumnInterpreter(), scan);
      System.out.println("max number is " + max);

      // Count the minimum value.
      long min = aggregationClient.min(TableName.valueOf(table_Name),
          new LongColumnInterpreter(), scan);
      System.out.println("min number is " + min);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public void createIndex() {
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    
    String indexName = "index_name";

    // Create index instance
    IndexSpecification iSpec = new IndexSpecification(indexName);

    iSpec.addIndexColumn(new HColumnDescriptor("info"), "name", ValueType.String, 100);
    
    IndexAdmin iAdmin = null;
    HBaseAdmin admin = null;
    try {
      // Instantiate IndexAdmin Object
      iAdmin = new IndexAdmin(conf);

      // Create Secondary Index
      iAdmin.addIndex(TableName.valueOf(tableName),
          iSpec);
      
      admin = new HBaseAdmin(conf);
      
      //Specify the encryption type of indexed column 
      HTableDescriptor htd = admin.getTableDescriptor(Bytes
          .toBytes(tableName));
      admin.disableTable(tableName);
      htd = admin.getTableDescriptor(Bytes.toBytes(tableName));
      //Instantiate index column description.
      HColumnDescriptor indexColDesc = new HColumnDescriptor(
          IndexMasterObserver.DEFAULT_INDEX_COL_DESC);
      
      //Set the description of index as the HTable description.
      htd.setValue(Constants.INDEX_COL_DESC_BYTES,
          indexColDesc.toByteArray());
      admin.modifyTable(tableName, htd);
      admin.enableTable(tableName);

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (admin != null) {
          try {
            admin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
      }
      if (iAdmin != null) {
        try {
          // Close IndexAdmin Object
          iAdmin.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Scan data by secondary index.
   */
  public void testScanDataByIndex() {
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    HTable table = null;
    ResultScanner scanner = null;
    try {
      table = new HTable(conf, tableName);
      // Create a filter for indexed column.
      Filter filter = new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("name"),
          CompareOp.EQUAL, "Li Gang".getBytes());
      Scan scan = new Scan();
      scan.setFilter(filter);
      scanner = table.getScanner(scan);
      System.out.println("Scan indexed data.");
      for (Result result : scanner) {
        for (Cell cell : result.rawCells()) {
          System.out.println(Bytes.toString(CellUtil.cloneRow(cell)) + ":"
              + Bytes.toString(CellUtil.cloneFamily(cell)) + ","
              + Bytes.toString(CellUtil.cloneQualifier(cell)) + ","
              + Bytes.toString(CellUtil.cloneValue(cell)));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (scanner != null) {
        // Close the scanner object.
        scanner.close();
      }
      try {
        if (table != null) {
          table.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void dropIndex() {
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    String indexName = "index_name";

    IndexAdmin iAdmin = null;
    try {
      // Instantiate IndexAdmin Object
      iAdmin = new IndexAdmin(conf);

      // Delete Secondary Index
      iAdmin.dropIndex(TableName.valueOf(tableName), indexName);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (iAdmin != null) {
        try {
          // Close Secondary Index
          iAdmin.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public void grantACL() {
    // Create the Configuration instance.
    Configuration conf = getConfiguration();

    String user = "huawei";
    String permissions = "RW";
   
    String familyName = "info";
    String qualifierName = "name";

    HTable mt = null;
    HBaseAdmin hAdmin = null;
    try {
      // Create ACL Instance
      mt = new HTable(conf, AccessControlLists.ACL_TABLE_NAME);

      CoprocessorRpcChannel service = mt
          .coprocessorService(HConstants.EMPTY_START_ROW);
      AccessControlProtos.AccessControlService.BlockingInterface protocol = AccessControlProtos.AccessControlService
          .newBlockingStub(service);
      Permission perm = new Permission(Bytes.toBytes(permissions));

      hAdmin = new HBaseAdmin(conf);
      HTableDescriptor ht = hAdmin.getTableDescriptor(TableName
          .valueOf(tableName));

      // Judge whether the table exists
      if (hAdmin.tableExists(tableName)) {
        // Judge whether ColumnFamily exists
        if (ht.hasFamily(Bytes.toBytes(familyName))) {
          // grant permission
          ProtobufUtil.grant(protocol, user, TableName.valueOf(tableName),
              Bytes.toBytes(familyName),
              (qualifierName == null ? null : Bytes.toBytes(qualifierName)),
              perm.getActions());
        } else {
          // grant permission
          ProtobufUtil.grant(protocol, user, TableName.valueOf(tableName),
              null, null, perm.getActions());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    } finally {
      if (mt != null) {
        try {
          // Close
          mt.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      if (hAdmin != null) {
        try {
          // Close HbaseAdmin Object
          hAdmin.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void main(String[] args) {
    TestSample ts = new TestSample();
    try {
      ts.testSample();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }
}
