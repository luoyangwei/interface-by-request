package com.req.demo;

import com.req.client.RequestMark;
import com.req.client.http.*;

/**
 * @author luoyangwei by 2022-08-01 14:52 created
 */
@RequestMark(hostname = "http://localhost:8104")
public interface SearchDemoClient {

    @Post(url = "/test/opinion")
    @Headers(headers = {@Header(key = "Content-Type", val = "application/json")})
    SearchPageResult<SearchOpinionVo> loadOpinions(@Body SearchOpinionReqVo reqVo);

}
