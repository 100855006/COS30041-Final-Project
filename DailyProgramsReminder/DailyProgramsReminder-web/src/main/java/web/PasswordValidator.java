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
@FacesValidator("validator.passwordValidator")
public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        boolean isValid = true;
        StringBuilder msg = new StringBuilder();
        
        String label = component.getAttributes().get("title").toString();
        String val = value.toString().trim();
        
        if(val.length() < 8) {
            msg.append("The ").append(label).append(" must be at least 8 characters long! ");
            
            isValid = false;
        } else if (val.length() > 50) {
            msg.append("The ").append(label).append(" must not be more than 50 characters long! ");
            
            isValid = false;
        }
        
        if(!val.matches("^.*[A-Z]+.*$")) {
            msg.append("The ").append(label).append(" must contain at least one uppercase character! ");
            
            isValid = false;
        }
        
        if(!val.matches("^.*[a-z]+.*$")) {
            msg.append("The ").append(label).append(" must contain at least one lowercase character! ");
            
            isValid = false;
        }
        
        if(!val.matches("^.*[0-9]+.*$")) {
            msg.append("The ").append(label).append(" must contain at least one number! ");
            
            isValid = false;
        }
        
        //checking the equality of two passwords if the field being validated is the confirm password
        if(component.getAttributes().get("id").equals("cPassword")) {
            Object pass = component.getAttributes().get("password");
            
            if(pass != null) {
                if(!val.equals(pass.toString().trim())) {
                msg.append("The ").append(label).append(" must match the Password! ");
            
                isValid = false;
            }
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
