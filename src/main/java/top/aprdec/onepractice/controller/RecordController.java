package top.aprdec.onepractice.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.aprdec.onepractice.Iinterface.Idempotent;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.RecordReqDTO;
import top.aprdec.onepractice.entity.UserExamRecordDO;
import top.aprdec.onepractice.service.RecordService;

import java.util.List;

@RestController
@RequestMapping("/api/record")
@Slf4j
@RequiredArgsConstructor
@Validated
public class RecordController {

    private final RecordService recordService;

    @PostMapping("/save")
    @Idempotent(timeout = 2)
    public AResult<String> saveRecord(@RequestBody RecordReqDTO dto) {
        log.info(dto.toString());
        String recordId = recordService.addRecord(dto);
        return AResult.success(recordId);
    }

    @GetMapping("/list")
    public AResult<List<UserExamRecordDO>> getRecordList(
            @RequestParam @Min(value = 1, message = "days最小为1") int days,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "pageNum最小为1") int pageNum,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "pageSize最小为1") int pageSize
    ) {
        List<UserExamRecordDO> recentRecords = recordService.getRecentRecords(days, pageNum, pageSize);
        return AResult.success(recentRecords);
    }

    @PostMapping("update")
    @Idempotent(timeout = 2)
    public AResult<Void> updateRecord(@RequestBody RecordReqDTO dto) {
        recordService.updateRecord(dto);
        return AResult.success();
    }



}
