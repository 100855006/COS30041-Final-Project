/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.text.SimpleDateFormat;
import java.util.Date;
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
@FacesValidator("validator.planDateValidator")
public class PlanDateValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        boolean isValid = true;
        StringBuilder msg = new StringBuilder();

        String label = component.getAttributes().get("title").toString();

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            Date val = (Date) value;

            Object minDateAtt = component.getAttributes().get("minDate");

            Date minDate = new Date();

            if (!minDateAtt.toString().toLowerCase().equals("today")) {
                minDate = (Date) minDateAtt;
            }

            if (val.before(minDate)) {
                msg.append("The ").append(label).append(" must be after ")
                        .append(sdf.format(minDate)).append("!");

                isValid = false;
            }

        } catch (Exception e) {

        }

        if (!isValid) {
            FacesMessage errorMessage = new FacesMessage(msg.toString());
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(errorMessage);
        }
    }
}
