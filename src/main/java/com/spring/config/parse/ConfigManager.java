package com.spring.config.parse;

import com.spring.config.Bean;
import com.spring.config.Property;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    // 读取配置文件=> 并返回读取结果
    public static Map<String,Bean> getConfig(String path) {
        Map<String,Bean> stringBeanMap = new HashMap<String, Bean>();
        // dom4j实现
          //1、创建解析器
        SAXReader reader = new SAXReader();
          //2、加载配置文件=>document元素
        InputStream is = ConfigManager.class.getResourceAsStream(path);
        Document document = null;
        try {
            document = reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException("xml配置不正确");
        }
        //3、定义xpath对象，取出所有bean元素
        String xpath = "//bean";
          //4、对Bean元素进行遍历
        List<Element> list = document.selectNodes(xpath);
        if (null != list && list.size() > 0) {
            for (Element element : list) {
                Bean bean = new Bean();
                String name = element.attributeValue("name");
                String className = element.attributeValue("class");
                bean.setName(name);
                bean.setClassName(className);
                // 获取Bean元素下的所以Property子元素
                List<Element> children = element.elements("property");
                if (null != children && children.size() >0) {
                    for (Element proEle : children) {
                        Property property = new Property();
                        property.setName(proEle.attributeValue("name"));
                        property.setValue(proEle.attributeValue("value"));
                        property.setRef(proEle.attributeValue("ref"));
                        bean.getProperties().add(property);
                    }
                }
                stringBeanMap.put(name,bean);
            }
        }
        //将bean元素的name/class属性封装到Bean对象中
            //获取Bean元素下的所以Property子元素，将属性name/value/ref封装到Property对象
            //将Property封装到Bean对象
            //将Bean封装到Map
        return stringBeanMap;
    }
}
