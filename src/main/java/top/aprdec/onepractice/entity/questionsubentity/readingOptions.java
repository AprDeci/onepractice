package top.aprdec.onepractice.entity.questionsubentity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class readingOptions implements Serializable {
    @Serial
    private static final long serialVersionUID = 2044533065787678305L;
    private List<Question> questions;

    @Data
    public static class Question {
        private String content;
        private List<Option> options;
        private int question_id;
    }

    @Data
    public static class Option {
        private String label;
        private String content;
    }
}