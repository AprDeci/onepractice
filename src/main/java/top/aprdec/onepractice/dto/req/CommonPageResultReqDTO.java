package top.aprdec.onepractice.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommonPageResultReqDTO {
    @NotNull(message = "page不能为空")
    @Min(value = 1, message = "page最小为1")
    private Integer page;

    @NotNull(message = "size不能为空")
    @Min(value = 1, message = "size最小为1")
    @Max(value = 100, message = "size最大为100")
    private Integer size;
}
