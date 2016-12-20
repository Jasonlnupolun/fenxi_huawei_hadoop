package cn.dinfo.cpic.Utils;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import Bean.Tables;

public class XMLUtil {
	 /** 
     * JavaBean转换成xml 
     * 默认编码UTF-8 
     * @param obj 
     * @param writer 
     * @return  
     */  
    public static String convertToXml(Object obj) {  
        return convertToXml(obj, "UTF-8");  
    }  
  
    /** 
     * JavaBean转换成xml 
     * @param obj 
     * @param encoding  
     * @return  
     */  
    public static String convertToXml(Object obj, String encoding) {  
        String result = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
            Marshaller marshaller = context.createMarshaller();  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
            marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);  
  
            StringWriter writer = new StringWriter();  
            marshaller.marshal(obj, writer);  
            result = writer.toString();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
        return result;  
    }  
  
    /** 
     * xml转换成JavaBean 
     * @param xml 
     * @param c 
     * @return 
     */  
    @SuppressWarnings("unchecked")  
    public static <T> T converyToJavaBean(String xml, Class<T> c) {  
        T t = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(c);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            t = (T) unmarshaller.unmarshal(new StringReader(xml));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }
        return t;  
    }  
    public static void main(String[] args) {
    	Class<Tables> c = (Class<Tables>) Tables.class;
    	String xml="<tables><table tname=\"zx_food_manager_index\"><rowkey rname=\"2015-11-04 20:03:50:457ab0a5f03-700f-48d7-a72a-d75f91a92932\"><family fname=\"manager_base_index\"><qualifier qname=\"manager_base_rowkey\">7c53746bc9f69a25d3053c177f7b2ee0-48ba5fae7d91935cf845b2023136e3ac</qualifier></family></rowkey><rowkey rname=\"2015-11-04 20:03:59:5545e77edc9-d800-4815-adbc-c432a4448a1d\"><family fname=\"manager_base_index\"><qualifier qname=\"manager_base_rowkey\">45748f805d33fa87093003397b0a4795-3b79a702d33c721d402f17723e1ef6d2</qualifier></family></rowkey><rowkey rname=\"2015-11-04 20:04:07:4922a1fc11d-dce6-43c1-8827-3120ab4b4953\"><family fname=\"manager_base_index\"><qualifier qname=\"manager_base_rowkey\">71fab11e69ee46939e3b39e0d52915ee-5d5b0f2d3bf9b9b60bb490592e133d65</qualifier></family></rowkey></table></tables>";
    	Tables tables=converyToJavaBean(xml, c);
    	System.out.println();
	}
}