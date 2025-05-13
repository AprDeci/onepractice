package top.aprdec.onepractice.dto.resp;

import lombok.Data;
import top.aprdec.onepractice.dto.resp.subentity.QuestionPart;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ExamQuestionRespDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2784836381880029612L;
    private Integer paperId;
    private QuestionPart[] questionParts;
}
