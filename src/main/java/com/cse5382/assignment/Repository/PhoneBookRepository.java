package com.cse5382.assignment.Repository;

import com.cse5382.assignment.Model.PhoneBookEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@Repository
public class PhoneBookRepository {


    @PersistenceContext//entityManager will be used to persist changes to mySQL database
    EntityManager entityManager;

    List<PhoneBookEntry> list = new ArrayList<>();
    public List<PhoneBookEntry> list(){
        return  list;
    }
    @Transactional//used to ensure changes are committed
    public void save(String name, String phoneNumber){
        PhoneBookEntry phoneBookEntry = new PhoneBookEntry();
        phoneBookEntry.setPhoneNumber(phoneNumber);
        phoneBookEntry.setName(name);
        list.add(phoneBookEntry);
        entityManager.persist(phoneBookEntry);
    }


    public void deleteByName(String name){
        int index = IntStream.range(0, list.size())
                .filter(i -> list.get(i).getName().equals(name))
                .findFirst()
                .orElse(-1);
        if(index!=-1){
            list.remove(index);
        }
    }


    public void deleteByNumber(String phoneNumber){
        int index = IntStream.range(0, list.size())
                .filter(i -> list.get(i).getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElse(-1);
        if(index!=-1){
            list.remove(index);
        }
    }

}
