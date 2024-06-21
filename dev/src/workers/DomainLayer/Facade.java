package workers.DomainLayer;
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

    public Facade(String password) {
        _lastUpdate = LocalDate.now();
        loginInfos = new HashMap<>();
        loginInfos.put(-1, password);
        Branch ABranch = new Branch("Admin", -1, "Admin", null);
        _worker = new HR(ABranch);
        ABranch.setHeadOfBranch(_worker);
        HeadOfBranch.allWorkers.put(-1, _worker);
    }
    public boolean login(int id,String password) {
        if(loginInfos.containsKey(id) && loginInfos.get(id).equals(password)) {
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
            return HeadOfBranchManager.setWorkerGlobal(id,wage);
        }
        return false;
    }
    public boolean setHourlyWage(int id,int wage){
        if(_worker.getIsBM()) {
            return HeadOfBranchManager.setWorkerHourly(id,wage);
        }
        return false;
    }
    public boolean setFullTimeJob(int id,boolean full){
        if(_worker.getIsBM()) {
            return HeadOfBranchManager.setFullTime(id,full);
        }
        return false;
    }
    public boolean setVacationDays(int id, int days){
        if(_worker.getIsBM()) {
            return HeadOfBranchManager.setVacation(id,days);
        }
        return false;
    }
    public boolean ResetVacationDays(int id){
        if(_worker.getIsBM()) {
            return HeadOfBranchManager.ResetVacationDays(id);
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
            if (WorkerManager.inBranch(worker,((HeadOfBranch)_worker).getBranchO().getName()))
                res += worker.getName() + ", " + worker.getID() + "\n";
        }
        res += "For the role, " + role + " , the workers who can work this shift are: \n";
        for(Worker worker : list[1]) {
            if (WorkerManager.inBranch(worker, ((HeadOfBranch)_worker).getBranchO().getName()))
                res += worker.getName() + ", " + worker.getID() + "\n";
        }
        return res;
    }
    public boolean addWorkerToShift(int id,String role) {
        if(!((HeadOfBranch)_worker).hasRole(role)) {
            return false;
        }
        Worker worker = HeadOfBranch.getWorker(id);
        if (!WorkerManager.inBranch(worker, ((HeadOfBranch)_worker).getBranchO().getName()))
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
        if (!WorkerManager.inBranch(shiftManager, ((HeadOfBranch)_worker).getBranchO().getName()))
            return false;
        return HeadOfBranchManager.createShift(((HeadOfBranch)_worker), shiftManager,date,dayShift,dayOfWeek);
    }

    public String showWorkerInfo() {
        String GRes = "Worker's info: \n" + "name" + _worker.getName() + "\n"
                + "Bank number:" + _worker.getBankNum() + "\n"
                + "Global wage:" + _worker.getGWage() + "\n"
                + "Date of start:" + _worker.getDateOfStart() + "\n"
                + "Total vacation days:" + _worker.getTotalVacationDays() + "\n"
                + "Current vacation days:" + _worker.getCurrVacationDays() + "\n"
                + "Branch: " + _worker.getBranch();
        String HRes = "Worker's info: \n" + "name" + _worker.getName() + "\n"
                + "Bank number:" + _worker.getBankNum() + "\n"
                + "Hourly wage:" + _worker.getHWage() + "\n"
                + "Date of start:" + _worker.getDateOfStart() + "\n"
                + "Total vacation days:" + _worker.getTotalVacationDays() + "\n"
                + "Current vacation days:" + _worker.getCurrVacationDays() + "\n"
                + "Branch: " + _worker.getBranch();;
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
        return _worker.getFullTimeJob();
    }

    public String showShift() {
        return HeadOfBranchManager.showShift(((HeadOfBranch)_worker));
    }

    public void load() {
        login(-1,"admin");
        loginInfos.put(22,"Benjamin");
        ((HR)_worker).addBranch("Tel-Aviv",1,"Tel-Aviv",new HeadOfBranch("Benjamin",22,2,2,2,"02.02.2022",true,2,2,null));
        loginInfos.put(1,"Alfred");
        loginInfos.put(2,"Benjamin");
        loginInfos.put(3,"Casey");
        loginInfos.put(4,"Daniel");
        loginInfos.put(5,"Emily");
        loginInfos.put(6,"Francis");
        loginInfos.put(7,"George");
        loginInfos.put(8,"Hanna");
        loginInfos.put(9,"Ian");
        loginInfos.put(10,"Kelly");
        loginInfos.put(11,"Kelly");
        loginInfos.put(12,"Louis");
        loginInfos.put(13,"Margo");
        loginInfos.put(14,"Nathan");
        loginInfos.put(15,"Oliver");
        ((HR)_worker).addWorker(new Worker("Alfred",1,1,1,1,"02.02.2022",true,1,1,false));
        ((HR)_worker).addWorker(new Worker("Benjamin",2,2,2,2,"03.02.2022",true,2,2,false));
        ((HR)_worker).addWorker(new Worker("Casey",3,3,3,3,"04.02.2022",true,3,3,false));
        ((HR)_worker).addWorker(new Worker("Daniel",4,4,4,4,"05.02.2022",true,4,4,false));
        ((HR)_worker).addWorker(new Worker("Emily",5,5,5,5,"02.02.2022",true,5,5,false));
        ((HR)_worker).addWorker(new Worker("Francis",6,6,6,6,"02.02.2022",true,6,6,false));
        ((HR)_worker).addWorker(new Worker("George",7,7,7,7,"02.02.2022",true,7,7,false));
        ((HR)_worker).addWorker(new Worker("Hanna",8,8,8,8,"02.02.2022",true,8,8,false));
        ((HR)_worker).addWorker(new Worker("Ian",9,9,9,9,"02.02.2022",true,9,9,false));
        ((HR)_worker).addWorker(new Worker("John",10,10,10,10,"02.02.2022",true,10,10,false));
        ((HR)_worker).addWorker(new Worker("Kelly",11,11,11,11,"02.02.2022",true,11,11,false));
        ((HR)_worker).addWorker(new Worker("Louis",12,12,12,12,"02.02.2022",true,12,12,false));
        ((HR)_worker).addWorker(new Worker("Margo",13,13,13,13,"02.02.2022",true,13,13,false));
        ((HR)_worker).addWorker(new Worker("Nathan",14,14,14,14,"02.02.2022",true,14,14,false));
        ((HR)_worker).addWorker(new Worker("Oliver",15,15,15,15,"02.02.2022",true,15,15,false));
        logOut();
        login(22,"Benjamin");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 1,"Cashier");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 2,"Cashier");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 3,"Driver");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 4,"Cleaner");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 5,"Cleaner");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 6,"Quartermaster");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 7,"Quartermaster");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 8,"Packer");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 9,"Packer");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 10,"Driver");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 11,"Shift-Manager");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 12,"Butcher");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 13,"Delly-Man");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 14, "Guard");
        HeadOfBranchManager.addRole(((HeadOfBranch)_worker), 15, "Guard");
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(1));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(2));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(3));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(4));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(5));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(6));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(7));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(8));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(9));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(10));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(11));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(12));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(13));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(14));
        HeadOfBranchManager.addWorkerToBranch(((HeadOfBranch)_worker), HeadOfBranch.getWorker(15));
        int plus = 0;
        if(LocalDate.now().getDayOfWeek() == DayOfWeek.FRIDAY)
            plus++;
        if(LocalDate.now().getDayOfWeek() == DayOfWeek.THURSDAY)
            plus+=2;
        HeadOfBranchManager.createShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(11),(LocalDate.now().getDayOfMonth() + "." + LocalDate.now().getMonthValue() + "." + LocalDate.now().getYear()),false,LocalDate.now().getDayOfWeek().getValue());
        HeadOfBranchManager.createShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(11),(LocalDate.now().getDayOfMonth() + "." + LocalDate.now().getMonthValue() + "." + LocalDate.now().getYear()),true,LocalDate.now().getDayOfWeek().getValue());
        HeadOfBranchManager.createShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(11),(LocalDate.now().plusDays(plus + 1).getDayOfMonth() + "." + LocalDate.now().plusDays(plus + 1).getMonthValue() + "." + LocalDate.now().plusDays(plus + 1).getYear()),false,1);
        HeadOfBranchManager.setHalfDayShiftOff(((HeadOfBranch)_worker), (LocalDate.now().plusDays(plus + 1).getDayOfMonth() + "." + LocalDate.now().plusDays(plus + 1).getMonthValue() + "." + LocalDate.now().plusDays(plus + 1).getYear()),true,1);
        HeadOfBranchManager.createShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(11),(LocalDate.now().plusDays(plus + 2).getDayOfMonth() + "." + LocalDate.now().plusDays(plus + 2).getMonthValue() + "." + LocalDate.now().plusDays(plus + 2).getYear()),true,2);
        HeadOfBranchManager.setAllDayOff(((HeadOfBranch)_worker), (LocalDate.now().plusDays(plus + 2).getDayOfMonth() + "." + LocalDate.now().plusDays(plus + 2).getMonthValue() + "." + LocalDate.now().plusDays(plus + 2).getYear()),3);
        ((HeadOfBranch)_worker).selectShift((LocalDate.now().plusDays(plus + 1).getDayOfMonth() + "." + LocalDate.now().plusDays(plus + 1).getMonthValue() + "." + LocalDate.now().plusDays(plus + 1).getYear()),false);
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(1),"Cashier");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(2),"Cashier");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(3),"Driver");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(4),"Cleaner");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(5),"Cleaner");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(6),"Quartermaster");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(7),"Quartermaster");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(8),"Packer");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(9),"Packer");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(10),"Driver");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(12),"Butcher");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(13),"Delly-Man");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(14),"Guard");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(15),"Guard");
        ((HeadOfBranch)_worker).selectShift((LocalDate.now().plusDays(plus + 2).getDayOfMonth() + "." + LocalDate.now().plusDays(plus + 2).getMonthValue() + "." + LocalDate.now().plusDays(plus + 2).getYear()),true);
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(1),"Cashier");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(2),"Cashier");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(3),"Driver");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(4),"Cleaner");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(5),"Cleaner");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(6),"Quartermaster");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(7),"Quartermaster");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(8),"Packer");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(9),"Packer");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(10),"Driver");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(12),"Butcher");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(13),"Delly-Man");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(14),"Guard");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(15),"Guard");
        ((HeadOfBranch)_worker).selectShift((LocalDate.now().getDayOfMonth() + "." + LocalDate.now().getMonthValue() + "." + LocalDate.now().getYear()),true);
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(1),"Cashier");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(2),"Cashier");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(3),"Driver");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(4),"Cleaner");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(5),"Cleaner");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(6),"Quartermaster");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(7),"Quartermaster");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(8),"Packer");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(9),"Packer");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(10),"Driver");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(12),"Butcher");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(13),"Delly-Man");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(14),"Guard");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(15),"Guard");
        ((HeadOfBranch)_worker).selectShift((LocalDate.now().getDayOfMonth() + "." + LocalDate.now().getMonthValue() + "." + LocalDate.now().getYear()),false);
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(1),"Cashier");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(2),"Cashier");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(3),"Driver");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(4),"Cleaner");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(5),"Cleaner");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(6),"Quartermaster");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(7),"Quartermaster");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(8),"Packer");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(9),"Packer");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(10),"Driver");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(12),"Butcher");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(13),"Delly-Man");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(14),"Guard");
        HeadOfBranchManager.addWorkerToShift(((HeadOfBranch)_worker), HeadOfBranch.getWorker(15),"Guard");
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
            return ((HeadOfBranch)HR.getBranchO(_worker.getBranch()).getBM()).lastDayToSetConstraints();
        }

        public boolean setLastDayForConstraints ( int day){
            return HeadOfBranchManager.setLastDayForConstraints(((HeadOfBranch)_worker),day);
        }

        public void checkUpdateDay () {
            if (_lastUpdate.isBefore(LocalDate.now())) {
                if (_worker.getID() == -1)
                    ((HR)_worker).checkUpdateDay();
                else if (_worker.getIsBM())
                    HeadOfBranchManager.checkUpdateDay(_worker);
                else
                    WorkerManager.checkUpdateDay(_worker);
                _lastUpdate = LocalDate.now();
            }
        }

        public boolean fireWorker( int id){

            if(_worker.getID() == -1 && id != -1 && loginInfos.containsKey(id)) {
                ((HR)_worker).fireWorker(id);
                loginInfos.remove(id);
                return true;
            }
            return false;
        }
        public boolean endContranct30DaysFromNow( int id){
            if(_worker.getID() == -1 && id != -1 && loginInfos.containsKey(id)) {
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
                return ((HeadOfBranch)_worker).checkIfRoleHasMinimalWorkers();
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
            if(_worker.getID() != -1) {
                return false;
            }
            HeadOfBranch headOfBranch = (HeadOfBranch) HeadOfBranch.getWorker(headOfBranchID);
            return ((HR)_worker).addBranch(name, id, address, headOfBranch);
        }

        public boolean removeBranch(int id) {
            if(_worker.getID() != -1) {
                return false;
            }
            return ((HR)_worker).removeBranch(id);
        }

        public boolean addWorkerToBranch(int workerID) {
            if(!_worker.getIsBM()) {
                return false;
            }
            Worker worker = HeadOfBranch.getWorker(workerID);
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
            if(_worker.getID() != -1) {
                return "Error: no permission to do that";
            }
            return ((HR)_worker).showBranches();
        }

        public boolean setHeadOfBranch(int branchID, int ID){
            if(_worker.getID() != -1) {
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
        return _worker.getID() == -1;
    }

    public String getBranch() {
        return _worker.getBranch();
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
        List<Worker> workers = HeadOfBranch.roleList.get("Shift-Manager");
        for(Worker worker : workers) {
            res += worker.getName() + ", " + worker.getID() + "\n";
        }
        return res;
    }
}

