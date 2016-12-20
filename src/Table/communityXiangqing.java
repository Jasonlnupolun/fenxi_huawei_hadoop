package Table;


import java.io.Serializable;
/**
 * 根据社区表采集社区详情表
 * @author CD
 *
 */
public class communityXiangqing implements Serializable{

	private static final long serialVersionUID = 66651699106586433L;
	private int id;
	private String name;//小区名称
	private String xqdz;//小区地址
	private String ssqy;//所属区域
	private String yb;//邮编
	private String cqms;//产权描述
	private String wylb;//物业类别
	private String jgsj;//竣工时间
	private String kfs;//开发商
	private String jzjg;//建筑结构
	private String jzlb;//建筑类别
	private String jzmj;//建筑面积
	private String zdmj;//占地面积
	private String dqhs;//当期用户
	private String zhs;//总户数
	private String lhl;//绿化率
	private String rjl;//容积率
	private String wyf;//物业费
	private String fjxx;//附加信息
	private String wybgdd;//物业办公地点
	private String gs;//供水
	private String gn;//供暖
	private String gd;//供电
	private String rq;//燃气
	private String txsb;//通讯设备
	private String wsfw;//卫生服务
	private String dtfw;//电梯服务
	private String aqgl;//安全管理
	private String tcw;//停车位
	private String xqjj;//小区简介
	private String jtzk;//交通状况
	private String zbxx;//周边信息
	private String pirce;//价格
	private String url;//小区详情url
	private String hbsy;//环比上月
	private String tbqn;//同比去年
	private String jjlq;//就近楼群
	private String lscjxx;//历史成交信息
	private String dls;//代理商
	private String analyMark;//OEC分析oec分析（0-未分析；1-已分析；3-内容为空
	public String synMark;
	public String synTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getXqdz() {
		return xqdz;
	}
	public void setXqdz(String xqdz) {
		this.xqdz = xqdz;
	}
	public String getSsqy() {
		return ssqy;
	}
	public void setSsqy(String ssqy) {
		this.ssqy = ssqy;
	}
	public String getYb() {
		return yb;
	}
	public void setYb(String yb) {
		this.yb = yb;
	}
	public String getCqms() {
		return cqms;
	}
	public void setCqms(String cqms) {
		this.cqms = cqms;
	}
	public String getWylb() {
		return wylb;
	}
	public void setWylb(String wylb) {
		this.wylb = wylb;
	}
	public String getJgsj() {
		return jgsj;
	}
	public void setJgsj(String jgsj) {
		this.jgsj = jgsj;
	}
	public String getKfs() {
		return kfs;
	}
	public void setKfs(String kfs) {
		this.kfs = kfs;
	}
	public String getJzjg() {
		return jzjg;
	}
	public void setJzjg(String jzjg) {
		this.jzjg = jzjg;
	}
	public String getJzlb() {
		return jzlb;
	}
	public void setJzlb(String jzlb) {
		this.jzlb = jzlb;
	}
	public String getJzmj() {
		return jzmj;
	}
	public void setJzmj(String jzmj) {
		this.jzmj = jzmj;
	}
	public String getZdmj() {
		return zdmj;
	}
	public void setZdmj(String zdmj) {
		this.zdmj = zdmj;
	}
	public String getDqhs() {
		return dqhs;
	}
	public void setDqhs(String dqhs) {
		this.dqhs = dqhs;
	}
	public String getZhs() {
		return zhs;
	}
	public void setZhs(String zhs) {
		this.zhs = zhs;
	}
	public String getLhl() {
		return lhl;
	}
	public void setLhl(String lhl) {
		this.lhl = lhl;
	}
	public String getRjl() {
		return rjl;
	}
	public void setRjl(String rjl) {
		this.rjl = rjl;
	}
	public String getWyf() {
		return wyf;
	}
	public void setWyf(String wyf) {
		this.wyf = wyf;
	}
	public String getFjxx() {
		return fjxx;
	}
	public void setFjxx(String fjxx) {
		this.fjxx = fjxx;
	}
	public String getWybgdd() {
		return wybgdd;
	}
	public void setWybgdd(String wybgdd) {
		this.wybgdd = wybgdd;
	}
	public String getGs() {
		return gs;
	}
	public void setGs(String gs) {
		this.gs = gs;
	}
	public String getGn() {
		return gn;
	}
	public void setGn(String gn) {
		this.gn = gn;
	}
	public String getGd() {
		return gd;
	}
	public void setGd(String gd) {
		this.gd = gd;
	}
	public String getRq() {
		return rq;
	}
	public void setRq(String rq) {
		this.rq = rq;
	}
	public String getTxsb() {
		return txsb;
	}
	public void setTxsb(String txsb) {
		this.txsb = txsb;
	}
	public String getWsfw() {
		return wsfw;
	}
	public void setWsfw(String wsfw) {
		this.wsfw = wsfw;
	}
	public String getDtfw() {
		return dtfw;
	}
	public void setDtfw(String dtfw) {
		this.dtfw = dtfw;
	}
	public String getAqgl() {
		return aqgl;
	}
	public void setAqgl(String aqgl) {
		this.aqgl = aqgl;
	}
	public String getTcw() {
		return tcw;
	}
	public void setTcw(String tcw) {
		this.tcw = tcw;
	}
	public String getXqjj() {
		return xqjj;
	}
	public void setXqjj(String xqjj) {
		this.xqjj = xqjj;
	}
	public String getJtzk() {
		return jtzk;
	}
	public void setJtzk(String jtzk) {
		this.jtzk = jtzk;
	}
	public String getZbxx() {
		return zbxx;
	}
	public void setZbxx(String zbxx) {
		this.zbxx = zbxx;
	}
	public String getPirce() {
		return pirce;
	}
	public void setPirce(String pirce) {
		this.pirce = pirce;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHbsy() {
		return hbsy;
	}
	public void setHbsy(String hbsy) {
		this.hbsy = hbsy;
	}
	public String getTbqn() {
		return tbqn;
	}
	public void setTbqn(String tbqn) {
		this.tbqn = tbqn;
	}
	public String getJjlq() {
		return jjlq;
	}
	public void setJjlq(String jjlq) {
		this.jjlq = jjlq;
	}
	public String getLscjxx() {
		return lscjxx;
	}
	public void setLscjxx(String lscjxx) {
		this.lscjxx = lscjxx;
	}
	public String getDls() {
		return dls;
	}
	public void setDls(String dls) {
		this.dls = dls;
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
