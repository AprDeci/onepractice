package top.aprdec.onepractice.dto.resp;

import lombok.Data;
import top.aprdec.onepractice.dto.resp.subentity.QuestionPart;

@Data
public class ExamQuestionRespDTO {
    private Integer paperId;
    private QuestionPart[] questionParts;
}
