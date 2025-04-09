package top.aprdec.onepractice.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VoteSubmitReqDTO {
    private Integer paperId;
    private Integer voteValue;
}
