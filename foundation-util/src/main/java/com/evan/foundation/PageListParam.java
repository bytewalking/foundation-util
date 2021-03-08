package com.evan.foundation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.evan.foundation.util.AssertUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：jfy
 * @date ：Created in 2020/12/29 12:22 上午
 * @description：
 * @version: 1.0
 */
@Data
@Builder
@NoArgsConstructor
public class PageListParam {

    private Integer pageNo = 1;

    private Integer pageSize = Integer.MAX_VALUE;

    public PageListParam(@JsonProperty("pageNo") Integer pageNo,
                         @JsonProperty("pageSize") Integer pageSize) {
        AssertUtil.isTrue(pageNo >= Globals.ONE, () -> "pageNo 应 >= Globals.ONE");
        AssertUtil.isTrue(pageSize >= Globals.ONE, () -> "pageSize 应 >= Globals.ONE");
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Integer getStartIndex() {
        return (pageNo - 1) * pageSize;
    }

}

