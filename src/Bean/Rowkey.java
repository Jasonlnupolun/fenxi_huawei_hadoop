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
public class Rowkey implements Serializable {

	@XmlAttribute(name = "rname")
	private String rname;

	@XmlElements(value = { @XmlElement(type = Family.class, name = "family") })
	private List<Family> familyList;

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public List<Family> getFamilyList() {
		return familyList;
	}

	public void setFamilyList(List<Family> familyList) {
		this.familyList = familyList;
	}

}