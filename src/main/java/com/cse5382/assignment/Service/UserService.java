package com.cse5382.assignment.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cse5382.assignment.Model.Users;
import com.cse5382.assignment.Repository.UserRepository;


@Service
@Transactional
public class UserService 
{
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    public void createUser(String username, String password,String role) throws Exception
    {
        if(validateCredentials(username,password))
        {
            //if credentials have valid form check if user already exists
            if(!userRepository.existsByUsername(username))
            {
                Users user=new Users(role);
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
            }
            else
            {
                throw new Exception("User Exists Already");
            }
        }
        else
        {
            throw new Exception("Invalid Input");
        }
    }
    
    public void deleteUser(String username,String password,String role)throws Exception
    {
        if(validateCredentials(username,password))
        {
            //user exists since access to endpoint is protected 
            Users user= userRepository.getUserByUsername(username);
            if(passwordEncoder.matches(password, user.getPassword()))
            {
                
                userRepository.deleteByUsername(username);
            }
            else
            {
                throw new Exception("Incorrect Password");
            }
        }
        else
        {
            throw new Exception("Invalid Input");
        }
    }
    public boolean validateCredentials(String username, String password)
    {
        Pattern patternName = Pattern.compile("[a-z0-9A-Z]{8,15}");
        
        Matcher matcherName = patternName.matcher("username");
        boolean matchFoundName = matcherName.matches();
        if(matchFoundName)
        {
            Pattern patternPass = Pattern.compile("[a-z0-9A-Z@!$-_]{8,20}");
        
            Matcher matcherPass = patternPass.matcher(password);
            boolean matchFoundPass = matcherPass.matches();
            if(matchFoundPass)
            {
                return true;
            }
            else
            {
                return false;
            }
            
        }
        else
        {
            return false;
        }
    }
}
