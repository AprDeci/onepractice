package top.aprdec.onepractice.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import top.aprdec.onepractice.commmon.constant.RedisKeyConstant;
import top.aprdec.onepractice.dto.req.RecordReqDTO;
import top.aprdec.onepractice.dto.resp.PaperIntroRespDTO;
import top.aprdec.onepractice.entity.UserExamRecordDO;
import top.aprdec.onepractice.service.PaperService;
import top.aprdec.onepractice.service.RecordService;
import top.aprdec.onepractice.util.BeanUtil;
import top.aprdec.onepractice.util.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 用户做题记录 暂时不考虑数据库存储
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecordServiceimpl implements RecordService {

    private final EasyEntityQuery easyEntityQuery;

    private final RedisUtil redisUtil;

    private final RedisTemplate<String,Object> redisTemplate;

    private final PaperService paperService;



    @Override
    public void addRecord(RecordReqDTO recordReqDTO) {
        long LoginId = StpUtil.getLoginIdAsLong();
        String uuid = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis() / 1000;
        String key = String.format(RedisKeyConstant.USER_RECORD+"%d:%d:%s",LoginId,timestamp,uuid);
        UserExamRecordDO convert = BeanUtil.convert(recordReqDTO, UserExamRecordDO.class);
//        paperinfo 会存入缓存
        PaperIntroRespDTO paperIntro = paperService.getPaperIntro(convert.getPaperId());
        String name = String.format("%s年%s月%s", paperIntro.getExamYear(),paperIntro.getExamMonth(),paperIntro.getPaperName());
        convert.setPaperType(paperIntro.getPaperType());
        convert.setPaperName(name);
        convert.setUserId(LoginId);
        convert.setRecordId(uuid);
//        插入redis 有效期30天
        redisUtil.set(key,convert,30*24*60*60);
    }


    // 获取最近N天的记录
    @Override
    public List<UserExamRecordDO> getRecentRecords(int days) {
        long minTimestamp = System.currentTimeMillis() - days * 24 * 60 * 60 * 1000L;
        long LoginId = StpUtil.getLoginIdAsLong();
        String pattern = RedisKeyConstant.USER_RECORD+LoginId+":*";
        List<UserExamRecordDO> result = new ArrayList<>();

        try(Cursor<String> cursor = redisTemplate.scan(ScanOptions.scanOptions().match(pattern).count(100).build())){
            while (cursor.hasNext()) {
                String key = cursor.next();
                System.out.println(key);
//                检查时间戳是否超过了指定天数
                if(Long.parseLong(key.split(":")[4])<minTimestamp){
                    Object o = redisTemplate.opsForValue().get(key);
                    UserExamRecordDO record = JSON.parseObject(o.toString(), UserExamRecordDO.class);
                    result.add(record);
                }
            }
        }

        return result;

    }


}
