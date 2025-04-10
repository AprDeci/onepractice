package top.aprdec.onepractice.entity.questionsubentity;

import lombok.Data;

import java.util.List;

/**
 * 匹配题专用字段
 */
@Data
public class matchContent {
    private String title;
    private List<paragraph> paragraphs;

    @Data
    public class paragraph{
        private String index;
        private String content;
    }
}
