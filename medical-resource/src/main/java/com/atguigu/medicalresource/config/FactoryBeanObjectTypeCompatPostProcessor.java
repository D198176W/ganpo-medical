package com.atguigu.medicalresource.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
<<<<<<< HEAD
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

=======
import org.springframework.core.log.LogMessage;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
>>>>>>> 9b1bb41fd6d89941f901e638a4766caca9f2260d
import java.util.Objects;

/**
 * 兼容补丁：将 BeanDefinition 属性 factoryBeanObjectType 为 String 的值尝试转换为 Class。
 * 仅作为临时修复，建议尽快升级引起问题的第三方库。
 */
@Component
public class FactoryBeanObjectTypeCompatPostProcessor implements BeanFactoryPostProcessor {

    private static final String ATTR_NAME = "factoryBeanObjectType";

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] names = beanFactory.getBeanDefinitionNames();
        for (String name : names) {
            BeanDefinition bd = beanFactory.getBeanDefinition(name);
            Object attr = bd.getAttribute(ATTR_NAME);
            if (attr instanceof String) {
                String className = (String) attr;
                try {
                    Class<?> clazz = ClassUtils.forName(className, this.getClass().getClassLoader());
                    // 将属性替换为 Class 对象
                    bd.setAttribute(ATTR_NAME, clazz);
                    // 打印日志（Spring Boot 的日志）
                    System.out.printf("FactoryBeanObjectTypeCompat: converted attribute for bean '%s' : %s -> %s%n",
                            name, className, clazz.getName());
                } catch (Throwable ex) {
                    // 如果找不到类（可能是因为不同 classloader），将错误记录下来供排查，但不阻塞启动
                    System.err.printf("FactoryBeanObjectTypeCompat: failed to load class '%s' for bean '%s' : %s%n",
                            className, name, ex.getMessage());
                }
            } else if (Objects.nonNull(attr)) {
                // 如果是其他类型（例如已是 Class），可选打印
                // System.out.printf("FactoryBeanObjectTypeCompat: bean '%s' has attr type %s%n", name, attr.getClass().getName());
            }
        }
    }
}