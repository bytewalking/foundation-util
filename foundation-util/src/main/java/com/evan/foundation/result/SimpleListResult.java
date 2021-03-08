package com.evan.foundation.result;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;
import com.evan.foundation.CursorAble;
import com.evan.foundation.Globals;
import com.evan.foundation.TraceIdAware;
import com.evan.foundation.util.AssertUtil;
import com.evan.foundation.util.NetUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author jiangfangyuan
 * @since 2019-07-27 13:32
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SimpleListResult<T> implements Serializable, TraceIdAware {


    private static final long serialVersionUID = -7930135262144871783L;

    /**
     * 错误码
     */
    private String errorCode = StringUtils.EMPTY;

    /**
     * 错误信息
     */
    private String errorMsg = StringUtils.EMPTY;

    /**
     * 数据集列表
     */
    private List<T> dataList;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 分布式链路跟踪id
     */
    private String traceId;


    private Map<String, String> cursorMap;

    /**
     * 主机名
     */
    private String hostName;

    @JsonProperty(value = "status")
    public int getStatus() {
        if (StringUtils.isBlank(errorCode)) {
            return 200;
        } else {
            return 500;
        }
    }

    @JsonProperty(value = "msg")
    public String getMsg() {
        if (StringUtils.isNotBlank(errorMsg)) {
            return errorMsg;
        } else {
            return StringUtils.EMPTY;
        }
    }

    public static <T extends CursorAble> SimpleListResult success(List<T> dataList) {
        SimpleListResult simpleListResult = new SimpleListResult();
        simpleListResult.setSuccess(true);
        simpleListResult.setDataList(dataList);
        if (CollectionUtils.isEmpty(dataList)) {
            return simpleListResult;
        }
        AssertUtil.notNull(dataList.get(dataList.size() - Globals.ONE).getCursor());
        Map<String, String> cursorMap = Maps.newHashMap();
        cursorMap.put("cursor", dataList.get(dataList.size() - Globals.ONE).getCursor());
        simpleListResult.setCursorMap(cursorMap);
        simpleListResult.setHostName(NetUtil.getHostName());
        return simpleListResult;
    }

    public static <T> SimpleListResult<T> failure(String errorCode, String errorMsg) {
        SimpleListResult<T> simpleListResult = new SimpleListResult<T>();
        simpleListResult.setSuccess(false);
        simpleListResult.setErrorCode(errorCode);
        simpleListResult.setErrorMsg(errorMsg);
        simpleListResult.setHostName(NetUtil.getHostName());
        return simpleListResult;
    }


}

