package com.jkojote.weblib;

import com.jkojote.weblib.config.MvcConfig;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigTest {

    @Test
    public void test() {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(MvcConfig.class);
    }
}
