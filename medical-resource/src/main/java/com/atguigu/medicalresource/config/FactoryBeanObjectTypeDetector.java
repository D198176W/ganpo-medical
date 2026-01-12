package com.atguigu.medicalresource.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class FactoryBeanObjectTypeDetector implements BeanFactoryPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(FactoryBeanObjectTypeDetector.class);
    private static final String ATTR = "factoryBeanObjectType";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            BeanDefinition bd = beanFactory.getBeanDefinition(name);
            Object attr = bd.getAttribute(ATTR);
            if (attr instanceof String) {
                String className = (String) attr;
                log.warn("[FactoryBeanObjectTypeDetector] bean='{}' has factoryBeanObjectType as String -> '{}'", name, className);
            }
        }
        log.info("[FactoryBeanObjectTypeDetector] scan finished, checked {} bean definitions", names.length);
    }
}