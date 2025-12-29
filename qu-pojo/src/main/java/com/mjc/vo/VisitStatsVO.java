package com.mjc.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitStatsVO {
    // 日期列表 (x轴) -> ["2025-12-23", "2025-12-24", ... "今天"]
    private List<String> dateList;

    // PV数据列表 (y轴1) -> [120, 132, ... 实时数据]
    private List<Integer> pvList;

    // UV数据列表 (y轴2) -> [10, 15, ... 实时数据]
    private List<Integer> uvList;
}