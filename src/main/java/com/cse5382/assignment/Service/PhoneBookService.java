package com.cse5382.assignment.Service;

import com.cse5382.assignment.Model.PhoneBookEntry;

import java.util.List;

public interface PhoneBookService {
    public List<PhoneBookEntry> list();
    //each function has to be able to throw exception so that the controller class can differentiate cause of failure
    public void add(PhoneBookEntry phoneBookEntry) throws Exception;

    public void deleteByName(String name) throws Exception;

    public String deleteByNumber(String phoneNumber) throws Exception;
    

}
