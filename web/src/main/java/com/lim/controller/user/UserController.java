package com.lim.controller.user;

import com.lim.Service.UserService;
import com.lim.constant.JwtClaimsConstant;
import com.lim.dto.UserLoginDTO;
import com.lim.entity.User;
import com.lim.properties.JwtProperties;
import com.lim.result.Result;
import com.lim.utils.JwtUtil;
import com.lim.vo.UserLoginVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/user")
public class UserController {

    private final JwtProperties jwtProperties;
    private final UserService userService;

    public UserController(JwtProperties jwtProperties, UserService userService) {
        this.jwtProperties = jwtProperties;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO) {
        //微信登录
        User user = userService.wxLogin(userLoginDTO);

        //生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .token(token)
                .openid(user.getOpenid())
                .build();
        return Result.success(userLoginVO);
    }
}
