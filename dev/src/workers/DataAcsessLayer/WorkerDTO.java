package workers.DataAcsessLayer;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

public class WorkerDTO {
    private int id;
    private String name;
    private int bankAccount;
    private int HWage;
    private int GWage;
    private LocalDate startDate;
    private boolean FTime;
    private int TVDays;
    private int CVDays;
    private String branchName;
    private boolean isHeadOfBranch;
    private boolean change;
    private String[][] pref;
    private List<String> licenses;
    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getBankAccount() {
        return bankAccount;
    }
    public int getHWage() {
        return HWage;
    }
    public int getGWage() {
        return GWage;
    }
    public String getStartDate() {
        return startDate.getDayOfMonth() + "/" + startDate.getMonthValue() + "/" + startDate.getYear();
    }
    public boolean getFTime() {
        return FTime;
    }
    public int getTVDays() {
        return TVDays;
    }
    public int getCVDays() {
        return CVDays;
    }
    public String getBranchName() {
        return branchName;
    }
    public boolean getIsHeadOfBranch() {
        return isHeadOfBranch;
    }
    public boolean getChange() {
        return change;
    }
    public List<String> getLicenses() {
        return licenses;
    }
    public void setID(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setBankAccount(int bankAccount) {
        this.bankAccount = bankAccount;
    }
    public void setHWage(int HWage) {
        this.HWage = HWage;
    }
    public void setGWage(int GWage) {
        this.GWage = GWage;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setFTime(boolean FTime) {
        this.FTime = FTime;
    }
    public void setTVDays(int TVDays) {
        this.TVDays = TVDays;
    }
    public void setCVDays(int CVDays) {
        this.CVDays = CVDays;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    public void setIsHeadOfBranch(boolean isHeadOfBranch) {
        this.isHeadOfBranch = isHeadOfBranch;
    }
    public void setChange(boolean change) {
        this.change = change;
    }
    public void setLicenses(List<String> licenses) {
        this.licenses = licenses;
    }



    public String[][] getPref() {
        return pref;
    }

    public void setPref(String[][] pref) {
        this.pref = pref;
    }

}
