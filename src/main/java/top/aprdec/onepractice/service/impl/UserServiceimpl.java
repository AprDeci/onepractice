package top.aprdec.onepractice.service.impl;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.designpattern.chain.AbstractChainContext;
import top.aprdec.onepractice.dto.UserRegistReqDTO;
import top.aprdec.onepractice.entity.UserDO;
import top.aprdec.onepractice.service.UserService;
import top.aprdec.onepractice.util.BeanUtil;

import static top.aprdec.onepractice.constant.RedisKeyConstant.LOCK_USER_REGISTER;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceimpl implements UserService {
    private final EasyEntityQuery easyEntityQuery;
    private final  RedissonClient redissonClient;
    private final RBloomFilter<String> userRegisterCachePenetrationFilter;
    private final AbstractChainContext<UserRegistReqDTO> abstractChainContext;


    public Boolean hasUsername(String username){
        boolean hasUsername = userRegisterCachePenetrationFilter.contains(username);
        if(hasUsername){
//            TODO:检查缓存层
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(UserRegistReqDTO requestparam) {
        //    TODO:过滤器链
        abstractChainContext.
        RLock rlock =redissonClient.getLock(LOCK_USER_REGISTER + requestparam.getUsername());
        boolean trylock = rlock.tryLock();
        if(!trylock){
            throw new RuntimeException("用户名已存在");
        }
        try {
            try {
                long inserted = easyEntityQuery.insertable(BeanUtil.convert(requestparam, UserDO.class)).executeRows();
                if (inserted < 1) {
                    throw new RuntimeException("注册失败");
                }
            } catch (Exception e) {
                log.error("用户名{}重复注册", requestparam.getUsername());
                throw new RuntimeException("用户名重复注册");
            }
            userRegisterCachePenetrationFilter.add(requestparam.getUsername());
        }finally {
            rlock.unlock();
        }
        }

    @Override
    public void login(String username, String password) {

    }

    @Override
    public void logout() {

    }
}



