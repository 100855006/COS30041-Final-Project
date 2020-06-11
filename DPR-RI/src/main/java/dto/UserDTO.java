/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 * The data transfer object used to transfer details of a user from the business tier to the web tier or vice versa 
 * @author Kamyab R. Bozorg
 */
public class UserDTO implements Serializable {
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String groupType;
    private Boolean active;

    public UserDTO(String username, String password, String email, String firstname, String lastname, String groupType, Boolean active) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.groupType = groupType;
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGroupType() {
        return groupType;
    }

    public Boolean getActive() {
        return active;
    }
    
    
}
