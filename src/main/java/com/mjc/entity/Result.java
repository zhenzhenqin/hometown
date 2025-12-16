package com.mjc.entity;

import lombok.Data;

/**
 * author mjc
 */
@Data
public class Result<T> {

    private Integer code; //返回的编码: 1:成功 0:失败
    private String msg; //返回的提示信息
    private T data; //返回的数据

    //无返回数据的成功
    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    //有返回数据的成功
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.code = 1;
        result.msg = "success";
        result.data = data;
        return result;
    }

    //失败返回
    public static <T> Result<T> error(String msg){
        Result<T> result = new Result<T>();
        result.code = 0;
        result.msg = msg;
        return result;
    }
}
