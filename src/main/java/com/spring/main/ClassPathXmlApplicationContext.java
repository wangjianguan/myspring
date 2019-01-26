package com.spring.main;

import com.spring.config.Bean;
import com.spring.config.Property;
import com.spring.config.parse.ConfigManager;
import com.spring.utils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory {

    private Map<String,Bean> map;

    /**
     * 模拟Spring的容器
     */
    private Map<String,Object> context = new HashMap<String, Object>();


    /**
     *
     * @param path
     */
    public ClassPathXmlApplicationContext(String path) {
        map = ConfigManager.getConfig(path);
        if (null != map) {
            for (Map.Entry<String,Bean> en : map.entrySet()) {
                String beanName = en.getKey();
                Bean bean = en.getValue();
                Object existsBean = context.get(bean);
                if (existsBean == null) {
                    Object beanObject = createBean(bean);
                    context.put(beanName,beanObject);
                }
            }
        }
    }


    private Object createBean(Bean bean){
        Object invoke = null;
        // 1、获得要创建的Class
        String className = bean.getClassName();
        Class clazz = null;
        Object object = null;
        try {
            clazz = Class.forName(className);
            //2、获得Class后使用class创建对象
            object = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("请检查您的Bean的Class配置" + className);
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("您的bean没有构造方法" + className);
        }
        //3、获取bean的属性，将其注入
        if (null != bean.getProperties()) {
            for (Property property : bean.getProperties()) {
                //获取要注入的属性名称
                String name = property.getName();
                Method method = null;
                try {
                    method = BeanUtils.getWriteMethod(object,name);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("您的bean没有set方法" + className);
                }
                Object result = null;
                if (null != property.getValue()) {
                    //value不等于空，表示是普通属性
                    //获取要注入的实际值
                    String value = property.getValue();
                    result = value;
                } else if (null != property.getRef()) {
                    //ref不等于空，表示是bean注入
                    // 先从容器中判断当前要注入的bean是否已经创建了
                    Object existsBean = context.get(property.getRef());
                    if (existsBean == null) {
                        existsBean  = createBean(map.get(property.getRef()));
                        context.put(property.getRef(),existsBean);
                    }
                    result = existsBean;
                }
                try {
                    method.invoke(object, result);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }

    public Object getBean(String beanName) {
        return context.get(beanName);
    }
}
