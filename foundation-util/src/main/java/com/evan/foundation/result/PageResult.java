package com.evan.foundation.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mayifan0701
 * @since 2020/3/3 12:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageResult<T> extends SimpleResult<T> implements Serializable {
    private static final long serialVersionUID = -4643838151183167338L;

    private Pageable pageable;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pageable implements Serializable {
        private static final long serialVersionUID = -6438298326446968708L;

        private Integer pageNo;
        private Integer pageSize;
        private Long totalRowsCount;
    }

    public static <T> PageResult<T> success(T result, Integer pageNo, Integer pageSize, Long totalRowsCount) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setSuccess(true);
        pageResult.setPageable(new Pageable(pageNo, pageSize, totalRowsCount));
        pageResult.setResult(result);
        return pageResult;
    }

    public static PageResult<?> failure(String errorCode, String errorMsg) {
        PageResult<?> pageResult = new PageResult<>();
        pageResult.setSuccess(false);
        pageResult.setErrorCode(errorCode);
        pageResult.setErrorMsg(errorMsg);
        return pageResult;
    }
}
