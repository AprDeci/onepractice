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
import top.aprdec.onepractice.util.RecordIdGenerator;
import top.aprdec.onepractice.util.RedisUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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

    private static final int RECORD_TTL_SECONDS = 30 * 24 * 60 * 60;

    private static final long RECORD_TTL_MILLIS = 30L * 24 * 60 * 60 * 1000;



    @Override
    public String addRecord(RecordReqDTO recordReqDTO) {
        long loginId = StpUtil.getLoginIdAsLong();
        String recordId = RecordIdGenerator.nextRecordId();
        long timestamp = System.currentTimeMillis();
        long expireBefore = timestamp - RECORD_TTL_MILLIS;

        String recordKey = String.format(RedisKeyConstant.USER_RECORD + "%d:%s", loginId, recordId);


        UserExamRecordDO convert = BeanUtil.convert(recordReqDTO, UserExamRecordDO.class);

        PaperIntroRespDTO paperIntro = paperService.getPaperIntro(convert.getPaperId());
        String name = String.format("%s年%s月%s", paperIntro.getExamYear(), paperIntro.getExamMonth(), paperIntro.getPaperName());

        convert.setPaperType(paperIntro.getPaperType());
        convert.setPaperName(name);
        convert.setUserId(loginId);
        convert.setRecordId(recordId);
        convert.setTimestamp(timestamp);

        // 使用事务保证原子性
        redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();

                // 1. 将记录详情存入String类型
                operations.opsForValue().set(recordKey, convert, RECORD_TTL_SECONDS, TimeUnit.SECONDS);

                // 2. 将记录ID添加到Sorted Set，score使用时间戳
                String sortedSetKey = RedisKeyConstant.USER_RECORD_SORTED_SET + loginId;
                operations.opsForZSet().add(sortedSetKey, recordKey, timestamp);
                operations.opsForZSet().removeRangeByScore(sortedSetKey, 0, expireBefore);

                // 3. 设置Sorted Set的过期时间
                operations.expire(sortedSetKey, RECORD_TTL_SECONDS, TimeUnit.SECONDS);

                return operations.exec();
            }
        });
//        返回recordid
        return recordId;
    }


    // 获取最近N天的记录
    @Override
    public List<UserExamRecordDO> getRecentRecords(int days, int pageNum, int pageSize) {
        if (days < 1) {
            days = 1;
        }
        if (pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        long loginId = StpUtil.getLoginIdAsLong();
        long now = System.currentTimeMillis();
        long minTimestamp = now - days * 24 * 60 * 60 * 1000L;

        String sortedSetKey = RedisKeyConstant.USER_RECORD_SORTED_SET + loginId;
        long expireBefore = now - RECORD_TTL_MILLIS;
        stringRedisTemplate.opsForZSet().removeRangeByScore(sortedSetKey, 0, expireBefore);



        // 使用ZREVRANGEBYSCORE获取指定时间范围内的记录（按时间倒序）
        Set<String> recordKeys = stringRedisTemplate.opsForZSet().reverseRangeByScore(
                sortedSetKey,
                minTimestamp,
                now,
                (long) (pageNum - 1) * pageSize,
                pageSize
        );

        List<UserExamRecordDO> result = new ArrayList<>();

        if (recordKeys == null || recordKeys.isEmpty()) {
            return result;
        }

        List<Object> records = redisTemplate.opsForValue().multiGet(new ArrayList<>(recordKeys));
        if (records == null || records.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> missingRecordKeys = new ArrayList<>();

        List<String> keyList = new ArrayList<>(recordKeys);
        for (int i = 0; i < records.size(); i++) {
            Object record = records.get(i);
            if (record == null) {
                if (i < keyList.size()) {
                    missingRecordKeys.add(keyList.get(i));
                }
                continue;
            }
            if (record instanceof UserExamRecordDO userExamRecord) {
                result.add(userExamRecord);
                continue;
            }
            result.add(JSON.parseObject(JSON.toJSONString(record), UserExamRecordDO.class));
        }

        if (!missingRecordKeys.isEmpty()) {
            stringRedisTemplate.opsForZSet().remove(sortedSetKey, missingRecordKeys.toArray());
        }

        return result;
    }

    @Override
    public void updateRecord(RecordReqDTO recordReqDTO) {
        long loginId = StpUtil.getLoginIdAsLong();
        long timestamp = System.currentTimeMillis();
        long expireBefore = timestamp - RECORD_TTL_MILLIS;
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
        existingRecord.setHasspendtime(recordReqDTO.getHasspendtime());


        // 更新记录（Sorted Set不需要更新，因为key和score都没变）
        redisTemplate.opsForValue().set(
                targetKey,
                existingRecord,
                RECORD_TTL_SECONDS,
                TimeUnit.SECONDS
        );

        String sortedSetKey = RedisKeyConstant.USER_RECORD_SORTED_SET + loginId;
        redisTemplate.opsForZSet().add(sortedSetKey, targetKey, timestamp);
        redisTemplate.opsForZSet().removeRangeByScore(sortedSetKey, 0, expireBefore);
        redisTemplate.expire(sortedSetKey, RECORD_TTL_SECONDS, TimeUnit.SECONDS);
    }


}
