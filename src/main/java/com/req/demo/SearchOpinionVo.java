package com.req.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author luoyangwei by 2022-08-03 16:07 created
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SearchOpinionVo implements Serializable {
    private static final long serialVersionUID = -5151737969681220664L;
    private Long opinionUserId;
    private Long opinionId;
    private Integer auditStatus;
    private String name;
    private Integer likeCountNumber;
    private Integer starSkyNumber;
    private Integer heat;
    private Integer participantNumber;
    private String opinionRoute;
    private Integer reduceWeight;
    private Boolean isUserOpinion;
}
