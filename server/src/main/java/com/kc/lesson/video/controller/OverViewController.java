package com.kc.lesson.video.controller;

import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.video.entity.VisitData;
import com.kc.lesson.video.mapper.OverviewMapper;
import com.kc.lesson.video.mapper.VideoMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/overview")
public class OverViewController {

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    OverviewMapper overviewMapper;

    private final static Logger logger = LoggerFactory.getLogger(OverViewController.class);

    @GetMapping("/count")
    public KCResponse count() {
        try {
            Map<String, Object> result = new HashMap<>();
            // 热门视频和热门分类
            result.put("popularVideos", overviewMapper.getPopularVideo());
            result.put("popularClassifications", overviewMapper.getPopularClassification());
            // 网站流量
            result.put("visitList", getVisitListForLastDays(7));
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", result);
        } catch (Exception e) {
            logger.error("获取统计信息失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    private List<Map<String, String>> getVisitListForLastDays(int days) {
        return getLastDays(days).stream()
                .map(this::getVisitDataForDay)
                .collect(Collectors.toList());
    }

    private Map<String, String> getVisitDataForDay(String day) {
        Map<String, String> visitData = new HashMap<>();
        visitData.put("day", day);

        List<VisitData> webVisitData = overviewMapper.getWebVisitData(day);
        int pv = webVisitData.stream().mapToInt(visit -> visit.count).sum(); // 计算pv总和
        int uv = webVisitData.size(); // 计算uv的数量

        visitData.put("pv", String.valueOf(pv));
        visitData.put("uv", String.valueOf(uv));
        return visitData;
    }

    // 获取过去 n 天的日期列表
    private List<String> getLastDays(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return IntStream.rangeClosed(1, days)
                .mapToObj(i -> sdf.format(DateUtils.addDays(new Date(), -i)))
                .sorted()
                .collect(Collectors.toList());
    }

}
