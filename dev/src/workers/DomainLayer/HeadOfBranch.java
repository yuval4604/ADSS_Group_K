package src.workers.DomainLayer;
import src.workers.DataAcsessLayer.*;

import java.time.LocalDate;
import java.util.*;

public class HeadOfBranch extends Worker {

    private Map<String,Integer> minimalWorkers;
    protected static Map<Integer,Worker> allWorkers;
    protected static List<Shift> allShifts;
    protected static Map<String,List<Worker>> roleList; // a list of qualified workers for each role
    private Shift currentShift; // the current shift that the HR is working on
    private int _lastdaytoSetConstraints;

    protected Branch _branch;

    public HeadOfBranch(String name, int id, int bankNum, int globalWage, int hourlyWage, String dateOfStart, boolean fullTimeJob, int totalVacationDays, int currentVacationDays, Branch branch) {
        super(name, id, bankNum, globalWage, hourlyWage, dateOfStart, fullTimeJob, totalVacationDays, currentVacationDays, true);
        if(id==0) {
            allWorkers = new HashMap<>();
            allShifts = new LinkedList<>();
            roleList = new HashMap<>();
        }
        _lastdaytoSetConstraints = 7;
        allWorkers.put(id,this);
        currentShift = null;
        roleList.put("Shift-Manager",new LinkedList<>());
        roleList.get("Shift-Manager").add(this);
        if(id != 0)
            HeadOfBranchDAO.addRoleList("Shift-Manager",id);
        _branch = branch;
        minimalWorkers = new HashMap<>();
            HeadOfBranchDTO hdto = new HeadOfBranchDTO();
            hdto.setID(id);
            if(branch != null)
                hdto.setBranchID(branch.getID());
            else
                hdto.setBranchID(-1);
            hdto.setLastDayForPrefs(_lastdaytoSetConstraints);
            HeadOfBranchDAO.addHeadOfBranch(hdto);

    }

    public HeadOfBranch(WorkerDTO w, HeadOfBranchDTO h) {
        super(w);
        if(w.getID()==0) {
            allWorkers = new HashMap<>();
            allShifts = new LinkedList<>();
            roleList = new HashMap<>();
        }
        _lastdaytoSetConstraints = h.getLastDayForPrefs();
        allWorkers.put(w.getID(),this);
        currentShift = null;
        _branch = Branch.getBranch(h.getBranchID(),this);
        minimalWorkers = HeadOfBranchDAO.getMinimalWorkers(w.getID());
        List<String> l = HeadOfBranchDAO.getRoleListRole(w.getID());
        for (String role : l) {
            if(!roleList.containsKey(role)) {
                roleList.put(role,new LinkedList<>());
            }
            if(!roleList.get(role).contains(this))
                roleList.get(role).add(this);
        }
    }

    public static Worker getWorker(int id) {
        if(!HR.getLoaded() || !allWorkers.containsKey(id)) {
            try {
                Worker worker;
                WorkerDTO w = WorkerDAO.getWorker(id);
                HeadOfBranchDTO h = HeadOfBranchDAO.getHeadOfBranch(id);
                List<String> l = HeadOfBranchDAO.getRoleListRole(id);
                if(id == 0)
                    worker = new HR(w,h);
                else if(h == null)
                    worker = new Worker(w);
                else
                    worker = new HeadOfBranch(w,h);
                for (String role : l) {
                    if(!roleList.containsKey(role)) {
                        roleList.put(role,new LinkedList<>());
                    }
                    roleList.get(role).add(new Worker(w));
                }
                if(worker != null) {
                    allWorkers.put(id,worker);
                    return worker;
                }
            } catch (Exception e) {
                return null;
            }
        }
        return allWorkers.get(id);
    }
    
    public static List<Worker> getRole(String role) { // list of size 2, first list - want , second list - can
        List<Integer> l = HeadOfBranchDAO.getRoleListWorkerID(role);
        for (Integer id : l) {
            if(!allWorkers.containsKey(id)) {
                Worker worker = getWorker(id);
                if(worker != null) {
                    allWorkers.put(id,worker);
                }
            }
            if(!roleList.get(role).contains(allWorkers.get(id)))
                roleList.get(role).add(allWorkers.get(id));
        }
        return roleList.get(role);
    }

    public static Map<Integer, Worker> getAllWorkers() {
        return allWorkers;
    }

    public static void newRole(String role) {
        roleList.put(role, new LinkedList<>());
    }

