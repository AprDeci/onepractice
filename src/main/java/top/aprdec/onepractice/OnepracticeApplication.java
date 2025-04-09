package top.aprdec.onepractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableConfigurationProperties
@EnableCaching
@EnableAsync
@EnableScheduling
public class OnepracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnepracticeApplication.class, args);
    }

}
