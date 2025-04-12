package top.aprdec.onepractice.controller;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.EmailCaptchaReqDTO;

import top.aprdec.onepractice.eenum.ErrorEnum;
import top.aprdec.onepractice.service.CaptchaService;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {
    private final CaptchaService captchaService;

    @GetMapping("/email")
    public AResult getEmailCaptcha(@RequestParam @Email(message = "邮箱格式不正确") String email){
        Boolean result = captchaService.getEmailCaptcha(email);
        if(result) return AResult.success();
        else return AResult.error(ErrorEnum.CAPTCHA_SEND_ERROR);
    }

    @PostMapping("/email/resetverify")
    public AResult checkEmailCaptchaWhenReset(@RequestBody EmailCaptchaReqDTO dto){
        Boolean b = captchaService.checkEmailCaptchawhenResetPassword(dto.getEmail(),dto.getCode());
        if(b) return AResult.success();
        else return AResult.error(ErrorEnum.CAPTCHA_ERROR);
    }
}
