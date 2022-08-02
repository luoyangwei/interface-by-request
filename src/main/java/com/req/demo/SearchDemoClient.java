package com.req.demo;

import com.req.client.RequestMark;
import com.req.client.http.*;

/**
 * @author luoyangwei by 2022-08-01 14:52 created
 */
@RequestMark(hostname = "http://xx.xxxx.xx/xx-xx")
public interface SearchDemoClient {

    /**
     * test load content
     *
     * @return string
     */
    @Request(url = "/hobby/production/search", requestMode = RequestMode.GET)
    String loadContents();

    @Post(url = "/hobby/user/search")
    @Headers(headers = {@Header(key = "Content-Type", val = "application/json")})
    SearchBaseResult<?> loadUsers(@Body SearchCommandReq req);

}
