package com.lim.service;

import com.lim.dto.UserLoginDTO;
import com.lim.entity.User;

public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);
}
