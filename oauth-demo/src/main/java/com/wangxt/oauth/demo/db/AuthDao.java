package com.wangxt.oauth.demo.db;

import com.wangxt.oauth.demo.pojo.AuthBo;
import com.wangxt.oauth.demo.util.CacheMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AuthDao {
    public static final Map<String, AuthBo> CLIENT_MAP = new HashMap<>();

    public static final CacheMap<String, String> CODE_MAP = new CacheMap<>(10L, TimeUnit.SECONDS);

    public static final CacheMap<String, String> SCOPE_MAP = new CacheMap<>(10L, TimeUnit.SECONDS);

    public static final CacheMap<String, String> TOKEN_MAP = new CacheMap<>(1L, TimeUnit.MINUTES);

    public static final CacheMap<String, String> REFRESH_TOKEN_MAP = new CacheMap<>(5L, TimeUnit.SECONDS);

    static {
        CLIENT_MAP.put("client1", new AuthBo("client1,secret1,https://www.baidu.com/1/,user vip"));
        CLIENT_MAP.put("client2", new AuthBo("client2,secret2,https://www.baidu.com/2/,user admin"));
        CLIENT_MAP.put("client3", new AuthBo("client3,secret3,https://www.baidu.com/3/,user user"));
    }
}
