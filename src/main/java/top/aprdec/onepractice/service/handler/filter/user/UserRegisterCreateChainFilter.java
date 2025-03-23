package top.aprdec.onepractice.service.handler.filter.user;

import top.aprdec.onepractice.designpattern.chain.AbstractChainHandler;
import top.aprdec.onepractice.dto.req.UserRegistReqDTO;
import top.aprdec.onepractice.eenum.UserChainMarkEnum;


public interface UserRegisterCreateChainFilter<T extends UserRegistReqDTO> extends AbstractChainHandler<UserRegistReqDTO> {

    default String mark(){
        return UserChainMarkEnum.USER_REGISTER_FILTER.name();
    }


}
