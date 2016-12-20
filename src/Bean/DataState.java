package Bean;

import java.io.Serializable;


/**
 * 数据状态 2015-7-24
 * @author CD
 *
 */
public class DataState implements Serializable{
	
	private static final long serialVersionUID = -1726717703468938165L;
	private int id;
	private String lastupdatetime;//最近一次采集分析时间
	private int totaldata;//数据总量(开始)
	private int daydata;//当天数据量(开始)
	private Integer type;//类别 0 采集  1同步
	private String dataType;//数据类型（酒店、餐饮等）
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLastupdatetime() {
		return lastupdatetime;
	}
	public void setLastupdatetime(String lastupdatetime) {
		this.lastupdatetime = lastupdatetime;
	}
	public int getTotaldata() {
		return totaldata;
	}
	public void setTotaldata(int totaldata) {
		this.totaldata = totaldata;
	}
	public int getDaydata() {
		return daydata;
	}
	public void setDaydata(int daydata) {
		this.daydata = daydata;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
