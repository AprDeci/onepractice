package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.UserLoginReqDTO;
import top.aprdec.onepractice.dto.req.UserRegistReqDTO;
import top.aprdec.onepractice.dto.resp.UserLoginRespDTO;
import top.aprdec.onepractice.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping("/info")
    public String getUserInfo(){
        return "1";
    }

    @PostMapping("/register")
    public AResult register(@RequestBody @Validated UserRegistReqDTO requestparam){
        return AResult.success(userService.register(requestparam));
    }

    @PostMapping("/login")
    public AResult login(@RequestBody @Validated UserLoginReqDTO requestparam){
        return AResult.success(userService.login(requestparam));
    }
}
