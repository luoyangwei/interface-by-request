package com.req;

import com.alibaba.fastjson.JSON;
import com.req.client.RequestScan;
import com.req.demo.SearchBaseResult;
import com.req.demo.SearchCommandReq;
import com.req.demo.SearchDemoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author luoyangwei by 2022-08-01 13:55 created
 */
@RequestScan(basePackages = {"com.req.demo"})
@SpringBootApplication
@Slf4j
public class ReqApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ReqApplication.class, args);
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            if ("searchDemoClient".equals(beanDefinitionName)) {
                SearchDemoClient searchDemoClient = (SearchDemoClient) context.getBean(beanDefinitionName);
//                searchDemoClient.loadContents();

                SearchCommandReq req = new SearchCommandReq();
                req.setSk("测试").setType(0).setPage(1).setPageSize(3).setUserType(0);
                SearchBaseResult<?> result = searchDemoClient.loadUsers(req);
                log.info("result: {}", JSON.toJSONString(result));
            }
        }
    }

}
