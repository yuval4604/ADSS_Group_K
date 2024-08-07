package src.workers.DomainLayer;


import src.workers.DataAcsessLayer.HeadOfBranchDAO;
import src.workers.DataAcsessLayer.BranchDAO;
import src.workers.DataAcsessLayer.HeadOfBranchDTO;
import src.workers.DataAcsessLayer.WorkerDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HR extends HeadOfBranch {
    private static Map<Integer,Branch> _branches;
    private static boolean loaded = false;
    private Map<LocalDate,List<Worker>> firedWorkers;

    public HR(Branch ABranch) {

        super("Admin",0,0,0,0,"",true,0,0,ABranch);

        _branches = new HashMap<>();
        _branches.put(0,ABranch);
        firedWorkers = new HashMap<>();
        HR.setLoaded();

    }

    public HR(WorkerDTO w, HeadOfBranchDTO h) {
        super(w,h);
        _branches = new HashMap<>();
        _branch.setHeadOfBranch(this);
        _branches.put(_branch.getID(),_branch);
        firedWorkers = new HashMap<>();
        HR.setLoaded();
        Map<Integer,String> branches = HeadOfBranchDAO.getNotices();
        for(Map.Entry<Integer,String> entry : branches.entrySet()) {
            String[] date = entry.getValue().split("\\.");
            LocalDate localDate = LocalDate.of(Integer.parseInt(date[2]),Integer.parseInt(date[1]),Integer.parseInt(date[0]));
            if(!firedWorkers.containsKey(localDate)) {
                firedWorkers.put(localDate,new LinkedList<>());
            }
            firedWorkers.get(localDate).add(HeadOfBranch.getWorker(entry.getKey()));
        }
    }

    public static boolean getLoaded() {
        return loaded;
    }

    public static void setLoaded()
    {
        loaded = true;
    }

    public void addWorker (Worker worker){
        allWorkers.put(worker.getID(), worker);
    }

    public boolean addBranch(String name, int id, String address, Worker headOfBranch) {
        if(!headOfBranch.getIsBM()) {
            return false;
        }
        Branch branch = new Branch(name, id, address, headOfBranch);
        ((HeadOfBranch)headOfBranch).setBranch(branch);
        _branches.put(id,branch);
        return true;
    }

    public boolean removeBranch(int id) {

        if(_branches.containsKey(id)) {
            BranchDAO.deleteBranch(id);
            _branches.remove(id);
            return true;
        }
        else {
            Branch b = Branch.getBranch(id,null);
            if(b != null) {
                b.cleanData();
                BranchDAO.deleteBranch(id);
                return true;
            }
        }
        return false;
    }

    public String showBranches() {
        StringBuilder sb = new StringBuilder();
        List<Branch> branches;
        branches = BranchManager.getRestOfBranches(_branches.keySet());
        for (Branch branch : branches) {
            _branches.put(branch.getID(),branch);
        }
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
        else {
            Branch b = Branch.getBranch(branchID,null);
            if(b != null) {
                b.setHeadOfBranch(headOfBranch);
                return true;
            }
        }
        return false;
    }

    public static String showBranch(int branch) {
        int id = -1000000000;
        for (Map.Entry<Integer, Branch> entry : _branches.entrySet()) {
            if(entry.getValue().getID() == (branch)) {
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
            if(allWorkers.get(id).getIsBM()) {
                HeadOfBranch headOfBranch = (HeadOfBranch) allWorkers.get(id);
                headOfBranch.deleteHeadOfBranch();
            }
            allWorkers.remove(id);
            WorkerManager.removeWorker(id);
            HeadOfBranchDAO.deleteRoles(id);
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
                    HeadOfBranchDAO.deleteNotice(worker.getID());
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
                String noticeDate = _30DaysFromNow.getDayOfMonth() + "." + _30DaysFromNow.getMonthValue() + "." + _30DaysFromNow.getYear();
                HeadOfBranchDAO.addNotice(worker.getID(),noticeDate);
                return true;
            } else {
                firedWorkers.get(_30DaysFromNow).add(worker);
                String noticeDate = _30DaysFromNow.getDayOfMonth() + "." + _30DaysFromNow.getMonthValue() + "." + _30DaysFromNow.getYear();
                HeadOfBranchDAO.addNotice(worker.getID(),noticeDate);
                return true;
            }

        } else {
            return false;
        }
    }

    public static Branch getBranchO(int branch) {
        for(Map.Entry<Integer, Branch> entry : _branches.entrySet()) {
            if(entry.getValue().getID() == (branch)) {
                return entry.getValue();
            }
        }
        BranchManager.getBranch(branch);
        return null;
    }
}
