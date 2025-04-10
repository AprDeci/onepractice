package top.aprdec.onepractice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.dto.req.VoteSubmitReqDTO;
import top.aprdec.onepractice.eenum.ErrorEnum;
import top.aprdec.onepractice.entity.PaperRateMappingDO;
import top.aprdec.onepractice.entity.VoteDO;
import top.aprdec.onepractice.entity.voteentity.VoteStatisticsEntity;
import top.aprdec.onepractice.exception.GeneralBusinessException;
import top.aprdec.onepractice.service.VoteService;
import top.aprdec.onepractice.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class VoteServiceimpl implements VoteService {

    private static final Logger log = LoggerFactory.getLogger(VoteServiceimpl.class);
    private final  EasyEntityQuery easyEntityQuery;
    private final RedisUtil redisUtil;
    private final EasyEntityQuery entityQuery;
    private final RedisTemplate<String,Object> redisTemplate;

    @Transactional
    @Override
    public void voteSubmit(VoteSubmitReqDTO dto){
        System.out.println(dto);
        Integer rating = dto.getVoteValue();
        if(rating < 1||rating>5){
            throw new GeneralBusinessException(ErrorEnum.PARAM_IS_INVALID);
        }
        Integer paperId = dto.getPaperId();
        long loginId = StpUtil.getLoginIdAsLong();
        String redisKey = "onepractice:vote:user:"+loginId+":paper:"+paperId;
        if(Boolean.TRUE.equals(redisUtil.haskey(redisKey))){
            throw new GeneralBusinessException(ErrorEnum.REPEAT_OPERATION);
        }

        long count = easyEntityQuery.queryable(VoteDO.class).where(v -> {
            v.paperId().eq(paperId);
            v.and(() -> v.userId().eq(loginId));
        }).count();
        if(count>0){
            throw new GeneralBusinessException(ErrorEnum.REPEAT_OPERATION);
        }

        VoteDO voteDO = new VoteDO();
        voteDO.setRating(rating);
        voteDO.setUserId(loginId);
        voteDO.setPaperId(paperId);

        updatePaperStatsInRedis(dto);

        easyEntityQuery.insertable(voteDO).executeRows();
        redisUtil.set(redisKey,rating,24, TimeUnit.HOURS);


    }



    @Override
    @Transactional
    public void updatePaperStatsInRedis(VoteSubmitReqDTO dto) {
        Integer paperId = dto.getPaperId();
        Integer rating = dto.getVoteValue();
        if (paperId == null || rating == null || rating < 0) {
            throw new GeneralBusinessException(ErrorEnum.PARAM_IS_INVALID);
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
                newDO.setRating(rating);
                newDO.setNumber(1);
                long l = easyEntityQuery.insertable(newDO).executeRows();
                if (l <= 0) {
                    throw new GeneralBusinessException(ErrorEnum.OPERATE_ERROR);
                }
                voteStats = new VoteStatisticsEntity();
                voteStats.setNumber(1);
                voteStats.setRatingnow(rating);
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
    public VoteDO getVoteByUserIdandPaperId(VoteSubmitReqDTO dto){
        long userId = StpUtil.getLoginIdAsLong();
        Integer paperId = dto.getPaperId();
        VoteDO voteDO = entityQuery.queryable(VoteDO.class).where(v -> {
            v.userId().eq(userId);
            v.and(() -> v.paperId().eq(paperId));
        }).firstOrNull();

        return voteDO;
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

    /**
     * 异步定时同步mysql和redis换粗->试卷评分
     */
    @Async("asyncTaskExecutor")
    @Scheduled(cron = "0 0 1 * * ?")//每日一点同步
    public void syncRating(){
        log.info("开始同步试卷评分");
        String pattern = RedisKeyConstant.PAPER_VOTE_KEY+"*";
        List<PaperRateMappingDO> batchlist = new ArrayList<>();
        long successCount = 0;
        long failCount = 0;
//        scan获取所有数据
        try(Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions().match(pattern).count(100).build())){
            while (cursor.hasNext()){
                try {
                    String key = cursor.next();
                    Integer id = Integer.valueOf(key.split(":")[3]);
                    //        循环同步数据库
                    Object o = redisTemplate.opsForValue().get(key);
                    VoteStatisticsEntity state = JSON.parseObject(o.toString(), VoteStatisticsEntity.class);
                    PaperRateMappingDO paperRateMappingDO = new PaperRateMappingDO(id, state.getRatingnow(), state.getNumber());
                    batchlist.add(paperRateMappingDO);
                    if (batchlist.size() >= 100) {
                        long l = easyEntityQuery.updatable(batchlist).batch().executeRows();
                        successCount += l;
                        batchlist.clear();
                    }
                }catch (Exception e){
                    log.error("处理数据失败",e);
                    failCount++;
                }
            }
            if(!batchlist.isEmpty()) {
                long l = easyEntityQuery.updatable(batchlist).executeRows();
                successCount += l;
            }
        }catch (Exception e){
            log.error("同步数据失败",e);
        }
        log.info("试卷评分同步成功更新{}条,失败{}条",successCount,failCount);

    }




}
