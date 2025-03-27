package top.aprdec.onepractice.entity.questionsubentity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode
public class matchingdata {
    private List<item> items;
    private Map<String, String> paragraphs;

    @Data
    static public class item{
        private int id;
        private String content;
    }

}
