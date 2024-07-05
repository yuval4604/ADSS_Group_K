package workers.DomainLayer;
import workers.DataAcsessLayer.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Character.*;

public class Facade {
    private Worker _worker;
    private Map<Integer,String> loginInfos;

    private LocalDate _lastUpdate;

    public Facade() {
        _lastUpdate = LocalDate.now();
        loginInfos = new HashMap<>();
        String password = LoginDAO.getPassword(0);

        WorkerDAO.createWorkerTable();
        ShiftDAO.createShiftTable();
        BranchDAO.createBranchTable();
        WorkerDAO.createPrefsTable();
        HeadOfBranchDAO.createHeadOfBranchTable();
        HeadOfBranchDAO.createMinimalWorkersTable();
        HeadOfBranchDAO.createRoleListTable();
        LoginDAO.createTable();

        if(password != null) {
            loginInfos.put(0,password);
        }
        _worker = HeadOfBranch.getWorker(0);
    }
    public boolean login(int id,String password) {
        if(loginInfos.containsKey(id) && loginInfos.get(id).equals(password)) {
            _worker = HeadOfBranch.getWorker(id);
            return true;
        }
        if(LoginDAO.checkLogin(id,password)) {
            loginInfos.put(id,password);
            _worker = HeadOfBranch.getWorker(id);
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
            boolean ans = HeadOfBranchManager.setWorkerGlobal(id,wage);
            if(_worker.getID() == id)
                _worker = HeadOfBranch.getWorker(id);
            return ans;
        }
        return false;
    }
    public boolean setHourlyWage(int id,int wage){
        if(_worker.getIsBM()) {
            boolean ans = HeadOfBranchManager.setWorkerHourly(id,wage);
            if(_worker.getID() == id)
                _worker = HeadOfBranch.getWorker(id);
            return ans;
        }
        return false;
    }
    public boolean setFullTimeJob(int id,boolean full){
        if(_worker.getIsBM()) {
            boolean ans = HeadOfBranchManager.setFullTime(id,full);
            if(_worker.getID() == id)
                _worker = HeadOfBranch.getWorker(id);
            return ans;
        }
        return false;
    }
    public boolean setVacationDays(int id, int days){
        if(_worker.getIsBM()) {
            boolean ans = HeadOfBranchManager.setVacation(id,days);
            if(_worker.getID() == id)
                _worker = HeadOfBranch.getWorker(id);
            return ans;
        }
        return false;
    }
    public boolean ResetVacationDays(int id){
        if(_worker.getIsBM()) {
            boolean ans = HeadOfBranchManager.ResetVacationDays(id);
            if(_worker.getID() == id)
                _worker = HeadOfBranch.getWorker(id);
            return ans;
        }
        return false;
    }
    public boolean useVacationDays(int days) {
        return WorkerManager.useVacationDays(_worker,days);
    }

