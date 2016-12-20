package Table;

import java.io.Serializable;
/**
 * 社区论坛表
 * @author c_chengdong
 *
 */
public class communityBbs implements Serializable{
	private static final long serialVersionUID = -8916197201577009867L;
	private Integer id;
	private String title;//论坛标题
	private String rqnum;//回复/人气数量
	private String author;//作者
	private String latewiter;//最后回复人及时间
	private String getTime;//获取时间
	private String baseUrl;//起始url
	private String url;//论坛内容url
	private String pnum;//总论坛数量
	private String text;//论坛内容
	private String synMark;
	private String synTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRqnum() {
		return rqnum;
	}
	public void setRqnum(String rqnum) {
		this.rqnum = rqnum;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLatewiter() {
		return latewiter;
	}
	public void setLatewiter(String latewiter) {
		this.latewiter = latewiter;
	}
	public String getGetTime() {
		return getTime;
	}
	public void setGetTime(String getTime) {
		this.getTime = getTime;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPnum() {
		return pnum;
	}
	public void setPnum(String pnum) {
		this.pnum = pnum;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
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
