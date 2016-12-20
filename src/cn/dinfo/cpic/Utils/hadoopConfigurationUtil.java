package cn.dinfo.cpic.Utils;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

public class hadoopConfigurationUtil {
	
	private static FileSystem fileSystem;
	private final static String PRINCIPAL = "username.client.kerberos.principal";
	private final static String KEYTAB = "username.client.keytab.file";
	private final static String CONFIGURATION_DIR_NAME = "configuration";
	private final static String KERBEROSFILE_DIR_NAME = "kerberosAuthFiles";
	private final static String[] CONFIGURATION_XML_NAME = {"core-site.xml","hdfs-site.xml","mapred-site.xml","yarn-site.xml"};
	private final static String[] KERBEROS_AUTH_FILES={"hdfs.keytab","krb5.conf"};
	private final static String HDFSPRINCIPAL = "hdfs/hadoop.hadoop.com@HADOOP.COM";
	private final static String HADOOP_SECURITY = "kerberos";
	
	
	
	
	public static void init() throws IOException {
		Configuration configuration = new Configuration();
		
		for(String confile: CONFIGURATION_XML_NAME){
			configuration.addResource(new Path(System.getProperty("user.dir")+File.separator+CONFIGURATION_DIR_NAME+File.separator+confile));
		}
		
		
		if (HADOOP_SECURITY.equalsIgnoreCase(configuration.get("hadoop.security.authentication"))) {
			configuration.set(PRINCIPAL, HDFSPRINCIPAL);
			configuration.set(KEYTAB,System.getProperty("user.dir") + File.separator + KERBEROSFILE_DIR_NAME + File.separator + KERBEROS_AUTH_FILES[1]);
			configuration.set(KEYTAB,System.getProperty("user.dir") + File.separator + KERBEROSFILE_DIR_NAME + File.separator + KERBEROS_AUTH_FILES[0]);

			String krbfilepath = System.getProperty("user.dir") + File.separator + KERBEROSFILE_DIR_NAME + File.separator+ KERBEROS_AUTH_FILES[1];
			
			System.setProperty("java.security.krb5.conf", krbfilepath);
			login(configuration);
		}
		try {
			fileSystem = FileSystem.get(configuration);
		} catch (IOException e) {
			throw new IOException("Get fileSystem failed");
		}

	}

	public static Boolean login(Configuration configuration) {
		boolean flag = false;
		UserGroupInformation.setConfiguration(configuration);
		try {
			UserGroupInformation.loginUserFromKeytab(configuration.get(PRINCIPAL), configuration.get(KEYTAB));
			System.out.println("UserGroupInformation.isLoginKeytabBased():" + UserGroupInformation.isLoginKeytabBased());
			flag = true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return flag;
	}

	private boolean createPath(final Path filePath) {
		try {
			if (!fileSystem.exists(filePath)) {
				fileSystem.mkdirs(filePath);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	private void mkdir(){
		Path destPath = new Path("/user/Brian1");
		if(!createPath(destPath)){
			System.err.println("failed to create destPath "+destPath);
			return;
		}
		System.out.println("success to create path " + destPath);
	}
	
	private void examples() throws IOException{
		try{
			init();
		}catch(IOException e){
			System.err.println("Init hdfs fileSystem failed!");
			e.printStackTrace();
			System.exit(1);
		}
		
		mkdir();
	}
	
	public static void main(String[] args) {
		hadoopConfigurationUtil cloudsTest = new hadoopConfigurationUtil();
		try {
			cloudsTest.examples();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
