/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import angularBeans.api.http.Get;

import angularBeans.api.AngularBean;
import angularBeans.api.NGModel;
import angularBeans.api.NGSubmit;
import bean.PlansManagementFacadeRemote;
import dto.PlanDTO;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Inject;


/**
 *
 * @author Kamyab R. Bozorg
 */
@AngularBean
public class CreatePlanAngularBean {
    @EJB
    private PlansManagementFacadeRemote plansManagementFacade;
    
    @Inject
    private UserManagedBean userManagedBean;
    
    String planTitle;
    String planDescription;
    String planDate;
    String planRepeat;
    String planEndRepeat;
    String planPriority;
    
    @Get
    @NGSubmit(backEndModels = "*")
    public boolean createPlan() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");
            Date date = sdf.parse(planDate);
            Date endRepeat = sdf.parse(planEndRepeat);

            Calendar dateTemp = Calendar.getInstance();
            dateTemp.setTime(date);
            //for some reason the retrieved date string is parsed into one day after the given date
            //So we need to find the day before
            dateTemp.add(Calendar.DATE, -1);
            
            Calendar endRepeatTemp = Calendar.getInstance();
            endRepeatTemp.setTime(endRepeat);
            //for some reason the retrieved date string is parsed into one day after the given date
            //So we need to find the day before
            endRepeatTemp.add(Calendar.DATE, -1);
            
            PlanDTO dto = new PlanDTO(0, this.planTitle, this.planDescription, dateTemp, 
                    Integer.parseInt(this.planRepeat), endRepeatTemp, Integer.parseInt(this.planPriority),
                    false);

            return plansManagementFacade.createPlan(userManagedBean.getUsername(), dto);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    @NGModel
    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    @NGModel
    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    @NGModel
    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }

    @NGModel
    public String getPlanRepeat() {
        return planRepeat;
    }

    public void setPlanRepeat(String planRepeat) {
        this.planRepeat = planRepeat;
    }

    @NGModel
    public String getPlanEndRepeat() {
        return planEndRepeat;
    }

    public void setPlanEndRepeat(String planEndRepeat) {
        this.planEndRepeat = planEndRepeat;
    }

    @NGModel
    public String getPlanPriority() {
        return planPriority;
    }

    public void setPlanPriority(String planPriority) {
        this.planPriority = planPriority;
    }
}
