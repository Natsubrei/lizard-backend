package com.lizard.lizardbackend.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果
 */
@Data
@AllArgsConstructor
public class PageResult implements Serializable {
    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页数据集合
     */
    private List<?> records;
}
