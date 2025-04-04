package top.aprdec.onepractice.dto.req;

import lombok.Data;

@Data
public class RecordReqDTO {
    private Integer paperId;

    private String type;

    private Integer isfinished;

    private String answers;

    private Integer score;

    private Integer timespend;


}
