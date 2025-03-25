package com.lim.Service;

import com.lim.dto.UserLoginDTO;
import com.lim.entity.User;

public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);
}
