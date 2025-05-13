package top.aprdec.onepractice.dto.req;

import lombok.Data;

@Data
public class UserSaveWordReqDTO {
    private Long userId;
    private String word;
}
