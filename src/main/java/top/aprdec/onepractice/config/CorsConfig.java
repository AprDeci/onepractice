package top.aprdec.onepractice.config;

import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsFilter corsFilter = new CorsFilter(request -> {

            String origin = request.getHeader(HttpHeaders.ORIGIN);

            if (!StringUtils.hasText(origin)) {
                // 非跨域请求
                return null;
            }

            CorsConfiguration config = new CorsConfiguration();
            // 允许所有域
            config.addAllowedOrigin(origin);

            // 允许所有请求 Header
            String requestHeders = request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
            if (StringUtils.hasText(requestHeders)) {
                config.setAllowedHeaders(Stream.of(requestHeders.split(",")).map(String::trim).distinct().toList());
            }

            // 默认允许 Javascript 访问的响应头
            config.setExposedHeaders(Arrays.asList("Cache-Control", "Content-Language", "Content-Length",
                    "Content-Type", "Expires", "Last-Modified", "Pragma"));

            // 允许携带凭证
            config.setAllowCredentials(true);

            // 允许所有请求方法
            config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT",
                    "PATCH", "DELETE", "OPTIONS", "TRACE"));

            // 预检缓存 30 分钟
            config.setMaxAge(Duration.ofMinutes(30));

            return config;
        });

        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(corsFilter);
        registrationBean.addUrlPatterns("/*"); // 拦截所有请求
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 最先执行
        return registrationBean;
    }
}
