package com.lim.Service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lim.Service.UserService;
import com.lim.constant.MessageConstant;
import com.lim.dto.UserLoginDTO;
import com.lim.entity.User;
import com.lim.exception.LoginFailedException;
import com.lim.mapper.UserMapper;
import com.lim.properties.WeChatProperties;
import com.lim.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //1.获取code对应openid
        String openId = getOpenId(userLoginDTO.getCode());
        //2.判断openid是否为空
        if (!StringUtils.hasText(openId)) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //3.根据openid查询数据库判断是否为新用户
        User user = userMapper.selectByOpenId(openId);
        if (user == null) {
            //未注册,创建一个新用户插入数据库
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.save(user);
            return user;
        }
        //已注册,直接返回
        return user;
    }

    /**
     * 根据code获取openid
     * @param code 鉴别码
     * @return openid
     */
    private String getOpenId(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        String responseBody = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/jscode2session", params);
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonNode.get("openid").asText();
    }
}
