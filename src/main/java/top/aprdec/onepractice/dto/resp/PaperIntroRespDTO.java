package top.aprdec.onepractice.dto.resp;

import com.easy.query.core.annotation.EntityProxy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@EntityProxy
@NoArgsConstructor
public class PaperIntroRespDTO {
    String paperName;
    Integer examYear;
    Integer examMonth;
    String paperType;
    Integer paperTime;
    String difficulty;
    Long sectionCount;
    int[] sectionQuestionCount;


}
