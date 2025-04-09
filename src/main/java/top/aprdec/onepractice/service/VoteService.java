package top.aprdec.onepractice.service;

import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.dto.req.VoteSubmitReqDTO;
import top.aprdec.onepractice.entity.PaperRateMappingDO;
import top.aprdec.onepractice.entity.VoteDO;

public interface VoteService {

    @Transactional
    void voteSubmit(VoteSubmitReqDTO dto);



    @Transactional
    void updatePaperStatsInRedis(VoteSubmitReqDTO dto);

    Integer getVoteByUserIdAndPaperId(String paperId);


    VoteDO getVoteByUserIdandPaperId(VoteSubmitReqDTO dto);

    PaperRateMappingDO getRatingByPaperId(Integer paperId);
}
