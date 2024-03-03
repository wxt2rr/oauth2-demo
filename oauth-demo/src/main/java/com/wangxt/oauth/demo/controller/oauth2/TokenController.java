package com.wangxt.oauth.demo.controller.oauth2;

import com.wangxt.oauth.demo.pojo.AuthBo;
import com.wangxt.oauth.demo.pojo.TokenBo;
import com.wangxt.oauth.demo.pojo.UserBo;
import com.wangxt.oauth.demo.pojo.resp.ErrorCode;
import com.wangxt.oauth.demo.util.ResUtil;
import com.wangxt.oauth.demo.pojo.resp.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.UUID;

import static com.wangxt.oauth.demo.db.AuthDao.*;
import static com.wangxt.oauth.demo.db.UserDao.USER_MAP;

@RestController
@RequestMapping("/oauth2/")
public class TokenController {

    @RequestMapping("token")
    public Result<Object> token(
            @RequestParam(required = false, defaultValue = "") String client_id,
            @RequestParam(required = false, defaultValue = "") String client_secret,
            String grant_type,
            @RequestParam(required = false, defaultValue = "") String code,
            @RequestParam(required = false, defaultValue = "") String refresh_token,
            @RequestParam(required = false, defaultValue = "") String username,
            @RequestParam(required = false, defaultValue = "") String password
    ) {
        // 根据 grant_type 判断是哪种授权方式
        if ("authorization_code".equals(grant_type)) {
            if (StringUtils.isAnyBlank(client_id, client_secret, code)) {
                return ResUtil.fail(ErrorCode.PARAM_FAIL);
            }

            AuthBo authBo = CLIENT_MAP.get(client_id);
            if (authBo == null) {
                return ResUtil.fail(ErrorCode.CLIENT_ID_ERROR);
            }

            if (!authBo.getClientSecret().equals(client_secret)) {
                return ResUtil.fail(ErrorCode.CLIENT_SECRET_ERROR);
            }

            String data = CODE_MAP.get(code);
            if (StringUtils.isBlank(data)) {
                return ResUtil.fail(ErrorCode.CODE_EXPIRED);
            }
            String[] arr = data.split(":");
            if (!arr[0].equals(client_id)) {
                return ResUtil.fail(ErrorCode.CODE_ERROR);
            }

            // 授权码是临时码，一次性的，使用之后立马删除
            CODE_MAP.remove(code);

            String accessToken = UUID.randomUUID().toString();
            // 绑定颁发的 token 和 scope，之后客户端使用 accessToken 访问 受保护资源时，可以通过 scope 进行越权检查
            SCOPE_MAP.put(accessToken, arr[1]);
            TOKEN_MAP.put(accessToken, client_id + ":" + client_secret);

            String refreshToken = UUID.randomUUID().toString();
            REFRESH_TOKEN_MAP.put(refresh_token, client_id + ":" + client_secret + ":" + accessToken);

            return ResUtil.success(new TokenBo(accessToken, 1L, refreshToken, 2L));
        }

        if ("refresh_token".equals(grant_type)) {
            if (StringUtils.isAnyBlank(refresh_token, client_id, client_secret)) {
                return ResUtil.fail(ErrorCode.PARAM_FAIL);
            }

            String[] arr = REFRESH_TOKEN_MAP.get(refresh_token).split(":");
            if (!arr[0].equals(client_id)) {
                return ResUtil.fail(ErrorCode.CLIENT_ID_ERROR);
            }
            if (!arr[1].equals(client_secret)) {
                return ResUtil.fail(ErrorCode.CLIENT_SECRET_ERROR);
            }

            String accessToken = UUID.randomUUID().toString();
            // 绑定颁发的 token 和 scope，之后客户端使用 accessToken 访问 受保护资源时，可以通过 scope 进行越权检查
            SCOPE_MAP.put(accessToken, arr[1]);
            TOKEN_MAP.put(accessToken, client_id + ":" + client_secret);

            String refreshToken = UUID.randomUUID().toString();
            REFRESH_TOKEN_MAP.put(refresh_token, client_id + ":" + client_secret);

            REFRESH_TOKEN_MAP.remove(refresh_token);
            TOKEN_MAP.remove(arr[2]);

            return ResUtil.success(new TokenBo(accessToken, 1L, refreshToken, 2L));
        }

        if ("password".equals(grant_type)) {
            if (StringUtils.isAnyBlank(client_id, client_secret, password, username)) {
                return ResUtil.fail(ErrorCode.PARAM_FAIL);
            }

            AuthBo authBo = CLIENT_MAP.get(client_id);
            if (authBo == null) {
                return ResUtil.fail(ErrorCode.CLIENT_ID_ERROR);
            }

            if (!authBo.getClientSecret().equals(client_secret)) {
                return ResUtil.fail(ErrorCode.CLIENT_SECRET_ERROR);
            }

            UserBo userBo = USER_MAP.get(username);
            if (Objects.isNull(userBo) || !userBo.getPassword().equals(password)) {
                return ResUtil.fail(ErrorCode.USER_NOT_EXISTS);
            }

            String accessToken = UUID.randomUUID().toString();
            // 绑定颁发的 token 和 scope，之后客户端使用 accessToken 访问 受保护资源时，可以通过 scope 进行越权检查
            SCOPE_MAP.put(accessToken, authBo.getScope());
            TOKEN_MAP.put(accessToken, client_id + ":" + client_secret + ":" + userBo.getUserId());

            String refreshToken = UUID.randomUUID().toString();
            REFRESH_TOKEN_MAP.put(refresh_token, client_id + ":" + client_secret);

            return ResUtil.success(new TokenBo(accessToken, 1L, refreshToken, 2L));
        }

        if ("client_credentials".equals(grant_type)) {
            if (StringUtils.isAnyBlank(client_id, client_secret)) {
                return ResUtil.fail(ErrorCode.PARAM_FAIL);
            }

            AuthBo authBo = CLIENT_MAP.get(client_id);
            if (authBo == null) {
                return ResUtil.fail(ErrorCode.CLIENT_ID_ERROR);
            }

            if (!authBo.getClientSecret().equals(client_secret)) {
                return ResUtil.fail(ErrorCode.CLIENT_SECRET_ERROR);
            }

            String accessToken = UUID.randomUUID().toString();
            // 绑定颁发的 token 和 scope，之后客户端使用 accessToken 访问 受保护资源时，可以通过 scope 进行越权检查
            SCOPE_MAP.put(accessToken, authBo.getScope());
            TOKEN_MAP.put(accessToken, client_id + ":" + client_secret);

            String refreshToken = UUID.randomUUID().toString();
            REFRESH_TOKEN_MAP.put(refresh_token, client_id + ":" + client_secret);

            return ResUtil.success(new TokenBo(accessToken, 1L, refreshToken, 2L));
        }

        if ("implicit".equals(grant_type)) {
            if (StringUtils.isAnyBlank(client_id)) {
                return ResUtil.fail(ErrorCode.PARAM_FAIL);
            }

            AuthBo authBo = CLIENT_MAP.get(client_id);
            if (authBo == null) {
                return ResUtil.fail(ErrorCode.CLIENT_ID_ERROR);
            }

            if (!authBo.getClientSecret().equals(client_secret)) {
                return ResUtil.fail(ErrorCode.CLIENT_SECRET_ERROR);
            }

            String accessToken = UUID.randomUUID().toString();
            // 绑定颁发的 token 和 scope，之后客户端使用 accessToken 访问 受保护资源时，可以通过 scope 进行越权检查
            SCOPE_MAP.put(accessToken, authBo.getScope());
            TOKEN_MAP.put(accessToken, client_id + ":" + client_secret);

            String refreshToken = UUID.randomUUID().toString();
            REFRESH_TOKEN_MAP.put(refresh_token, client_id + ":" + client_secret);

            return ResUtil.success(new TokenBo(accessToken, 1L, refreshToken, 2L));
        }

        return ResUtil.fail(ErrorCode.UN_SUPPORT_TYPE);
    }
}
