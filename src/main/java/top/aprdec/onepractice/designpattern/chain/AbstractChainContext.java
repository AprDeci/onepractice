package top.aprdec.onepractice.designpattern.chain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import top.aprdec.onepractice.util.ApplicationContextHolder;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AbstractChainContext<T> implements CommandLineRunner {

    private final Map<String, List<AbstractChainHandler>> abstractChainHandlerContainer = new HashMap<>();


    public void handler(String mark,T requestParam){
        List<AbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(mark);
        if(CollectionUtils.isEmpty(abstractChainHandlers)){
            throw new RuntimeException(String.format("[%s] 责任链ID未定义",mark));
        }
        abstractChainHandlers.forEach(each->each.handler(requestParam));
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String,AbstractChainHandler> chainFilterMap = ApplicationContextHolder
                .getBeansOfType(AbstractChainHandler.class);
        chainFilterMap.forEach((beanName,bean)->{
            List<AbstractChainHandler> abstractChainHandlers = abstractChainHandlerContainer.get(bean.mark());
            if (CollectionUtils.isEmpty(abstractChainHandlers)){
                abstractChainHandlers = new ArrayList();
            }
            abstractChainHandlers.add(bean);
            List<AbstractChainHandler> actualAbstractChainHandlers = abstractChainHandlers.stream()
                    .sorted(Comparator.comparing((Ordered::getOrder)))
                    .collect(Collectors.toList());
            abstractChainHandlerContainer.put(bean.mark(),actualAbstractChainHandlers);

        });
    }
}
