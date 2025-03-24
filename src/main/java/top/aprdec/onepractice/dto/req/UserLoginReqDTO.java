package top.aprdec.onepractice.dto.req;

import lombok.Data;

@Data
public class UserLoginReqDTO {
    private String usernameorEmail;
    private String password;
}
