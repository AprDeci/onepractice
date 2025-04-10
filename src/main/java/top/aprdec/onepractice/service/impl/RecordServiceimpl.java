package top.aprdec.onepractice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.dto.req.RecordReqDTO;
import top.aprdec.onepractice.dto.resp.PaperIntroRespDTO;
import top.aprdec.onepractice.eenum.ErrorEnum;
import top.aprdec.onepractice.entity.UserExamRecordDO;
import top.aprdec.onepractice.exception.GeneralBusinessException;
import top.aprdec.onepractice.service.PaperService;
import top.aprdec.onepractice.service.RecordService;
import top.aprdec.onepractice.util.BeanUtil;
import top.aprdec.onepractice.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户做题记录 暂时不考虑数据库存储
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecordServiceimpl implements RecordService {

    private final EasyEntityQuery easyEntityQuery;

    private final RedisUtil redisUtil;

    private final RedisTemplate<String, Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    private final PaperService paperService;



    @Override
    public void addRecord(RecordReqDTO recordReqDTO) {
        long loginId = StpUtil.getLoginIdAsLong();
        String uuid = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis();

        String recordKey = String.format(RedisKeyConstant.USER_RECORD+"%d:%s", loginId, uuid);


        UserExamRecordDO convert = BeanUtil.convert(recordReqDTO, UserExamRecordDO.class);

        PaperIntroRespDTO paperIntro = paperService.getPaperIntro(convert.getPaperId());
        String name = String.format("%s年%s月%s", paperIntro.getExamYear(), paperIntro.getExamMonth(), paperIntro.getPaperName());

        convert.setPaperType(paperIntro.getPaperType());
        convert.setPaperName(name);
        convert.setUserId(loginId);
        convert.setRecordId(uuid);
        convert.setTimestamp(timestamp);

        // 使用事务保证原子性
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                // 1. 将记录详情存入String类型
                operations.opsForValue().set(recordKey, convert, 30 * 24 * 60 * 60, TimeUnit.SECONDS);

                // 2. 将记录ID添加到Sorted Set，score使用时间戳
                String sortedSetKey = RedisKeyConstant.USER_RECORD_SORTED_SET + loginId;
                operations.opsForZSet().add(sortedSetKey, recordKey, timestamp);

                // 3. 设置Sorted Set的过期时间
                operations.expire(sortedSetKey, 30 * 24 * 60 * 60, TimeUnit.SECONDS);

                return operations.exec();
            }
        });
    }


    // 获取最近N天的记录
    @Override
    public List<UserExamRecordDO> getRecentRecords(int days, int pageNum, int pageSize) {
        long loginId = StpUtil.getLoginIdAsLong();
        long minTimestamp = System.currentTimeMillis() - days * 24 * 60 * 60 * 1000L;

        String sortedSetKey = RedisKeyConstant.USER_RECORD_SORTED_SET + loginId;



        // 使用ZREVRANGEBYSCORE获取指定时间范围内的记录（按时间倒序）
        Set<String> recordKeys = stringRedisTemplate.opsForZSet().reverseRangeByScore(
                sortedSetKey,
                minTimestamp,
                System.currentTimeMillis(),
                (pageNum - 1) * pageSize,
                pageSize
        );

        List<UserExamRecordDO> result = new ArrayList<>();

        if (recordKeys != null && !recordKeys.isEmpty()) {


            log.info("keys"+ recordKeys);
            // 批量获取记录详情
            List<Object> records = redisTemplate.opsForValue().multiGet(recordKeys);

            log.info("recoids"+records.toString());
                for (Object record : records) {
                        result.add(JSON.parseObject(record.toString(), UserExamRecordDO.class));
                }
        }

        return result;
    }

    @Override
    public void updateRecord(RecordReqDTO recordReqDTO) {
        long loginId = StpUtil.getLoginIdAsLong();
        long timestamp = System.currentTimeMillis();
        String recordId = recordReqDTO.getRecordId();
        String targetKey = String.format(RedisKeyConstant.USER_RECORD + "%d:%s", loginId, recordId);

        if (!redisTemplate.hasKey(targetKey)) {
            throw new GeneralBusinessException(ErrorEnum.PARAM_IS_INVALID);
        }

        UserExamRecordDO existingRecord = JSON.parseObject(
                redisTemplate.opsForValue().get(targetKey).toString(),
                UserExamRecordDO.class
        );

        // 设置更新后的属性(时间戳,score,answer)
        existingRecord.setTimestamp(timestamp);
        existingRecord.setScore(recordReqDTO.getScore());
        existingRecord.setAnswers(recordReqDTO.getAnswers());
        existingRecord.setIsfinished(recordReqDTO.getIsfinished());


        // 更新记录（Sorted Set不需要更新，因为key和score都没变）
        redisTemplate.opsForValue().set(
                targetKey,
                existingRecord,
                30 * 24 * 60 * 60,
                TimeUnit.SECONDS
        );
    }


}
