package top.aprdec.onepractice.controller;

import com.easy.query.core.api.pagination.EasyPageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.PaperqueryDTO;
import top.aprdec.onepractice.dto.resp.PaperIntroRespDTO;
import top.aprdec.onepractice.dto.resp.PaperWithRatingRespDTO;
import top.aprdec.onepractice.dto.resp.PaperdataRespDTO;
import top.aprdec.onepractice.entity.PaperDO;
import top.aprdec.onepractice.service.PaperService;

import java.awt.print.Paper;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/paper")
@RequiredArgsConstructor
public class PaperController {

    private final PaperService paperService;

    @GetMapping("/all")
    public AResult getallpaper() {
        List allPapers = paperService.getAllPapers();
        return AResult.success(allPapers);
    }

    @PostMapping("/page")
    public AResult getPapersByPageandSize(@RequestBody Integer page,@RequestBody Integer size) {
        PaperdataRespDTO result = paperService.getPapersByPageAndSize(page, size);
        return AResult.success(result);
    }

    @PostMapping("/getPaperwithQuerys")
    public AResult getPaperWithquerysByPageAndSize(@RequestBody PaperqueryDTO querys) {
        EasyPageResult<PaperDO> result = paperService.getPaperswithQuerysByPageAndSize(querys);
        return AResult.success(result);
    }

    @PostMapping("/getPaperandRatingWithQuerys")
    public AResult getPaperandRatingWithQuerysByPageAndSize(@RequestBody PaperqueryDTO querys) {
        EasyPageResult<PaperWithRatingRespDTO> result = paperService.getPapersAndRatingWithQuerysByPageAndSize(querys);
        return AResult.success(result);
    }

    @GetMapping("/type")
    public AResult getPaperByType(@RequestParam String type) {
        List<PaperDO> papers = paperService.getPapersBytype(type);
        return AResult.success(papers);
    }

    @GetMapping("/types")
    public AResult getAllPaperTypes(){
        List<String> allTypes = paperService.getAllTypes();
        return AResult.success(allTypes);
    }

    @GetMapping("/intro")
    public AResult getPaperIntro(@RequestParam Integer id){
        PaperIntroRespDTO paperIntroRespDTO = paperService.getPaperIntro(id);
        return AResult.success(paperIntroRespDTO);
    }
}
