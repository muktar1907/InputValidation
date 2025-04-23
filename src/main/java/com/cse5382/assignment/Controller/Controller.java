package com.cse5382.assignment.Controller;

import com.cse5382.assignment.Model.PhoneBookEntry;
import com.cse5382.assignment.Model.Users;
import com.cse5382.assignment.Service.PhoneBookService;
import com.cse5382.assignment.Service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);
    @Autowired//causes Springboot to create instance of PhoneBookServiceImpl since it implements the interface PhoneBookService
    PhoneBookService phoneBookService;
    @Autowired
    UserService userService;
    @GetMapping(path = "/PhoneBook/list")
    public List<PhoneBookEntry> list(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for(PhoneBookEntry e : phoneBookService.list())
        {
            System.out.println("Name:"+e.getName()+"\nPhone: "+e.getPhoneNumber()+"\n");
        }
        //gets the authentication info of currently logged in user, will not apply to failed login attempts
        LOGGER.info("User '{}' has accessed the contents of the phonebook", authentication.getName());
        return phoneBookService.list();
    }

    @PostMapping(path = "/PhoneBook/add")
    public ResponseEntity<?> add(@RequestBody PhoneBookEntry phoneBookEntry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            phoneBookService.add(phoneBookEntry);
        }catch(Exception e){
            if(e.getMessage().equals("Invalid Input"))
            {
                
                LOGGER.warn("User '{}' attempted to add an entry with invalid inputs",authentication.getName());
                return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
            }
            else if(e.getMessage().equals("Existing Phone Number"))
            {
                LOGGER.info("User '{}' attempted to add a phone number that already exists", authentication.getName());
                return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
            }
            
            LOGGER.warn("User :'{}' \n\tUnexpected error occured when adding phone book entry",authentication.getName());
            return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("User '{}' has added a new entry to the phone book:{Name:'{}',Number:'{}'}",authentication.getName(),phoneBookEntry.getName(),phoneBookEntry.getPhoneNumber());
        
        return new ResponseEntity<>("Added:\nName:"+phoneBookEntry.getName()+"\nPhone: "+phoneBookEntry.getPhoneNumber()+"\n",HttpStatus.OK);
        
    }

    @PutMapping(path = "/PhoneBook/deleteByName")
    public ResponseEntity<?> deleteByName(@RequestParam String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            phoneBookService.deleteByName(name);
        }catch(Exception e){
            if(e.getMessage().equals("Invalid Input"))
            {
                LOGGER.warn("User '{}' attempted to delete an entry by name but the input was invalid", authentication.getName());
                return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
            }
            else if(e.getMessage().equals("Content Not Found"))
            {
                LOGGER.info("User '{}' attempted to delete a non-existent entry", authentication.getName());
                return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
            }
            LOGGER.warn("User :'{}' \n\tUnexpected error occured when adding phone book entry",authentication.getName());
            return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("User '{}' has deleted an entry from the phone book by name: Name:'{}'",authentication.getName(),name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/PhoneBook/deleteByNumber")
    public ResponseEntity<?> deleteByNumber(@RequestParam String number){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name;
        try {
            name=phoneBookService.deleteByNumber(number);
        }catch(Exception e){
            if(e.getMessage().equals("Invalid Input"))
            {
                LOGGER.warn("User '{}' attempted to delete an entry by number but the input was invalid", authentication.getName());
                return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
            }
            else if(e.getMessage().equals("Content Not Found"))
            {
                LOGGER.info("User '{}' attempted to delete a non-existent entry", authentication.getName());
                return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
            }
            System.out.println(e);
            LOGGER.warn("User :'{}' \n\tUnexpected error occured when deleting phone book entry",authentication.getName());
            return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("User '{}' has deleted an entry from the phone book by number:[Name: '{}', Number:'{}']",authentication.getName(),name,number);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping(path="/PhoneBook/createUser")
    public ResponseEntity<?> createUser(@RequestBody Users user)
    {
        try
        {
            if(user==null)
            {
                System.out.println("null user");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            userService.createUser(user.getUsername(), user.getPassword(), "ROLE_USER");
            
        }
        catch(Exception e)
        {
            if(e.getMessage().equals("Invalid Input")||e.getMessage().equals("User Exists Already"))
            {
                return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
            }
            
            else
            {
                System.out.println(e);
                LOGGER.warn("Unexpected error occured when attempting to create new user");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                
            }
        }
        LOGGER.info("A new User was created with username: '{}'",user.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping(path="/PhoneBook/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String password)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try
        {
            userService.deleteUser(authentication.getName(), password, "ROLE_USER");
        }
        catch(Exception e)
        {
            if(e.getMessage().equals("Invalid Input")||e.getMessage().equals("Incorrect Password"))
            {
                LOGGER.warn("Attempted deletion of account belonging to '{}' failed due to incorrect password",authentication.getName());
                return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
            }
            else
            {
                LOGGER.warn("Unexpected error occured when attempting to create new user");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                
            }
        }
        LOGGER.info("User '{}' deleted their account",authentication.getName());
        return new ResponseEntity<>(HttpStatus.OK);
        
    }

    @PutMapping(path="/PhoneBook/reset")
    public ResponseEntity<?> resetDB() throws Exception
    {
        System.out.println("Resetting Database");
        phoneBookService.reset();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
