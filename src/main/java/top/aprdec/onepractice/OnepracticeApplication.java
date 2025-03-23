package top.aprdec.onepractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties
public class OnepracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnepracticeApplication.class, args);
    }

}
