package top.aprdec.onepractice.dto.req;

import lombok.Data;

@Data
public class UserRegistReqDTO {

    private String username;

    private String password;

    private String email;

    private Integer userType;


}
