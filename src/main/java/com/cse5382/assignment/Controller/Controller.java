package com.cse5382.assignment.Controller;

import com.cse5382.assignment.Model.PhoneBookEntry;
import com.cse5382.assignment.Service.PhoneBookService;
import com.cse5382.assignment.Service.PhoneBookServiceImpl;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(PhoneBookServiceImpl.class);
    @Autowired//causes Springboot to create instance of PhoneBookServiceImpl since it implements the interface PhoneBookService
    PhoneBookService phoneBookService;

    @GetMapping(path = "/PhoneBook/list")
    public List<PhoneBookEntry> list(){
        for(PhoneBookEntry e : phoneBookService.list())
        {
            System.out.println("Name:"+e.getName()+"\nPhone: "+e.getPhoneNumber()+"\n");
        }
        //gets the authentication info of currently logged in user, will not apply to failed login attempts
        
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
                
                LOGGER.info("User '{}' attempted to add an entry with invalid inputs",authentication.getName());
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
                LOGGER.info("User '{}' attempted to delete an entry by name but the input was invalid", authentication.getName());
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
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "/PhoneBook/deleteByNumber")
    public ResponseEntity<?> deleteByNumber(@RequestParam String number){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            phoneBookService.deleteByNumber(number);
        }catch(Exception e){
            if(e.getMessage().equals("Invalid Input"))
            {
                LOGGER.info("User '{}' attempted to delete an entry by number but the input was invalid", authentication.getName());
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
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
