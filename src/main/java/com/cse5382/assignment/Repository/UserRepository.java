package com.cse5382.assignment.Repository;
import com.cse5382.assignment.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface UserRepository extends JpaRepository<Users,Long> 
{
    //parameterized query to prevent injection attacks
    @Query("Select user from Users user where user.username= :username")
    //custom function to get username from database
    public Users getUserByUsername(@Param("username") String username);

    public boolean existsByUsername(String username);

   // @Query("delete user from Users user where user.username= :username")
    public void deleteByUsername(@Param("username") String username);
    
    
} 
