package top.aprdec.onepractice.dto;

import lombok.Data;

@Data
public class UserRegistReqDTO {

    private String username;

    private String password;

    private String email;

    private Integer userType;


}
