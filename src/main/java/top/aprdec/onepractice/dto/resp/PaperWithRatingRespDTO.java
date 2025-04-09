package top.aprdec.onepractice.dto.resp;

import com.easy.query.core.annotation.*;
import com.easy.query.core.enums.RelationTypeEnum;
import com.easy.query.core.proxy.ProxyEntityAvailable;
import lombok.Data;
import top.aprdec.onepractice.dto.resp.proxy.PaperWithRatingRespDTOProxy;
import top.aprdec.onepractice.entity.QuestionsDO;
import top.aprdec.onepractice.util.easyentity.QuestionCountColumnValueSQLConverter;

import java.util.List;

@Data
@EntityProxy
public class PaperWithRatingRespDTO implements ProxyEntityAvailable<PaperWithRatingRespDTO , PaperWithRatingRespDTOProxy> {
    private Integer paperId;

    private String paperName;

    private Integer examYear;

    private Integer examMonth;

    private Integer version;

    /**
     * 总时长(分钟)
     */
    private Integer totalTime;

    private String type;

    private List<QuestionsDO> questions;

    private Long questionCount;

    private Integer rating;

    private Integer number;

}
