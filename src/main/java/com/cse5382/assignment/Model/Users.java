package com.cse5382.assignment.Model;
import javax.persistence.*;

@Entity
@Table(name="Users")
public class Users {
    @Id
    @Column(name="UserID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String role;//ROLE_USER(read only) and ROLE_ADMIN(read/write) are the 2 valid roles. Spring security expects "ROLE_" prefix
    
    public Users(String role)
    {
        this.role=role;
        
    }
    public Users()//no args constructor needed for hibernate to work
    {

        
    }
    //getters and setters
    
    public String getUsername()
    {
        return username;
    }
    public void setUsername(String username)
    {
        this.username=username;
    }

    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }

    public String getRole()
    {
        return role;
    }
    public void setRole(String role)
    {
        this.role=role;;
    }
}