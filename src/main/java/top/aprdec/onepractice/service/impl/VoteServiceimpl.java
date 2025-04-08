package top.aprdec.onepractice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.dto.req.VoteSubmitReqDTO;
import top.aprdec.onepractice.entity.PaperRateMappingDO;
import top.aprdec.onepractice.entity.VoteDO;
import top.aprdec.onepractice.entity.voteentity.VoteStatisticsEntity;
import top.aprdec.onepractice.service.VoteService;
import top.aprdec.onepractice.util.RedisUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class VoteServiceimpl implements VoteService {

    private final  EasyEntityQuery easyEntityQuery;
    private final RedisUtil redisUtil;
    private final EasyEntityQuery entityQuery;

    @Transactional
    @Override
    public void voteSubmit(VoteSubmitReqDTO dto){
        Integer rating = dto.getRating();
        if(rating < 1||rating>5){
            throw new RuntimeException("评分范围1-5");
        }
        Integer paperId = dto.getPaperId();
        long loginId = StpUtil.getLoginIdAsLong();
        String redisKey = "vote:user:"+loginId+":paper:"+paperId;
        if(Boolean.TRUE.equals(redisUtil.haskey(redisKey))){
            throw new RuntimeException("已经投过票了");
        }

        long count = easyEntityQuery.queryable(VoteDO.class).where(v -> {
            v.paperId().eq(paperId);
            v.and(() -> v.userId().eq(loginId));
        }).count();
        if(count>0){
            throw new RuntimeException("已经投过票了");
        }

        VoteDO voteDO = new VoteDO();
        voteDO.setRating(rating);
        voteDO.setUserId(loginId);
        voteDO.setPaperId(paperId);

        updatePaperStatsInRedis(dto);

        easyEntityQuery.insertable(voteDO).executeRows();
        redisUtil.set(redisKey,rating,24, TimeUnit.HOURS);
        //TODO 异步 mysql统计

    }



    @Override
    @Transactional
    public void updatePaperStatsInRedis(VoteSubmitReqDTO dto) {
        Integer paperId = dto.getPaperId();
        Integer rating = dto.getRating();
        if (paperId == null || rating == null || rating < 0) {
            throw new IllegalArgumentException("参数错误");
        }

        String statsKey = RedisKeyConstant.PAPER_VOTE_KEY + paperId;
        VoteStatisticsEntity voteStats;

//        缓存中没有
        if (!redisUtil.haskey(statsKey)) {
//            数据库取
            PaperRateMappingDO paperRateMappingDO = easyEntityQuery.queryable(PaperRateMappingDO.class).where(p -> p.paperid().eq(paperId)).firstOrNull();
            if (paperRateMappingDO == null) {
                PaperRateMappingDO newDO = new PaperRateMappingDO();
                newDO.setPaperid(paperId);
                long l = easyEntityQuery.insertable(newDO).executeRows();
                if (l <= 0) {
                    throw new RuntimeException("insert paper rate mapping failed");
                }
                voteStats = new VoteStatisticsEntity();
                voteStats.setNumber(0);
                voteStats.setRatingnow(0);
            }else {
                voteStats = new VoteStatisticsEntity();
                voteStats.setNumber(paperRateMappingDO.getNumber());
                voteStats.setRatingnow(paperRateMappingDO.getRating());
            }
        } else {
            Object cachedData = redisUtil.get(statsKey);
                voteStats = JSON.parseObject(cachedData.toString(), VoteStatisticsEntity.class);
                int oldNumber = voteStats.getNumber();
                int oldRating = voteStats.getRatingnow();

                int newRating = (oldRating * oldNumber + rating) / (oldNumber + 1);

                voteStats.setNumber(oldNumber + 1);
                voteStats.setRatingnow(newRating);
        }
        redisUtil.set(statsKey, voteStats, 48, TimeUnit.HOURS);
    }

    /**
     * 获取用户指定试卷投票
     */
    @Override
    public Integer getVoteByUserIdAndPaperId(String paperId){
        long userId = StpUtil.getLoginIdAsLong();
//        检查缓存
        String redisKey = "vote:user:"+userId+":paper:"+paperId;
        if( redisUtil.haskey(redisKey)){
            return Integer.parseInt(redisUtil.getString(redisKey));
        }
//        检查数据库
        VoteDO voteDO = entityQuery.queryable(VoteDO.class).where(v -> {
            v.userId().eq(userId);
            v.and(() -> v.paperId().eq(Integer.valueOf(paperId)));
        }).firstOrNull();

        return voteDO.getRating();

    }

    @Override
    public Integer getVoteByUserId(){
        long userId = StpUtil.getLoginIdAsLong();
        List<VoteDO> Votelist = entityQuery.queryable(VoteDO.class).where(v -> v.userId().eq(userId)).toList();
        //TODO:返回试卷信息+评分+时间VO
        return null;
    }

    @Override
    public PaperRateMappingDO getRatingByPaperId(Integer paperId) {
//        检查缓存
        String statsKey = RedisKeyConstant.PAPER_VOTE_KEY + paperId;
        if (redisUtil.haskey(statsKey)) {
            Object o = redisUtil.get(statsKey);
            VoteStatisticsEntity state = JSON.parseObject(o.toString(), VoteStatisticsEntity.class);
            PaperRateMappingDO result = new PaperRateMappingDO();
            result.setPaperid(paperId);
            result.setRating(state.getRatingnow());
            result.setNumber(state.getNumber());
            return result;
        } else {
//        检查数据库
            PaperRateMappingDO result = easyEntityQuery.queryable(PaperRateMappingDO.class).where(p -> p.paperid().eq(paperId))
                    .firstNotNull();
//        创建缓存
            VoteStatisticsEntity state = new VoteStatisticsEntity(result.getNumber(), result.getRating());

            redisUtil.set(statsKey, state, 48, TimeUnit.HOURS);
            return result;
        }
    }





}
