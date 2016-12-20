package Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/** brief description
 * <p>Date : 2015年6月19日 下午1:42:04</p>
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
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Rowkeybean implements Serializable{
	@XmlElements(value = { @XmlElement(type = String.class, name = "rowkey") })
    List<String> rowkeylist=new ArrayList<String>();
	/**
	 * @return the rowkeylist
	 */
	public List<String> getRowkeylist() {
		return rowkeylist;
	}
	/**
	 * @param rowkeylist the rowkeylist to set
	 */
	public void setRowkeylist(List<String> rowkeylist) {
		this.rowkeylist = rowkeylist;
	}
}
