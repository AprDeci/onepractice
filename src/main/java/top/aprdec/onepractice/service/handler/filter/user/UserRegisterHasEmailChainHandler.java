package top.aprdec.onepractice.service.handler.filter.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import top.aprdec.onepractice.dto.req.UserRegistReqDTO;
import top.aprdec.onepractice.eenum.ErrorEnum;
import top.aprdec.onepractice.exception.GeneralBusinessException;
import top.aprdec.onepractice.service.UserService;

@Component
@AllArgsConstructor
public class UserRegisterHasEmailChainHandler implements UserRegisterCreateChainFilter<UserRegistReqDTO> {

    private final UserService userService;

    @Override
    public void handler(UserRegistReqDTO requestParam) {
        if (!userService.hasEmail(requestParam.getEmail())) {
            throw new GeneralBusinessException(ErrorEnum.EMAIL_EXIST);
        }
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
