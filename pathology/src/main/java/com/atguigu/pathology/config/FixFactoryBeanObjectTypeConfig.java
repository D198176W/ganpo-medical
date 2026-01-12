package com.atguigu.pathology.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ClassUtils;

@Configuration
public class FixFactoryBeanObjectTypeConfig {

    private static final Logger log = LoggerFactory.getLogger(FixFactoryBeanObjectTypeConfig.class);

//     诊断 bean（上文）
    @Bean
    public static BeanFactoryPostProcessor detectFactoryBeanObjectTypeAttrs() {
        return beanFactory -> {
            if (beanFactory instanceof DefaultListableBeanFactory dlbf) {
                for (String name : dlbf.getBeanDefinitionNames()) {
                    BeanDefinition bd = dlbf.getBeanDefinition(name);
                    Object attr = bd.getAttribute("factoryBeanObjectType");
                    if (attr != null && attr instanceof String) {
                        log.error("BeanDefinition '{}' has factoryBeanObjectType attribute of type String: {}", name, attr);
                    }
                }
            }
        };
    }

    // 自动修复 bean：把字符串解析为 Class
    @Bean
    public static BeanFactoryPostProcessor fixFactoryBeanObjectTypeAttrs() {
        return beanFactory -> {
            if (beanFactory instanceof DefaultListableBeanFactory dlbf) {
                ClassLoader cl = dlbf.getBeanClassLoader();
                for (String name : dlbf.getBeanDefinitionNames()) {
                    BeanDefinition bd = dlbf.getBeanDefinition(name);
                    Object attr = bd.getAttribute("factoryBeanObjectType");
                    if (attr instanceof String s) {
                        try {
                            Class<?> resolved = ClassUtils.forName(s, cl);
                            bd.setAttribute("factoryBeanObjectType", resolved);
                            log.info("Resolved factoryBeanObjectType for bean '{}' -> {}", name, resolved.getName());
                        } catch (Throwable ex) {
                            log.warn("Could not resolve factoryBeanObjectType '{}' for bean '{}'", s, name, ex);
                        }
                    }
                }
            }
        };
    }
}