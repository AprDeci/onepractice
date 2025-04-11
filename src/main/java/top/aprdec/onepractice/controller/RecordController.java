package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.RecordReqDTO;
import top.aprdec.onepractice.entity.UserExamRecordDO;
import top.aprdec.onepractice.service.RecordService;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@Slf4j
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/save")
    public AResult saveRecord(@RequestBody RecordReqDTO dto){
        log.info(dto.toString());
        String recordId = recordService.addRecord(dto);
        return AResult.success(recordId);
    }

    @GetMapping("/list")
    public AResult getRecordList(@RequestParam int days){
        List<UserExamRecordDO> recentRecords = recordService.getRecentRecords(days,1,10);
        return AResult.success(recentRecords);
    }

    @PostMapping("update")
    public AResult updateRecord(@RequestBody RecordReqDTO dto){
        recordService.updateRecord(dto);
        return AResult.success();
    }

}
