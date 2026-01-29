package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.CommonPageResultReqDTO;
import top.aprdec.onepractice.dto.req.UserSaveWordReqDTO;
import top.aprdec.onepractice.service.UserSavedWordService;

@RestController
@RequestMapping("/api/word")
@RequiredArgsConstructor
public class UserSavedWordController {
    private final UserSavedWordService userSavedWordService;

    @PostMapping("/add")
    public AResult collectWord(@RequestBody UserSaveWordReqDTO dto){
        userSavedWordService.addSavedWord(dto);
        return AResult.success();
    }

    @PostMapping("/hascollected")
    public AResult hasCollected(@RequestBody UserSaveWordReqDTO dto){
        return AResult.success(userSavedWordService.hascollected(dto));
    }

    @PostMapping("/getcollectedwords")
    public AResult getCollectedWords(@RequestBody CommonPageResultReqDTO dto){
        return AResult.success(userSavedWordService.getUserCollectedWords(dto));
    }

    @DeleteMapping("/delete")
    public AResult deleteWord(@RequestBody UserSaveWordReqDTO dto){
        userSavedWordService.removeSavedWord(dto);
        return AResult.success();
    }

}


