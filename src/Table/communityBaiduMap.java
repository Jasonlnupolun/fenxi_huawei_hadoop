package Table;

import java.io.Serializable;
/**
 * 百度景点表
 * @author c_chengdong
 *
 */
public class communityBaiduMap implements Serializable{

	private static final long serialVersionUID = -4118785478975462180L;
	public Integer id;
	public String merchantName;//商户名称
	public String merchantUrl;//商户URL
	public String merchantAddress;//商户地址
	public String merchantTag;//商户标签
	public String storeIntroduction;//门店介绍
	public String longitude;//纬度
	public String latitude;//经度
	public String distance;//距离
	public String getTime;//获取时间
	public String type;//0可用，1不可用
	public String oldlatandlng;//原始经纬度
	public String analyMark;//oec分析标志（0-未分析；1-已分析；3-内容为空
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
	public String getStoreIntroduction() {
		return storeIntroduction;
	}
	public void setStoreIntroduction(String storeIntroduction) {
		this.storeIntroduction = storeIntroduction;
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
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getGetTime() {
		return getTime;
	}
	public void setGetTime(String getTime) {
		this.getTime = getTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getOldlatandlng() {
		return oldlatandlng;
	}
	public void setOldlatandlng(String oldlatandlng) {
		this.oldlatandlng = oldlatandlng;
	}
	public String getAnalyMark() {
		return analyMark;
	}
	public void setAnalyMark(String analyMark) {
		this.analyMark = analyMark;
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
