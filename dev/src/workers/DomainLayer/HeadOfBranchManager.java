package workers.DomainLayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HeadOfBranchManager {
    public static boolean setWorkerGlobal(int id,int wage) {
        Worker worker = HeadOfBranch.getWorker(id);
        if(worker != null && !worker.getFullTimeJob())
            return false;
        worker.setWage(wage);
        return true;
    }

    public static boolean setFullTime(int id,boolean full) {
        Worker worker = HeadOfBranch.getWorker(id);
        if(worker == null || worker.getFullTimeJob() == full){
            return false;
        }
        worker.setFullTimeJob(full);
        return true;
    }

    public static boolean setVacation(int id, int days) {
        Worker worker = HeadOfBranch.getWorker(id);
        if(worker == null) {
            return false;
        }
        worker.setVacationDays(days);
        return true;
    }

    public static boolean ResetVacationDays(int id) {
        Worker worker = HeadOfBranch.getWorker(id);
        if(worker == null) {
            return false;
        }
        WorkerManager.ResetVacationDays(worker);
        return true;
    }

    public static boolean setWorkerHourly(int id,int wage) {
        Worker worker = HeadOfBranch.getWorker(id);
        if(worker == null || worker.getFullTimeJob())
            return false;
        worker.setHWage(wage);
        return true;
    }

    public static List<Worker>[] getAvailableWorkersOfRole(HeadOfBranch hb, String role) {
        List<Worker> roleWorkerList = HeadOfBranch.getRole(role);
        List<Worker>[] availableWorker = new List[2];
        availableWorker[0] = new LinkedList<>();
        availableWorker[1] = new LinkedList<>();
        if(roleWorkerList == null) {
            return availableWorker;
        }
        Shift currentShift = hb.getCurrentShift();
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

    public static boolean addWorkerToShift(HeadOfBranch hb, Worker worker,String role) {
        Shift currentShift = hb.getCurrentShift();
        if(currentShift != null && currentShift.getActive() &&  ShiftManager.notIn(currentShift,worker) && (worker.getCons(currentShift.getDayOfWeek(),currentShift.getDayShift()).equals(Constraints.can) || worker.getCons(currentShift.getDayOfWeek(),currentShift.getDayShift()).equals(Constraints.want))){
            currentShift.addWorker(worker,role);
            if(role.equals("Driver") && !hb.getMinimalWorkers().containsKey("Quartermaster")) {
                needQuartermaster(hb, true);
            }
            return true;
        }
        return false;
    }

    private static void needQuartermaster(HeadOfBranch hb, boolean need) {
        Shift currentShift = hb.getCurrentShift();
        currentShift.setNeedQuartermaster(need);
    }

    public static boolean createShift(HeadOfBranch hb, Worker shiftManager,String date,boolean dayShift,int dayOfWeek) {
        if(!hb.selectShift(date,dayShift)) {
            Shift shift = new Shift(shiftManager,date,dayShift,dayOfWeek,true,hb.getBranchO());
            hb.addShift(shift);
            if(hb.getMinimalWorkers().containsKey("Driver") && !hb.getMinimalWorkers().containsKey("Quartermaster")) {
                needQuartermaster(hb, true);
            }
            return true;
        }
        return false;
    }

    public static boolean setHalfDayShiftOff(HeadOfBranch hb, String date,boolean dayShift,int dayOfWeek) { // shift manager is null, active is set to false
        if(!hb.selectShift(date,dayShift)) {
            Shift shift = new Shift(null, date, dayShift, dayOfWeek, false, hb.getBranchO());
            hb.addShiftToList(shift);
            for (Map.Entry<Integer, Worker> entry : HeadOfBranch.getAllWorkers().entrySet()) { // for each worker, setting the same dayShift as inactive
                entry.getValue().addConstraints(dayOfWeek, dayShift, Constraints.inactive);
            }
            return true;
        }
        return false;
    }

    public static boolean addRole (HeadOfBranch hb, int id, String role){
        Worker worker = hb.getAllWorkers().get(id);
        Map<String, List<Worker>> roleList = HeadOfBranch.getRoleList();
        if (worker != null) {
            List<License> lList = worker.getLicenses();
            if (role.equals("Driver") && !lList.contains(License.C) && !lList.contains(License.D)) {
                return false;
            }
            if (roleList.containsKey(role)) {
                HeadOfBranch.addWorkerToRole(role, worker);
            } else {
                HeadOfBranch.newRole(role);
                HeadOfBranch.addWorkerToRole(role, worker);

            }
            return true;
        }
        return false;
    }

    public static boolean setAllDayOff(HeadOfBranch hb, String date, int dayOfWeek){
        return setHalfDayShiftOff(hb, date, true, dayOfWeek) && setHalfDayShiftOff(hb, date, false, dayOfWeek);
    }

    public static boolean removeRole (HeadOfBranch hb, int id, String role){
        Worker worker = HeadOfBranch.getAllWorkers().get(id);
        if (worker != null) {
            if (HeadOfBranch.getRoleList().containsKey(role)) {
                HeadOfBranch.removeWorkerFromRole(role, worker);
                return true;
            }
        }
        return false;
    }

    public static List<String> getRoles (Worker worker){
        List<String> roles = new LinkedList<>();
        for (Map.Entry<String, List<Worker>> entry : HeadOfBranch.getRoleList().entrySet()) {
            if (entry.getValue().contains(worker)) {
                roles.add(entry.getKey());
            }
        }
        return roles;
    }

    public static boolean isInactive(HeadOfBranch hb) {
        if (hb.getCurrentShift() == null) {
            return true;
        }
        return !hb.getCurrentShift().getActive();
    }

    public static String showShift(HeadOfBranch hb) {
        Shift currentShift = hb.getCurrentShift();
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

    public static boolean removeWorkerFromShift(HeadOfBranch hb, int id, String role) {
        Shift currentShift = hb.getCurrentShift();
        if (currentShift != null) {
            Worker worker = HeadOfBranch.getAllWorkers().get(id);
            if (worker != null) {
                Map<String,List<Worker>> workers = currentShift.getWorkers();
                List<Worker> workersList = workers.get(role);
                if(workersList.contains(worker)) {
                    currentShift.getWorkers().get(role).remove(worker);
                    currentShift.removeWorker(worker);
                }
                else
                    return false;
                if(role.equals("Driver") && !hb.getMinimalWorkers().containsKey("Driver") && !hb.getMinimalWorkers().containsKey("Quartermaster")) {
                    needQuartermaster(hb, false);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean setLastDayForConstraints(HeadOfBranch hb, int day) {
        if(day < 0 || day > 6)
            return false;
        hb.setLastDayToSetConstraints(day);
        return true;
    }

    public static void checkUpdateDay(Worker hb) {
        for (Map.Entry<Integer, Worker> entry : HeadOfBranch.getAllWorkers().entrySet()) {
            if(entry.getValue().getID() != hb.getID())
                WorkerManager.checkUpdateDay(entry.getValue());
        }
    }

    public static void setMinimalAmount(HeadOfBranch hb, String role, int amount) {
        Map<String,Integer> minimalWorkers = hb.getMinimalWorkers();
        if(minimalWorkers.containsKey(role)) {
            if(amount == 0) {
                minimalWorkers.remove(role);
                hb.removeAmount(role);
            }
            else {
                minimalWorkers.replace(role, amount);
                hb.replaceAmount(role, amount);
            }

        }
        else if(amount == 0)
            return;
        else if(amount > 0) {
            minimalWorkers.put(role, amount);
            hb.addAmount(role, amount);
        }
        if(role.equals("Driver") && amount > 0 && !minimalWorkers.containsKey("Quartermaster")) {
            setMinimalAmount(hb,"Quartermaster",1);
        }
    }

    public static boolean addWorkerToBranch(HeadOfBranch hb, Worker worker) {
        Branch branch = hb.getBranchO();
        if(branch.getWorkers().contains(worker))
            return false;
        branch.addWorker(worker);
        worker.setBranch(branch.getID());
        return true;
    }

    public static boolean removeWorkerFromBranch(HeadOfBranch hb, Worker worker) {
        Branch branch = hb.getBranchO();
        if(!branch.getWorkers().contains(worker))
            return false;
        branch.removeWorker(worker);
        return true;
    }

    public static boolean checkIfRoleHasMinimalWorkers(HeadOfBranch bm) {
        Shift currentShift = bm.getCurrentShift();
        if(currentShift.getNeedQuartermaster() && !currentShift.getWorkers().containsKey("Quartermaster"))
            return false;
        Map<String,Integer> minimalWorkers = bm.getMinimalWorkers();
        if(minimalWorkers.isEmpty())
            return true;
        if(currentShift.getWorkers().isEmpty())
            return false;
        for (Map.Entry<String,Integer> entry : minimalWorkers.entrySet()) {
            if(currentShift.getWorkers().containsKey(entry.getKey()) || currentShift.getWorkers().get(entry.getKey()).size() < entry.getValue())
                return false;
        }
        return true;
    }

    public static void deleteShift(String s, boolean b,int i) {
        ShiftManager.deleteShift(s,b,i);
    }
}
