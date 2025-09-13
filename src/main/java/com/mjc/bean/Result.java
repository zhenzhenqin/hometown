package com.mjc.bean;

import lombok.Data;

@Data
public class Result {

    private Integer code; //返回的编码: 1:成功 0:失败
    private String msg; //返回的提示信息
    private Object data; //返回的数据

    //无返回数据的成功
    public static Result success() {
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        return result;
    }

    //有返回数据的成功
    public static Result success(Object data) {
        Result result = new Result();
        result.code = 1;
        result.msg = "success";
        result.data = data;
        return result;
    }

    //失败返回
    public static Result error(String msg){
        Result result = new Result();
        result.code = 0;
        result.msg = msg;
        return result;
    }
}
