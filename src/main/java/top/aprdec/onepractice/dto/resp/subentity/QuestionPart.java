package top.aprdec.onepractice.dto.resp.subentity;

import lombok.Data;
import top.aprdec.onepractice.entity.QuestionsDO;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QuestionPart implements Serializable {
    @Serial
    private static final long serialVersionUID = 6075274268221156079L;

    private QuestionsDO[] questions;
}
