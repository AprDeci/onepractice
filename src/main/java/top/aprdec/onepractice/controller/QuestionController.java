package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.aprdec.onepractice.commmon.AResult;
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
    public AResult getQuestionsByPaperId(@RequestParam Integer Id){
        log.info(Id.toString());
        List<QuestionsDO> questions = questionService.getQuestionByPaperId(Id);
        return AResult.success(questions);
    }

    @GetMapping("/getByType")
    public AResult getQuestionsByPaperIdAndType(@RequestParam Integer Id,@RequestParam String type){
        List<QuestionsDO> questions = questionService.getQuestionByPaperIdAndType(Id,type);
        return AResult.success(questions);
    }

    @GetMapping("/getAllByIdSplitByPart")
    public AResult getQuestionsByPaperIdSplitByPart(@RequestParam Integer Id){
        ExamQuestionRespDTO questionsByPaperIdSplitByPart = questionService.getQuestionsByPaperIdSplitByPart(Id);
        return AResult.success(questionsByPaperIdSplitByPart);
    }

}
