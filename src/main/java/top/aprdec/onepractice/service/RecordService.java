package top.aprdec.onepractice.service;

import top.aprdec.onepractice.dto.req.RecordReqDTO;
import top.aprdec.onepractice.entity.UserExamRecordDO;

import java.util.List;

public interface RecordService {
    String addRecord(RecordReqDTO recordReqDTO);


    // 获取最近N天的记录
    List<UserExamRecordDO> getRecentRecords(int days, int pageNum, int pageSize);

    void updateRecord(RecordReqDTO recordReqDTO);
}
