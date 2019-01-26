package com.spring.test;

import com.spring.bean.A;
import com.spring.bean.B;
import com.spring.config.Bean;
import com.spring.config.parse.ConfigManager;
import com.spring.main.ClassPathXmlApplicationContext;
import org.junit.Test;

import java.util.Map;

public class MyTest {

    @Test
    public void test1() {
        Map<String, Bean> config = ConfigManager.getConfig("/applicationContext.xml");
        System.out.print(config.toString());
    }

    @Test
    public void test2() {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");

        A a = (A)classPathXmlApplicationContext.getBean("a");

        System.out.print(a.getName());
    }

    @Test
    public void test3() {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");

        B b = (B) classPathXmlApplicationContext.getBean("b");

        System.out.print(b.getA().getName());
    }


}
