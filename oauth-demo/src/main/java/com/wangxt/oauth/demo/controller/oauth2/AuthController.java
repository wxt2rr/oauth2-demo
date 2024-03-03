package com.wangxt.oauth.demo.controller.oauth2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.wangxt.oauth.demo.db.AuthDao.*;
import static com.wangxt.oauth.demo.util.AuthUtil.checkP1SubOfP2;

@RestController
@RequestMapping("/oauth2/")
public class AuthController {

    /**
     * 授权接口
     *
     * @param client_id     客户端 Id
     * @param redirect_uri  授权通过后重定向地址
     * @param response_type 授权类型
     * @param scope         授权范围
     * @param state         状态（防止 crfs 攻击）
     */
    @RequestMapping("authorize")
    public void authorize(
            HttpServletRequest request,
            HttpServletResponse response,
            String client_id,
            String redirect_uri,
            String response_type,
            String scope,
            String state
    ) throws Exception {
        // 首先需要校验参数，判断客户端传来的参数是否正确，主要是 redirect_uri 和 client_id 是否正确
        if (!CLIENT_MAP.containsKey(client_id)) {
            response.getWriter().write("client_id not found");
            return;
        }
        if (!CLIENT_MAP.get(client_id).getRedirectUrl().equals(redirect_uri)) {
            response.getWriter().write("redirect_uri not match");
            return;
        }

        // 判断 scope 是否越权
        if (!checkP1SubOfP2(scope, CLIENT_MAP.get(client_id).getScope())) {
            response.getWriter().write("scope not match");
            return;
        }

        // 把这次申请的 scope 临时存起来，因为 用户进行 确权时 需要进行越权校验
        SCOPE_MAP.put(client_id, scope);

        // 如果校验通过，则重定向到 授权页面，然后将 客户端的信息传给授权页面，需要进行信息展示
        response.sendRedirect("https://www.baidu.com?client_id=" + client_id);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
    }

    @RequestMapping("/api/auth")
    public void auth(
            HttpServletRequest request,
            HttpServletResponse response,
            String client_id,
            String redirect_uri,
            String response_type,
            String re_scope,
            String state
    ) throws Exception {
        // 首先需要校验参数，判断客户端传来的参数是否正确，主要是 redirect_uri 和 client_id 是否正确
        if (!CLIENT_MAP.containsKey(client_id)) {
            response.getWriter().write("client_id not found");
            return;
        }

        String redirectUrl = CLIENT_MAP.get(client_id).getRedirectUrl();
        if (!redirectUrl.equals(redirect_uri)) {
            response.getWriter().write("redirect_uri not match");
            return;
        }

        // 验证申请的 scope 是否越权
        if (!checkP1SubOfP2(re_scope, SCOPE_MAP.get(client_id))) {
            response.getWriter().write("scope not match");
            return;
        }

        if ("code".equals(response_type)) {
            // 生成 code，因为下一步需要拿着 code 换取 accessToken，所以需要将 scope 进行保存
            final String code = UUID.randomUUID().toString();
            CODE_MAP.put(code, client_id + ":" + ":" + re_scope);

            response.sendRedirect(redirect_uri + "?code=" + code + (StringUtils.isBlank(state) ? "" : "&state=" + state));
            response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        }

    }
}
