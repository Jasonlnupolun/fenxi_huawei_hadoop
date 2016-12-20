package cn.dinfo.cpic.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
	private static Log log=LogFactory.getLog(HttpClientUtil.class);
	public static void main(String[] args) throws Exception {
//		HttpClientUtil.docProcVsm("http://192.168.2.117:8080/sfyqServer/getReplyList", "3785894194962786");
//		HttpClientUtil.docProcVsm("http://192.168.2.132:8080/bocDataServer/hbaseapp?state=0", "");
		HttpClientUtil.docProcVsm("http://localhost:8080/CpicDataServer/hbaseapp?tableName=enhfc3F1cmw=&state=MA==", "");
	}
	@SuppressWarnings("deprecation")
	public synchronized static String docProcVsm(String serverUrl,String param){
		HttpClient httpclient = HttpClients.createDefault();
//		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
	    HttpPost httpPost=new  HttpPost(serverUrl);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		//nvps.add(new BasicNameValuePair("wbid", wbId));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			//String str = convertStreamToString(resEntity.getContent());
			String str = EntityUtils.toString(resEntity, "UTF-8");
			str=Base64.getFromBase64(str);
			return str;
		} catch (Exception e) {
			log.error("访问数据接口出错！");
			log.error(serverUrl);
		}
		return null;
	}
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			if(reader!=null){
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
