/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dto.UserDTO;
import entity.User;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Kamyab R. Bozorg
 */
@Stateless
@DeclareRoles({"DPR-USER"})
public class UsersManagementFacade implements UsersManagementFacadeRemote {

    @EJB
    private PlansBeanLocal plansBean;
    
    
    @PersistenceContext(unitName = "com.bozorg_DailyProgramsReminder-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    private void create(User user) {
        em.persist(user);
    }

    private void edit(User user) {
        em.merge(user);
    }

    private void remove(User user) {
        em.remove(em.merge(user));
    }

    private User find(String username) {
        return em.find(User.class, username);
    }
    
    private User myDTO2DAO(UserDTO userDTO) {
        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setGroupType(userDTO.getGroupType());
        user.setActive(userDTO.getActive());

        return user;
    }

    private UserDTO myDAO2DTO(User user) {
        UserDTO userDTO = new UserDTO(
            user.getUsername(),
            user.getPassword(),
            user.getEmail(),
            user.getFirstname(),
            user.getLastname(),
            user.getGroupType(),
            user.getActive()
        );

        return userDTO;
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public UserDTO getUser(String username) {
        User user = this.find(username);

        UserDTO userDTO = null;

        if (user != null) {
            UserDTO temp = this.myDAO2DTO(user);
            
            userDTO = new UserDTO(
                    temp.getUsername(),
                    null,
                    temp.getEmail(),
                    temp.getFirstname(),
                    temp.getLastname(),
                    temp.getGroupType(),
                    temp.getActive()
            );
        }

        return userDTO;
    }
    
    @Override
    @PermitAll
    public Boolean createUser(UserDTO userDTO) {
        if (find(userDTO.getUsername()) != null) {
            // user whose username can be found 
            return false;
        }

        // user whose username could not be found
        try {
            User user = this.myDTO2DAO(userDTO);
            user.setGroupType("DPR-USER");
            user.setActive(Boolean.TRUE);
            
            this.create(user);    // add to database
            
            //create the table for user's plans
            if(plansBean.createPlansTable(user.getUsername())) {
                return true;
            } else {
                this.removeUser(user.getUsername());
                
                return false;
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
            
            return false; // something is wrong, but should not be here though
        }
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public Boolean updateUser(UserDTO userDTO) {
        User currentUser = find(userDTO.getUsername());
        if (currentUser == null || !currentUser.getActive()) {
            // user whose username cannot be found or user is inactive
            return false;
        }

        // user whose username can be found
        try {
            User user = this.myDTO2DAO(userDTO);
            user.setUsername(currentUser.getUsername()); //just in case, username must not be updated
            user.setPassword(currentUser.getPassword()); //just in case, password must not be updated
            user.setGroupType(currentUser.getGroupType()); //just in case, group type must not be updated
            
            this.edit(user);    // edit database
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            
            return false; // something is wrong, but should not be here though
        }
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public Boolean updateUserPassword(String username, String currentPassword, String newPassword) {
        User currentUser = find(username);
        if (currentUser == null || !currentUser.getActive()) {
            // user whose username cannot be found or user is inactive
            return false;
        }

        // user whose username can be found
        try {
            if(currentUser.getPassword().equals(currentPassword)) {
                currentUser.setPassword(newPassword);
                
                return true;
            }
            
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            
            return false; // something is wrong, but should not be here though
        }
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public Boolean activateUser(String username) {
        User currentUser = find(username);
        if (currentUser == null || currentUser.getActive()) {
            // user whose username cannot be found or user is already active
            return false;
        }

        // user whose username can be found
        try {
            currentUser.setActive(true);
                
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            
            return false; // something is wrong, but should not be here though
        }
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public Boolean deactivateUser(String username) {
        User currentUser = find(username);
        if (currentUser == null || !currentUser.getActive()) {
            // user whose username cannot be found or user is not active
            return false;
        }

        // user whose username can be found
        try {
            currentUser.setActive(false);
                
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            
            return false; // something is wrong, but should not be here though
        }
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public Boolean removeUser(String username) {
        User user = find(username);

        if (user == null) {
            return false;
        }

        
        if(plansBean.dropPlansTable(username)) {
            this.remove(user);
            
            return true;
        }
        
        return false;
    }
}
