package Table;

import java.io.Serializable;

public class communityOriginal implements Serializable{

	private static final long serialVersionUID = -6230442010782450872L;
	private Integer id;
	private String cid;//身份证号
	private String ciFamilyAddress;//家庭住址
	private String ciMobile;//电话
	private String city;//城市
	private String province;//省份
	private String type;//0未获得城市，1获得城市，2获得城市失败
	private int useType;//地址可以找到社区状态 0 是可使用状态，1找到社区名称，2获取不到经纬度，3在范围内未找到社区名称
	private String communityName;//社区名
	private String synMark;
	private String synTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCiFamilyAddress() {
		return ciFamilyAddress;
	}
	public void setCiFamilyAddress(String ciFamilyAddress) {
		this.ciFamilyAddress = ciFamilyAddress;
	}
	public String getCiMobile() {
		return ciMobile;
	}
	public void setCiMobile(String ciMobile) {
		this.ciMobile = ciMobile;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getUseType() {
		return useType;
	}
	public void setUseType(int useType) {
		this.useType = useType;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getSynMark() {
		return synMark;
	}
	public void setSynMark(String synMark) {
		this.synMark = synMark;
	}
	public String getSynTime() {
		return synTime;
	}
	public void setSynTime(String synTime) {
		this.synTime = synTime;
	}
}
