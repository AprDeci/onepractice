package top.aprdec.onepractice.dto.req;

import lombok.Data;

/**
 * 条件筛选
 */
@Data
public class PaperqueryDTO {
    Integer page;

    Integer size;

    String type;

    Integer year;
}
