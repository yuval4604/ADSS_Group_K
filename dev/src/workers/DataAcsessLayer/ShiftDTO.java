package src.workers.DataAcsessLayer;

import java.util.List;
import java.util.Map;

public class ShiftDTO {
    private boolean active;
    private int managerId;
    private String date; // dd-mm-yyyy

    private int dayOfWeek;
    private boolean dayShift; // true - morning / false - evening
    private Map<String, List<Integer>> workers; // map contains a role and a list of workers
    private int branchId;

    private boolean needQuartermaster;

    public boolean getActive() {
        return active;
    }
    public int getManagerId() {
        return managerId;
    }
    public String getDate() {
        return date;
    }
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    public boolean getDayShift() {
        return dayShift;
    }
    public Map<String, List<Integer>> getWorkers() {
        return workers;
    }
    public int getBranchId() {
        return branchId;
    }
    public boolean getNeedQM() {
        return needQuartermaster;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    public void setDayShift(boolean dayShift) {
        this.dayShift = dayShift;
    }
    public void setWorkers(Map<String, List<Integer>> workers) {
        this.workers = workers;
    }
    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }
    public void setNeedQM(boolean needQuartermaster) {
        this.needQuartermaster = needQuartermaster;
    }

}
