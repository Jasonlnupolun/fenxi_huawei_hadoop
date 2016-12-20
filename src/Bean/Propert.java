package Bean;

import java.io.InputStream;
import java.util.Properties;

/** brief description
 * <p>Date : 2015年5月28日 下午6:31:23</p>
 * <p>Module : </p>
 * <p>Description: </p>
 * <p>Remark : </p>
 * @author Administrator
 * @version 
 * <p>------------------------------------------------------------</p>
 * <p> 修改历史</p>
 * <p> 序号 日期 修改人 修改原因</p>
 * <p> 1 </p>
 */

public  class Propert {
//	private static final Log LOG = LogFactory.getLog(PropertiesUtils.class);
	private String prop;
	private final static Properties properties = new Properties();
	public Propert(String path) {
		this.prop=path;
		load();
	}
	private void load() {
//		LOG.info("加载属性文件==>" + this.prop);
		InputStream in = null;
		try {
			in = getClass().getClassLoader().getResourceAsStream(this.prop);
			if(in != null) {
				properties.load(in);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	public String get(String key){
		return properties.getProperty(key);
	}
}
