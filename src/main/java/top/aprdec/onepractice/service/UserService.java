package top.aprdec.onepractice.service;

import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.dto.req.UserRegistReqDTO;
import top.aprdec.onepractice.dto.resp.UserRegistRespDTO;

public interface UserService {

    Boolean hasUsername(String username);

//    账号注册,登陆,登出方法

    @Transactional(rollbackFor = Exception.class)
    UserRegistRespDTO register(UserRegistReqDTO requestparam);

    void login(String username, String password);

    public void logout();
}