    public static Map<String, List<Worker>> getRoleList() {
        return roleList;
    }

    public static void removeWorkerFromRole(String role, Worker worker) {
        roleList.get(role).remove(worker);
        HeadOfBranchDAO.deleteRoleList(role,worker.getID());
    }

    public static Shift getShift(String shift, Branch branch) {
        String[] s = shift.split(",");
        for (Shift sh : allShifts) {
            if(sh.getDate().equals(s[0]) && sh.getDayShift() == Boolean.parseBoolean(s[1]) && sh.getBranch().getID() == Integer.parseInt(s[2])){
                return sh;
            }
        }
        ShiftDTO sdto = HeadOfBranchDAO.getShift(s[0],Boolean.parseBoolean(s[1]),Integer.parseInt(s[2]));
        if(sdto.getDate() == null)
            return null;
        Shift sh = new Shift(sdto,branch);
        allShifts.add(sh);
        return sh;
    }

    public void removeAmount(String role) {
        HeadOfBranchDAO.deleteMinimalWorkers(this.branch, role);
    }

    public Shift getCurrentShift() {
        return currentShift;
    }
    
    public boolean selectShift(String date,boolean dayShift) {
        for (Shift shift : allShifts) {
            if(shift.getDayShift() == dayShift && shift.getDate().equals(date)) {
                currentShift = shift;
                return true;
            }
        }
        ShiftDTO sdto = HeadOfBranchDAO.getShift(date,dayShift,_branch.getID());
        if(sdto.getDate() == null)
            return false;
        currentShift = new Shift(HeadOfBranchDAO.getShift(date,dayShift,_branch.getID()),_branch);
        return true;
    }

    public void addShiftToList(Shift shift) {
        allShifts.add(shift);
    }

    public boolean contains(int id) {
        boolean cont =  allWorkers.containsKey(id);
        if(cont)
            return true;
        try {
            boolean b = WorkerDAO.getWorker(id) != null;
            return b;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasRole(String role) {
        return roleList.containsKey(role);
    }

    public static List<Shift> getAllShifts() {
        return allShifts;
    }

    public int lastDayToSetConstraints() {
        return _lastdaytoSetConstraints;
    }

    public static Shift getOnGoingShift(int bID) {
        for (Shift shift : allShifts) {
            if(shift.getLocalDate().isEqual(LocalDate.now()) && shift.getBranch().getID() == bID) {
                return shift;
            }
        }
        return null;
    }

    public Map<String, Integer> getMinimalWorkers() {
        return minimalWorkers;
    }

    public Branch getBranchO() {
        return _branch;
    }


    protected void setBranch(Branch branch) {
        _branch = branch;
        this.branch = branch.getID();
        HeadOfBranchDTO hdto = new HeadOfBranchDTO();
        hdto.setID(this.getID());
        hdto.setBranchID(branch.getID());
        HeadOfBranchDAO.updateHeadOfBranchBID(hdto);
    }

    public Map<String, Integer> getMinimalWorkersForShift() {
        return minimalWorkers;
    }

    public void takeOffBranch() {
        _branch = null;
        this.branch = -1;
        HeadOfBranchDTO hdto = new HeadOfBranchDTO();
        hdto.setID(this.getID());
        hdto.setBranchID(-1);
        HeadOfBranchDAO.updateHeadOfBranchBID(hdto);
    }

    public void addShift(Shift shift) {
        _branch.addShift(shift);
        allShifts.add(shift);
        currentShift = shift;
    }

    public static void addWorkerToRole(String role, Worker worker) {
        roleList.get(role).add(worker);
        HeadOfBranchDAO.addRoleList(role,worker.getID());
    }

    public void setLastDayToSetConstraints(int day) {
        _lastdaytoSetConstraints = day;
        HeadOfBranchDTO hdto = new HeadOfBranchDTO();
        hdto.setID(this.getID());
        hdto.setLastDayForPrefs(day);
        HeadOfBranchDAO.updateHeadOfBranchLDP(hdto);
    }

    public void replaceAmount(String role, int amount) {
        HeadOfBranchDAO.updateMinimalWorkers(this.branch,role,amount);
    }

    public void addAmount(String role, int amount) {
        HeadOfBranchDAO.addMinimalWorkers(this.branch,role,amount);
    }

    protected void deleteHeadOfBranch() {
        HeadOfBranchDAO.deleteHeadOfBranch(this.getID());
    }
}