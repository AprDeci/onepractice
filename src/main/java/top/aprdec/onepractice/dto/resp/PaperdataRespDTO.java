package top.aprdec.onepractice.dto.resp;

import lombok.Data;
import top.aprdec.onepractice.entity.PaperDO;

import java.util.List;

@Data
public class PaperdataRespDTO {
    private Long total;
    private List<PaperDO> papers;
}
