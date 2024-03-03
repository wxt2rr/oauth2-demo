package com.wangxt.oauth.demo.pojo;

import lombok.Data;

@Data
public class AuthBo {
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
    private String scope;

    public AuthBo(String param){
        String[] split = param.split(",");
        this.clientId = split[0];
        this.clientSecret = split[1];
        this.redirectUrl = split[2];
        this.scope = split[3];
    }
}
