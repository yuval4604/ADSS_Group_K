package workers.DomainLayer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Connector {
    private Worker Worker;
    private HeadOfHR head;
    private Map<Integer,String> loginInfos;

    public Connector(String password) {
        loginInfos = new HashMap<>();
        head = new HeadOfHR();
        loginInfos.put(-1, password);


    }
    public boolean login(int id,String password) {
        if(loginInfos.get(id).equals(password)) {
            Worker = head.getWorker(id);
            return true;
        }
        return false;
    }
    public void logOut() {
        Worker = null;
    }
    public void setBank(int bank) {
        Worker.setBankNum(bank);
    }
    public boolean setGlobalWage(int id,int wage){
        if(Worker.getIsHR()) {
            head.setWorkerGlobal(id,wage);
            return true;
        }
        return false;
    }
    public boolean setHourlyWage(int id,int wage){
        if(Worker.getIsHR()) {
            head.setWorkerHourly(id,wage);
            return true;
        }
        return false;
    }
    public boolean setFullTimeJob(int id,boolean full){
        if(Worker.getIsHR()) {
            head.setFullTime(id,full);
            return true;
        }
        return false;
    }
    public boolean setVacationDays(int id, int days){
        if(Worker.getIsHR()) {
            head.setVacation(id,days);
            return true;
        }
        return false;
    }
    public boolean ResetVacactionDays(int id){
        if(Worker.getIsHR()) {
            head.ResetVacationDays(id);
            return true;
        }
        return false;
    }
    public boolean useVacationDays(int days) {
        return Worker.useVacationDays(days);
    }

    public String getAvailableWorkersOfRole(String role) {
        String res = "";
        List<Worker>[] list = head.getAvailableWorkersOfRole(role);
        res += "For the role, " + role + " , the workers who want to work this shift are: ";
        for(Worker worker : list[0]) {
            res += worker.getName() + ", " + worker.getID();
        }
        res += "For the role, " + role + " , the workers who can work this shift are: ";
        for(Worker worker : list[1]) {
            res += worker.getName() + ", " + worker.getID();
        }
        return res;
    }
    public boolean addWorkerToShift(int id,String role) {
        Worker worker = head.getWorker(id);
        return head.addWorkerToShift(worker,role);
    }
    public boolean selectShift(String date,boolean dayShift) {
        return head.selectShift(date,dayShift);
    }
    public boolean createShift(int id,String date,boolean dayShift,int dayOfWeek,boolean active) {
        Worker shiftManager = head.getWorker(id);
        return head.createShift(shiftManager,date,dayShift,dayOfWeek,active);
    }

    public String showWorkerInfo(int id) {
        Worker worker = head.getWorker(id);
        String Gres = "Worker's info: \n" + "name" + worker.getName() + "\n"
                + "Bank number:" + worker.getBankNum() + "\n"
                + "Global wage:" + worker.getGWage() + "\n"
                + "Date of start:" + worker.getDateOfStart() + "\n"
                + "Total vacation days" + worker.getTotalVacationDays() + "\n"
                + "Current vacation days:" + worker.getCurrVacationDays();
        String Hres = "Worker's info: \n" + "name" + worker.getName() + "\n"
                + "Bank number:" + worker.getBankNum() + "\n"
                + "Hourly wage:" + worker.getHWage() + "\n"
                + "Date of start:" + worker.getDateOfStart() + "\n"
                + "Total vacation days" + worker.getTotalVacationDays() + "\n"
                + "Current vacation days:" + worker.getCurrVacationDays();
        String res = worker.getFullTimeJob() ? Gres : Hres;
        return res;

    }

    public String showWorkerConstraints(int id) {
        Worker worker = head.getWorker(id);
        String morning = "||";
        String evening = "||";
        Constraints[][] cons = worker.getCons();
        for (int i = 0; i < 6; i++) {
            morning = morning + (cons[0][i]).toString() + "||";
            evening = evening + (cons[1][i]).toString() + "||";
        }
        return morning + "\n" + evening;
    }

    public boolean setHalfDayShiftOff(String date,boolean dayShift,int dayOfWeek) {
        if(Worker.getIsHR())
            return head.setHalfDayShiftOff(date,dayShift,dayOfWeek);
        return false;
    }
    public boolean setAlldayOff(String date,int dayOfWeek) {
        if(Worker.getIsHR()) {
            return head.setAlldayOff(date,dayOfWeek);
        }
        return false;
    }
    public boolean changePassword(int id,String oldPass,String newPass) {
        Worker worker = head.getWorker(id);
        if(loginInfos.get(id).equals(oldPass) & worker.equals(Worker)) {
            loginInfos.remove(id,oldPass);
            loginInfos.put(id,newPass);
            return true;
        }
        return false;
    }
    public boolean addConstraints(int day,boolean dayShift, Constraints cons) {
        boolean res = Worker.addConstraints(day,dayShift,cons);
        return res;
    }

}
