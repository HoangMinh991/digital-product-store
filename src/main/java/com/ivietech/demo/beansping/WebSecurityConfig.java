package com.ivietech.demo.beansping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.rememberMe().key("uniqueAndSecret").tokenValiditySeconds(2419200);
        http
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**",
                         "/", "/detail/**", "/img/**","/img_b/**", "/uploads/**","/viewCartDetail/**","/viewproduct/**","/cart/**").permitAll()
                .antMatchers("/login/**",
                        "/resetPassword/**","/passwordResetConfirm/**",
                        "/registrationConfirm/**","/register/**","/rate/**" ).permitAll()
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/updatePassword/**",
                        "/user/changePassword/**")
                .hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
                .permitAll()
                .and()
                .logout().logoutUrl("/j_spring_security_logout")
                .logoutSuccessUrl("/login?message=logout");

    }

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

}
