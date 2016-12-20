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
public class Family implements Serializable {

	@XmlAttribute(name = "fname")
	private String fname;

	@XmlElements(value = { @XmlElement(type = Qualifier.class, name = "qualifier") })
	private List<Qualifier> qualifierList;

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public List<Qualifier> getQualifierList() {
		return qualifierList;
	}

	public void setQualifierList(List<Qualifier> qualifierList) {
		this.qualifierList = qualifierList;
	}

}