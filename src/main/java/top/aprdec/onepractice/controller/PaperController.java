package top.aprdec.onepractice.controller;

import com.easy.query.core.api.pagination.EasyPageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.PaperqueryDTO;
import top.aprdec.onepractice.dto.resp.PaperIntroRespDTO;
import top.aprdec.onepractice.dto.resp.PaperWithRatingRespDTO;
import top.aprdec.onepractice.entity.PaperDO;
import top.aprdec.onepractice.service.PaperService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/paper")
@RequiredArgsConstructor
@Validated
public class PaperController {

    private final PaperService paperService;

    @GetMapping("/all")
    public AResult<List<PaperDO>> getallpaper() {
        List<PaperDO> allPapers = paperService.getAllPapers();
        return AResult.success(allPapers);
    }

    @PostMapping("/getPaperwithQuerys")
    public AResult<EasyPageResult<PaperDO>> getPaperWithquerysByPageAndSize(@RequestBody @Valid PaperqueryDTO querys) {
        EasyPageResult<PaperDO> result = paperService.getPaperswithQuerysByPageAndSize(querys);
        return AResult.success(result);
    }

    @PostMapping("/getPaperandRatingWithQuerys")
    public AResult<EasyPageResult<PaperWithRatingRespDTO>> getPaperandRatingWithQuerysByPageAndSize(@RequestBody @Valid PaperqueryDTO querys) {
        EasyPageResult<PaperWithRatingRespDTO> result = paperService.getPapersAndRatingWithQuerysByPageAndSize(querys);
        return AResult.success(result);
    }

    @GetMapping("/type")
    public AResult<List<PaperDO>> getPaperByType(@RequestParam String type) {
        List<PaperDO> papers = paperService.getPapersBytype(type);
        return AResult.success(papers);
    }

    @GetMapping("/types")
    public AResult<List<String>> getAllPaperTypes() {
        List<String> allTypes = paperService.getAllTypes();
        return AResult.success(allTypes);
    }

    @GetMapping("/intro")
    public AResult<PaperIntroRespDTO> getPaperIntro(@RequestParam Integer id) {
        PaperIntroRespDTO paperIntroRespDTO = paperService.getPaperIntro(id);
        return AResult.success(paperIntroRespDTO);
    }
}
