package com.evan.foundation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ：evan
 * @date ：Created in 2021/03/08 12:24 上午
 * @description：
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageListResult<T> {

    private List<T> dataList;

    private Long totalCount;

}

