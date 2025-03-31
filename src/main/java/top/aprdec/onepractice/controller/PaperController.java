package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.resp.PaperIntroRespDTO;
import top.aprdec.onepractice.entity.PaperDO;
import top.aprdec.onepractice.service.PaperService;

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
