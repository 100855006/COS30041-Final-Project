/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Kamyab R. Bozorg
 */
@FacesValidator("validator.newPasswordValidator")
public class NewPasswordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        boolean isValid = true;
        StringBuilder msg = new StringBuilder();
        
        String val = value.toString().trim();
        
        Object oldPassword = component.getAttributes().get("oldPassword");
            
            if(oldPassword != null) {
                if(val.equals(oldPassword.toString().trim())) {
                    msg.append("The new password cannot be the same as the old password!");

                    isValid = false;
                }
            }
        
        if(!isValid) {
            FacesMessage errorMessage = new FacesMessage(msg.toString());
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(errorMessage);
        }
        
        try {
            
        } catch (Exception e) {
            
        }
    }

}
