/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import dto.UserDTO;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Kamyab R. Bozorg
 */
@Named(value = "userManagedBean")
@SessionScoped
public class UserManagedBean implements Serializable {
    private static final String LOGOUT = "logout";

    @EJB
    private bean.UsersManagementFacadeRemote usersManagementFacade;
    
    String username;
    String email;
    String firstname;
    String lastname;
    String groupType;
    Boolean active;
    
    private Boolean isLoggedIn;
    
    
    //for updating the user password only
    String oldPassword;
    String password;
    String cPassword;

    /**
     * Creates a new instance of UserManagedBean
     */
    public UserManagedBean() {        
        this.username = "";
        this.email = "";
        this.firstname = "";
        this.lastname = "";
        this.groupType = "";
        this.active = false; 
        
        this.isLoggedIn = Boolean.FALSE;
    }
    
    @PostConstruct
    public void initIt(){
        fillUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
    }
    
    public void fillUser(String username) {
        UserDTO userDTO = usersManagementFacade.getUser(username);
        
        if(userDTO != null) {
            this.username = userDTO.getUsername();
            this.email = userDTO.getEmail();
            this.firstname = userDTO.getFirstname();
            this.lastname = userDTO.getLastname();
            this.groupType = userDTO.getGroupType();
            this.active = userDTO.getActive();
        } else {
            logoutResult();
        }
    }
    
    public String reactivateUserAccount() {
        if (groupType.equals("DPR-USER")) {
            if (!active) {
                if(usersManagementFacade.activateUser(this.username)) {
                    return "reactivateSuccess";
                }
            } 
        }
        
        return "reactivateFailure";
    }
    
    public String deactivateUserAccount() {
        if(!this.isLoggedIn) {
            this.sendError(500);
            
            return null;
        }
        
        if (groupType.equals("DPR-USER")) {
            if (active) {
                if(usersManagementFacade.deactivateUser(this.username)) {
                    this.isLoggedIn = false;
                    return "deactivateSuccess";
                }
            } 
        }
        
        return "deactivateFailure";
    }
    
    public String updateUserProfile() {
        if(!this.isLoggedIn) {
            this.sendError(500);
            
            return null;
        }
        
        if(username.isEmpty() || email.isEmpty() || firstname.isEmpty() 
                || lastname.isEmpty() || !groupType.equals("DPR-USER")) {
            return "updateDebug";
        } else {
            //note: username, password and group type cannot be updated by this method
            if (usersManagementFacade.updateUser(
                new UserDTO(username, null, 
                        email.toLowerCase(), firstname, lastname, groupType, active))){
                return "updateSuccess";
            } else {
                return "updateFailure";
            }
        }
    }
    
    public String updateUserPassword() {
        if(!this.isLoggedIn) {
            this.sendError(500);
            
            return null;
        }
        
        // check empId is null
        if (username.isEmpty() || !groupType.equals("DPR-USER")) {
            return "debug";
        }

        // newPassword and confirmPassword are the same - checked by the validator during input to JSF form
        boolean result = usersManagementFacade.updateUserPassword(username, encrypt(oldPassword), encrypt(password));
        
        if (result) {
            return "success";
        } else {
            return "failure";
        }
    }
    
    public String deleteUserAccountPermanently() {
        if (groupType.equals("DPR-USER")) {
            if (!active) {
                if(usersManagementFacade.removeUser(this.username)) {
                    this.isLoggedIn = Boolean.FALSE;
                    return "deleteSuccess";
                }
            } 
        }
        
        return "deleteFailure";
    }
    
    public void redirect() throws IOException {
        if (groupType.equals("DPR-USER")) {
            if (active) {
                this.isLoggedIn = Boolean.TRUE;
                ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
                        .getResponse()).sendRedirect("faces/user/home.xhtml");
            } else {
                ((HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
                        .getResponse()).sendRedirect("faces/private/reactivate.xhtml");
            }
        }
        else {
            logoutResult();
        }
    }
    
    public String logoutResult() {
        // terminate the session by invalidating the session context
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        
        try {
            request.logout();
        } catch (ServletException ex) {
            // do nothing
        }
        
        // terminate the session by invalidating the session context
        HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
        session.invalidate();
        
        this.isLoggedIn = Boolean.FALSE;
        
        // terminate the user's login credentials
        return LOGOUT;
    }
    
    public void sendError(int code) {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();
        
        try {
            response.sendError(code);
        } catch (IOException e) {
            //do nothing
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

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
    public Boolean isLoggedIn() {
        return isLoggedIn;
    }
    
    
    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }

}
