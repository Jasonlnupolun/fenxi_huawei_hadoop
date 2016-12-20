package dbcp;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 采用映射将集合映射到bean的帮助类
 * @author zhangjie
 *
 */
public class myReflect {
 
    /**
     * map映射到bean
     * 返回实体实例
     * @param <T>
     * @param pojo
     * @param map
     * @return
     */
    public static <T> Object convertMap2Bean(Class classType,
            Map map) {    
        Object classEntry = null;
        try {
            classEntry = classType.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator it = map.keySet().iterator();
        // 遍历结果集数据列
        while (it.hasNext()) {
            // 获取属性名
            String fieldName = it.next().toString();
            // 属性名首字母大写
            String stringLetter = fieldName.substring(0, 1).toUpperCase();
 
            // 生成get/set方法名
            String setName = "set" + stringLetter + fieldName.substring(1);
            String getName = "get" + stringLetter + fieldName.substring(1);
 
            // 方法名反射获取get/set方法
            Method getMethod = null;
            try {
                getMethod = classType.getMethod(getName, new Class[] {});
            } catch (SecurityException e2) {
 
                e2.printStackTrace();
            } catch (NoSuchMethodException e2) {
 
                e2.printStackTrace();
            }
 
            Method setMethod = null;
            try {
            	if(getMethod!=null){
            		setMethod = classType.getMethod(setName,
                            new Class[] { getMethod.getReturnType() });
            	}
            } catch (SecurityException e1) {
 
                e1.printStackTrace();
            } catch (NoSuchMethodException e1) {
 
                e1.printStackTrace();
            }
 
            // 通过方法获取参数类型
            Class<? extends Object> fieldType = setMethod.getParameterTypes()[0];
            // 获取数据并做数据类型转换
 
            String valueString = null;
            try {
                valueString = map.get(fieldName).toString();
            } catch (Exception e) {
 
            }
            Object value = formatValue(valueString, fieldType);
            // 赋值操作
            try {
                setMethod.invoke(classEntry, new Object[] { value });
            } catch (IllegalArgumentException e) {
 
                e.printStackTrace();
            } catch (IllegalAccessException e) {
 
                e.printStackTrace();
            } catch (InvocationTargetException e) {
 
                e.printStackTrace();
            }
 
        }
 
        return classEntry;
    }
 
    /**
     * list映射到bean
     * 
     * @param <T>
     * @param pojo
     * @param map
     * @return
     */
    public static  List convertList2Bean(Class classType, List list) {
 
        List lPojos = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Map map = new HashMap();
            map = (Map) list.get(i);
            Object t = convertMap2Bean(classType, map);
            lPojos.add(t);
        }
        return lPojos;
    }
 
    /**
     * 进行数据转换
     * 供以上两个方法使用
     * @param fieldValue
     * @param fieldType
     * @return
     */
    private static Object formatValue(String fieldValue,
            Class<? extends Object> fieldType) {
        Object value = null;
 
        if (fieldType == Integer.class || "int".equals(fieldType.getName())) {
            if (fieldValue != null) {
                value = Integer.parseInt(fieldValue);
            }
            else{
                value=0;
            }
        } else if (fieldType == Float.class
                || "float".equals(fieldType.getName())) {
            if (fieldValue != null) {
                value = Float.parseFloat(fieldValue);
            }
            else{
                value=0;
            }
        } else if (fieldType == Double.class
                || "double".equals(fieldType.getName())) {
            if (fieldValue != null) {
                value = Double.parseDouble(fieldValue);
            }
            else{
                value=0;
            }
        } else if (fieldType == Date.class || fieldType == java.util.Date.class) {
            if (fieldValue != null) {
                value = Timestamp.valueOf(fieldValue);
            }
            else{
                value=0;
            }
        } else {
            value = fieldValue;
        }
 
        return value;
    }
     
    /**
     * 将bean转为map
     * @param obj
     * @return
     */
        public static Map<String, Object> Bean2Map(Object obj) {  
               
            if(obj == null){  
                return null;  
            }          
            Map<String, Object> map = new HashMap<String, Object>();  
            try {  
                BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
                for (PropertyDescriptor property : propertyDescriptors) {  
                    String key = property.getName();  
       
                    // 过滤class属性  
                    if (!key.equals("class")) {  
                        // 得到property对应的getter方法  
                        Method getter = property.getReadMethod();  
                        Object value = getter.invoke(obj);  
       
                        map.put(key, value);  
                    }  
       
                }  
            } catch (Exception e) {  
                System.out.println("transBean2Map Error " + e);  
            }  
       
            return map;  
       
        }
}