package com.req.demo;

import com.req.client.http.Request;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author luoyangwei by 2022-08-02 19:54 created
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@RequiredArgsConstructor
public class SearchCommandReq {
    private String sk;
    private int type;
    private int userType;
    private int page = 1;
    private int pageSize = 3;
}
