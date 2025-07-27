package top.aprdec.onepractice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.Iinterface.ReuqireRecaptcha;
import top.aprdec.onepractice.designpattern.chain.AbstractChainContext;
import top.aprdec.onepractice.dto.req.ResetPasswordReqDTO;
import top.aprdec.onepractice.dto.req.UserLoginReqDTO;
import top.aprdec.onepractice.dto.req.UserRegistReqDTO;
import top.aprdec.onepractice.dto.resp.UserInfoRespDTO;
import top.aprdec.onepractice.dto.resp.UserLoginRespDTO;
import top.aprdec.onepractice.dto.resp.UserRegistRespDTO;
import top.aprdec.onepractice.eenum.ErrorEnum;
import top.aprdec.onepractice.eenum.UserChainMarkEnum;
import top.aprdec.onepractice.entity.UserDO;
import top.aprdec.onepractice.entity.proxy.UserDOProxy;
import top.aprdec.onepractice.exception.GeneralBusinessException;
import top.aprdec.onepractice.service.CaptchaService;
import top.aprdec.onepractice.service.UserService;
import top.aprdec.onepractice.util.BeanUtil;

import java.util.Optional;

import static top.aprdec.onepractice.commmon.constant.RedisKeyConstant.LOCK_USER_REGISTER;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceimpl implements UserService {
    private final EasyEntityQuery easyEntityQuery;
    private final  RedissonClient redissonClient;
    private final RBloomFilter<String> userRegisterCachePenetrationFilter;
    private final AbstractChainContext<UserRegistReqDTO> abstractChainContext;
    private final CaptchaService captchaService;


    public Boolean hasUsername(String username){
        boolean hasUsername = userRegisterCachePenetrationFilter.contains(username);
        if(hasUsername){
            long count = easyEntityQuery.queryable(UserDO.class).where(u -> u.username().eq(username)).count();
            if(count > 0) return false;
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public UserRegistRespDTO register(UserRegistReqDTO requestparam) {
        abstractChainContext.handler(UserChainMarkEnum.USER_REGISTER_FILTER.name(),requestparam);
        RLock rlock =redissonClient.getLock(LOCK_USER_REGISTER + requestparam.getUsername());
        Boolean checkcaptcha = captchaService.checkEmailCaptcha(requestparam.getEmail(),requestparam.getCaptchacode());
        if(!checkcaptcha){
            throw new GeneralBusinessException(ErrorEnum.CAPTCHA_ERROR);
        }
        boolean trylock = rlock.tryLock();
        if(!trylock){
            throw new GeneralBusinessException(ErrorEnum.USERNAME_EXIST);
        }
        try {
            try {
                requestparam.setEmail(requestparam.getEmail().toLowerCase());
                long inserted = easyEntityQuery.insertable(BeanUtil.convert(requestparam, UserDO.class)).executeRows();
                if (inserted < 1) {
                    throw new GeneralBusinessException(ErrorEnum.REGISTER_ERROR);
                }
            } catch (Exception e) {
                log.error("用户名{}重复注册", requestparam.getUsername());
                throw new GeneralBusinessException(ErrorEnum.USERNAME_EXIST);
            }
            userRegisterCachePenetrationFilter.add(requestparam.getUsername());
        }finally {
            rlock.unlock();
        }
        return BeanUtil.convert(requestparam, UserRegistRespDTO.class);
        }

    @Override
    @ReuqireRecaptcha
    public UserLoginRespDTO login(UserLoginReqDTO requestparam) {
        String usernameorEmail = requestparam.getUsernameorEmail();
        boolean emailflag = false;
        for(char c: usernameorEmail.toCharArray()){
            if(c == '@'){
                emailflag = true;
            }
        }
        String username;
        if(emailflag){
            requestparam.setUsernameorEmail(requestparam.getUsernameorEmail().toLowerCase());
            username = Optional.ofNullable(easyEntityQuery.queryable(UserDO.class)
                    .where(u -> u.email().eq(usernameorEmail))
                    .firstOrNull())
                    .map(UserDO::getUsername)
                    .orElseThrow(()->new GeneralBusinessException(ErrorEnum.USER_NOT_EXIST));
        }else {
            username = requestparam.getUsernameorEmail();
        }
        UserDO userDO = easyEntityQuery.queryable(UserDO.class)
                .where(u -> {
                    u.username().eq(username);
                    u.password().eq(requestparam.getPassword());
                }).firstOrNull();
        if(userDO != null){
            StpUtil.login(userDO.getId());
            String tokenValue = StpUtil.getTokenInfo().tokenValue;
            UserLoginRespDTO result = new UserLoginRespDTO(userDO.getId(), userDO.getUsername(),tokenValue);
            return result;
        }
        throw new GeneralBusinessException(ErrorEnum.PASSWORD_OR_USER_ERROR);

    }

    @Override
    public void logout() {

    }

    @Override
    public UserInfoRespDTO getUserInfoById(Long id){
        UserDO userDO = easyEntityQuery.queryable(UserDO.class)
                .where(u -> u.id().eq(id))
                .select(u -> new UserDOProxy()
                        .id().set(u.id())
                        .username().set(u.username())
                        .usertype().set(u.usertype())
                        .email().set(u.email())
                ).firstOrNull();
        if(userDO != null){
            return new UserInfoRespDTO(userDO.getUsername(),userDO.getUsertype(),userDO.getEmail());
        }else{
            throw new GeneralBusinessException(ErrorEnum.USER_NOT_EXIST);
        }
    }

    @Override
    public Boolean ResetPassword(ResetPasswordReqDTO dto) {
        String email = dto.getEmail();
        UserDO user = easyEntityQuery.queryable(UserDO.class).where(u -> u.email().eq(email)).firstNotNull();
        user.setPassword(dto.getPassword());
        long l = easyEntityQuery.updatable(user).executeRows();
        if(l == 0){
            throw new GeneralBusinessException(ErrorEnum.OPERATE_ERROR);
        }
        return true;
    }

}



