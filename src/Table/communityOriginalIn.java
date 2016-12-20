package Table;

import java.io.Serializable;

public class communityOriginalIn implements Serializable{

	private static final long serialVersionUID = -6230442010782450872L;
	private Integer id;
	private String cid;//身份证号
	private String ciFamilyAddress;//家庭住址
	private String ciMobile;//电话
	private String insuranceType;
	private String insuranceName;
	private String insuranceTime;
	private String zhufuxian;
	private String premiumStyle;
	private String claimSettlement;
	private String cancellationType;
	private String cancellationTime;
	private String communityName;
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
	public String getInsuranceType() {
		return insuranceType;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public String getInsuranceName() {
		return insuranceName;
	}
	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}
	public String getInsuranceTime() {
		return insuranceTime;
	}
	public void setInsuranceTime(String insuranceTime) {
		this.insuranceTime = insuranceTime;
	}
	public String getZhufuxian() {
		return zhufuxian;
	}
	public void setZhufuxian(String zhufuxian) {
		this.zhufuxian = zhufuxian;
	}
	public String getPremiumStyle() {
		return premiumStyle;
	}
	public void setPremiumStyle(String premiumStyle) {
		this.premiumStyle = premiumStyle;
	}
	public String getClaimSettlement() {
		return claimSettlement;
	}
	public void setClaimSettlement(String claimSettlement) {
		this.claimSettlement = claimSettlement;
	}
	public String getCancellationType() {
		return cancellationType;
	}
	public void setCancellationType(String cancellationType) {
		this.cancellationType = cancellationType;
	}
	public String getCancellationTime() {
		return cancellationTime;
	}
	public void setCancellationTime(String cancellationTime) {
		this.cancellationTime = cancellationTime;
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
