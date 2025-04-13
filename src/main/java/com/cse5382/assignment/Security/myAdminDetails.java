package com.cse5382.assignment.Security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cse5382.assignment.Model.Admin;

public class myAdminDetails implements UserDetails 
{
    private Admin admin;//instance of admin class

    //constructor
    public myAdminDetails(Admin admin)
    {
        this.admin=admin;
    }
    @Override
    public String getUsername()
    {
        return admin.getUser();
    }

    @Override
    public String getPassword()
    {
        return admin.getPass();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority= new SimpleGrantedAuthority(admin.getRole());
        return Arrays.asList(authority);//function expects a collection
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;//don't allow accounts to expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;//don't allow account to get locked,may implement later
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;//don't allow credentials to expire
    }

    @Override
    public boolean isEnabled() {
        return true;//assumes user is active
    }
}
