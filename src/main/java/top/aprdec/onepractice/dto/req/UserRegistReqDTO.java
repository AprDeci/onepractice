package top.aprdec.onepractice.dto.req;

import com.github.dozermapper.core.Mapping;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistReqDTO implements HasCaptchaToken{
    @Size(min = 3, max=20,message = "用户名长度在3到20之间")
    private String username;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z][^\\s]{5,}$",message = "密码至少六位,必须字母开头包含数字")
    private String password;
    @Email
    private String email;

    private String captchacode;

    private Integer usertype;

    private String recaptchaToken;
}
