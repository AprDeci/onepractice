package top.aprdec.onepractice.annotation;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import top.aprdec.onepractice.Iinterface.Idempotent;
import top.aprdec.onepractice.exception.CommonException;

import java.lang.reflect.Method;

@Aspect
@Configuration
@Order(2)
@Slf4j
public class IdempotentAspect {

    private RedissonClient redissonClient;

    @Autowired
    public IdempotentAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Around("execution(public * *(..)) && @annotation(top.aprdec.onepractice.Iinterface.Idempotent)")
    public Object interceptor(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        if(StringUtils.isEmpty(idempotent.keyPrefix())){
            throw new CommonException("Idempotent注解的keyPrefix不能为空");
        }
        StringBuilder sb = new StringBuilder();
        sb.append(idempotent.keyPrefix()).append(StpUtil.getLoginIdAsString()).append(idempotent.delimiter()).append(method.getName());
        final String lockKey = sb.toString();
        log.info(lockKey);
        RLock lock = redissonClient.getLock(lockKey);
        boolean isLocked = false;
        try {
            // 如果锁已被当前线程持有（重入次数 > 0），直接拒绝
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                throw new RuntimeException("请勿重复请求");
            }
            // 尝试获取锁
            isLocked = lock.tryLock(0, idempotent.timeout(), idempotent.timeUnit());
            if (!isLocked) {
                throw new RuntimeException(idempotent.message());
            }
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw new CommonException("系统异常:"+e.getMessage());
        } finally {
            if (isLocked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }
}
