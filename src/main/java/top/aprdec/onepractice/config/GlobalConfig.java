package top.aprdec.onepractice.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.aprdec.onepractice.interceptor.AutoRefreshTokenInterceptor;

@Configuration
public class GlobalConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new AutoRefreshTokenInterceptor()).excludePathPatterns("/**");
    }
}
