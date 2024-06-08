package workers.DomainLayer;
import java.time.LocalDate;
import java.util.*;

public class BranchManager extends Worker {
    private static Map<Integer,Worker> allWorkers;
    private List<Shift> allShifts;
    private static Map<String,List<Worker>> roleList; // a list of qualified workers for each role
    private Shift currentShift; // the current shift that the HR is working on
    private int _lastdaytoSetConstraints;

    private Map<LocalDate,List<Worker>> firedWorkers;

    private Branch _branch;

    public BranchManager(String name, int id, int bankNum, int globalWage, int hourlyWage, String dateOfStart, boolean fullTimeJob, int totalVacationDays, int currentVacationDays, Branch branch) {
        super(name, id, bankNum, globalWage, hourlyWage, dateOfStart, fullTimeJob, totalVacationDays, currentVacationDays);
        _lastdaytoSetConstraints = 6;
        allWorkers = new HashMap<>();
        allShifts = new LinkedList<>();
        allWorkers.put(-1,this);
        currentShift = null;
        roleList = new HashMap<>();
        roleList.put("Shift-Manager",new LinkedList<>());
        roleList.get("Shift-Manager").add(this);
        firedWorkers = new HashMap<>();
        _branch = branch;
    }

    public static Worker getWorker(int id) {
        if(!allWorkers.containsKey(id))
            return null;
        return allWorkers.get(id);
    }
    public boolean setWorkerGlobal(int id,int wage) {
        if(!allWorkers.containsKey(id)) {
            return false;
        }
        if(!allWorkers.get(id).getFullTimeJob())
            return false;
        allWorkers.get(id).setWage(wage);
        return true;
    }
    public boolean setFullTime(int id,boolean full) {
        if(!allWorkers.containsKey(id)) {
            return false;
        }
        allWorkers.get(id).setFullTimeJob(full);
        return true;
    }
    public boolean setVacation(int id, int days) {
        if(!allWorkers.containsKey(id)) {
            return false;
        }
        allWorkers.get(id).setVacationDays(days);
        return true;
    }
    public boolean ResetVacationDays(int id) {
        if(!allWorkers.containsKey(id)) {
            return false;
        }
        allWorkers.get(id).ResetVacationDays();
        return true;
    }
    public boolean setWorkerHourly(int id,int wage) {
        if(!allWorkers.containsKey(id)) {
            return false;
        }
        if(allWorkers.get(id).getFullTimeJob())
            return false;
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
        if(currentShift != null && currentShift.getActive() &&  currentShift.notIn(worker)){
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
            for (Map.Entry<Integer, Worker> entry : allWorkers.entrySet()) { // for each worker, setting the same dayShift as inactive
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

    public boolean setAllDayOff(String date, int dayOfWeek){
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

    public static List<String> getRoles (Worker worker){
        List<String> roles = new LinkedList<>();
        for (Map.Entry<String, List<Worker>> entry : roleList.entrySet()) {
            if (entry.getValue().contains(worker)) {
                roles.add(entry.getKey());
            }
        }
        return roles;
    }

    public boolean isInactive() {
        if (currentShift == null) {
            return true;
        }
        return !currentShift.getActive();
    }

    public boolean contains(int id) {
        return allWorkers.containsKey(id);
    }

    public boolean hasRole(String role) {
        return roleList.containsKey(role);
    }

    public String showShift() {
        if (currentShift == null) {
            return "No shift selected";
        }
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String res = "";
        res += "Shift Manager: " + currentShift.getShiftManager().getName() + "-" + currentShift.getShiftManager().getID() + "\n";
        res += "Date: " + currentShift.getDate() + "\n";
        res += "Day Shift: " + currentShift.getDayShift() + "\n";
        res += "Day of Week: " + days[currentShift.getDayOfWeek()] + "\n";
        res += "Active: " + currentShift.getActive() + "\n";
        res += "Workers: \n";
        for (Map.Entry<String, List<Worker>> entry : currentShift.getWorkers().entrySet()) {
            res += entry.getKey() + ": ";
            for (Worker worker : entry.getValue()) {
                res += worker.getName() + "-" + worker.getID() + ", ";
            }
            res = res.substring(0, res.length() - 2);
            res += "\n";
        }
        return res;
    }

    public boolean removeWorkerFromShift(int id, String role) {
        if (currentShift != null) {
            Worker worker = allWorkers.get(id);
            if (worker != null) {
                Map<String,List<Worker>> workers = currentShift.getWorkers();
                List<Worker> workersList = workers.get(role);
                if(workersList.contains(worker))
                    currentShift.getWorkers().get(role).remove(worker);
                else
                    return false;
                return true;
            }
        }
        return false;
    }
    public Shift getShiftFromCertainDay(LocalDate date, boolean dayShift) {
        for (Shift shift : allShifts) {
            if(shift.getDayShift() == dayShift && shift.getLocalDate().equals(date)) {
                return shift;
            }
        }
        return null;
    }
    public List<Shift> getAllShifts() {
        return allShifts;
    }

    public boolean fireWorker(int id) {
        if(allWorkers.containsKey(id)) {
            allWorkers.remove(id);
            return true;
        }
        for(Map.Entry<String,List<Worker>> entry : roleList.entrySet()) {
            if(entry.getValue().contains(allWorkers.get(id))) {
                entry.getValue().remove(allWorkers.get(id));
            }
        }
        return false;
    }

    public boolean endContranct30DaysFromNow(int id) {
        if (allWorkers.containsKey(id)) {
            Worker worker = allWorkers.get(id);
            LocalDate _30DaysFromNow = LocalDate.now().plusDays(30);
            if (!firedWorkers.containsKey(_30DaysFromNow)) {
                firedWorkers.put(_30DaysFromNow, new LinkedList<>());
                firedWorkers.get(_30DaysFromNow).add(worker);
                return true;
            } else {
                firedWorkers.get(_30DaysFromNow).add(worker);
                return true;
            }
        } else {
            return false;
        }
    }
    public int lastDayToSetConstraints() {
        return _lastdaytoSetConstraints;
    }

    public boolean setLastDayForConstraints(int day) {
        if(day < 0 || day > 6)
            return false;
        _lastdaytoSetConstraints = day;
        return true;
    }

    public void checkUpdateDay() {
        for (Map.Entry<Integer, Worker> entry : allWorkers.entrySet()) {
            entry.getValue().checkUpdateDay();
        }
        for(Map.Entry<LocalDate,List<Worker>> entry:firedWorkers.entrySet()) {
            if(entry.getKey().isBefore(LocalDate.now())||entry.getKey().isEqual(LocalDate.now()) ){
                for(Worker worker : entry.getValue()) {
                    fireWorker(worker.getID());
                }
                firedWorkers.remove(entry.getKey());
            }
        }

    }

    public Shift getOnGoingShift() {
        for (Shift shift : allShifts) {
            if(shift.getLocalDate() == LocalDate.now()) {
                return shift;
            }
        }
        return null;
    }
    public boolean setMinimalWorkers(String role,int num) {
        if(currentShift == null)
            return false;
        if(!currentShift.getMinimalWorkers().containsKey(role)) {
            currentShift.getMinimalWorkers().put(role, num);
            return true;
        }
        else {
            currentShift.getMinimalWorkers().replace(role,num);
            return true;
        }
    }
    public boolean checkIfRoleHasMinimalWorkers() {
        Map<String,Integer> minimal = currentShift.getMinimalWorkers();
        for (Map.Entry<String,Integer> entry : minimal.entrySet()) {
            if(currentShift.getWorkers().get(entry.getKey()).size() < entry.getValue())
                return false;
        }
        return true;
    }

    public boolean addWorkerToBranch(Worker worker) {
        if(_branch.getWorkers().contains(worker))
            return false;
        _branch.addWorker(worker);
        return true;
    }

    public boolean removeWorkerFromBranch(Worker worker) {
        if(!_branch.getWorkers().contains(worker))
            return false;
        _branch.removeWorker(worker);
        return true;
    }
}