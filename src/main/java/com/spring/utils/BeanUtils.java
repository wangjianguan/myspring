package com.spring.utils;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanUtils {

    public static Method getWriteMethod(Object beanObj,String name) {
        //此方法使用内省技术实现
        Method method = null;
        try {
            //1、分析Bean对象，使用Introspector的getBeanInfo方法获得beanInfo
            BeanInfo beanInfo = Introspector.getBeanInfo(beanObj.getClass());
            //2、根据BenInfo获得所以属性的描述器
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (null != propertyDescriptors && propertyDescriptors.length>0) {
                for (PropertyDescriptor pd : propertyDescriptors) {
                    // 遍历描述器
                    String pdName = pd.getName();
                    if (pdName.equals(name)) {
                        method = pd.getWriteMethod();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (method == null) {
            throw new RuntimeException("属性方法不存在");
        }

        return method;

    }


}
