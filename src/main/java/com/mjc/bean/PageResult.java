package com.mjc.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * author mjc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {
    private Long total; //总条数
    private List rows; //当前页数据
}
