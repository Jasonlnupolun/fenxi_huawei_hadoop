package Table;

import java.io.Serializable;
/**
 * 社区点评表
 * @author c_chengdong
 *
 */
public class communityDianping implements Serializable{
	private static final long serialVersionUID = -2958549629922369601L;
	private Integer id;
	private String text;//点评内容
	private String author;//作者
	private String star;//评论等级评分
	private String ptags;//价格 地段 交通 等等级标签
	private String postTime;//发表时间
	private String tdtags;//评论所提及到的标签
	private String source;//来源网站、客户端
	private String gradetags;//总评分等级标签
	private String dptags;//点评大家都在说的标签信息
	private String znum;//赞的数量
	private String hnum;//回复数量
	private String url;//点评url
	private String analyMark;//oec分析（0-未分析；1-已分析；3-内容为空
	private String synMark;
	private String sysTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public String getPtags() {
		return ptags;
	}
	public void setPtags(String ptags) {
		this.ptags = ptags;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	public String getTdtags() {
		return tdtags;
	}
	public void setTdtags(String tdtags) {
		this.tdtags = tdtags;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getGradetags() {
		return gradetags;
	}
	public void setGradetags(String gradetags) {
		this.gradetags = gradetags;
	}
	public String getDptags() {
		return dptags;
	}
	public void setDptags(String dptags) {
		this.dptags = dptags;
	}
	public String getZnum() {
		return znum;
	}
	public void setZnum(String znum) {
		this.znum = znum;
	}
	public String getHnum() {
		return hnum;
	}
	public void setHnum(String hnum) {
		this.hnum = hnum;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getSysTime() {
		return sysTime;
	}
	public void setSysTime(String sysTime) {
		this.sysTime = sysTime;
	}
}
