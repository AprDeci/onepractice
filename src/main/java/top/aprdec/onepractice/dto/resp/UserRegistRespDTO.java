package top.aprdec.onepractice.dto.resp;


import lombok.Data;

@Data
public class UserRegistRespDTO {
    private String username;
    private String password;
    private String email;
}
