package Table;


import java.io.Serializable;

/**
 * 根据客户地址采集周边社区信息存储表
 * @author CD
 *
 */
public class communityBaiduInfo implements Serializable{
	private static final long serialVersionUID = -6842440964769509595L;
	private Integer id;
	private String merchantName;//商户名称
	private String communityName;//社区名称
	private String province;//省份
	private String city;//城市
	private String merchantUrl;//商户url
	private String merchantAddress;//商户地址
	private String merchantTag;//商户标签
	private String merchantTel;//商户电话
	private String openTime;//营业时间
	private String merchantDesc;//商户描述
	private String storeIntroduction;//门店介绍
	private String feature;//特色
	private String longitude;//纬度
	private String latitude;//经度
	private String commentUrl;
	private String distance;//距离
	private String bbsUrl;//小区论坛url
	private String dianpingUrl;//小区点评url
	private String managerUrl;//小区详情url
	private String manageType;//基本信息0可用，1不可用
	private String bbsType;//bbs的状态0可用，1不可用
	private String dianpingType;//点评状态0可用，1不可用
	private String analyMark;
	private String nameMark;//是否把社区名存入地址表（0-分析；1-已分析出存入；2-未分析出分析）
	public String synMark;
	public String synTime; 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getMerchantUrl() {
		return merchantUrl;
	}
	public void setMerchantUrl(String merchantUrl) {
		this.merchantUrl = merchantUrl;
	}
	public String getMerchantAddress() {
		return merchantAddress;
	}
	public void setMerchantAddress(String merchantAddress) {
		this.merchantAddress = merchantAddress;
	}
	public String getMerchantTag() {
		return merchantTag;
	}
	public void setMerchantTag(String merchantTag) {
		this.merchantTag = merchantTag;
	}
	public String getMerchantTel() {
		return merchantTel;
	}
	public void setMerchantTel(String merchantTel) {
		this.merchantTel = merchantTel;
	}
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getMerchantDesc() {
		return merchantDesc;
	}
	public void setMerchantDesc(String merchantDesc) {
		this.merchantDesc = merchantDesc;
	}
	public String getStoreIntroduction() {
		return storeIntroduction;
	}
	public void setStoreIntroduction(String storeIntroduction) {
		this.storeIntroduction = storeIntroduction;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getCommentUrl() {
		return commentUrl;
	}
	public void setCommentUrl(String commentUrl) {
		this.commentUrl = commentUrl;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getBbsUrl() {
		return bbsUrl;
	}
	public void setBbsUrl(String bbsUrl) {
		this.bbsUrl = bbsUrl;
	}
	public String getDianpingUrl() {
		return dianpingUrl;
	}
	public void setDianpingUrl(String dianpingUrl) {
		this.dianpingUrl = dianpingUrl;
	}
	public String getManagerUrl() {
		return managerUrl;
	}
	public void setManagerUrl(String managerUrl) {
		this.managerUrl = managerUrl;
	}
	public String getManageType() {
		return manageType;
	}
	public void setManageType(String manageType) {
		this.manageType = manageType;
	}
	public String getBbsType() {
		return bbsType;
	}
	public void setBbsType(String bbsType) {
		this.bbsType = bbsType;
	}
	public String getDianpingType() {
		return dianpingType;
	}
	public void setDianpingType(String dianpingType) {
		this.dianpingType = dianpingType;
	}
	public String getAnalyMark() {
		return analyMark;
	}
	public void setAnalyMark(String analyMark) {
		this.analyMark = analyMark;
	}
	public String getNameMark() {
		return nameMark;
	}
	public void setNameMark(String nameMark) {
		this.nameMark = nameMark;
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
