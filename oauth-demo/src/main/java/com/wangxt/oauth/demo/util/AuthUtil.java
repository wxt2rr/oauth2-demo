package com.wangxt.oauth.demo.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class AuthUtil {

    public static boolean checkP1SubOfP2(String reScope, String scope) {
        return checkP1SubOfP2(reScope, scope, " ");
    }

    public static boolean checkP1SubOfP2(String reScope, String scope, String regex) {
        if (null == scope || null == reScope) {
            return false;
        }

        Set<String> set = Arrays.stream(scope.split(regex)).collect(Collectors.toSet());
        return Arrays.stream(reScope.split(":")).allMatch(set::contains);
    }
}
