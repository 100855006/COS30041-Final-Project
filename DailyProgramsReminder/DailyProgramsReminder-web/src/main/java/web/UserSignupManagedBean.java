/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import dto.UserDTO;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Kamyab R. Bozorg
 */
@Named(value = "userSignupManagedBean")
@RequestScoped
public class UserSignupManagedBean {
    
    @EJB
    private bean.UsersManagementFacadeRemote usersManagementFacade;
    
    @Size(min=8,max=50)
    @NotNull
    String username;
    
    @Size(min=8,max=50)
    @NotNull
    String password;
    
    @Size(min=8,max=100)
    @NotNull
    String email;
    
    @Size(max=50)
    @NotNull
    String firstname;
    
    @Size(max=50)
    @NotNull
    String lastname;

    /**
     * Creates a new instance of UserSignupManagedBean
     */
    public UserSignupManagedBean() {
    }
    
    public String signup() {
        if(username.isEmpty() || password.isEmpty() || email.isEmpty() 
                || firstname.isEmpty() || lastname.isEmpty()) {
            return "debug";
        } else {
            if (usersManagementFacade.createUser(
                new UserDTO(username.toLowerCase(), encrypt(password), 
                        email.toLowerCase(), firstname, lastname, "DPR-USER", true))){
                return "success";
            } else {
                return "failure";
            }
        }
    }
    
    private String encrypt(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        
        return "Encryption Error";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
