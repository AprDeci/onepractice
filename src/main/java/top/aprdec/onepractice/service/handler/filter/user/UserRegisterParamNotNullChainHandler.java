package top.aprdec.onepractice.service.handler.filter.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.dto.UserRegistReqDTO;
import java.util.Objects;

@Component
@Slf4j
public class UserRegisterParamNotNullChainHandler implements UserRegisterCreateChainFilter<UserRegistReqDTO>{
    @Override
    public void handler(UserRegistReqDTO requestParam) {
        log.info("------------责任链参数校验------------");
        if(Objects.isNull(requestParam.getUsername())){
            throw new RuntimeException("用户名不能为空");
        } else if (Objects.isNull(requestParam.getPassword())){
            throw new RuntimeException("密码不能为空");
        } else if (Objects.isNull(requestParam.getEmail())){
            throw new RuntimeException("邮箱不能为空");
        } else if (Objects.isNull(requestParam.getUserType())){
            throw new RuntimeException("用户类型不能为空");
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }


}
