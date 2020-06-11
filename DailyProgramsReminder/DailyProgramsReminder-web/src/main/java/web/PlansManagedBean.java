/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import bean.PlansManagementFacadeRemote;
import dto.PlanDTO;
import javax.inject.Named;
import javax.enterprise.context.ConversationScoped;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.inject.Inject;

/**
 *
 * @author Kamyab R. Bozorg
 */
@Named(value = "plansManagedBean")
@ConversationScoped
public class PlansManagedBean implements Serializable {

    @Inject
    private Conversation conversation;

    @EJB
    private PlansManagementFacadeRemote plansManagementFacade;

    @Inject
    private UserManagedBean userManagedBean;

    int planId;
    String planTitle;
    String planDescription;
    Date planDate;
    String planRepeat;
    Date planEndRepeat;
    String planPriority;
    boolean planCompleted;
    
    PlanDTO planTobeUpdated;

    //Search variable for plans
    String title;

    Date singleDate;

    Date startDate;
    Date endDate;

    public enum GetPlansType {
        ALL, TITLE, SINGLE_DATE, DATE_RANGE, INCOMPLETE;
    }

    GetPlansType searchType;

    /**
     * Creates a new instance of PlansManagedBean
     */
    public PlansManagedBean() {
        planId = 0;
        planTitle = "";
        planDescription = "";
        planDate = new Date();
        planRepeat = "2";
        planEndRepeat = new Date();
        planPriority = "0";
        planCompleted = false;
        
        title = "";
        singleDate = new Date();
        startDate = new Date();
        endDate = new Date();
        searchType = GetPlansType.ALL;
    }

    @PostConstruct
    public void initIt() {
        try {
            if (!userManagedBean.isLoggedIn()) {
                userManagedBean.sendError(500);
                userManagedBean.logoutResult();
            }
        } catch (Exception e) {
            userManagedBean.logoutResult();
        }
    }

