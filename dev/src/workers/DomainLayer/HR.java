package workers.DomainLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HR extends BranchManager{
    private Map<Integer,Branch> _branch;
    private Map<LocalDate,List<Worker>> firedWorkers;

    public HR(Branch ABranch) {
        super("Admin",-1,0,0,0,"",true,0,0,ABranch);
        _branch = new HashMap<>();
        _branch.put(-1,ABranch);
        firedWorkers = new HashMap<>();
    }

    public void addWorker (Worker worker){
        allWorkers.put(worker.getID(), worker);
    }

    public boolean addBranch(String name, int id, String address, Worker branchManager) {
        Branch branch = new Branch(name, id, address, branchManager);
        _branch.put(id,branch);
        return true;
    }

    public boolean removeBranch(int id) {
        if(_branch.containsKey(id)) {
            _branch.remove(id);
            return true;
        }
        return false;
    }

    public String showBranches() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Branch> entry : _branch.entrySet()) {
            sb.append("Branch ID: ").append(entry.getKey()).append(" Branch Name: ").append(entry.getValue().getName()).append("\n");
        }
        return sb.toString();
    }

    public boolean setBranchManager(int branchID, Worker branchManager) {
        if(_branch.containsKey(branchID)) {
            _branch.get(branchID).setBranchManager(branchManager);
            return true;
        }
        return false;
    }

    public String showBranch(int branchID) {
        if(_branch.containsKey(branchID)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Branch Name: ").append(_branch.get(branchID).getName()).append("\n");
            sb.append("Branch ID: ").append(_branch.get(branchID).getID()).append("\n");
            sb.append("Branch Address: ").append(_branch.get(branchID).getAddress()).append("\n");
            sb.append("Branch Manager ID: ").append(_branch.get(branchID).getBranchManager().getID()).append(" Branch Manager Name: ").append(_branch.get(branchID).getBranchManager().getName()).append("\n");
            sb.append("Workers: \n");
            for (Worker worker : _branch.get(branchID).getWorkers()) {
                sb.append("- Worker ID: ").append(worker.getID()).append(" Worker Name: ").append(worker.getName()).append("\n");
            }
            sb.append("Shifts: \n");
            for (Shift shift : _branch.get(branchID).getShifts()) {
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
}
