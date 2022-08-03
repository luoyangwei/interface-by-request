package com.req.demo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author luoyangwei by 2022-08-03 16:06 created
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class SearchPageResult<T extends Serializable> implements Serializable {
    private static final long serialVersionUID = 2802709188602127043L;

    /**
     * 当前页数
     * targetPage
     */
    private Integer targetPage;
    /**
     * 每页记录数 pageSize
     */
    private Integer pageSize;

    /**
     * 总页数 pageCount
     */
    private Integer pageCount;

    /**
     * 总记录数 totalRecord
     */
    private Integer totalRecord;

    /**
     * type
     */
    private Integer type;

    /**
     * fun version
     */
    private String funVersion;

    /**
     * 列表数据 itemList
     */
    private List<T> itemList;

}
