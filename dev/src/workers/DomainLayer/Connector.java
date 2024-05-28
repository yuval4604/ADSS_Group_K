package workers.DomainLayer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connector {
    private Worker _worker;
    private HeadOfHR head;
    private Map<Integer,String> loginInfos;

    public Connector(String password) {
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
    public boolean ResetVacactionDays(int id){
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
        String Gres = "Worker's info: \n" + "name" + _worker.getName() + "\n"
                + "Bank number:" + _worker.getBankNum() + "\n"
                + "Global wage:" + _worker.getGWage() + "\n"
                + "Date of start:" + _worker.getDateOfStart() + "\n"
                + "Total vacation days" + _worker.getTotalVacationDays() + "\n"
                + "Current vacation days:" + _worker.getCurrVacationDays();
        String Hres = "Worker's info: \n" + "name" + _worker.getName() + "\n"
                + "Bank number:" + _worker.getBankNum() + "\n"
                + "Hourly wage:" + _worker.getHWage() + "\n"
                + "Date of start:" + _worker.getDateOfStart() + "\n"
                + "Total vacation days" + _worker.getTotalVacationDays() + "\n"
                + "Current vacation days:" + _worker.getCurrVacationDays();
        String res = _worker.getFullTimeJob() ? Gres : Hres;
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
    public boolean setAlldayOff(String date,int dayOfWeek) {
        if(_worker.getIsHR()) {
            return head.setAlldayOff(date,dayOfWeek);
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

    public boolean addWorker(String name, int id, int bankNum, boolean fullTime, int globalWage, int hourlyWage, String dateOfStart, int totalVacationDays, int currentVacationDays, String password) {
        if(!head.contains(id)) {
            Worker worker = new Worker(name,id,bankNum,globalWage,hourlyWage,dateOfStart,fullTime,totalVacationDays,currentVacationDays);
            head.addWorker(worker);
            loginInfos.put(id,password);
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
}
