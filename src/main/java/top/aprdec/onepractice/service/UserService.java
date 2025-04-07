package top.aprdec.onepractice.service;

import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.dto.req.ResetPasswordReqDTO;
import top.aprdec.onepractice.dto.req.UserLoginReqDTO;
import top.aprdec.onepractice.dto.req.UserRegistReqDTO;
import top.aprdec.onepractice.dto.resp.UserInfoRespDTO;
import top.aprdec.onepractice.dto.resp.UserLoginRespDTO;
import top.aprdec.onepractice.dto.resp.UserRegistRespDTO;

public interface UserService {

    Boolean hasUsername(String username);

//    账号注册,登陆,登出方法

    @Transactional(rollbackFor = Exception.class)
    UserRegistRespDTO register(UserRegistReqDTO requestparam);



    UserLoginRespDTO login(UserLoginReqDTO requestparam);

    public void logout();

    UserInfoRespDTO getUserInfoById(Long id);

    Boolean ResetPassword(ResetPasswordReqDTO dto);
}
