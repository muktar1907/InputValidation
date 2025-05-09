package com.cse5382.assignment.Service;

import com.cse5382.assignment.Repository.PhoneBookRepository;
import com.cse5382.assignment.Model.PhoneBookEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service//lets springboot know to use this class when creating instance of class that uses PhoneBookService interface
@Transactional
public class PhoneBookServiceImpl implements PhoneBookService{
    @Autowired
    PhoneBookRepository phoneBookRepository;

    @Override
    public List<PhoneBookEntry> list() {
        return phoneBookRepository.list();
    }
    //input validation will be handled in the service class before calls to repository
    public static boolean validateName(String name)
    {
        //name validation
        String namePattern="[a-z]{1,}('|-)?[a-z]{1,}( [a-z]{1,}('|-)?[a-z]{1,}(-)?[a-z]{1,})?";
        Pattern validName= Pattern.compile(namePattern,Pattern.CASE_INSENSITIVE);
        Matcher nameMatch= validName.matcher(name);
        boolean nMatch=nameMatch.matches();
        if(!nMatch)
        {
            namePattern="[a-z]{1,}('|-)?[a-z]{1,}(-)?[a-z]{1,}, [a-z]{1,}('|-)?[a-z]{1,}( [a-z]{1,}(')?[a-z]{1,}| [a-z]{1}.)?";
            validName=Pattern.compile(namePattern,Pattern.CASE_INSENSITIVE);
            nameMatch= validName.matcher(name);
            nMatch=nameMatch.matches();
        }
        return nMatch;
    }
    public static boolean validateNum(String phoneNumber)
    {
        //phone validation-------------------------------------------------------------------------------------------------------------
        int i=1;
        boolean pMatch=false;
        String numPattern="";
        while(pMatch==false && i<=8)
        {
            //((\\+?[0-9]{1,3})( |))?(\\([0-9]{2,3}\\)( )?)?([0-9]){3}([. -])([0-9]){4}
            switch(i)
            {
                case 1:
                    numPattern= "([0-9]){5}";
                    break;
                case 2:
                    numPattern="((\\+?[0-9]{1,3})( |))(\\([0-9]{2,3}\\)( )?)?([0-9]){3}([. -])([0-9]){4}";
                    break;
                case 3:
                    numPattern="((\\+?[0-9]{1,3})( |-))?([0-9]{2,3}-)?([0-9]){3}-([0-9]){4}";
                    break;
                case 4:
                    numPattern="((\\+?[0-9]{1,3})( |.))?([0-9]{2,3}\\.)?([0-9]){3}\\.([0-9]){4}";
                    break;
                case 5:
                    numPattern="((\\+?[0-9]{1,3})( | [0-9]{1} ))?([0-9]{3} )?([0-9]){3} ([0-9]){4}";
                    break;
                case 6:
                    numPattern="(\\+45|[0-9]{2})? [0-9]{2} [0-9]{2} [0-9]{2} [0-9]{2}";
                    break;
                case 7:
                    numPattern="(\\+45 |[0-9]{2} )?[0-9]{4,5}[. ][0-9]{4,5}";
                    break;
                case 8:
                    numPattern="((\\+?[0-9]{1,3})( |))?(\\([0-9]{2,3}\\))?([0-9]){3}([. -])([0-9]){4}";
                    break;
                default:
                    break;

                    
            }


            Pattern validNum= Pattern.compile(numPattern);
            Matcher numMatch= validNum.matcher(phoneNumber);
            pMatch=numMatch.matches();
            i++;
        }
        return pMatch;
    }
    @Override
    public void add(PhoneBookEntry phoneBookEntry) throws Exception {
        
        if(validateName(phoneBookEntry.getName())==true && validateNum(phoneBookEntry.getPhoneNumber())==true)
        {
            if(!(phoneBookRepository.numExists(phoneBookEntry.getPhoneNumber())))//if the number doesn't yet exist then save it
            {
                phoneBookRepository.save(phoneBookEntry.getName(),phoneBookEntry.getPhoneNumber());
            }
            else
            {
                throw new Exception("Existing Phone Number");
            }
        }
        else if(validateName(phoneBookEntry.getName())==false || validateNum(phoneBookEntry.getPhoneNumber())==false)
        {
            throw new Exception("Invalid Input");//this exception will be caught by the controller
        }
    }

    @Override
    public void deleteByName(String name) throws Exception{
        //must validate input to prevent injection attack
        boolean valid= validateName(name);
        System.out.println(name+" is valid: "+valid);
        if(valid==true)
        {
            boolean exists=phoneBookRepository.deleteByName(name);
            if(!exists)
            {
                throw new Exception("Content Not Found");
            }

        }
        else
        {
            throw new Exception("Invalid Input");
        }
        
    }


    @Override
    public String deleteByNumber(String phoneNumber) throws Exception{
        //must validate input to prevent injection attack
        boolean valid= validateNum(phoneNumber);
        if(valid == true)
        {
            String exists=phoneBookRepository.deleteByNumber(phoneNumber);
            if(exists.equals(""))
            {
                throw new Exception("Content Not Found");
            }
            return exists;
        }
        else
        {
            throw new Exception("Invalid Input");
        }
    }
    @Override
    public void reset()
    {
        try
        {
            phoneBookRepository.deleteAll();
        }catch(Exception e)
        {
            System.out.println("reset DB error: "+e);
        }
    }
}
