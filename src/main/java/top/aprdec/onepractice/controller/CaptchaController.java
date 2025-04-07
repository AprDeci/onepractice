package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.EmailCaptchaReqDTO;
import top.aprdec.onepractice.eenum.CodeEnum;
import top.aprdec.onepractice.service.CaptchaService;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {
    private final CaptchaService captchaService;

    @GetMapping("/email")
    public AResult getEmailCaptcha(@RequestParam String email){
        Boolean result = captchaService.getEmailCaptcha(email);
        if(result) return AResult.success();
        else return AResult.error(CodeEnum.CAPTCHA_SEND_ERROR.getCode(), CodeEnum.CAPTCHA_SEND_ERROR.getMsg());
    }

    @PostMapping("/email/resetverify")
    public AResult checkEmailCaptchaWhenReset(@RequestBody EmailCaptchaReqDTO dto){
        Boolean b = captchaService.checkEmailCaptchawhenResetPassword(dto.getEmail(),dto.getCode());
        if(b) return AResult.success();
        else return AResult.error(CodeEnum.CAPTCHA_ERROR.getCode(), CodeEnum.CAPTCHA_ERROR.getMsg());
    }
}
