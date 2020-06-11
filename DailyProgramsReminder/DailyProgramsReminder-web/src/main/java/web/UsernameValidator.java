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
@FacesValidator("validator.usernameValidator")
public class UsernameValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        boolean isValid = true;
        StringBuilder msg = new StringBuilder();
        
        String val = value.toString().trim();
        
        if(val.length() < 8) {
            msg.append("The username must be at least 8 characters long! ");
            
            isValid = false;
        } else if (val.length() > 50) {
            msg.append("The username must not be more than 50 characters long! ");
            
            isValid = false;
        }
        
        if(!val.matches("^[a-z0-9]+$")) {
            msg.append("The username is not entered correctly! (lowercase letters and numbers are only allowed!");
            
            isValid = false;
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