    public String getAvailableWorkersOfRole(String role) {
        String res = "";
        List<Worker>[] list = HeadOfBranchManager.getAvailableWorkersOfRole(((HeadOfBranch)_worker), role);
        res += "For the role, " + role + " , the workers who want to work this shift are: \n";
        for(Worker worker : list[0]) {
            if (WorkerManager.inBranch(worker,((HeadOfBranch)_worker).getBranchO().getID()))
                res += worker.getName() + ", " + worker.getID() + "\n";
        }
        res += "For the role, " + role + " , the workers who can work this shift are: \n";
        for(Worker worker : list[1]) {
            if (WorkerManager.inBranch(worker, ((HeadOfBranch)_worker).getBranchO().getID()))
                res += worker.getName() + ", " + worker.getID() + "\n";
        }
        return res;
    }
    public boolean addWorkerToShift(int id,String role) {
        if(!((HeadOfBranch)_worker).hasRole(role)) {
            return false;
        }
        Worker worker = HeadOfBranch.getWorker(id);
        if (!WorkerManager.inBranch(worker, ((HeadOfBranch)_worker).getBranchO().getID()))
            return false;
        return HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker),worker,role);
    }
    public boolean selectShift(String date,boolean dayShift) {
        return ((HeadOfBranch)_worker).selectShift(date,dayShift);
    }
    public boolean createShift(int id,String date,boolean dayShift,int dayOfWeek) {
        if(!_worker.getIsBM())
            return false;
        Worker shiftManager = HeadOfBranch.getWorker(id);
        if (!WorkerManager.inBranch(shiftManager, ((HeadOfBranch)_worker).getBranchO().getID()))
            return false;
        return HeadOfBranchManager.createShift(((HeadOfBranch)_worker), shiftManager,date,dayShift,dayOfWeek);
    }

    public String showWorkerInfo() {
        String GRes = "Worker's info: \n" + "name: " + _worker.getName() + "\n"
                + "Bank number: " + _worker.getBankNum() + "\n"
                + "Global wage: " + _worker.getGWage() + "\n"
                + "Date of start: " + _worker.getDateOfStart() + "\n"
                + "Total vacation days: " + _worker.getTotalVacationDays() + "\n"
                + "Current vacation days: " + _worker.getCurrVacationDays() + "\n"
                + "Branch: " + _worker.getBranch() + "\n"
                + "Licenses: " + WorkerManager.getWorkerLicense(_worker);
        String HRes = "Worker's info: \n" + "name: " + _worker.getName() + "\n"
                + "Bank number: " + _worker.getBankNum() + "\n"
                + "Hourly wage: " + _worker.getHWage() + "\n"
                + "Date of start: " + _worker.getDateOfStart() + "\n"
                + "Total vacation days: " + _worker.getTotalVacationDays() + "\n"
                + "Current vacation days: " + _worker.getCurrVacationDays() + "\n"
                + "Branch: " + _worker.getBranch() + "\n"
                + "Licenses: " + WorkerManager.getWorkerLicense(_worker);
        return _worker.getFullTimeJob() ? GRes : HRes;
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
            return HeadOfBranchManager.setHalfDayShiftOff(((HeadOfBranch)_worker),date,dayShift,dayOfWeek);
        return false;
    }
    public boolean setAllDayOff(String date, int dayOfWeek) {
        if(_worker.getIsBM()) {
            return HeadOfBranchManager.setAllDayOff(((HeadOfBranch)_worker),date,dayOfWeek);
        }
        return false;
    }
    public boolean changePassword(String oldPass,String newPass) {
        int id = _worker.getID();
        if(!isLegalPassword(newPass))
            return false;
        if(loginInfos.get(id).equals(oldPass)) {
            LoginDAO.updatePassword(id,newPass);
            HeadOfBranch.getWorker(id).setChangedPassword();
            loginInfos.remove(id,oldPass);
            loginInfos.put(id,newPass);
            return true;
        }
        return false;
    }

    private boolean isLegalPassword(String newPass) {
        boolean UCase = false;
        boolean LCase = false;
        boolean num = false;
        if(newPass.length() < 8)
            return false;
        List<Character> digits = new LinkedList<>();
        for (int i = 0; i < newPass.length(); i++) {

            if(!num && isDigit(newPass.charAt(i)))
                num = true;
            if(!LCase && isLowerCase(newPass.charAt(i)))
                LCase = true;
            if(!UCase && isUpperCase(newPass.charAt(i)))
                UCase = true;
        }
        boolean ans = UCase & LCase & num;
        return ans;
    }

    public boolean addConstraints(int day,boolean dayShift, Constraints cons) {
        boolean res = _worker.addConstraints(day,dayShift,cons);
        return res;
    }

    public boolean addWorker(String name, int id, int bankNum, boolean fullTime, int globalWage, int hourlyWage, String dateOfStart, int totalVacationDays, int currentVacationDays) {
        if(!((HeadOfBranch)_worker).contains(id)) {
            Worker worker = new Worker(name,id,bankNum,globalWage,hourlyWage,dateOfStart,fullTime,totalVacationDays,currentVacationDays,false);
            ((HR)_worker).addWorker(worker);
            loginInfos.put(id,name);
            LoginDAO.insertUser(id,name);
            return true;
        }

        return false;
    }

    public boolean addRole(int id, String role) {
        return HeadOfBranchManager.addRole(((HeadOfBranch)_worker), id,role);
    }

    public boolean removeRole(int id, String role) {
        return HeadOfBranchManager.removeRole(((HeadOfBranch)_worker), id,role);
    }

    public String getRoles() {
        String res = "";
        List<String> roles = HeadOfBranchManager.getRoles(_worker);
        for(String role : roles) {
            res += role + ", ";
        }
        if(res.length() > 2)
            res = res.substring(0,res.length()-2);
        return res;
    }

    public boolean isInactive() {
        return HeadOfBranchManager.isInactive(((HeadOfBranch)_worker));
    }

    public boolean getIsBM() {
        return _worker.getIsBM();
    }

    public boolean isFullTime(int id) {
        Worker worker = HeadOfBranch.getWorker(id);
        if(worker == null)
            return false;
        return worker.getFullTimeJob();
    }

    public String showShift() {
        return HeadOfBranchManager.showShift(((HeadOfBranch)_worker));
    }

    public boolean removeWorkerFromShift(int id, String role) {
        return HeadOfBranchManager.removeWorkerFromShift(((HeadOfBranch)_worker), id,role);
    }

    public Object getCons(int i, boolean b) {
        return _worker.getCons(i,b);
    }

    public String showWorkerShifts(String fromDate,String ToDate) {
        List<Shift> Shifts = new LinkedList<>();
        LocalDate fromLocalDate = LocalDate.of(Integer.parseInt(fromDate.substring(6,10)),Integer.parseInt(fromDate.substring(3,5)),Integer.parseInt(fromDate.substring(0,2)));
        LocalDate toLocalDate = LocalDate.of(Integer.parseInt(ToDate.substring(6,10)),Integer.parseInt(ToDate.substring(3,5)),Integer.parseInt(ToDate.substring(0,2)));
        for (Shift shift : HeadOfBranch.getAllShifts()) {
            if (shift.getLocalDate().isAfter(fromLocalDate) && shift.getLocalDate().isBefore(toLocalDate)) {
                if (ShiftManager.isWorkerInShift(shift,_worker.getID())){
                    Shifts.add(shift);
                }
            }
        }
        String res = "Shifts:\n";
        for (Shift shift : Shifts) {
            res += shift.getDate() + "," + (shift.getDayShift()?"day shift":"night shift") + "," + shift.getBranch().getName()+ "\n";
        }
        return res;
    }

        public boolean hasChangedPassword () {
            return _worker.getHasChangedPassword();
        }

        public int lastDayToSetConstraints () {
            if (_worker.getID() == 0)
                return ((HR)_worker).lastDayToSetConstraints();
            return ((HeadOfBranch)HR.getBranchO(_worker.getBranch()).getBM()).lastDayToSetConstraints();
        }

        public boolean setLastDayForConstraints ( int day){
            return HeadOfBranchManager.setLastDayForConstraints(((HeadOfBranch)_worker),day);
        }

        public void checkUpdateDay () {
            if (_lastUpdate.isBefore(LocalDate.now())) {
                if (_worker.getID() == 0)
                    ((HR)_worker).checkUpdateDay();
                else if (_worker.getIsBM())
                    HeadOfBranchManager.checkUpdateDay(_worker);
                else
                    WorkerManager.checkUpdateDay(_worker);
                _lastUpdate = LocalDate.now();
            }
        }

        public boolean fireWorker( int id){

            if(_worker.getID() == 0 && id != 0 && loginInfos.containsKey(id)) {
                ((HR)_worker).fireWorker(id);
                loginInfos.remove(id);
                LoginDAO.deleteUser(id);
                return true;
            }
            return false;
        }
        public boolean endContranct30DaysFromNow( int id){
            if(_worker.getID() == 0 && id != 0 && loginInfos.containsKey(id)) {
                ((HR)_worker).endContranct30DaysFromNow(id);
                return true;
            }
            return false;
        }
        public boolean setMinimalWorkers(String role,int num){
            if(_worker.getIsBM()) {
                HeadOfBranchManager.setMinimalAmount(((HeadOfBranch)_worker),role, num);
                return true;
            }
            return false;
        }
        public boolean checkIfRoleHasMinimalWorkers() {
            if(_worker.getIsBM()) {
                return HeadOfBranchManager.checkIfRoleHasMinimalWorkers(((HeadOfBranch)_worker));
            }
            return false;
        }

        public boolean altarOnGoingShift (int id, String role,int bID){

            Shift shift = HeadOfBranch.getOnGoingShift(bID);
            if(shift == null) {
                return false;
            }
            if(shift.getShiftManager().getID() == _worker.getID()) {
                ShiftManager.altarRole(shift,id,role);
                return true;
            }
            return false;
        }

        public boolean addBranch(String name, int id, String address, int headOfBranchID) {
            if(_worker.getID() != 0) {
                return false;
            }
            HeadOfBranch headOfBranch = (HeadOfBranch) HeadOfBranch.getWorker(headOfBranchID);
            return ((HR)_worker).addBranch(name, id, address, headOfBranch);
        }

        public boolean removeBranch(int id) {
            if(_worker.getID() != 0) {
                return false;
            }
            return ((HR)_worker).removeBranch(id);
        }

        public boolean addWorkerToBranch(int workerID) {
            if(!_worker.getIsBM()) {
                return false;
            }
            Worker worker = HeadOfBranch.getWorker(workerID);
            if(worker == null) {
                return false;
            }
            HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), worker);
            return true;
        }

        public boolean removeWorkerFromBranch(int workerID) {
            if(!_worker.getIsBM()) {
                return false;
            }
            Worker worker = HeadOfBranch.getWorker(workerID);
            HeadOfBranchManager.removeWorkerFromBranch(((HeadOfBranch)_worker), worker);
            return true;
        }

        public String showBranches () {
            if(_worker.getID() != 0) {
                return "Error: no permission to do that";
            }
            return ((HR)_worker).showBranches();
        }

        public boolean setHeadOfBranch(int branchID, int ID){
            if(_worker.getID() != 0) {
                return false;
            }
            Worker BM = HeadOfBranch.getWorker(ID);
            if(BM == null) {
                return false;
            }
            if(!BM.getIsBM()) {
                return false;
            }
            return ((HR)_worker).setHeadOfBranch(branchID, BM);
        }

        public String showBranch (){
            if(!_worker.getIsBM()) {
                return "Error: no permission to do that";
            }
            return HR.showBranch(_worker.getBranch());
        }

    public boolean getIsHR() {
        return _worker.getID() == 0;
    }

    public String getBranch() {
        return HR.getBranchO(_worker.getBranch()).getName();
    }

    public boolean createBM(String name, int id, int bankNum, boolean fullTime, int globalWage, int hourlyWage, String dateOfStart, int totalVacationDays, int currentVacationDays) {
        if(!((HeadOfBranch)_worker).contains(id)) {
            HeadOfBranch worker = new HeadOfBranch(name,id,bankNum,globalWage,hourlyWage,dateOfStart,fullTime,totalVacationDays,currentVacationDays,null);
            ((HR)_worker).addWorker(worker);
            loginInfos.put(id,name);
            return true;
        }

        return false;
    }

    public String getShiftManagers() {
        String res = "";
        List<Worker> workers = HeadOfBranch.getRole("Shift-Manager");
        for(Worker worker : workers) {
            res += worker.getName() + ", " + worker.getID() + "\n";
        }
        return res;
    }

    public boolean addLicense(String license) {
        int id = _worker.getID();
        if(license.equals("A"))
            return WorkerManager.addLicense(_worker,License.A);
        if(license.equals("B"))
            return WorkerManager.addLicense(_worker,License.B);
        if(license.equals("C"))
            return WorkerManager.addLicense(_worker,License.C);
        if(license.equals("D"))
            return WorkerManager.addLicense(_worker,License.D);
        return false;
    }

    public void deleteShift(String s, boolean b, int i) {
        HeadOfBranchManager.deleteShift(s,b,i);
    }
}

