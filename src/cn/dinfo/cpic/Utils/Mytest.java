package cn.dinfo.cpic.Utils;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Connection;

public class Mytest {
public static void main(String[] args) {
	
	Connection connection  = HBaseConfigurationUtil.getConnection();
	try {
		connection.getAdmin();
		System.out.println("get admin success");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
