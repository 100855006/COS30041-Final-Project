/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dto.PlanDTO;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ejb.Remote;

/**
 *
 * @author Kamyab R. Bozorg
 */
@Remote
public interface PlansManagementFacadeRemote {

    Boolean createPlansTable(String username);

    ArrayList<PlanDTO> getPlans(String username);
    
    ArrayList<PlanDTO> getIncompletedPlans(String username);
    
    ArrayList<PlanDTO> getPlansByTitle(String username, String title);

    ArrayList<PlanDTO> getPlansByDate(String username, Calendar date);

    ArrayList<PlanDTO> getPlansByDateRange(String username, Calendar from, Calendar to);
    
    ArrayList<PlanDTO> getTodayPlans(String username);

    PlanDTO getPlan(String username, String planId);

    Boolean createPlan(String username, PlanDTO plan);

    Boolean updatePlan(String username, PlanDTO plan);

    Boolean deletePlan(String username, Integer planId);
    
}
