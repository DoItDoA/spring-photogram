package com.cos.insta.config;

import com.cos.insta.config.oauth.OAuth2DetailsService;
import com.cos.insta.util.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@EnableWebSecurity // 해당 파일로 시큐리티를 활성화
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    OAuth2DetailsService oAuth2DetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/notification/**", "/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/signin") // 인증되지 않은 주소는 전부다 여기로 이동, GET
                .loginProcessingUrl("/auth/signin") // POST 스프링 시큐리티가 로그인 프로세스 진행(UserDetailsService실행)
                .defaultSuccessUrl("/") // 로그인 성공시 여기로 이동
                .failureHandler((request, response, exception) -> {
                    response.setContentType("text/html; charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.print(Script.back("유저네임 혹은 비밀번호를 찾을 수 없습니다."));
                })
                .and()
                .oauth2Login()// form 로그인도 하고, oauth2 로그인도 한다
                .userInfoEndpoint()// oauth2 로그인을 하면 최종응답으로 회원정보를 바로 받을 수 있다.
                .userService(oAuth2DetailsService);
    }
}
