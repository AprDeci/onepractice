package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

}


