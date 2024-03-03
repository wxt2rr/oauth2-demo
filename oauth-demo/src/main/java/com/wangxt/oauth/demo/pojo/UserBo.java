package com.wangxt.oauth.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBo {
    private String userId;
    private String username;
    private String password;
    private String nickname;
}
