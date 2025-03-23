package top.aprdec.onepractice.service;

import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.dto.UserRegistReqDTO;

public interface UserService {

//    账号注册,登陆,登出方法

    @Transactional(rollbackFor = Exception.class)
    void register(UserRegistReqDTO requestparam);

    void login(String username, String password);

    public void logout();
}
