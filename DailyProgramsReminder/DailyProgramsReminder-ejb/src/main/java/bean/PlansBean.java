/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dto.PlanDTO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ejb.Stateless;

/**
 *
 * @author Kamyab R. Bozorg
 */
@Stateless
public class PlansBean implements PlansBeanLocal {
    
    private static Connection getConnection() throws SQLException, IOException {
        System.setProperty("jdbc.drivers", "org.apache.derby.jdbc.ClientDriver");

        String url = "jdbc:derby://localhost:1527/daily-programs-reminder;create=true";
        String username = "APP";
        String password = "APP";

        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Boolean createPlansTable(String username) {
        boolean result = false;
        
        Connection cnnct = null;
        Statement stmnt = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            stmnt.execute("CREATE TABLE " + username.toUpperCase() + "_PLANS  ( "
                    + "PLAN_ID INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
                    + "PLAN_TITLE VARCHAR(50) NOT NULL, "
                    + "PLAN_DESCRIPTION LONG VARCHAR, "
                    + "PLAN_DATE TIMESTAMP NOT NULL, "
                    + "PLAN_REPEAT INT NOT NULL, "
                    + "PLAN_END_REPEAT TIMESTAMP NOT NULL, "
                    + "PLAN_PRIORITY INT NOT NULL, "
                    + "PLAN_COMPLETED BOOLEAN NOT NULL)");
            
            result = true;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
            
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
        }
        
        return result;
    }
    
    @Override
    public Boolean dropPlansTable(String username) {
        boolean result = false;
        
        Connection cnnct = null;
        Statement stmnt = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            stmnt.execute("DROP TABLE " + username.toUpperCase() + "_PLANS");
            
            result = true;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
            
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
        }
        
