package top.aprdec.onepractice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupLogger implements CommandLineRunner {
    @Value("${server.port:8080}")
    private String port;

    @Override
    public void run(String... args) throws Exception {
        logInfo();
    }

    //    打印信息
    private  void logInfo(){
        String line = "=".repeat(40);
        System.out.println("\n" + line);
        System.out.println("OnepracticeApplication is starting...");
        System.out.printf("Docs: http://localhost:%s/scalar\n", port);
        System.out.println(line + "\n");
    }
}
