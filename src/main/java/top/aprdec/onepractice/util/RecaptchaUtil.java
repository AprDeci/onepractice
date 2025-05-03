package top.aprdec.onepractice.util;

import cn.hutool.core.date.DateTime;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class RecaptchaUtil {
    @Value("${recaptcha.key}")
    private String secretKey;
    @Value("${recaptcha.url}")
    private String verifyUrl;

    private final RestTemplate restTemplate;

    public RecaptchaUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean verifyRecaptcha(String token) {
        if(token == null || token.isEmpty()) {
            return false;
        }
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secretKey);
        params.add("response", token);
//        ip?暂时不写
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<RecaptchaResponse> response;
        try{
            response = restTemplate.postForEntity(verifyUrl, request, RecaptchaResponse.class);
        }catch (Exception e){
            log.error("reCaptcha验证失败"+e.getMessage());
            return false;
        }
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            return false;
        }
        RecaptchaResponse recaptchaResponse = response.getBody();
        log.info("reCaptcha验证结果:"+recaptchaResponse);
        return recaptchaResponse.isSuccess() && recaptchaResponse.getScore() > 0.5;
    }

    /**
     * recaptcha响应结构
     */
    @Data
    private static class RecaptchaResponse {
        private boolean success;
        private double score;
        private String action;
        private DateTime challengeTs;
        private String hostname;
        private String[] errorCodes;
    }
}
