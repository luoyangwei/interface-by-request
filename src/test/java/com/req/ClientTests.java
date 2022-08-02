package com.req;

import com.req.demo.SearchDemoClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author luoyangwei by 2022-08-01 13:56 created
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ReqApplication.class)
@ActiveProfiles("dev")
@Slf4j
public class ClientTests {

    @Autowired(required = false)
    private SearchDemoClient searchDemoClient;


    @Test
    public void searchDemoClientTest() {
//        String response = searchDemoClient.loadContents();
        Object response = searchDemoClient.loadContents();
        log.info("response: {}", response);
    }

}
