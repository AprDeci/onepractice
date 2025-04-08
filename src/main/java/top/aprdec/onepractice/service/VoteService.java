package top.aprdec.onepractice.service;

import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.dto.req.VoteSubmitReqDTO;
import top.aprdec.onepractice.entity.PaperRateMappingDO;

public interface VoteService {

    @Transactional
    void voteSubmit(VoteSubmitReqDTO dto);



    @Transactional
    void updatePaperStatsInRedis(VoteSubmitReqDTO dto);

    Integer getVoteByUserIdAndPaperId(String paperId);

    Integer getVoteByUserId();

    PaperRateMappingDO getRatingByPaperId(Integer paperId);
}
