package src.workers.DataAcsessLayer;

public class HeadOfBranchDTO {
    private int ID;
    private int BranchID;
    private int LastDayForPrefs;


    public int getID() {
        return ID;
    }

    public int getBranchID() {
        return BranchID;
    }

    public int getLastDayForPrefs() {
        return LastDayForPrefs;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setBranchID(int BranchID) {
        this.BranchID = BranchID;
    }

    public void setLastDayForPrefs(int LastDayForPrefs) {
        this.LastDayForPrefs = LastDayForPrefs;
    }
}
