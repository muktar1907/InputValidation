package com.cse5382.assignment.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;




@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Autowired//creates instance of something automatically
    private AdminDetailsService adminDetailsService;
    @Autowired
    private CustomAuthenticationEntryPoint myAuthenticationEntryPoint;
    @Bean
    //defines how spring will secure different urls
    public SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception
    {
        http.csrf().disable()
        .authorizeHttpRequests(auth->auth
        .antMatchers("/PhoneBook/list").permitAll()//all visitors of this url have access
        //visitors to the next 3 urls must be admin
        .antMatchers("/PhoneBook/add").hasRole("ADMIN")
        .antMatchers("/PhoneBook/deleteByName").hasRole("ADMIN")
        .antMatchers("/PhoneBook/deleteByNumber").hasRole("ADMIN")
        //any other request must be authenticated(for the sake of completeness)
        .anyRequest().authenticated()
        ).httpBasic(httpBasic->httpBasic.authenticationEntryPoint(myAuthenticationEntryPoint));//enable http basic authorization, helps log failed logins

        return http.build();//http.build creates a security filter chain using the specifications above
        //a security filter chain is essentially a checkpoint that all requests go through and the filters above are what it checks for
    }

    @Bean//allows other components to use the method within application context
    public AuthenticationManager authenticationManager(HttpSecurity http)throws Exception
    {
        //the AuthenticationManagerBuilder is the object used to build login/authentication system
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            //tells spring to use my predefined implementation of the UserDetailsService
            //which is what I use to load admins from the database for authentication
            .userDetailsService(adminDetailsService)
            //tells spring to encode passwords with a particular predefined encoder
            //then compare it to what's stored in the db when an admin tries to login
            .passwordEncoder(passwordEncoder())
            .and().build();
    }
 
    
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
