/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dto.PlanDTO;
import java.util.ArrayList;
import java.util.Calendar;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Kamyab R. Bozorg
 */
@DeclareRoles({"DPR-USER"})
@Stateless
public class PlansManagementFacade implements PlansManagementFacadeRemote {

    @EJB
    private PlansBeanLocal plansBean;

    public PlansManagementFacade() {

    }
    
    @Override
    @PermitAll
    public Boolean createPlansTable(String username) {
        if(username.isEmpty()) {
            return false;
        }
        
        return plansBean.createPlansTable(username);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public ArrayList<PlanDTO> getPlans(String username) {
        if(username.isEmpty()) {
            return null;
        }
        
        return plansBean.getPlans(username);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public ArrayList<PlanDTO> getIncompletedPlans(String username) {
        if(username.isEmpty()) {
            return null;
        }
        
        return plansBean.getIncompletedPlans(username);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public ArrayList<PlanDTO> getPlansByTitle(String username, String title) {
        if(username.isEmpty()) {
            return null;
        }
        
        return plansBean.getPlansByTitle(username, title);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public ArrayList<PlanDTO> getTodayPlans(String username) {
        if(username.isEmpty()) {
            return null;
        }
        
        Calendar today = Calendar.getInstance();
        
        return plansBean.getPlansByDate(username, today);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public ArrayList<PlanDTO> getPlansByDate(String username, Calendar date) {
        if(username.isEmpty()) {
            return null;
        }
        
        return plansBean.getPlansByDate(username, date);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public ArrayList<PlanDTO> getPlansByDateRange(String username, Calendar from, Calendar to) {
        if(username.isEmpty()) {
            return null;
        }
        
        return plansBean.getPlansByDateRange(username, from, to);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public PlanDTO getPlan(String username, String planId) {
        if(username.isEmpty() || planId.isEmpty()) {
            return null;
        }
        
        return plansBean.getPlan(username, planId);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public Boolean createPlan(String username, PlanDTO plan) {
        if(username.isEmpty()) {
            return false;
        }
        
        if(plan.getTitle().isEmpty() || plan.getTitle() == null) {
            return false;
        }
        
        if(plan.getDate() == null) {
            return false;
        }
        
        //0 = none, 1 = hourly, 2 = daily, 3 = weekly, 4 = biweekly, 5 = monthly
        //6 = every 3 months, 7 = every 6 months, 8 = yearly
        if(plan.getRepeat() == null || plan.getRepeat() < 0 || plan.getRepeat() > 8) {
            return false;
        }
        
        if(plan.getEndRepeat() == null) {
            return false;
        }
        
        if(plan.getRepeat() != 0) {
            if(plan.getEndRepeat().before(plan.getDate())) {
                return false;
            }
        }
        
        //0 = low, 1 = medium, 2 = high
        if(plan.getPriority() == null || plan.getPriority() < 0 || plan.getPriority() > 2) {
            return false;
        }
        
        //newly created plan must not be in completed state
        if(plan.getCompleted() == null || plan.getCompleted() == true) {
            return false;
        }
        
        return plansBean.createPlan(username, plan);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public Boolean updatePlan(String username, PlanDTO plan) {
        if(username.isEmpty()) {
            return false;
        }
        
        if(plan.getId() == null || plan.getId() < 1){
            return false;
        }
        
        if(plan.getTitle().isEmpty() || plan.getTitle() == null) {
            return false;
        }
        
        if(plan.getDate() == null) {
            return false;
        }
        
        //0 = none, 1 = hourly, 2 = daily, 3 = weekly, 4 = biweekly, 5 = monthly
        //6 = every 3 months, 7 = every 6 months, 8 = yearly
        if(plan.getRepeat() == null || plan.getRepeat() < 0 || plan.getRepeat() > 8) {
            return false;
        }
        
        if(plan.getEndRepeat() == null) {
            return false;
        }
        
        if(plan.getRepeat() != 0) {
            if(plan.getEndRepeat().before(plan.getDate())) {
                return false;
            }
        }
        
        //0 = low, 1 = medium, 2 = high
        if(plan.getPriority() == null || plan.getPriority() < 0 || plan.getPriority() > 2) {
            return false;
        }
        
        //newly created plan must not be in completed state
        if(plan.getCompleted() == null) {
            return false;
        }
        
        return plansBean.updatePlan(username, plan);
    }
    
    @Override
    @RolesAllowed({"DPR-USER"})
    public Boolean deletePlan(String username, Integer planId) {
        if(username.isEmpty()) {
            return false;
        }
        
        if(planId == null || planId < 1) {
            return false;
        }
        
        return plansBean.deletePlan(username, planId);
    }
}
