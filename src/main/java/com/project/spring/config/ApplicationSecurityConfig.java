package com.project.spring.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                )
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/welcome").permitAll();
                            auth.requestMatchers("/api/**").permitAll();
                            auth.requestMatchers("/user").hasAnyAuthority("user");
                            auth.requestMatchers("/admin").hasAnyAuthority("admin");
                            auth.requestMatchers("/products/**").permitAll();
                            auth.requestMatchers("/products/filter").permitAll();
                            auth.requestMatchers("/logout").permitAll();
                            auth.requestMatchers("/css/**").permitAll();
                            auth.requestMatchers("/assets/**").permitAll();
                            auth.requestMatchers("/cart/**").permitAll();
                            auth.requestMatchers("/").permitAll();
                            auth.requestMatchers("/products/detail").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        /*.failureHandler(authenticationFailureHandler())*/
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .rememberMe(remmember -> remmember
                        .key("remember-me")
                        .tokenValiditySeconds(86400)
                )
                .exceptionHandling(handling -> handling.accessDeniedPage("/403"))
                .httpBasic(withDefaults())
        ;
        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler("/products");
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationFailureHandler simpleUrlAuthenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
        simpleUrlAuthenticationFailureHandler.setDefaultFailureUrl("/login?error=true");
        return simpleUrlAuthenticationFailureHandler;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
