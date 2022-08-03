package com.req.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author luoyangwei by 2022-08-03 16:08 created
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchOpinionReqVo implements Serializable {
    private static final long serialVersionUID = 5685801046050031842L;
    private String sk;
    private Integer page = 1;
    private Integer pageSize = 10;
}
