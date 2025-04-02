package top.aprdec.onepractice.dto.resp;

import com.easy.query.core.annotation.EntityProxy;
import lombok.Data;
import top.aprdec.onepractice.entity.questionsubentity.answer;

import java.util.List;

@Data
@EntityProxy
public class AnswersRespDTO {
    private Integer paperId;

    List<answer> answers;


}
