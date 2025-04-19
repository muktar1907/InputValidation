package com.cse5382.assignment.Security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException 
    {
        //getting username from http authorization header
        String username="";
        //if the header has content and starts with "Basic ", this api uses http basic not forms
        String header=request.getHeader("Authorization");
        if(header!=null&& header.startsWith("Basic "))
        {
            try 
            {
                //decoding credentials to get username used in login attempt
                //substring starts right after header prefix
                String encodedCredentials=header.substring(("Basic ").length());
                //decode
                byte[] decodedBytes=Base64.getDecoder().decode(encodedCredentials);
                //create new string
                String credentials= new String(decodedBytes,StandardCharsets.UTF_8);//utf-8 is global standard
                String[] userAndPass=credentials.split(":");
                username=userAndPass[0];
                LOGGER.warn("Failed attempt to access {} with username '{}'",request.getRequestURI(),username);
            } catch (Exception e) 
            {
                
                LOGGER.warn("Incorrect header formatting in attempt to access {}",request.getRequestURI());
            }
            
        }
        else
        {
            LOGGER.warn("Failed attempt to access {}",request.getRequestURI());
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Failed to authenticate user\"}");//write error message to response body, client visible
        
    }

}
