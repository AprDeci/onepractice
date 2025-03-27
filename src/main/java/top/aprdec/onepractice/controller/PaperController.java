package top.aprdec.onepractice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.aprdec.onepractice.commmon.AResult;

@Slf4j
@RestController
@RequestMapping("/api/paper")
@RequiredArgsConstructor
public class PaperController {

    public AResult getallpaper() {
        // TODO 获取所有试卷
        return null;
    }
}
