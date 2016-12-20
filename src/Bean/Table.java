package Bean;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Table implements Serializable {

	@XmlAttribute(name = "tname")
	private String tname;
	
	@XmlElements(value = { @XmlElement(type = Rowkey.class, name = "rowkey") })
	private List<Rowkey> rowkeyList;

	
	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public List<Rowkey> getRowkeyList() {
		return rowkeyList;
	}

	public void setRowkeyList(List<Rowkey> rowkeyList) {
		this.rowkeyList = rowkeyList;
	}

}