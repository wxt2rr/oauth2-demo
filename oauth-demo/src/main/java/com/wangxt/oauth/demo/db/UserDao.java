package com.wangxt.oauth.demo.db;

import com.wangxt.oauth.demo.pojo.UserBo;

import java.util.HashMap;
import java.util.Map;

public class UserDao {
    public static final Map<String, UserBo> USER_MAP = new HashMap<>();

    static {
        USER_MAP.put("uid1", new UserBo("uid1","wangxt","wangxt","geekTao"));
        USER_MAP.put("uid2", new UserBo("uid2","wangxt2","wangxt2","geekTao2"));
    }
}
