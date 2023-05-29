package com.ticket.reservation.config;

import com.ticket.reservation.handler.*;
import com.ticket.reservation.service.impl.MyUserDetailServiceImpl;
import com.ticket.reservation.service.impl.UserServiceImpl;
import org.apache.coyote.http11.HttpOutputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启权限注解,默认是关闭的
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthenticationEnryPoint authenticationEnryPoint;    //未登录
    @Autowired
    AuthenticationSuccess authenticationSuccess;    //登录成功
    @Autowired
    AuthenticationFailure authenticationFailure;    //登录失败
    @Autowired
    AuthenticationLogout authenticationLogout;      //注销
    @Autowired
    AccessDeny accessDeny;      //无权访问
    @Autowired
    SessionInformationExpiredStrategy sessionInformationExpiredStrategy;    //检测异地登录
    @Autowired
    SelfAuthenticationProvider selfAuthenticationProvider;      //自定义认证逻辑处理

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailServiceImpl();
    }

    //加密方式
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(selfAuthenticationProvider);
    }

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //cors()解决跨域问题，csrf()会与restful风格冲突，默认springsecurity是开启的，所以要disable()关闭一下
        http.cors().and().csrf().disable();

        //     /index需要权限为ROLE_USER才能访问   /hello需要权限为ROLE_ADMIN才能访问
        http.authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/csrf").permitAll()
                .antMatchers("/api/register").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()  //开启登录
                .loginProcessingUrl("/api/login")
                .permitAll()  //允许所有人访问
                .successHandler(authenticationSuccess) // 登录成功逻辑处理
                .failureHandler(authenticationFailure) // 登录失败逻辑处理

                .and()
                .logout()   //开启注销
                .logoutUrl("/api/user/logout")
                .permitAll()    //允许所有人访问
                .logoutSuccessHandler(authenticationLogout) //注销逻辑处理
                .deleteCookies("JSESSIONID")    //删除cookie

                .and().exceptionHandling()
                .accessDeniedHandler(accessDeny)    //权限不足的时候的逻辑处理
                .authenticationEntryPoint(authenticationEnryPoint)  //未登录是的逻辑处理

                .and()
                .sessionManagement()
                .maximumSessions(1)     //最多只能一个用户登录一个账号
                .expiredSessionStrategy(sessionInformationExpiredStrategy)  //异地登录的逻辑处理
        ;
    }
}
