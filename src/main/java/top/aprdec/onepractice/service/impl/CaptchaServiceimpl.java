package top.aprdec.onepractice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.eenum.EmailTemplateEnum;
import top.aprdec.onepractice.service.CaptchaService;
import top.aprdec.onepractice.util.EmailUtil;
import top.aprdec.onepractice.util.RedisUtil;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import static top.aprdec.onepractice.eenum.EmailTemplateEnum.VERIFICATION_CODE_EMAIL_SUBTITLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaptchaServiceimpl implements CaptchaService {
    private final EmailUtil emailutil;
    private final RedisUtil redisutil;

    @Override
    public Boolean getEmailCaptcha(String email) {
        if(StringUtils.isBlank(email)){
            throw new RuntimeException("邮箱不能为空");
        }
        String key = RedisKeyConstant.CAPTCHA_EMAIL + email;
        String captcha = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        log.info("captcha:{}", captcha);

        // 检查缓存
        if(!redisutil.haskey(key)){
            CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                try {
                    emailutil.sendEmail(email,
                            EmailTemplateEnum.VERIFICATION_CODE_EMAIL_HTML.set(captcha),
                            VERIFICATION_CODE_EMAIL_SUBTITLE.getTemplate());
                    return true; // 发送成功
                } catch (Exception e) {
                    log.error("验证码发送失败: {}", e.getMessage());
                    return false; // 发送失败
                }
            }).thenApply(result -> {
                if(result) {
                    redisutil.set(key, captcha, 5 * 60); // 只有发送成功才存入Redis
                }
                return result;
            });

            try {
                return future.get(); // 等待异步操作完成并返回结果
            } catch (InterruptedException | ExecutionException e) {
                log.error("异步操作异常: {}", e.getMessage());
                return false;
            }
        } else {
            throw new RuntimeException("验证码已发送，请稍后再试");
        }
    }

    @Override
    public Boolean checkEmailCaptcha(String email, String captcha) {
        String key = RedisKeyConstant.CAPTCHA_EMAIL+email;
        String cacheCaptcha = redisutil.getString(key);
        if(!StringUtils.isBlank(cacheCaptcha)) {
            Boolean result =  captcha.equals(cacheCaptcha);
            if(result){
                redisutil.del(key);
            }
            return result;
        }else{
            return false;
        }
    }

}
