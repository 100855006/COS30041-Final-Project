/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dto.UserDTO;
import javax.ejb.Remote;

/**
 *
 * @author kamya
 */
@Remote
public interface UsersManagementFacadeRemote {

    UserDTO getUser(String username);
    
    Boolean createUser(UserDTO userDTO);

    Boolean updateUser(UserDTO userDTO);

    Boolean updateUserPassword(String username, String currentPassword, String newPassword);
    
    Boolean activateUser(String username);

    Boolean deactivateUser(String username);

    Boolean removeUser(String username);

}
