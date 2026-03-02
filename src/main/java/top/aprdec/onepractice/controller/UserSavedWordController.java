package top.aprdec.onepractice.controller;

import com.easy.query.core.api.pagination.EasyPageResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.aprdec.onepractice.commmon.AResult;
import top.aprdec.onepractice.dto.req.CommonPageResultReqDTO;
import top.aprdec.onepractice.dto.req.UserSaveWordReqDTO;
import top.aprdec.onepractice.entity.WordsDO;
import top.aprdec.onepractice.service.UserSavedWordService;

@RestController
@RequestMapping("/api/word")
@RequiredArgsConstructor
public class UserSavedWordController {
    private final UserSavedWordService userSavedWordService;

    @PostMapping("/add")
    public AResult<Void> collectWord(@RequestBody UserSaveWordReqDTO dto) {
        userSavedWordService.addSavedWord(dto);
        return AResult.success();
    }

    @PostMapping("/hascollected")
    public AResult<Boolean> hasCollected(@RequestBody UserSaveWordReqDTO dto) {
        return AResult.success(userSavedWordService.hascollected(dto));
    }

    @PostMapping("/getcollectedwords")
    public AResult<EasyPageResult<WordsDO>> getCollectedWords(@RequestBody @Valid CommonPageResultReqDTO dto) {
        return AResult.success(userSavedWordService.getUserCollectedWords(dto));
    }

    @DeleteMapping("/delete")
    public AResult<Void> deleteWord(@RequestBody UserSaveWordReqDTO dto) {
        userSavedWordService.removeSavedWord(dto);
        return AResult.success();
    }

}

