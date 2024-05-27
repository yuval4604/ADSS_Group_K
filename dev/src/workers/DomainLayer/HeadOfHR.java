package workers.DomainLayer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HeadOfHR {
    private Worker worker;
    private Map<Integer,Worker> allWorkers;
    private List<Shift> allShifts;
    private Map<String,List<Worker>> roleList; // a list of qualified workers for each role
    private Shift currentShift; // the current shift that the HR is working on

    public HeadOfHR() {
        allWorkers = new HashMap<>();
        allShifts = new LinkedList<>();
        worker = new Worker("Admin",-1,0,0,0,"",true,0,0);
        allWorkers.put(-1,worker);
        currentShift = null;
    }

    public Worker getWorker(int id) {
        return allWorkers.get(id);
    }
    public boolean setWorkerGlobal(int id,int wage) {
        if(allWorkers.get(id) == null) {
            return false;
        }
        allWorkers.get(id).setWage(wage);
        return true;
    }
    public boolean setFullTime(int id,boolean full) {
        if(allWorkers.get(id) == null) {
            return false;
        }
        allWorkers.get(id).setFullTimeJob(full);
        return true;
    }
    public boolean setVacation(int id, int days) {
        if(allWorkers.get(id) == null) {
            return false;
        }
        allWorkers.get(id).setVacationDays(days);
        return true;
    }
    public boolean ResetVacationDays(int id) {
        if(allWorkers.get(id) == null) {
            return false;
        }
        allWorkers.get(id).ResetVacationDays();
        return true;
    }
    public boolean setWorkerHourly(int id,int wage) {
        if(allWorkers.get(id) == null) {
            return false;
        }
        allWorkers.get(id).setHWage(wage);
        return true;
    }
    
    public List<Worker>[] getAvailableWorkersOfRole(String role) { // list of size 2, first list - want , second list - can
        List<Worker> roleWorkerList = roleList.get(role);
        List<Worker>[] availableWorker = new List[2]; 
        availableWorker[0] = new LinkedList<>();
        availableWorker[1] = new LinkedList<>();
        if(roleWorkerList == null) {
            return availableWorker;
        }
        for(Worker worker : roleWorkerList) {
            Constraints con = worker.getCons(currentShift.getDayOfWeek(), currentShift.getDayShift());
            if(con.equals(Constraints.want)) {
                availableWorker[0].add(worker);
            }
            else if(con.equals(Constraints.can)){
                availableWorker[1].add(worker);
            }
            
        }
        return availableWorker;
    }
    public boolean addWorkerToShift(Worker worker,String role) {
        if(currentShift != null) {
            currentShift.addWorker(worker,role);
            return true;
        }
        return false;
    }
    
    public boolean selectShift(String date,boolean dayShift) {
        for (Shift shift : allShifts) {
            if(shift.getDayShift() == dayShift && shift.getDate().equals(date)) {
                currentShift = shift;
                return true;
            }
        }
        return false;
    }
    public boolean createShift(Worker shiftManager,String date,boolean dayShift,int dayOfWeek) {
        if(!selectShift(date,dayShift)) {
            Shift shift = new Shift(shiftManager,date,dayShift,dayOfWeek,true);
            allShifts.add(shift);
            currentShift = shift;
            return true;
        }
        return false;
    }


    public boolean setHalfDayShiftOff(String date,boolean dayShift,int dayOfWeek) { // shift manager is null, active is set to false
        if(!selectShift(date,dayShift)) {
            Shift shift = new Shift(null, date, dayShift, dayOfWeek, false);
            allShifts.add(shift);
            for (Map.Entry<Integer, Worker> entry : allWorkers.entrySet()) { // for each worker, setting the same dayshift as inactive
                entry.getValue().addConstraints(dayOfWeek, dayShift, Constraints.inactive);
            }
            return true;
        }
        return false;
    }
    public void addWorker (Worker worker){
        allWorkers.put(worker.getID(), worker);
    }

    public boolean addRole ( int id, String role){
        Worker worker = allWorkers.get(id);
        if (worker != null) {
            if (roleList.containsKey(role)) {
                roleList.get(role).add(worker);
            } else {
                roleList.put(role, new LinkedList<>());
                roleList.get(role).add(worker);

            }
            return true;
        }
        return false;
    }

    public boolean setAlldayOff (String date,int dayOfWeek){
        return setHalfDayShiftOff(date, true, dayOfWeek) && setHalfDayShiftOff(date, false, dayOfWeek);
    }


    public boolean removeRole ( int id, String role){
        Worker worker = allWorkers.get(id);
        if (worker != null) {
            if (roleList.containsKey(role)) {
                roleList.get(role).remove(worker);
                return true;
            }
        }
        return false;
    }

    public List<String> getRoles (Worker worker){
        List<String> roles = new LinkedList<>();
        for (Map.Entry<String, List<Worker>> entry : roleList.entrySet()) {
            if (entry.getValue().contains(worker)) {
                roles.add(entry.getKey());
            }
        }
        return roles;
    }
}