package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.VoteSubmitReqDTO;
import top.aprdec.onepractice.service.VoteService;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping("/submit")
    public AResult submitVote(@RequestBody VoteSubmitReqDTO dto) {
        voteService.voteSubmit(dto);
        return AResult.success();
    }

    @PostMapping("/update")
    public AResult updateVote(@RequestBody VoteSubmitReqDTO dto) {
        voteService.updatePaperStatsInRedis(dto);
        return AResult.success();
    }

    @PostMapping("/ratingget")
    public AResult getVoteByPaperId(@RequestBody Integer paperId) {
        return AResult.success(voteService.getRatingByPaperId(paperId));
    }


}
