package workers.DomainLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HR extends HeadOfBranch {
    private static Map<Integer,Branch> _branches;
    private Map<LocalDate,List<Worker>> firedWorkers;

    public HR(Branch ABranch) {

        super("Admin",-1,0,0,0,"",true,0,0,ABranch);

        _branches = new HashMap<>();
        _branches.put(-1,ABranch);
        firedWorkers = new HashMap<>();

    }

    public void addWorker (Worker worker){
        allWorkers.put(worker.getID(), worker);
    }

    public boolean addBranch(String name, int id, String address, Worker headOfBranch) {
        Branch branch = new Branch(name, id, address, headOfBranch);
        ((HeadOfBranch)headOfBranch).setBranch(branch);
        _branches.put(id,branch);
        return true;
    }

    public boolean removeBranch(int id) {
        if(_branches.containsKey(id)) {
            _branches.remove(id);
            return true;
        }
        return false;
    }

    public String showBranches() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Branch> entry : _branches.entrySet()) {
            sb.append("Branch ID: ").append(entry.getKey()).append(" Branch Name: ").append(entry.getValue().getName()).append("\n");
        }
        return sb.toString();
    }

    public boolean setHeadOfBranch(int branchID, Worker headOfBranch) {
        if(_branches.containsKey(branchID)) {
            _branches.get(branchID).setHeadOfBranch(headOfBranch);
            return true;
        }
        return false;
    }

    public static String showBranch(String branchName) {
        int id = -1000000000;
        for (Map.Entry<Integer, Branch> entry : _branches.entrySet()) {
            if(entry.getValue().getName().equals(branchName)) {
                id =  entry.getValue().getID();
            }
        }
        if(_branches.containsKey(id)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Branch Name: ").append(_branches.get(id).getName()).append("\n");
            sb.append("Branch ID: ").append(_branches.get(id).getID()).append("\n");
            sb.append("Branch Address: ").append(_branches.get(id).getAddress()).append("\n");
            sb.append("Branch Manager ID: ").append(_branches.get(id).getHeadOfBranch().getID()).append(" Branch Manager Name: ").append(_branches.get(id).getHeadOfBranch().getName()).append("\n");
            sb.append("Workers: \n");
            for (Worker worker : _branches.get(id).getWorkers()) {
                sb.append("- Worker ID: ").append(worker.getID()).append(" Worker Name: ").append(worker.getName()).append("\n");
            }
            sb.append("Roles: \n");
            for (Map.Entry<String, List<Worker>> entry : roleList.entrySet()) {
                sb.append("Role: ").append(entry.getKey()).append("\n");
                for (Worker worker : entry.getValue()) {
                    sb.append("- Worker ID: ").append(worker.getID()).append(" Worker Name: ").append(worker.getName()).append("\n");
                }
            }
            sb.append("minimal workers for shift: \n");
            for (Map.Entry<String, Integer> entry : BranchManager.getMinimalWorkersForShift(_branches.get(id)).entrySet()) {
                sb.append(" Role: ").append(entry.getKey()).append(", Number of workers: ").append(entry.getValue()).append("\n");
            }
            sb.append("Shifts: \n");
            for (Shift shift : _branches.get(id).getShifts()) {
                sb.append(" Shift Date: ").append(shift.getLocalDate()).append("," + (shift.getDayShift()?"day shift":"night shift")).append("\n");
            }
            return sb.toString();
        }
        return "Branch not found";
    }

    public boolean fireWorker(int id) {
        if(allWorkers.containsKey(id)) {
            allWorkers.remove(id);
            return true;
        }
        for(Map.Entry<String, List<Worker>> entry : roleList.entrySet()) {
            if(entry.getValue().contains(allWorkers.get(id))) {
                entry.getValue().remove(allWorkers.get(id));
            }
        }
        return false;
    }

    public void checkUpdateDay() {
        for (Map.Entry<Integer, Worker> entry : allWorkers.entrySet()) {
            if(entry.getValue().getID() != getID())
                WorkerManager.checkUpdateDay(entry.getValue());
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

    public static Branch getBranchO(String branch) {
        for(Map.Entry<Integer, Branch> entry : _branches.entrySet()) {
            if(entry.getValue().getName().equals(branch)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
