/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * The data transfer object used to transfer details of a plan from the business tier to the web tier or vice versa 
 * @author Kamyab R. Bozorg
 */
public class PlanDTO implements Serializable {
    private Integer id;
    private String title;
    private String description;
    private Calendar date;
    private Integer repeat;
    private Calendar endRepeat;
    private Integer priority;
    private Boolean completed;
    
    public PlanDTO() {
        this.id = -1;
        this.title = "";
        this.description = "";
        this.date = Calendar.getInstance();
        this.repeat = -1;
        this.endRepeat = Calendar.getInstance();
        this.priority = -1;
        this.completed = false;
    }

    public PlanDTO(Integer id, String title, String description, Calendar date, Integer repeat, Calendar endRepeat, Integer priority, Boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.repeat = repeat;
        this.endRepeat = endRepeat;
        this.priority = priority;
        this.completed = completed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getDate() {
        return date;
    }
    
    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        return sdf.format(this.date.getTime());
    }
    
    public String getTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm z");
        
        return sdf.format(this.date.getTime());
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Integer getRepeat() {
        return repeat;
    }
    
    public Long getRepeatInMillis() {
        final Long HOUR = new Long(1 * 60 * 60 * 1000);
        final Long DAY = 24 * HOUR;
        final Long WEEK = 7 * DAY;
        final Long MONTH = 4 * WEEK;
        final Long YEAR = 12 * MONTH;
        switch(this.repeat) {
            case 0:
                return new Long(0);
            case 1:
                return HOUR;
            case 2:
                return DAY;
            case 3:
                return WEEK;
            case 4:
                return 2 * WEEK;
            case 5:
                return MONTH;
            case 6:
                return 3 * MONTH;
            case 7:
                return 6 * MONTH;
            case 8:
                return YEAR;
            default:
                return new Long(0);                
        }
    }
    
    public String getRepeatString() {
        switch(this.repeat) {
            case 0:
                return "Never";
            case 1:
                return "Hourly";
            case 2:
                return "Daily";
            case 3:
                return "Weekly";
            case 4:
                return "Biweekly";
            case 5:
                return "Monthly";
            case 6:
                return "Every 3 Months";
            case 7:
                return "Every 6 Months";
            case 8:
                return "yearly";
            default:
                return "undefined";                
        }
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public Calendar getEndRepeat() {
        return endRepeat;
    }
    
    public String getEndRepeatString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        return sdf.format(this.endRepeat.getTime());
    }

    public void setEndRepeat(Calendar endRepeat) {
        this.endRepeat = endRepeat;
    }

    public Integer getPriority() {
        return priority;
    }
    
    public String getPriorityString() {
        switch(this.priority) {
            case 0:
                return "None";
            case 1:
                return "Low";
            case 2:
                return "Medium";
            case 3:
                return "High";
            default:
                return "undefined";
        }
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
