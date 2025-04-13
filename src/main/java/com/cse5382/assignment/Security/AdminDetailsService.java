package com.cse5382.assignment.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cse5382.assignment.Model.Admin;
import com.cse5382.assignment.Repository.AdminRepository;

//used by spring security to load user data for login authentication and authorization
@Service
public class AdminDetailsService implements UserDetailsService 
{
    @Autowired
    AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        //gets admin object from database
        Admin admin = adminRepository.getUserByUsername(username);//get an admin by their username and set admin(variable) to it 
        
        if(admin==null)// admin doesn't exist
        {
            //throw exception if admin isn't found
            throw new UsernameNotFoundException("Could not find user");
        }

        return new myAdminDetails(admin);
    }

}
