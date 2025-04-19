package com.cse5382.assignment.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cse5382.assignment.Model.Users;
import com.cse5382.assignment.Repository.UserRepository;

//used by spring security to load user data for login authentication and authorization
@Service
public class MyUserDetailsService implements UserDetailsService 
{
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        //gets admin object from database
        Users user = userRepository.getUserByUsername(username);//get an admin by their username and set admin(variable) to it 
        
        if(user==null)// admin doesn't exist
        {
            //throw exception if admin isn't found
            throw new UsernameNotFoundException("Could not find user");
        }

        return new myUserDetails(user);
    }

}
