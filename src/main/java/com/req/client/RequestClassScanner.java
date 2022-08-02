package com.req.client;

import com.req.client.agent.RequestFactoryBean;
import com.req.client.agent.RequestHttpImpl;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Set;

/**
 * @author luoyangwei by 2022-08-01 14:40 created
 */

@Slf4j
public class RequestClassScanner extends ClassPathBeanDefinitionScanner {

    private final AnnotationTypeFilter annotationTypeFilter =
            new AnnotationTypeFilter(RequestMark.class, false, true);

    private final Class<?> requestFactoryBeanClass = RequestFactoryBean.class;

    private final OkHttpClient okHttpClient;

    public RequestClassScanner(BeanDefinitionRegistry registry, BeanFactory beanFactory) {
        super(registry);
        okHttpClient = (OkHttpClient) beanFactory.getBean("okHttpClient");
        addDefaultIncludeFilter();
    }

    public void addDefaultIncludeFilter() {
        super.addIncludeFilter(annotationTypeFilter);
    }


    @NotNull
    @Override
    protected Set<BeanDefinitionHolder> doScan(@NotNull String... basePackages) {
        Assert.notEmpty(basePackages, "At least one base package must be specified");
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        if (beanDefinitions.isEmpty()) {
            log.warn("No Request interface was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        }
        processBeanDefinitions(beanDefinitions);

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        try {
            GenericBeanDefinition definition;
            for (BeanDefinitionHolder holder : beanDefinitions) {
                definition = (GenericBeanDefinition) holder.getBeanDefinition();
                String beanClassName = definition.getBeanClassName();
                Class<?> targetClass = Class.forName(beanClassName);
                log.info("Creating RequestFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName
                        + "' Interface");
                definition.setBeanClass(this.requestFactoryBeanClass);
                definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
                definition.getConstructorArgumentValues().addGenericArgumentValue(targetClass);
                definition.getPropertyValues().add("requestHttpClient", new RequestHttpImpl(okHttpClient));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface();
    }

}
