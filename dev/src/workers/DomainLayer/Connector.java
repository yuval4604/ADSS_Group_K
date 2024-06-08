package workers.DomainLayer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Connector {
    private Worker _worker;
    private Map<Integer,String> loginInfos;

    private LocalDate _lastUpdate;

    public Connector(String password) {
        _lastUpdate = LocalDate.now();
        loginInfos = new HashMap<>();
        loginInfos.put(-1, password);
        Branch ABranch = new Branch("Admin", -1, "Admin", null);
        _worker = new HR(ABranch);
        ABranch.setBranchManager(_worker);
    }
    public boolean login(int id,String password) {
        if(loginInfos.containsKey(id) && loginInfos.get(id).equals(password)) {
            _worker = BranchManager.getWorker(id);
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
        if(_worker.getIsBM()) {
            return ((BranchManager)_worker).setWorkerGlobal(id,wage);
        }
        return false;
    }
    public boolean setHourlyWage(int id,int wage){
        if(_worker.getIsBM()) {
            return ((BranchManager)_worker).setWorkerHourly(id,wage);
        }
        return false;
    }
    public boolean setFullTimeJob(int id,boolean full){
        if(_worker.getIsBM()) {
            return ((BranchManager)_worker).setFullTime(id,full);
        }
        return false;
    }
    public boolean setVacationDays(int id, int days){
        if(_worker.getIsBM()) {
            return ((BranchManager)_worker).setVacation(id,days);
        }
        return false;
    }
    public boolean ResetVacationDays(int id){
        if(_worker.getIsBM()) {
            return ((BranchManager)_worker).ResetVacationDays(id);
        }
        return false;
    }
    public boolean useVacationDays(int days) {
        return _worker.useVacationDays(days);
    }

    public String getAvailableWorkersOfRole(String role) {
        String res = "";
        List<Worker>[] list = ((BranchManager)_worker).getAvailableWorkersOfRole(role);
        res += "For the role, " + role + " , the workers who want to work this shift are: \n";
        for(Worker worker : list[0]) {
            if (worker.inBranch(((BranchManager)_worker).getBranch().getName()))
                res += worker.getName() + ", " + worker.getID() + "\n";
        }
        res += "For the role, " + role + " , the workers who can work this shift are: \n";
        for(Worker worker : list[1]) {
            if (worker.inBranch(((BranchManager)_worker).getBranch().getName()))
                res += worker.getName() + ", " + worker.getID() + "\n";
        }
        return res;
    }
    public boolean addWorkerToShift(int id,String role) {
        if(!((BranchManager)_worker).hasRole(role)) {
            return false;
        }
        Worker worker = BranchManager.getWorker(id);
        if (!worker.inBranch(((BranchManager)_worker).getBranch().getName()))
            return false;
        return ((BranchManager)_worker).addWorkerToShift(worker,role);
    }
    public boolean selectShift(String date,boolean dayShift) {
        return ((BranchManager)_worker).selectShift(date,dayShift);
    }
    public boolean createShift(int id,String date,boolean dayShift,int dayOfWeek) {
        Worker shiftManager = BranchManager.getWorker(id);
        if (!shiftManager.inBranch(((BranchManager)_worker).getBranch().getName()))
            return false;
        return ((BranchManager)_worker).createShift(shiftManager,date,dayShift,dayOfWeek);
    }

    public String showWorkerInfo() {
        String GRes = "Worker's info: \n" + "name" + _worker.getName() + "\n"
                + "Bank number:" + _worker.getBankNum() + "\n"
                + "Global wage:" + _worker.getGWage() + "\n"
                + "Date of start:" + _worker.getDateOfStart() + "\n"
                + "Total vacation days:" + _worker.getTotalVacationDays() + "\n"
                + "Current vacation days:" + _worker.getCurrVacationDays() + "\n"
                + "Branches: " + _worker.getBranches();
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
        if(_worker.getIsBM())
            return ((BranchManager)_worker).setHalfDayShiftOff(date,dayShift,dayOfWeek);
        return false;
    }
    public boolean setAllDayOff(String date, int dayOfWeek) {
        if(_worker.getIsBM()) {
            return ((BranchManager)_worker).setAllDayOff(date,dayOfWeek);
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
        if(!((BranchManager)_worker).contains(id)) {
            Worker worker = new Worker(name,id,bankNum,globalWage,hourlyWage,dateOfStart,fullTime,totalVacationDays,currentVacationDays,false);
            ((BranchManager)_worker).addWorker(worker);
            loginInfos.put(id,name);
            return true;
        }

        return false;
    }

    public boolean addRole(int id, String role) {
        return ((BranchManager)_worker).addRole(id,role);
    }

    public boolean removeRole(int id, String role) {
        return ((BranchManager)_worker).removeRole(id,role);
    }

    public String getRoles() {
        String res = "";
        List<String> roles = BranchManager.getRoles(_worker);
        for(String role : roles) {
            res += role + ", ";
        }
        res = res.substring(0,res.length()-2);
        return res;
    }

    public boolean isInactive() {
        return ((BranchManager)_worker).isInactive();
    }

    public boolean getIsBM() {
        return _worker.getIsBM();
    }

    public boolean isFullTime(int id) {
        return _worker.getFullTimeJob();
    }

    public String showShift() {
        return ((BranchManager)_worker).showShift();
    }

    public void load() {
        login(-1,"admin");
        ((HR)_worker).addBranch("Tel-Aviv",1,"Tel-Aviv",new Worker("Benjamin",22,2,2,2,"2",true,2,2,false));
        logOut();
        login(22,"Benjamin");
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
        ((BranchManager)_worker).addWorker(new Worker("Alfred",1,1,1,1,"1",true,1,1,false));
        ((BranchManager)_worker).addWorker(new Worker("Benjamin",2,2,2,2,"2",true,2,2,false));
        ((BranchManager)_worker).addWorker(new Worker("Casey",3,3,3,3,"3",true,3,3,false));
        ((BranchManager)_worker).addWorker(new Worker("Daniel",4,4,4,4,"4",true,4,4,false));
        ((BranchManager)_worker).addWorker(new Worker("Emily",5,5,5,5,"5",true,5,5,false));
        ((BranchManager)_worker).addWorker(new Worker("Francis",6,6,6,6,"6",true,6,6,false));
        ((BranchManager)_worker).addWorker(new Worker("George",7,7,7,7,"7",true,7,7,false));
        ((BranchManager)_worker).addWorker(new Worker("Hanna",8,8,8,8,"8",true,8,8,false));
        ((BranchManager)_worker).addWorker(new Worker("Ian",9,9,9,9,"9",true,9,9,false));
        ((BranchManager)_worker).addWorker(new Worker("John",10,10,10,10,"10",true,10,10,false));
        ((BranchManager)_worker).addWorker(new Worker("Kelly",11,11,11,11,"11",true,11,11,false));
        ((BranchManager)_worker).addWorker(new Worker("Louis",12,12,12,12,"12",true,12,12,false));
        ((BranchManager)_worker).addWorker(new Worker("Margo",13,13,13,13,"13",true,13,13,false));
        ((BranchManager)_worker).addWorker(new Worker("Nathan",14,14,14,14,"14",true,14,14,false));
        ((BranchManager)_worker).addWorker(new Worker("Oliver",15,15,15,15,"15",true,15,15,false));
        ((BranchManager)_worker).addRole(1,"Cashier");
        ((BranchManager)_worker).addRole(2,"Cashier");
        ((BranchManager)_worker).addRole(3,"Delivery");
        ((BranchManager)_worker).addRole(4,"Cleaner");
        ((BranchManager)_worker).addRole(5,"Cleaner");
        ((BranchManager)_worker).addRole(6,"Quartermaster");
        ((BranchManager)_worker).addRole(7,"Quartermaster");
        ((BranchManager)_worker).addRole(8,"Packer");
        ((BranchManager)_worker).addRole(9,"Packer");
        ((BranchManager)_worker).addRole(10,"Delivery");
        ((BranchManager)_worker).addRole(11,"Shift-Manager");
        ((BranchManager)_worker).addRole(12,"Butcher");
        ((BranchManager)_worker).addRole(13,"Delly-Man");
        ((BranchManager)_worker).addRole(14, "Guard");
        ((BranchManager)_worker).addRole(15, "Guard");
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(1));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(2));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(3));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(4));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(5));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(6));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(7));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(8));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(9));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(10));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(11));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(12));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(13));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(14));
        ((BranchManager)_worker).addWorkerToBranch(BranchManager.getWorker(15));
        ((BranchManager)_worker).createShift(BranchManager.getWorker(11),"01.01.2021",false,1);
        ((BranchManager)_worker).setHalfDayShiftOff("01.01.2021",true,1);
        ((BranchManager)_worker).createShift(BranchManager.getWorker(11),"02.01.2021",true,2);
        ((BranchManager)_worker).setAllDayOff("03.01.2021",3);
        ((BranchManager)_worker).selectShift("01.01.2021",false);
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(1),"Cashier");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(2),"Cashier");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(3),"Delivery");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(4),"Cleaner");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(5),"Cleaner");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(6),"Quartermaster");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(7),"Quartermaster");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(8),"Packer");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(9),"Packer");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(10),"Delivery");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(12),"Butcher");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(13),"Delly-Man");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(14),"Guard");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(15),"Guard");
        ((BranchManager)_worker).selectShift("02.01.2021",true);
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(1),"Cashier");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(2),"Cashier");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(3),"Delivery");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(4),"Cleaner");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(5),"Cleaner");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(6),"Quartermaster");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(7),"Quartermaster");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(8),"Packer");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(9),"Packer");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(10),"Delivery");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(12),"Butcher");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(13),"Delly-Man");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(14),"Guard");
        ((BranchManager)_worker).addWorkerToShift(BranchManager.getWorker(15),"Guard");
    }

    public boolean removeWorkerFromShift(int id, String role) {
        return ((BranchManager)_worker).removeWorkerFromShift(id,role);
    }

    public Object getCons(int i, boolean b) {
        return _worker.getCons(i,b);
    }

    public String showWorkerShifts(String fromDate,String ToDate,int id) {
        List<Shift> Shifts = new LinkedList<>();
        LocalDate fromLocalDate = LocalDate.of(Integer.parseInt(fromDate.split(".")[2]), Integer.parseInt(fromDate.split(".")[1]), Integer.parseInt(fromDate.split(".")[0]));
        LocalDate toLocalDate = LocalDate.of(Integer.parseInt(ToDate.split(".")[2]), Integer.parseInt(ToDate.split(".")[1]), Integer.parseInt(ToDate.split(".")[0]));
        for (Shift shift : ((BranchManager)_worker).getAllShifts()) {
            if (shift.getLocalDate().isAfter(fromLocalDate) && shift.getLocalDate().isBefore(toLocalDate)) {
                if (shift.getWorkers().containsKey(id)) {
                    Shifts.add(shift);
                }
            }
        }
        String res = "";
        for (Shift shift : Shifts) {
            res += shift.getDate() + "," + (shift.getDayShift()?"day shift":"night shift") + "," + shift.getBranch().getName()+ "\n";
        }
        return res;
    }

        public boolean hasChangedPassword () {
            return _worker.hasChangedPassword();
        }

        public int lastDayToSetConstraints () {
            return ((BranchManager)_worker).lastDayToSetConstraints();
        }

        public boolean setLastDayForConstraints ( int day){
            return ((BranchManager)_worker).setLastDayForConstraints(day);
        }

        public void checkUpdateDay () {
            if (_lastUpdate.isBefore(LocalDate.now())) {
                _worker.checkUpdateDay();
                _lastUpdate = LocalDate.now();
            }
        }

        public boolean fireWorker( int id){

            if(_worker.getIsBM()) {
                ((HR)_worker).fireWorker(id);
                return true;
            }
            return false;
        }
        public boolean endContranct30DaysFromNow( int id){
            if(_worker.getIsBM()) {
                ((HR)_worker).endContranct30DaysFromNow(id);
                return true;
            }
            return false;
        }
        public boolean setMinimalWorkers(String role,int num){
            if(_worker.getIsBM()) {
                ((BranchManager)_worker).setMinimalAmount(role, num);
                return true;
            }
            return false;
        }
        public boolean checkIfRoleHasMinimalWorkers() {
            if(_worker.getIsBM()) {
                return ((BranchManager)_worker).checkIfRoleHasMinimalWorkers();
            }
            return false;
        }

        public boolean altarOnGoingShift (int id, String role,int bID){

            Shift shift = BranchManager.getOnGoingShift(bID);
            if(shift == null) {
                return false;
            }
            if(shift.getShiftManager().getID() == _worker.getID()) {
                shift.altarRole(id,role);
                return true;
            }
            return false;
        }

        public boolean addBranch(String name, int id, String address, int branchManagerID) {
            if(!_worker.getIsBM()) {
                return false;
            }
            BranchManager branchManager = (BranchManager)BranchManager.getWorker(branchManagerID);
            return ((HR)_worker).addBranch(name, id, address, branchManager);
        }

        public boolean removeBranch(int id) {
            if(!_worker.getIsBM()) {
                return false;
            }
            return ((HR)_worker).removeBranch(id);
        }

        public boolean addWorkerToBranch(int workerID) {
            if(!_worker.getIsBM()) {
                return false;
            }
            Worker worker = BranchManager.getWorker(workerID);
            ((BranchManager)_worker).addWorkerToBranch(worker);
            return true;
        }

        public boolean removeWorkerFromBranch(int workerID) {
            if(!_worker.getIsBM()) {
                return false;
            }
            Worker worker = BranchManager.getWorker(workerID);
            ((BranchManager)_worker).removeWorkerFromBranch(worker);
            return true;
        }

        public String showBranches () {
            if(!_worker.getIsBM()) {
                return "Error: no permission to do that";
            }
            return ((HR)_worker).showBranches();
        }

        public boolean setBranchManager ( int branchID, int ID){
            if(_worker.getID() != -1) {
                return false;
            }
            Worker BM = BranchManager.getWorker(ID);
            if(BM == null) {
                return false;
            }
            if(!_worker.getIsBM()) {
                return false;
            }
            return ((HR)_worker).setBranchManager(branchID, BM);
        }

        public String showBranch ( int branchID){
            if(!_worker.getIsBM()) {
                return "Error: no permission to do that";
            }
            return ((HR)_worker).showBranch(branchID);
        }

    public boolean getIsHR() {
        return _worker.getID() == -1;
    }

    public boolean joinBranch(String BranchName) {
        _worker.addOptionalBranches(BranchName);
        return true;
    }
}

