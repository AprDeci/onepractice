package top.aprdec.onepractice.designpattern.chain;

import org.springframework.core.Ordered;

public interface AbstractChainHandler<T> extends Ordered {



    void handler(T requestParam);

    String mark();
}
