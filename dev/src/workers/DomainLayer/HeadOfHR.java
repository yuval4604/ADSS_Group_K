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
    public void setWorkerGlobal(int id,int wage) {
        allWorkers.get(id).setWage(wage);
    }
    public void setFullTime(int id,boolean full) {
        allWorkers.get(id).setFullTimeJob(full);
    }
    public void setVacation(int id, int days) {
        allWorkers.get(id).setVacationDays(days);
    }
    public void ResetVacationDays(int id) {
        allWorkers.get(id).ResetVacationDays();
    }
    public void setWorkerHourly(int id,int wage) {
        allWorkers.get(id).setHWage(wage);
    }
    
    public List<Worker>[] getAvailableWorkersOfRole(String role) { // list of size 2, first list - want , second list - can
        List<Worker> roleWorkerList = roleList.get(role);
        List<Worker>[] availableWorker = new List[2]; 
        availableWorker[0] = new LinkedList<>();
        availableWorker[1] = new LinkedList<>();
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
            Shift shift = new Shift(shiftManager,date,dayShift,dayOfWeek);
            allShifts.add(shift);
            currentShift = shift;
            return true;
        }
        return false;
    }
}
