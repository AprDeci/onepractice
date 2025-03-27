package top.aprdec.onepractice.service.impl;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.aprdec.onepractice.entity.PaperDO;
import top.aprdec.onepractice.service.PaperService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaperServiceimpl implements PaperService {
    private final EasyEntityQuery easyEntityQuery;

    @Override
    public List getAllPapers() {
        return easyEntityQuery.queryable(PaperDO.class).toList();
    }
}
