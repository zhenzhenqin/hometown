package com.mjc.vo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserLoginVO {
    private String id;
    private String username;
    private String password;
}
