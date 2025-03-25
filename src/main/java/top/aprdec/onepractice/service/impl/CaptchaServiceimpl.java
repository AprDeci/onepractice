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
import java.util.concurrent.ThreadLocalRandom;

import static top.aprdec.onepractice.eenum.EmailTemplateEnum.VERIFICATION_CODE_EMAIL_SUBTITLE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaptchaServiceimpl implements CaptchaService {
    private final EmailUtil emailutil;
    private final RedisUtil redisutil;

    @Override
    public void getEmailCaptcha(String email) {
        String key = RedisKeyConstant.CAPTCHA_EMAIL+email;
        String captcha = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        log.info("captcha:{}",captcha);
        //检查缓存 发送过->不再发送 没发送过->设置缓存,发送
        if(!redisutil.haskey(key)){
//            异步发送
            CompletableFuture.runAsync(()->{
                try {
                    emailutil.sendEmail(email, EmailTemplateEnum.VERIFICATION_CODE_EMAIL_HTML.set(captcha),VERIFICATION_CODE_EMAIL_SUBTITLE.getTemplate());
                }catch (Exception e){
                    throw new RuntimeException("验证码发送失败"+e.getMessage());
                }
            }).thenRunAsync(()->{
                redisutil.set(key,captcha, 5*60);
            });
        }else throw new RuntimeException("验证码已发送，请稍后再试");
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
