package com.ringme.cms.config;


import com.ringme.cms.service.sys.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestTemplate;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {
    @Value("${server.servlet.context-path}")
    private static String CONTEXTPATH;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {

        http = http.authorizeRequests()
                .antMatchers("/login/**",
                         "/captcha.jpg",
                        "/generate-security",
                        "/img/**",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/vendor/**",
                        "/actuator/**",
                        "/alert-start",
                        "/ws/**",
                        "/start-live/**").permitAll()
                .antMatchers("/**").authenticated().and();
        http = http.addFilterBefore(captchaFilter(), UsernamePasswordAuthenticationFilter.class);
        http = http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .invalidSessionUrl("/login?time=expired")
                .maximumSessions(3)
                .expiredUrl("/login?time=expired").and().and();

        http.formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .failureUrl("/login-error")
                        .defaultSuccessUrl("/index")
                        .usernameParameter("username")
                        .passwordParameter("password"))
                .authenticationProvider(authenticationProvider())
                .userDetailsService(userDetailsService)
                .logout((logout) -> logout
                        .permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID"));
        http = http.rememberMe().key("ringmecms").rememberMeParameter("remember-me").tokenValiditySeconds(86400).and();

        http = http.csrf().disable().headers().disable();
        http = http.exceptionHandling().accessDeniedPage("/403").and();

        return http.build();
    }
//    @Bean
    //    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/images/**",
//                "/styles/**",
//                "/static/**",
//                "/captcha.jpg",
//                "/resources/**",
//                "/css/**",
//                "/static/css/**",
//                "/img/**",
//                "/js/**",
//                "/vendor/**",
//                "/vendor/fontawesome-free/**");
//    }
    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken(); // cấu hình sử dụng header để truyền session id
    }
    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public FilterRegistrationBean<CustomFilter> customFilterRegistration() {
        FilterRegistrationBean<CustomFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(customFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }
    @Bean
    public CustomFilter customFilter() {
        return new CustomFilter();
    }

    @Bean
    public CaptchaFilter captchaFilter() {
        return new CaptchaFilter();
    }


}
