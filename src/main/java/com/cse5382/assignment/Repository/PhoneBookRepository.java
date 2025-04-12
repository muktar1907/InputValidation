package com.cse5382.assignment.Repository;

import com.cse5382.assignment.Model.PhoneBookEntry;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

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
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Component
@Repository
public class PhoneBookRepository {


    @PersistenceContext//entityManager will be used to persist changes to mySQL database
    EntityManager entityManager;

    List<PhoneBookEntry> list = new ArrayList<>();
    public List<PhoneBookEntry> list(){
        TypedQuery<PhoneBookEntry> phonebookList= entityManager.createQuery("select p from phonebook p",PhoneBookEntry.class);
        List<PhoneBookEntry>result=phonebookList.getResultList();
        return  result;
    }
    @Transactional//used to ensure changes are committed
    public void save(String name, String phoneNumber){
        PhoneBookEntry phoneBookEntry = new PhoneBookEntry();
        phoneBookEntry.setPhoneNumber(phoneNumber);
        phoneBookEntry.setName(name);
        list.add(phoneBookEntry);
        entityManager.persist(phoneBookEntry);
    }

    @Transactional//ensures consistency of database after updates
    public boolean deleteByName(String name){
        /*int index = IntStream.range(0, list.size())
                .filter(i -> list.get(i).getName().equals(name))
                .findFirst()
                .orElse(-1);
        if(index!=-1){
            list.remove(index);
        }*/
        //create queries
        //first must check if the name exists in the database at all
        //typed queries return specified type
        TypedQuery<PhoneBookEntry> findName=entityManager.createQuery("select p from phonebook p where p.name = :name",PhoneBookEntry.class);
        findName.setParameter("name", name);
        List<PhoneBookEntry>result=findName.getResultList();//gets list of entries where that have the requested name
        if(result.size()==0)//if result has size 0(0 entries) then the name doesn't exist
        {
            return false;
        }
        
        Query query=entityManager.createQuery("delete from phonebook p where p.name = :name");
        query.setParameter("name", name);
        query.executeUpdate();
        return true;
    }

    @Transactional//ensures consistency of database after updates
    public boolean deleteByNumber(String phoneNumber){
        int index = IntStream.range(0, list.size())
                .filter(i -> list.get(i).getPhoneNumber().equals(phoneNumber))
                .findFirst()
                .orElse(-1);
        if(index!=-1){
            list.remove(index);
        }
        //create queries
        //first must check if the name exists in the database at all
        TypedQuery<PhoneBookEntry> findNum=entityManager.createQuery("select p from phonebook p where p.phoneNumber = :phoneNumber",PhoneBookEntry.class);
        findNum.setParameter("phoneNumber", phoneNumber);
        List<PhoneBookEntry>result=findNum.getResultList();//gets list of entries where that have the requested name
        if(result.size()==0)//if result has size 0(0 entries) then the name doesn't exist
        {
            return false;
        }

        Query query=entityManager.createQuery("delete from phonebook p where p.phoneNumber = :phoneNumber");
        query.setParameter("phoneNumber", phoneNumber);
        query.executeUpdate();
        return true;
    }
    
}
