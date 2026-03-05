package top.aprdec.onepractice.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordReqDTO {
    @Email
    private String email;
    @NotBlank(message = "重置凭证不能为空")
    private String resetToken;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z][^\\s]{5,}$",message = "密码至少六位,字母开头包含数字")
    private String password;
}
