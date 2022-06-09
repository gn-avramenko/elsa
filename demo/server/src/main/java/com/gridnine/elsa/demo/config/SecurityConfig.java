package com.gridnine.elsa.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/index", "/login", "/auth/**").
                permitAll()
                .antMatchers("/restws","/restws/**")
                .authenticated()
                .and().httpBasic(withDefaults())
                .formLogin(withDefaults()).csrf().disable();
//        http.authorizeRequests().antMatchers("/", "/index", "/login", "/authenticate")
//                .permitAll()
//                .antMatchers("/restws").authenticated();
//                .and()
//                .formLogin()
//                .loginPage("/login").permitAll()
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .loginProcessingUrl("/authenticate")
//                .failureUrl("/denied").permitAll()
//                .and()
//                .logout()
//                .and()
//                /**
//                 * Applies to User Roles - not to login failures or unauthenticated access attempts.
//                 */
//                .exceptionHandling()
//                .accessDeniedHandler(accessDeniedHandler());

    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
