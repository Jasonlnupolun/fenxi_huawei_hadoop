package dbcp;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

/**
* @ClassName: JdbcUtils_DBCP
* @Description: 数据库连接工具类
*
*/ 
public class JdbcUtils_DBCP {
    private static DataSource ds = null;
    //在静态代码块中创建数据库连接池
    static{
        try{
            //加载dbcpconfig.properties配置文件
//            InputStream in = JdbcUtils_DBCP.class.getClassLoader().getResourceAsStream("conf/dbcpconfig.properties");
        	String path=System.getProperty("user.dir")+File.separator+"conf"+File.separator+"dbcpconfig.properties";
			File file = new File(path);
			FileInputStream in = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(in,"utf-8");
        	Properties prop = new Properties();
            prop.load(isr);
            //创建数据源
            ds = BasicDataSourceFactory.createDataSource(prop);
        }catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    /**
    * @Method: getConnection
    * @Description: 从数据源中获取数据库连接
    * @return Connection
    * @throws SQLException
    */ 
    public static Connection getConnection() throws SQLException{
        //从数据源中获取数据库连接
        return ds.getConnection();
    }
    
    /**
    * @Method: release
    * @Description: 释放资源，
    * 释放的资源包括Connection数据库连接对象，负责执行SQL命令的Statement对象，存储查询结果的ResultSet对象
    *
    * @param conn
    * @param st
    * @param rs
    */ 
    public static void release(Connection conn,Statement st,ResultSet rs){
        if(rs!=null){
            try{
                //关闭存储查询结果的ResultSet对象
                rs.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            rs = null;
        }
        if(st!=null){
            try{
                //关闭负责执行SQL命令的Statement对象
                st.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if(conn!=null){
            try{
                //将Connection连接对象还给数据库连接池
                conn.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}