        return result;
    }

    @Override
    public java.util.ArrayList<PlanDTO> get(String query) {
        Connection cnnct = null;
        Statement stmnt = null;
        
        ArrayList<PlanDTO> plans = new ArrayList<>();

        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            ResultSet rows = stmnt.executeQuery(query);
            
            
            while (rows.next()) {
                Calendar date = Calendar.getInstance();
                date.setTime(rows.getTimestamp("PLAN_DATE"));
                
                Calendar endRepeat = Calendar.getInstance();
                endRepeat.setTime(rows.getTimestamp("PLAN_END_REPEAT"));
                
                plans.add(new PlanDTO(
                        rows.getInt("PLAN_ID"),
                        rows.getString("PLAN_TITLE"),
                        rows.getString("PLAN_DESCRIPTION"),
                        date,
                        rows.getInt("PLAN_REPEAT"),
                        endRepeat,
                        rows.getInt("PLAN_PRIORITY"),
                        rows.getBoolean("PLAN_COMPLETED")
                ));
            }
 
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        return plans;
    }
    
    @Override
    public java.util.ArrayList<PlanDTO> getPlans(String username) {
        String query = "SELECT * FROM " + username.toUpperCase() + "_PLANS ORDER BY PLAN_DATE DESC";
        
        return get(query);
    }
    
    @Override
    public java.util.ArrayList<PlanDTO> getIncompletedPlans(String username) {
        String query = "SELECT * FROM " + username.toUpperCase() + "_PLANS "
                + "WHERE PLAN_COMPLETED = FALSE ORDER BY PLAN_DATE DESC";
        
        return get(query);
    }
    
    @Override
    public java.util.ArrayList<PlanDTO> getPlansByTitle(String username, String title) {
        String query = "SELECT * FROM " + username.toUpperCase() + "_PLANS "
                + "WHERE PLAN_TITLE LIKE '%" + title + "%' ORDER BY PLAN_DATE DESC";
        
        return get(query);
    }
    
    @Override
    public java.util.ArrayList<PlanDTO> getPlansByDate(String username, Calendar date) {  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date.getTime());
        
        String query = "SELECT * FROM " + username.toUpperCase() 
                + "_PLANS WHERE Date(PLAN_DATE) = '" + dateString + "' ORDER BY PLAN_DATE DESC";
        
        return get(query);
    }
    
    @Override
    public java.util.ArrayList<PlanDTO> getPlansByDateRange(String username, Calendar from, Calendar to) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromString = sdf.format(from.getTime());
        String toString = sdf.format(to.getTime());
        
        String query = "SELECT * FROM " + username.toUpperCase() 
                + "_PLANS WHERE Date(PLAN_DATE) BETWEEN '" + fromString 
                + "' and '" + toString + "' ORDER BY PLAN_DATE DESC";
        
        return get(query);
    }
    
    @Override
    public PlanDTO getPlan(String username, String planId) {
        Connection cnnct = null;
        Statement stmnt = null;
        
        PlanDTO plan = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            ResultSet row = stmnt.executeQuery("SELECT * FROM " + username.toUpperCase() + "_PLANS WHERE PLAN_ID = " 
                    + planId);
            
            
            if (row.next()) {
                Calendar date = Calendar.getInstance();
                date.setTime(row.getTimestamp("PLAN_DATE"));
                
                Calendar endRepeat = Calendar.getInstance();
                endRepeat.setTime(row.getTimestamp("PLAN_END_REPEAT"));
                
                plan = new PlanDTO(
                        row.getInt("PLAN_ID"),
                        row.getString("PLAN_TITLE"),
                        row.getString("PLAN_DESCRIPTION"),
                        date,
                        row.getInt("PLAN_REPEAT"),
                        endRepeat,
                        row.getInt("PLAN_PRIORITY"),
                        row.getBoolean("PLAN_COMPLETED")
                );
            }
            
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        return plan;
    }
    
    @Override
    public Boolean createPlan(String username, PlanDTO plan) {
        boolean result = false;
        
        Connection cnnct = null;
        PreparedStatement stmnt = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.prepareStatement("INSERT INTO " + username.toUpperCase() + "_PLANS "
                    + "(PLAN_TITLE, PLAN_DESCRIPTION, PLAN_DATE, PLAN_REPEAT, PLAN_END_REPEAT, PLAN_PRIORITY, PLAN_COMPLETED) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmnt.setString(1, plan.getTitle());
            stmnt.setString(2, plan.getDescription());
            stmnt.setTimestamp(3, new Timestamp(plan.getDate().getTimeInMillis()));
            stmnt.setInt(4, plan.getRepeat());
            stmnt.setTimestamp(5, new Timestamp(plan.getEndRepeat().getTimeInMillis()));
            stmnt.setInt(6, plan.getPriority());
            stmnt.setBoolean(7, plan.getCompleted());

            if (stmnt.executeUpdate() == 0) {
                System.out.println("Cannot insert plan!");
            } else {
                result = true;
            }

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
        }

        return result;
    }

    @Override
    public Boolean updatePlan(String username, PlanDTO plan) {
        boolean result = false;
        
        Connection cnnct = null;
        PreparedStatement stmnt = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.prepareStatement("UPDATE " + username.toUpperCase() + "_PLANS SET "
                    + "PLAN_TITLE = ?, PLAN_DESCRIPTION = ?, PLAN_DATE = ?, PLAN_REPEAT = ?, "
                    + "PLAN_END_REPEAT = ?, PLAN_PRIORITY = ?, PLAN_COMPLETED = ? WHERE PLAN_ID = ?");
            stmnt.setString(1, plan.getTitle());
            stmnt.setString(2, plan.getDescription());
            stmnt.setTimestamp(3, new Timestamp(plan.getDate().getTimeInMillis()));
            stmnt.setInt(4, plan.getRepeat());
            stmnt.setTimestamp(5, new Timestamp(plan.getEndRepeat().getTimeInMillis()));
            stmnt.setInt(6, plan.getPriority());
            stmnt.setBoolean(7, plan.getCompleted());
            stmnt.setInt(8, plan.getId());

            if (stmnt.executeUpdate() == 0) {
                System.out.println("Cannot update plan (maybe the PlanId does not exist)!");
            } else {
                result = true;
            }

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
        }

        return result;
    }

    @Override
    public Boolean deletePlan(String username, Integer planId) {
        boolean result = false;
        
        Connection cnnct = null;
        Statement stmnt = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            int rows = stmnt.executeUpdate("DELETE FROM " + username.toUpperCase() + "_PLANS WHERE PLAN_ID = " 
                    + planId);
            
            if (rows == 0) {
                System.out.println("Cannot delete plan (maybe the PlanId does not exist)!");
                return false;
            }

            result = true;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (stmnt != null) {
                try {
                    stmnt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    
                    result = false;
                }
            }
        }
        
        return result;
    }
}
