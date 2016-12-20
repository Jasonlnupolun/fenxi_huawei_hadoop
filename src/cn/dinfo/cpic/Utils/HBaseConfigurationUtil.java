package cn.dinfo.cpic.Utils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.security.User;
import org.apache.hadoop.hbase.zookeeper.ZKUtil;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.log4j.Logger;

import Sync.GlobelFlag;


/**
 *  this class includ two methods.
 *  getHBaseConfiguration() return the hbase configuration instance
 *  getConnection() return the hbase connection instance
 *  
 * @author c_sulinbing
 * @version 1.0
 */

public class HBaseConfigurationUtil {
	
	private final static Logger logger = Logger.getLogger(HBaseConfigurationUtil.class);
	
	private final static String PRINCIPAL_KEY = "username.client.kerberos.principal";
	private final static String KEYTAB_KEY = "username.client.keytab.file";
	
	private final static String HDFS_KERBEROS_PRINCIPAL = "hdfs/hadoop.hadoop.com@HADOOP.COM";
	private final static String HADOOP_SECURITY = "kerberos";
	private final static String HADOOP_USER_NAME="HADOOP_USER_NAME";
	
	
	private final static String HBASE_KERBEROS_PRINCIPAL = "hbase/hadoop.hadoop.com@HADOOP.COM";
	private final static String ZKSERVER_KERBEROS_PRINCIPAL = "zookeeper/hadoop.hadoop.com";
	
	private final static String[] KERBEROS_AUTH_FILES={"hdfs.keytab","hbase.keytab","jaas.conf","krb5.conf"};
	
	private final static String CONF_FILE_DIR_NAME = "configuration";
	private final static String KERBEROS_AUTH_FILES_DIR_NAME = "kerberosAuthFiles";
	
	private final static String[] configurationXmlName = {"core-site.xml","hdfs-site.xml","hbase-site.xml"};
	
	private static Connection connection = null;
	private static Admin admin = null;
	private static Table table = null;
	
	
	/**
	 * 	load hbase configuration files and auth files
	 *  use configuration files create hbase configuration instance
	 *  use autfiles longin hbase and zookeeper
	 *  
	 * @return Configuration
	 * @throws IOException
	 */
	public static Configuration getHBaseConfiguration() throws IOException {
		
		Configuration configuration = HBaseConfiguration.create(); 
		
		String conFilesPath = System.getProperty("user.dir")+File.separator+CONF_FILE_DIR_NAME+File.separator;
//		logger.info("The configuration Files Path is  " + conFilesPath);
		
		for(String confile: configurationXmlName){
			configuration.addResource(new Path(conFilesPath+confile));
//			logger.info("Loading the hbase configuration files: "+conFilesPath+confile );
		}
		
		if(User.isHBaseSecurityEnabled(configuration)){
			
			//authentication file dir 
			String authFilesPath = System.getProperty("user.dir")+File.separator+KERBEROS_AUTH_FILES_DIR_NAME+File.separator;
//			logger.info("The auth files path is  "+ authFilesPath);
			
			//set zookeeper server pricipal for login zkserver
			System.setProperty("zookeeper.server.principal",ZKSERVER_KERBEROS_PRINCIPAL);
//			logger.info("set the  zookeeper.server.principal " + ZKSERVER_KERBEROS_PRINCIPAL);
			
			//jaas.conf file, it is included in the client package file
			System.setProperty("java.security.auth.login.config",authFilesPath+KERBEROS_AUTH_FILES[2]);
//			logger.info("set the java.security.auth.login.config from  "+authFilesPath+KERBEROS_AUTH_FILES[2]);
			
			
			//set the kerberos server info, point to the kerberos client
			System.setProperty("java.security.krb5.conf",authFilesPath+KERBEROS_AUTH_FILES[3]);
//			logger.info("set the java.security.krb5.conf from "+ authFilesPath+KERBEROS_AUTH_FILES[3]);
			
			//set the hadoop username 
			System.setProperty(HADOOP_USER_NAME, HBASE_KERBEROS_PRINCIPAL);
			
			//set the keytab file name 
			configuration.set(KEYTAB_KEY,authFilesPath+KERBEROS_AUTH_FILES[1]);
			configuration.set(KEYTAB_KEY,HBASE_KERBEROS_PRINCIPAL);
			
			//if the hadoop security is kerberos, use the hdfs user to login 
			if (HADOOP_SECURITY.equalsIgnoreCase(configuration.get("hadoop.security.authentication"))) {
				configuration.set(KEYTAB_KEY,authFilesPath+KERBEROS_AUTH_FILES[0]);
				configuration.set(PRINCIPAL_KEY, HDFS_KERBEROS_PRINCIPAL);
				boolean loginFlag = login(configuration);
				if(loginFlag){
					logger.info("Login HDFS Successful!");
				}else{
					logger.error("Login failed. please check the username and check the password file hdfs.keytab.");
				}
				
			}
			
			try{
				//login hbase server 
				User.login(configuration, KEYTAB_KEY, PRINCIPAL_KEY,InetAddress.getLocalHost().toString());
				//login zookeeper server 
				ZKUtil.loginClient(configuration, KEYTAB_KEY, PRINCIPAL_KEY,InetAddress.getLocalHost().toString());
				
			}catch(UnknownHostException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
		logger.info("create configuration instance successful!");
		
		//return the HBase configuration instance
		return configuration;
		
	}
	
	
	/**
	 *  use hbase configuration get hbase Connection instance from ConnectionFactory
	 *  hbase and zookeeper have to longin success 
	 * @author c_sulinbing
	 * @return Connection
	 */
	public static Connection getConnection(){
		try {
				connection = ConnectionFactory.createConnection(getHBaseConfiguration());
		} catch (IOException e) {
			GlobelFlag.setSqurl(false);
	  		GlobelFlag.setHotelManager(false);
	  		GlobelFlag.setHotelDianping(false);
	  		GlobelFlag.setFoodManager(false);
	  		GlobelFlag.setFoodDianping(false);
			e.printStackTrace();
		}
		
		logger.info("create hbase Connection instance successful!");
		//return the hbase Connection instance 
		return connection;
	}
	
	
	/**
	 *  use the hadoop configuration login to hdfs
	 * @param configuration
	 * @return
	 */
	public static Boolean login(Configuration configuration) {
		boolean flag = false;
		UserGroupInformation.setConfiguration(configuration);
		try {
			UserGroupInformation.loginUserFromKeytab(configuration.get(PRINCIPAL_KEY), configuration.get(KEYTAB_KEY));
			System.out.println("UserGroupInformation.isLoginKeytabBased():" + UserGroupInformation.isLoginKeytabBased());
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 
	 * @return HBase Admin Object
	 */
	public static Admin getAdmin(){
		try {
			System.out.println("获取连接...");
			connection = getConnection();
			System.out.println("获取连接成功...");
			System.out.println("获取admin");
			admin = connection.getAdmin();
			System.out.println("获取admin成功");
		} catch (IOException e) {
		  		GlobelFlag.setSqurl(false);
		  		GlobelFlag.setHotelManager(false);
		  		GlobelFlag.setHotelDianping(false);
		  		GlobelFlag.setFoodManager(false);
		  		GlobelFlag.setFoodDianping(false);
		  		e.printStackTrace();
		}
		return admin;
	}
	
	/**
	 * 
	 * @param tableName
	 * @return
	 */
	public static Table getTable(String tableName){
		try {
			table = connection.getTable(TableName.valueOf(tableName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return table;
	}
	
}