    public String today() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        return sdf.format(Calendar.getInstance().getTime());
    }

    public List<PlanDTO> getTodayPlans() {
        return plansManagementFacade.getTodayPlans(userManagedBean.getUsername());
    }

    public String setSearchTypeForResult(GetPlansType searchType) {
        setSearchType(searchType);

        return "search";
    }

    public List<PlanDTO> getPlans() {
        List<PlanDTO> res = null;
        switch (this.searchType) {
            case ALL:
                res = plansManagementFacade.getPlans(userManagedBean.getUsername());

                break;
            case TITLE:
                if (title.isEmpty() || title == null) {
                    res = plansManagementFacade.getPlans(userManagedBean.getUsername());
                } else {
                    res = plansManagementFacade.getPlansByTitle(userManagedBean.getUsername(), this.title);
                }

                break;
            case SINGLE_DATE:
                if (singleDate != null) {
                    Calendar date = Calendar.getInstance();
                    date.setTime(this.singleDate);
                    res = plansManagementFacade.getPlansByDate(userManagedBean.getUsername(), date);
                } else {
                    res = plansManagementFacade.getPlans(userManagedBean.getUsername());
                }

                break;
            case DATE_RANGE:
                if (startDate != null && endDate != null) {
                    Calendar fromDate = Calendar.getInstance();
                    fromDate.setTime(this.startDate);

                    Calendar toDate = Calendar.getInstance();
                    toDate.setTime(this.endDate);

                    res = plansManagementFacade.getPlansByDateRange(userManagedBean.getUsername(),
                            fromDate, toDate);
                } else {
                    res = plansManagementFacade.getPlans(userManagedBean.getUsername());
                }

                break;
            case INCOMPLETE:
                res = plansManagementFacade.getIncompletedPlans(userManagedBean.getUsername());
                break;
            default:
                res = plansManagementFacade.getPlans(userManagedBean.getUsername());
        }

        return res;
    }

    public String createPlan() {
        try {
            //if the repeat is set to Never, the end repeat date must be same as the date
            if(Integer.parseInt(this.planRepeat) == 0) {
                this.planEndRepeat = (Date) this.planDate.clone();
            }
            
            Calendar dateTemp = Calendar.getInstance();
            dateTemp.setTime(this.planDate);
            
            Calendar endRepeatTemp = Calendar.getInstance();
            endRepeatTemp.setTime(this.planEndRepeat);
            
            PlanDTO dto = new PlanDTO(0, this.planTitle, this.planDescription, dateTemp, 
                    Integer.parseInt(this.planRepeat), endRepeatTemp, Integer.parseInt(this.planPriority),
                    false);
            
            if(plansManagementFacade.createPlan(userManagedBean.getUsername(), dto)) {
                return "success";
            } else {
                return "failure";
            }
            
        } catch (Exception e) {
            return "failure";
        }
    }
    
    public String viewPlan(Integer id) {
        try {
            this.conversation.begin();
            
            planTobeUpdated = plansManagementFacade.getPlan(userManagedBean.getUsername(), id.toString());
            if(planTobeUpdated == null) {
                return "viewFailure";
            }

            this.planId = planTobeUpdated.getId();
            this.planTitle = planTobeUpdated.getTitle();
            this.planDescription = planTobeUpdated.getDescription();
            this.planDate.setTime(planTobeUpdated.getDate().getTimeInMillis());
            this.planRepeat = planTobeUpdated.getRepeat().toString();
            this.planEndRepeat.setTime(planTobeUpdated.getEndRepeat().getTimeInMillis());
            this.planPriority = planTobeUpdated.getPriority().toString();
            this.planCompleted = planTobeUpdated.getCompleted();

            return "viewSuccess";
        } catch (Exception e) {
            return "viewFailure";
        }
    }

    public String editPlan() {
        try {
            //if the repeat is set to Never, the end repeat date must be same as the date
            if(Integer.parseInt(this.planRepeat) == 0) {
                this.planEndRepeat = (Date) this.planDate.clone();
            }
            
            Calendar dateTemp = Calendar.getInstance();
            dateTemp.setTime(this.planDate);
            
            Calendar endRepeatTemp = Calendar.getInstance();
            endRepeatTemp.setTime(this.planEndRepeat);
            
            PlanDTO dto = new PlanDTO(this.planId, this.planTitle, this.planDescription, dateTemp, 
                    Integer.parseInt(this.planRepeat), endRepeatTemp, Integer.parseInt(this.planPriority),
                    this.planCompleted);
            
            //create a new plan if it is a repeating plan
            if(planTobeUpdated.getCompleted() == false && dto.getCompleted() == true && dto.getRepeat() != 0) {
                Calendar newDate = Calendar.getInstance();
                newDate.setTimeInMillis(dto.getDate().getTimeInMillis() + dto.getRepeatInMillis());
                
                System.out.println(dto.getDate().getTime());
                System.out.println(newDate.getTime());
                //before creating new plan, check if the new date is not after the end repeat date.
                //otherwise, server will throw an error
                if(!newDate.after(dto.getEndRepeat())) {
                    PlanDTO creatDto = new PlanDTO(dto.getId(), dto.getTitle(), dto.getDescription(), 
                    newDate, dto.getRepeat(), dto.getEndRepeat(), dto.getPriority(), false);

                    if(!plansManagementFacade.createPlan(userManagedBean.getUsername(), creatDto)) {
                        this.conversation.end();
                        return "failure";
                    }
                }
            }
            
            if(plansManagementFacade.updatePlan(userManagedBean.getUsername(), dto)) {
                this.conversation.end();
                return "success";
            } else {
                this.conversation.end();
                return "failure";
            }
            
        } catch (Exception e) {
            return "failure";
        }
    }

    public String deletePlan(int id) {
        if (plansManagementFacade.deletePlan(userManagedBean.getUsername(), id)) {
            return "deleteSuccess";
        } else {
            return "deleteFailure";
        }
    }

    public String getSearchResultText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String res = "Showing results for | ";
        switch (this.searchType) {
            case ALL:
                res = "";
            case TITLE:
                if (title.isEmpty() || title == null) {
                    res = "";
                } else {
                    res += "Title: " + this.title;
                }

                break;
            case SINGLE_DATE:
                if (singleDate != null) {
                    res += "Date: " + sdf.format(this.singleDate);
                } else {
                    res = "";
                }

                break;
            case DATE_RANGE:
                if (startDate != null && endDate != null) {
                    res += "From Date: " + sdf.format(this.startDate) + " To Date: " + sdf.format(this.endDate);
                } else {
                    res = "";
                }
                break;
            case INCOMPLETE:
                res += "Incomplete Plans";
                break;
            default:
                return "";
        }

        return res;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getSingleDate() {
        return singleDate;
    }

    public void setSingleDate(Date singleDate) {
        this.singleDate = singleDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public GetPlansType getSearchType() {
        return searchType;
    }

    public void setSearchType(GetPlansType searchType) {
        this.searchType = searchType;
    }
    
    public int getPlanId() {
        return planId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public String getPlanRepeat() {
        return planRepeat;
    }

    public void setPlanRepeat(String planRepeat) {
        this.planRepeat = planRepeat;
    }

    public Date getPlanEndRepeat() {
        return planEndRepeat;
    }

    public void setPlanEndRepeat(Date planEndRepeat) {
        this.planEndRepeat = planEndRepeat;
    }

    public String getPlanPriority() {
        return planPriority;
    }

    public void setPlanPriority(String planPriority) {
        this.planPriority = planPriority;
    }

    public boolean isPlanCompleted() {
        return planCompleted;
    }

    public void setPlanCompleted(boolean planCompleted) {
        this.planCompleted = planCompleted;
    }
}
