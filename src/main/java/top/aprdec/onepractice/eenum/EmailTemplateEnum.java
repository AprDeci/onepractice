package top.aprdec.onepractice.eenum;


import lombok.Getter;

import java.util.Locale;

@Getter
public enum EmailTemplateEnum {
    VERIFICATION_CODE_EMAIL_SUBTITLE("onepractice验证码","验证码标题"),
    VERIFICATION_CODE_EMAIL_HTML("<html><body>用户你好，你的验证码是:<h1>%s</h1></body></html>","登录验证");

    private String template;

    private String desc;


    EmailTemplateEnum(String template, String desc) {
        this.template = template;
        this.desc = desc;
    }

    public String set(Object... args){
        return String.format(Locale.ROOT,this.template,args);
    }


}
