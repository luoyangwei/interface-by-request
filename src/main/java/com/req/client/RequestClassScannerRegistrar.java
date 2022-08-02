package com.req.client;

import lombok.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author luoyangwei by 2022-08-01 14:32 created
 */
public class RequestClassScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanFactoryAware {
    private static final String BASE_PACKAGE_METHOD_NAME = "basePackages";
    private static final String BASE_PACKAGE_DEFAULT_METHOD_NAME = "value";
    private ResourceLoader resourceLoader;
    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry,
                                        @NonNull BeanNameGenerator importBeanNameGenerator) {

        RequestClassScanner requestClassScanner = new RequestClassScanner(registry, beanFactory);
        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            requestClassScanner.setResourceLoader(resourceLoader);
        }

        Set<String> basePackages = getBasePackages(importingClassMetadata);
        requestClassScanner.doScan(StringUtils.toStringArray(basePackages));
    }

    /**
     * 获取base packages
     */
    protected static Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        // 获取到@EnableSimpleRpcClients注解所有属性
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(RequestScan.class.getCanonicalName());
        Set<String> basePackages = new HashSet<>();
        assert attributes != null;
        // value 属性是否有配置值，如果有则添加
        for (String pkg : (String[]) attributes.get(BASE_PACKAGE_DEFAULT_METHOD_NAME)) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        // basePackages 属性是否有配置值，如果有则添加
        for (String pkg : (String[]) attributes.get(BASE_PACKAGE_METHOD_NAME)) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }

        // 如果上面两步都没有获取到basePackages，那么这里就默认使用当前项目启动类所在的包为basePackages
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }

        return basePackages;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
