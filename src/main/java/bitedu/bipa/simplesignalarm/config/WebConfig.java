package bitedu.bipa.simplesignalarm.config;

import bitedu.bipa.simplesignalarm.interceptor.AuthorityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    AuthorityInterceptor authorityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorityInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 엔드포인트에 대한 CORS 설정을 적용
                .allowedOrigins("http://localhost:3000","http://localhost:8080", "https://ec2-43-202-224-51.ap-northeast-2.compute.amazonaws.com", "http://ec2-43-202-224-51.ap-northeast-2.compute.amazonaws.com", "https://simple-sign-frontend-orpin.vercel.app")
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowCredentials(true) // 자격 증명 허용 여부
                .maxAge(3600); // 사전 검사 (Preflight) 요청 캐시 시간 (초)
    }
}
