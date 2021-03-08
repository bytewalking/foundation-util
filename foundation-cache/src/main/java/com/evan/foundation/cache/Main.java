package com.evan.foundation.cache;

import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.IntStream;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        String str = "11";
        ConcurrentMap<Object, Object> hotKeyStatisticsMap = CacheBuilder.newBuilder().maximumSize(100L).build().asMap();
        IntStream.rangeClosed(1, 1000).forEach((item) -> {
            String key = "sadaf" + item;
            hotKeyStatisticsMap.put(key, "" + item);
        });
        System.out.println(hotKeyStatisticsMap.keySet());
    }
}

