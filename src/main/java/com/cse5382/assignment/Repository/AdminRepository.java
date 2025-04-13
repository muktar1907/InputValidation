package com.cse5382.assignment.Repository;
import com.cse5382.assignment.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface AdminRepository extends JpaRepository<Admin,Long> 
{
    //parameterized query to prevent injection attacks
    @Query("Select admin from Admin admin where admin.username= :username")
    //custom function to get username from database
    public Admin getUserByUsername(@Param("username") String username);
    
} 
