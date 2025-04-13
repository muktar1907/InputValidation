package com.cse5382.assignment.Security;
import com.cse5382.assignment.Model.Admin;
import com.cse5382.assignment.Repository.AdminRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
//will execute on startup
public class AdminInitializer implements CommandLineRunner
{
    @Autowired
    //instance of admin repo needed to check if initial admin exists and if not to add it
    private AdminRepository adminRepository;

    @Autowired
    //instance of a passwordEncoder needed to encode the initial admin's password
    private PasswordEncoder passwordEncoder;
    //default values taken from environment variables
    @Value("${Initial_Admin_Username}")
    private String defaultUsername;
    @Value("${Initial_Admin_Password}")
    private String defaultPassword;
    @Override
    public void run(String... args)
    {
        
        if(adminRepository.getUserByUsername(defaultUsername)==null)//if the default admin doesn't exist
        {
            System.out.println("Initializing Admin");
            Admin admin= new Admin();
            admin.setUser(defaultUsername);
            //encode password before storing it
            admin.setPass(passwordEncoder.encode(defaultPassword));

            adminRepository.save(admin);
            System.out.println("Default Admin Initialized");
        }
        else
        {
            System.out.println("Default user already exists in db");
        }
    }


}
