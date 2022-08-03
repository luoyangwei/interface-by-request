package com.req;

import com.alibaba.fastjson.JSON;
import com.req.client.RequestScan;
import com.req.demo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

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
                SearchOpinionReqVo reqVo = new SearchOpinionReqVo();
                reqVo.setSk("test");
                SearchPageResult<SearchOpinionVo> result = searchDemoClient.loadOpinions(reqVo);
                log.info("result: {}", result);
                List<SearchOpinionVo> opinionVos = result.getItemList();
                for (SearchOpinionVo opinionVo : opinionVos) {
                    log.info("SearchOpinionVo: {}, {}", opinionVo.getOpinionId(), opinionVo.getName());
                }
            }
        }
    }

}
