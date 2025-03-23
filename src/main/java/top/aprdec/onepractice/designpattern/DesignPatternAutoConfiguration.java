package top.aprdec.onepractice.designpattern;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.aprdec.onepractice.designpattern.chain.AbstractChainContext;

@ImportAutoConfiguration()
@Configuration
public class DesignPatternAutoConfiguration {
    @Bean
    public AbstractChainContext abstractChainContext() {
        return new AbstractChainContext();
    }
}
