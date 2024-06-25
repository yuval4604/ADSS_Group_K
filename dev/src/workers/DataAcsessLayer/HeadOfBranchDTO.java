package workers.DataAcsessLayer;

public class HeadOfBranchDTO {
    private int ID;
    private int BranchID;
    private int LastDayForPrefs;

    public HeadOfBranchDTO(int ID, int branchID, int lastDayForPrefs) {
        this.ID = ID;
        BranchID = branchID;
        LastDayForPrefs = lastDayForPrefs;
    }

    public int getID() {
        return ID;
    }

    public int getBranchID() {
        return BranchID;
    }

    public int getLastDayForPrefs() {
        return LastDayForPrefs;
    }
}
