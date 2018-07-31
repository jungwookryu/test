package com.ht.connected.home.backend.user;

import java.security.Principal;
import java.util.Map;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ht.connected.home.backend.common.Common;
import com.ht.connected.home.backend.controller.rest.CommonController;

@RestController
public class LoginController extends CommonController {

    UsersService usersService;

    @Autowired
    @Qualifier("errorMessageSource")
    MessageSource errorMessageSource;

    @Autowired
    private TokenEndpoint tokenEndpoint;

    @Autowired
    public LoginController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping(value = "/authentication/login")
    public ResponseEntity<OAuth2AccessToken> getAccessToken(Principal principal,
            @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {
        return tokenEndpoint.getAccessToken(principal, parameters);
    }

    /**
     * @param principal
     * @param parameters
     * @return
     * @throws HttpRequestMethodNotSupportedException
     */
//    @ApiOperation(value = "사용자 로그인", response = ResponseEntity.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "memNo", value = "회원번호", required = true, dataType = "string", paramType = "query", defaultValue = "12345") })
//    @ApiResponses({
//            @ApiResponse(code = 200, message = "success"),
//            @ApiResponse(code = 401, message = "Bad client credentials") })
//    @ResponseBody
    @PostMapping(value = "/authentication/login")
    public ResponseEntity<OAuth2AccessToken> postAccessToken(Principal principal,
            @RequestParam Map<String, String> parameters, @RequestBody User users)
            throws HttpRequestMethodNotSupportedException {
        String grant_type = parameters.getOrDefault("grant_type", "");
        if (Common.empty(grant_type)) {
            parameters.put("grant_type", "password");

            if ((null == users.getPushToken()) || (null == users.getConnectedType())) {
                throw new BadClientCredentialsException();
            }

            String userEmail = parameters.getOrDefault("user_email", "");
            String password = parameters.getOrDefault("password", "");
            String userName = parameters.getOrDefault("username", "");
            if (Common.empty(userEmail) && Common.empty(userName)) {
                throw new BadClientCredentialsException();
            }
            if (Common.empty(userEmail) && Common.notEmpty(userName)) {
                userEmail = userName;
                parameters.put("user_email", userName);
            }
            if (Common.notEmpty(userEmail) && Common.empty(userName)) {
                parameters.put("username", userEmail);
            }
            User rtnUsers = usersService.getUser(userEmail);
            if (null == rtnUsers) {
                throw new BadClientCredentialsException();
            }
            if (!rtnUsers.getPassword().equals(Common.encryptHash(MessageDigestAlgorithms.SHA_256, password))) {
                throw new BadClientCredentialsException();
            };

            rtnUsers.setPushToken(users.getPushToken());
            rtnUsers.setPushType(users.getPushType());
            rtnUsers.setConnectedType(users.getConnectedType());

            User returnUsers = usersService.save(rtnUsers);
            logger.debug("returnUsers:::" + returnUsers.toString());
        }
        return tokenEndpoint.postAccessToken(principal, parameters);
    }

}
