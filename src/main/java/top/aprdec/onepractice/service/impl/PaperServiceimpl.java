package top.aprdec.onepractice.service.impl;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.api.pagination.EasyPageResult;
import com.easy.query.core.util.EasyStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.aprdec.onepractice.dto.req.PaperqueryDTO;
import top.aprdec.onepractice.dto.resp.PaperIntroRespDTO;
import top.aprdec.onepractice.dto.resp.PaperdataRespDTO;
import top.aprdec.onepractice.entity.PaperDO;
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
    public PaperdataRespDTO getPapersByPageAndSize(Integer page, Integer size) {
        EasyPageResult<PaperDO> pageResult = easyEntityQuery.queryable(PaperDO.class).toPageResult(page, size);
        PaperdataRespDTO paperdataRespDTO = new PaperdataRespDTO();
        paperdataRespDTO.setPapers(pageResult.getData());
        paperdataRespDTO.setTotal(pageResult.getTotal());
        return paperdataRespDTO;
    }

    @Override
    public EasyPageResult<PaperDO> getPaperswithQuerysByPageAndSize(PaperqueryDTO querys) {
        EasyPageResult<PaperDO> result = easyEntityQuery.queryable(PaperDO.class)
                .where(p -> {
                    p.type().eq(!EasyStringUtil.isEmpty(querys.getType()),querys.getType());
                    p.examYear().eq(querys.getYear()!=null&&querys.getYear()!=0,querys.getYear());
                }).toPageResult(querys.getPage(), querys.getSize());
        return result;
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
