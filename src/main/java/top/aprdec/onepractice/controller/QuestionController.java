package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.resp.AnswersRespDTO;
import top.aprdec.onepractice.dto.resp.ExamQuestionRespDTO;
import top.aprdec.onepractice.entity.QuestionsDO;
import top.aprdec.onepractice.service.QuestionService;
import top.aprdec.onepractice.service.impl.QuestionServiceimpl;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
    private static final Logger log = LoggerFactory.getLogger(QuestionController.class);
    private final QuestionService questionService;

    @GetMapping("/getById")
    public AResult<List<QuestionsDO>> getQuestionsByPaperId(@RequestParam Integer id) {
        log.info(id.toString());
        List<QuestionsDO> questions = questionService.getQuestionByPaperId(id);
        return AResult.success(questions);
    }

    @GetMapping("/getByType")
    public AResult<List<QuestionsDO>> getQuestionsByPaperIdAndType(@RequestParam Integer id, @RequestParam String type) {
        List<QuestionsDO> questions = questionService.getQuestionByPaperIdAndType(id, type);
        return AResult.success(questions);
    }

    @GetMapping("/getAllByIdSplitByPart")
    public AResult<ExamQuestionRespDTO> getQuestionsByPaperIdSplitByPart(@RequestParam Integer id) {
        ExamQuestionRespDTO questionsByPaperIdSplitByPart = questionService.getQuestionsByPaperIdSplitByPart(id);
        return AResult.success(questionsByPaperIdSplitByPart);
    }

    @GetMapping("/getAnswersByPaperId")
    public AResult<AnswersRespDTO> getAnswersByPaperId(@RequestParam Integer id) {
        AnswersRespDTO dto = questionService.getAnswersByPaperId(id);
        return AResult.success(dto);
    }

}
