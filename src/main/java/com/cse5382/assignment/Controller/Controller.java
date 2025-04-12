package com.cse5382.assignment.Controller;

import com.cse5382.assignment.Model.PhoneBookEntry;
import com.cse5382.assignment.Service.PhoneBookService;
import com.cse5382.assignment.Service.PhoneBookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {
    @Autowired//causes Springboot to create instance of PhoneBookServiceImpl since it implements the interface PhoneBookService

    PhoneBookService phoneBookService;

    @GetMapping(path = "phoneBook/list")
    public List<PhoneBookEntry> list(){
        for(PhoneBookEntry e : phoneBookService.list())
        {
            System.out.println("Name:"+e.getName()+"\nPhone: "+e.getPhoneNumber()+"\n");
        }
        return phoneBookService.list();
    }

    @PostMapping(path = "phoneBook/add")
    public ResponseEntity<?> add(@RequestBody PhoneBookEntry phoneBookEntry){
        try {
            phoneBookService.add(phoneBookEntry);
        }catch(Exception e){
            if(e.getMessage().equals("Invalid Input"))
            {
                return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Added:\nName:"+phoneBookEntry.getName()+"\nPhone: "+phoneBookEntry.getPhoneNumber()+"\n",HttpStatus.OK);
    }

    @PutMapping(path = "phoneBook/deleteByName")
    public ResponseEntity<?> deleteByName(@RequestParam String name){
        try {
            phoneBookService.deleteByName(name);
        }catch(Exception e){
            if(e.getMessage().equals("Invalid Input"))
            {
                return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
            }
            else if(e.getMessage().equals("Content Not Found"))
            {
                return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(path = "phoneBook/deleteByNumber")
    public ResponseEntity<?> deleteByNumber(@RequestParam String number){
        try {
            phoneBookService.deleteByNumber(number);
        }catch(Exception e){
            if(e.getMessage().equals("Invalid Input"))
            {
                return new ResponseEntity<Error>(HttpStatus.BAD_REQUEST);
            }
            else if(e.getMessage().equals("Content Not Found"))
            {
                return new ResponseEntity<Error>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Error>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
