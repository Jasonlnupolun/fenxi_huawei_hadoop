package dbcp;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * action或者servlet直接访问的数据调用帮助类
 * 
 * @author zhangjie
 * 
 */
public class DBHelp {
	private static Log log=LogFactory.getLog(DBHelp.class);
	/**
	 * 插入bean实体 参数类型 1.表名 2.对应实体数据
	 * 
	 * @param tableName
	 * @param bean
	 * @return
	 */
	public static int insertBean(String tableName, Object bean) {
		// System.out.println(bean.toString());
		String sqlString = "insert into " + tableName + " ";
		String keys = "(";
		String values = " values(";
		Map map = new HashMap();
		map = myReflect.Bean2Map(bean);
		Iterator iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			Object value = map.get(key);
			if (value != null) {
				keys += key + ",";
				values += "'" + value.toString() + "',";
			}
		}
		keys = keys.substring(0, keys.length() - 1) + ")";
		values = values.substring(0, values.length() - 1) + ")";
		sqlString += keys + values;

		return executeSQL(sqlString);
	}

	/**
	 * 
	 * 执行增删改
	 * 
	 * @param sql
	 */
	public static int executeSQL(String sql, Object... args) {
		int result = 0;
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = JdbcUtils_DBCP.getConnection();
			stat = conn.prepareStatement(sql);
			if(args!=null){
				for (int i = 0; i < args.length; i++) {
					stat.setObject(i + 1, args[i]);
				}
			}
			result = stat.executeUpdate();
		} catch (SQLException e) {
//			log.error("出错");
//			log.info(sql);
		} finally {
			JdbcUtils_DBCP.release(conn, stat, null);
		}
		return result;
	}

	/**
	 * 执行查询
	 * 
	 * @param <T>
	 *            返回list<bean> 参数说明 classType 为返回实体的类型
	 * @throws SQLException
	 */
	public static List queryForListBean(String sql, Object... args) {
		Class classType = null;
		// 很据sql语句动态实例实体类型
		try {
			classType = Class.forName(getBeanUrl(sql));
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		}
		List list = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = JdbcUtils_DBCP.getConnection();
			stat = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				stat.setObject(i + 1, args[i]);
			}

			rs = stat.executeQuery();
			list = resultSetToList(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils_DBCP.release(conn, stat, rs);
		}
		if(list!=null&&list.size()>0){
			// 将list注入到bean
			return myReflect.convertList2Bean(classType, list);
		}
		return null;
	}

	/**
	 * 查询 返回单实体
	 * 
	 * @param <T>
	 * 
	 * @param <T>
	 * @param <T>
	 * @param pojo
	 * @param sql
	 * @param args
	 * @return
	 */
	public static Object queryForbean(String sql, Object... args) {

		Class classType = null;
		// 很据sql语句动态实例实体类型
		try {
			classType = Class.forName("dzu.sc.bean.Acount");
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		}

		Map map = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = JdbcUtils_DBCP.getConnection();
			stat = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				stat.setObject(i + 1, args[i]);
			}

			rs = stat.executeQuery();
			map = (Map) resultSetToList(rs).get(0);
			// 查询结束后映射到bean

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils_DBCP.release(conn, stat, rs);
		}
		if(map!=null&&map.size()>0){
			return myReflect.convertMap2Bean(classType, map);
		}
		return null;
	}

	/**
	 * 直接执行查询 返回list集合 注意 ：为了便于操作 该list中封装map实现
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public static List queryForList(String sql, Object... args) {
		List list = null;
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = JdbcUtils_DBCP.getConnection();
			stat = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				stat.setObject(i + 1, args[i]);
			}

			rs = stat.executeQuery();
			list = resultSetToList(rs);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils_DBCP.release(conn, stat, rs);
		}

		return list;
	}

	/**
	 * 直接执行查询 返回Map集合,方便多表直接查询 注意 ：为了便于操作 该list中封装map实现
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public static Map queryForMap(String sql, Object... args) {
		Map map = new HashMap();
		ResultSet rs = null;
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = JdbcUtils_DBCP.getConnection();
			stat = conn.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				stat.setObject(i + 1, args[i]);
			}

			rs = stat.executeQuery();
			map = (Map) resultSetToList(rs).get(0);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils_DBCP.release(conn, stat, rs);
		}

		return map;
	}

	/**
	 * 将结果result换为list
	 * */
	private static List resultSetToList(ResultSet rs)
			throws java.sql.SQLException {
		if (rs == null)
			return Collections.EMPTY_LIST;
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		List list = new ArrayList();

		Map rowData = new HashMap();
		while (rs.next()) {
			rowData = new HashMap(columnCount);

			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);
		}
		return list;
	}

	/**
	 * 读取配置文件 获取bean的包目录
	 */
	private static String getBeanUrl(String sql) {
		String[] beanNames = sql.split(" ");
		String beanName = "";
		String beanUrl = "";
		for (int i = 0; i < beanNames.length; i++) {
			if (beanNames[i].toString().toLowerCase().endsWith("from")) {
				beanName = beanNames[i + 1];
				break;
			}
		}
		Properties properties = new Properties();
		try {
			properties.load(DBHelp.class.getClassLoader().getResourceAsStream(
					"dbconfig.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		beanUrl = properties.getProperty("beanUrl") + "." + beanName;
		return beanUrl;
	}
	/**
	 * 
	 * 执行查询
	 * 
	 * @param sql
	 */
	public static Map<String, Object> query(String sql, Object... args) {
		Map<String, Object> map = new HashMap<String, Object>();
		Connection conn = null;
		ResultSet resultSet=null;
		PreparedStatement stat = null;
		try {
			conn = JdbcUtils_DBCP.getConnection();
			stat = conn.prepareStatement(sql);
			resultSet = stat.executeQuery();//返回查询结果
			ResultSetMetaData metaData = resultSet.getMetaData();
			int col_len = metaData.getColumnCount();
			while(resultSet.next()){
				for(int i=0; i<col_len; i++ ){
					String cols_name = metaData.getColumnName(i+1);
					Object cols_value = resultSet.getObject(cols_name);
					if(cols_value == null){
						cols_value = "";
					}
					map.put(cols_name, cols_value);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtils_DBCP.release(conn, stat, null);
		}
		return map;
	}
	public static void main(String[] args) {
		int result=executeSQL("insert into datastate(dacount) values(11)", null);
	}
}
