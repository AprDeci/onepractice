package top.aprdec.onepractice.service;

import top.aprdec.onepractice.dto.req.RecordReqDTO;
import top.aprdec.onepractice.entity.UserExamRecordDO;

import java.util.List;

public interface RecordService {
    void addRecord(RecordReqDTO recordReqDTO);

    // 获取最近N天的记录
    List<UserExamRecordDO> getRecentRecords(int days);

    void updateRecord(RecordReqDTO recordReqDTO, String recordId);
}
