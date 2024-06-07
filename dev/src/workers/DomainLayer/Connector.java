package workers.DomainLayer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Connector {
    private Worker _worker;
    private HeadOfHR head;
    private Map<Integer,String> loginInfos;

    private LocalDate _lastUpdate;

    public Connector(String password) {
        _lastUpdate = LocalDate.now();
        loginInfos = new HashMap<>();
        head = new HeadOfHR();
        loginInfos.put(-1, password);
    }
    public boolean login(int id,String password) {
        if(loginInfos.containsKey(id) && loginInfos.get(id).equals(password)) {
            _worker = head.getWorker(id);
            return true;
        }
        return false;
    }
    public void logOut() {
        _worker = null;
    }
    public void setBank(int bank) {
        _worker.setBankNum(bank);
    }
    public boolean setGlobalWage(int id,int wage){
        if(_worker.getIsHR()) {
            return head.setWorkerGlobal(id,wage);
        }
        return false;
    }
    public boolean setHourlyWage(int id,int wage){
        if(_worker.getIsHR()) {
            return head.setWorkerHourly(id,wage);
        }
        return false;
    }
    public boolean setFullTimeJob(int id,boolean full){
        if(_worker.getIsHR()) {
            return head.setFullTime(id,full);
        }
        return false;
    }
    public boolean setVacationDays(int id, int days){
        if(_worker.getIsHR()) {
            return head.setVacation(id,days);
        }
        return false;
    }
    public boolean ResetVacationDays(int id){
        if(_worker.getIsHR()) {
            return head.ResetVacationDays(id);
        }
        return false;
    }
    public boolean useVacationDays(int days) {
        return _worker.useVacationDays(days);
    }

    public String getAvailableWorkersOfRole(String role) {
        String res = "";
        List<Worker>[] list = head.getAvailableWorkersOfRole(role);
        res += "For the role, " + role + " , the workers who want to work this shift are: \n";
        for(Worker worker : list[0]) {
            res += worker.getName() + ", " + worker.getID() + "\n";
        }
        res += "For the role, " + role + " , the workers who can work this shift are: \n";
        for(Worker worker : list[1]) {
            res += worker.getName() + ", " + worker.getID();
        }
        return res;
    }
    public boolean addWorkerToShift(int id,String role) {
        if(!head.hasRole(role)) {
            return false;
        }
        Worker worker = head.getWorker(id);
        return head.addWorkerToShift(worker,role);
    }
    public boolean selectShift(String date,boolean dayShift) {
        return head.selectShift(date,dayShift);
    }
    public boolean createShift(int id,String date,boolean dayShift,int dayOfWeek) {
        Worker shiftManager = head.getWorker(id);
        return head.createShift(shiftManager,date,dayShift,dayOfWeek);
    }

    public String showWorkerInfo() {
        String GRes = "Worker's info: \n" + "name" + _worker.getName() + "\n"
                + "Bank number:" + _worker.getBankNum() + "\n"
                + "Global wage:" + _worker.getGWage() + "\n"
                + "Date of start:" + _worker.getDateOfStart() + "\n"
                + "Total vacation days:" + _worker.getTotalVacationDays() + "\n"
                + "Current vacation days:" + _worker.getCurrVacationDays();
        String HRes = "Worker's info: \n" + "name" + _worker.getName() + "\n"
                + "Bank number:" + _worker.getBankNum() + "\n"
                + "Hourly wage:" + _worker.getHWage() + "\n"
                + "Date of start:" + _worker.getDateOfStart() + "\n"
                + "Total vacation days:" + _worker.getTotalVacationDays() + "\n"
                + "Current vacation days:" + _worker.getCurrVacationDays();
        String res = _worker.getFullTimeJob() ? GRes : HRes;
        return res;

    }

    public String showWorkerConstraints() {
        String morning = "||";
        String evening = "||";
        Constraints[][] cons = _worker.getCons();
        for (int i = 0; i < 6; i++) {
            morning += (cons[i][0]).toString() + "||";
            evening += (cons[i][1]).toString() + "||";
        }
        return morning + "\n" + evening;
    }

    public boolean setHalfDayShiftOff(String date,boolean dayShift,int dayOfWeek) {
        if(_worker.getIsHR())
            return head.setHalfDayShiftOff(date,dayShift,dayOfWeek);
        return false;
    }
    public boolean setAllDayOff(String date, int dayOfWeek) {
        if(_worker.getIsHR()) {
            return head.setAllDayOff(date,dayOfWeek);
        }
        return false;
    }
    public boolean changePassword(String oldPass,String newPass) {
        int id = _worker.getID();
        if(loginInfos.get(id).equals(oldPass)) {
            loginInfos.remove(id,oldPass);
            loginInfos.put(id,newPass);
            return true;
        }
        return false;
    }
    public boolean addConstraints(int day,boolean dayShift, Constraints cons) {
        boolean res = _worker.addConstraints(day,dayShift,cons);
        return res;
    }

    public boolean addWorker(String name, int id, int bankNum, boolean fullTime, int globalWage, int hourlyWage, String dateOfStart, int totalVacationDays, int currentVacationDays) {
        if(!head.contains(id)) {
            Worker worker = new Worker(name,id,bankNum,globalWage,hourlyWage,dateOfStart,fullTime,totalVacationDays,currentVacationDays);
            head.addWorker(worker);
            loginInfos.put(id,name);
            return true;
        }

        return false;
    }

    public boolean addRole(int id, String role) {
        return head.addRole(id,role);
    }

    public boolean removeRole(int id, String role) {
        return head.removeRole(id,role);
    }

    public String getRoles() {
        String res = "";
        List<String> roles = head.getRoles(_worker);
        for(String role : roles) {
            res += role + ", ";
        }
        res = res.substring(0,res.length()-2);
        return res;
    }

    public boolean isInactive() {
        return head.isInactive();
    }

    public boolean getIsHR() {
        return _worker.getIsHR();
    }

    public boolean isFullTime(int id) {
        return _worker.getFullTimeJob();
    }

    public String showShift() {
        return head.showShift();
    }

    public void load() {
        loginInfos.put(1,"1234");
        loginInfos.put(2,"1234");
        loginInfos.put(3,"1234");
        loginInfos.put(4,"1234");
        loginInfos.put(5,"1234");
        loginInfos.put(6,"1234");
        loginInfos.put(7,"1234");
        loginInfos.put(8,"1234");
        loginInfos.put(9,"1234");
        loginInfos.put(10,"1234");
        loginInfos.put(11,"1234");
        loginInfos.put(12,"1234");
        loginInfos.put(13,"1234");
        loginInfos.put(14,"1234");
        loginInfos.put(15,"1234");
        head.addWorker(new Worker("Alfred",1,1,1,1,"1",true,1,1));
        head.addWorker(new Worker("Benjamin",2,2,2,2,"2",true,2,2));
        head.addWorker(new Worker("Casey",3,3,3,3,"3",true,3,3));
        head.addWorker(new Worker("Daniel",4,4,4,4,"4",true,4,4));
        head.addWorker(new Worker("Emily",5,5,5,5,"5",true,5,5));
        head.addWorker(new Worker("Francis",6,6,6,6,"6",true,6,6));
        head.addWorker(new Worker("George",7,7,7,7,"7",true,7,7));
        head.addWorker(new Worker("Hanna",8,8,8,8,"8",true,8,8));
        head.addWorker(new Worker("Ian",9,9,9,9,"9",true,9,9));
        head.addWorker(new Worker("John",10,10,10,10,"10",true,10,10));
        head.addWorker(new Worker("Kelly",11,11,11,11,"11",true,11,11));
        head.addWorker(new Worker("Louis",12,12,12,12,"12",true,12,12));
        head.addWorker(new Worker("Margo",13,13,13,13,"13",true,13,13));
        head.addWorker(new Worker("Nathan",14,14,14,14,"14",true,14,14));
        head.addWorker(new Worker("Oliver",15,15,15,15,"15",true,15,15));
        head.addRole(1,"Cashier");
        head.addRole(2,"Cashier");
        head.addRole(3,"Delivery");
        head.addRole(4,"Cleaner");
        head.addRole(5,"Cleaner");
        head.addRole(6,"Quartermaster");
        head.addRole(7,"Quartermaster");
        head.addRole(8,"Packer");
        head.addRole(9,"Packer");
        head.addRole(10,"Delivery");
        head.addRole(11,"Shift-Manager");
        head.addRole(12,"Butcher");
        head.addRole(13,"Delly-Man");
        head.addRole(14, "Guard");
        head.addRole(15, "Guard");
        head.createShift(head.getWorker(11),"01.01.2021",false,1);
        head.setHalfDayShiftOff("01.01.2021",true,1);
        head.createShift(head.getWorker(11),"02.01.2021",true,2);
        head.setAllDayOff("03.01.2021",3);
        head.selectShift("01.01.2021",false);
        head.addWorkerToShift(head.getWorker(1),"Cashier");
        head.addWorkerToShift(head.getWorker(2),"Cashier");
        head.addWorkerToShift(head.getWorker(3),"Delivery");
        head.addWorkerToShift(head.getWorker(4),"Cleaner");
        head.addWorkerToShift(head.getWorker(5),"Cleaner");
        head.addWorkerToShift(head.getWorker(6),"Quartermaster");
        head.addWorkerToShift(head.getWorker(7),"Quartermaster");
        head.addWorkerToShift(head.getWorker(8),"Packer");
        head.addWorkerToShift(head.getWorker(9),"Packer");
        head.addWorkerToShift(head.getWorker(10),"Delivery");
        head.addWorkerToShift(head.getWorker(12),"Butcher");
        head.addWorkerToShift(head.getWorker(13),"Delly-Man");
        head.addWorkerToShift(head.getWorker(14),"Guard");
        head.addWorkerToShift(head.getWorker(15),"Guard");
        head.selectShift("02.01.2021",true);
        head.addWorkerToShift(head.getWorker(1),"Cashier");
        head.addWorkerToShift(head.getWorker(2),"Cashier");
        head.addWorkerToShift(head.getWorker(3),"Delivery");
        head.addWorkerToShift(head.getWorker(4),"Cleaner");
        head.addWorkerToShift(head.getWorker(5),"Cleaner");
        head.addWorkerToShift(head.getWorker(6),"Quartermaster");
        head.addWorkerToShift(head.getWorker(7),"Quartermaster");
        head.addWorkerToShift(head.getWorker(8),"Packer");
        head.addWorkerToShift(head.getWorker(9),"Packer");
        head.addWorkerToShift(head.getWorker(10),"Delivery");
        head.addWorkerToShift(head.getWorker(12),"Butcher");
        head.addWorkerToShift(head.getWorker(13),"Delly-Man");
        head.addWorkerToShift(head.getWorker(14),"Guard");
        head.addWorkerToShift(head.getWorker(15),"Guard");
    }

    public boolean removeWorkerFromShift(int id, String role) {
        return head.removeWorkerFromShift(id,role);
    }

    public Object getCons(int i, boolean b) {
        return _worker.getCons(i,b);
    }

    public String showWorkerShifts(String fromDate,String ToDate,int id) {
        List Shifts = new LinkedList<>();
        LocalDate fromLocalDate = LocalDate.of(Integer.parseInt(fromDate.split(".")[2]), Integer.parseInt(fromDate.split(".")[1]), Integer.parseInt(fromDate.split(".")[0]));
        LocalDate toLocalDate = LocalDate.of(Integer.parseInt(ToDate.split(".")[2]), Integer.parseInt(ToDate.split(".")[1]), Integer.parseInt(ToDate.split(".")[0]));
        for (Shift shift : head.getAllShifts()) {
            if (shift.getLocalDate().isAfter(fromLocalDate) && shift.getLocalDate().isBefore(toLocalDate)) {
                if (shift.getWorkers().containsKey(id)) {
                    Shifts.add(shift);
                }
            }
        }
        return Shifts.toString();
    }

        public boolean hasChangedPassword () {
            return _worker.hasChangedPassword();
        }

        public int lastDayToSetConstraints () {
            return head.lastDayToSetConstraints();
        }

        public boolean setLastDayForConstraints ( int day){
            return head.setLastDayForConstraints(day);
        }

        public void checkUpdateDay () {
            if (_lastUpdate.isBefore(LocalDate.now())) {
                head.checkUpdateDay();
                _lastUpdate = LocalDate.now();
            }
        }

        public boolean fireWorker( int id){

            if(_worker.getIsHR()) {
                head.fireWorker(id);
                return true;
            }
            return false;
        }
        public boolean endContranct30DaysFromNow( int id){
            if(_worker.getIsHR()) {
                head.endContranct30DaysFromNow(id);
                return true;
            }
            return false;
        }
        public boolean setMinimalWorkers(String role,int num){
            if(_worker.getIsHR()) {
                return head.setMinimalWorkers(role, num);
            }
            return false;
        }
        public boolean checkIfRoleHasMinimalWorkers() {
            if(_worker.getIsHR()) {
                return head.checkIfRoleHasMinimalWorkers();
            }
            return false;
        }

        public boolean altarOnGoingShift (int id, String role){
            Shift shift = head.getOnGoingShift();
            if(shift == null) {
                return false;
            }
            if(shift.getShiftManager().getID() == _worker.getID()) {
                shift.altarRole(id,role);
                return true;
            }
            return false;
        }
}

