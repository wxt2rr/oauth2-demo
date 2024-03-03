package com.wangxt.oauth.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenBo {
    private String accessToken;
    private Long expiresIn;
    private String refreshToken;
    private Long refreshExpiresIn;
}
