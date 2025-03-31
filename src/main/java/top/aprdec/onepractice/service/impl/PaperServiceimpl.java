package top.aprdec.onepractice.service.impl;

import com.alibaba.druid.sql.PagerUtils;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.aprdec.onepractice.dto.resp.PaperIntroRespDTO;
import top.aprdec.onepractice.dto.resp.proxy.PaperIntroRespDTOProxy;
import top.aprdec.onepractice.entity.PaperDO;
import top.aprdec.onepractice.entity.QuestionsDO;
import top.aprdec.onepractice.entity.proxy.PaperDOProxy;
import top.aprdec.onepractice.service.PaperService;
import top.aprdec.onepractice.util.PaperUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaperServiceimpl implements PaperService {
    private final EasyEntityQuery easyEntityQuery;
    private final PaperUtil paperUtil;

    @Override
    public List<PaperDO> getAllPapers() {
        return easyEntityQuery.queryable(PaperDO.class).toList();
    }


    @Override
    public PaperDO getPaperById(Integer id) {
        PaperDO paperDO = easyEntityQuery.queryable(PaperDO.class)
                .where(p -> p.paperId().eq(id))
                .firstOrNull();
        return Optional.ofNullable(paperDO)
                .orElseThrow(()->new RuntimeException("试卷不存在"));
    }


    @Override
    public List<PaperDO> getQuestionsByPaperId(Integer id) {
        return easyEntityQuery.queryable(PaperDO.class)
                .where(q -> q.paperId().eq(id))
                .toList();
    }

    @Override
    public List<PaperDO> getPapersBytype(String type) {
        return easyEntityQuery.queryable(PaperDO.class)
                .where(q -> q.type().eq(type))
                .toList();
    }

    @Override
    public List<String> getAllTypes() {
        List<String> types = easyEntityQuery.queryable(PaperDO.class)
                .select(p -> p.type()).distinct().toList();
        return types;
    }

    @Override
    public PaperIntroRespDTO getPaperIntro(Integer id) {
        PaperDO paperDO = easyEntityQuery.queryable(PaperDO.class)
                .where(p -> p.paperId().eq(id)).firstOrNull();

        String type = paperDO.getType();
        PaperIntroRespDTO paperIntroRespDTO = new PaperIntroRespDTO(paperDO.getPaperName(),paperDO.getExamYear(), paperDO.getExamMonth(), type,paperDO.getTotalTime(), null,PaperUtil.getPartCount(type),PaperUtil.getPartQuestionCount(type));
        return paperIntroRespDTO;


    }


}
