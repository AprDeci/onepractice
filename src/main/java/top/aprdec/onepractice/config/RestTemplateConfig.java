package top.aprdec.onepractice.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);             // 最大连接数为 200
        connectionManager.setDefaultMaxPerRoute(20);    // 每个路由的最大连接数为 20

        CloseableHttpClient httpClient = HttpClients.custom()   // 建 HTTP 客户端
                .setConnectionManager(connectionManager)
                .build();

        // 创建请求工
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // 返回 RestTemplate
        return new RestTemplate(factory);

    }
}
