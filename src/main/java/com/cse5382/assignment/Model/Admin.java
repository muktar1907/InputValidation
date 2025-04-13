package com.cse5382.assignment.Model;
import javax.persistence.*;

@Entity
@Table(name="Admins")
public class Admin {
    @Id
    @Column(name="adminID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String password;
    private String role="ROLE_ADMIN";//spring security expects "ROLE_" prefix
    //getters and setters
    public String getUser()
    {
        return username;
    }
    public void setUser(String username)
    {
        this.username=username;
    }

    public String getPass()
    {
        return password;
    }
    public void setPass(String password)
    {
        this.password=password;
    }

    public String getRole()
    {
        return role;
    }
}