package workers.DomainLayer;

import workers.DataAcsessLayer.ShiftDAO;
import workers.DataAcsessLayer.ShiftDTO;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Shift {
    private boolean _active;
    private Worker shiftManager;
    private String _date; // dd-mm-yyyy
    private LocalDate _localDate;

    private int _dayOfWeek;
    private boolean _dayShift; // true - morning / false - evening
    private Map<String, List<Worker>> workers; // map contains a role and a list of workers
    private Branch _branch;
    private boolean needQuartermaster;
    public Shift(Worker sManager,String date, boolean dayShift,int dayOfWeek, boolean active, Branch branch) {
        shiftManager = sManager;
        _date = date;
        _dayShift = dayShift;
        workers = new HashMap<>();
        _dayOfWeek = dayOfWeek;
        _active = active;
        _localDate = LocalDate.of(Integer.parseInt(date.split("\\.")[2]), Integer.parseInt(date.split("\\.")[1]), Integer.parseInt(date.split("\\.")[0]));
        _branch = branch;
        needQuartermaster = false;
        ShiftDTO shiftDTO = new ShiftDTO();
        shiftDTO.setActive(active);
        shiftDTO.setBranchId(branch.getID());
        shiftDTO.setDate(date);
        shiftDTO.setDayOfWeek(dayOfWeek);
        shiftDTO.setDayShift(dayShift);
        if(sManager != null)
            shiftDTO.setManagerId(sManager.getID());
        else
            shiftDTO.setManagerId(-1);
        shiftDTO.setNeedQM(needQuartermaster);
        ShiftDAO.insertShift(shiftDTO);
    }

    public Shift(ShiftDTO shift) {
        _date = shift.getDate();
        _dayShift = shift.getDayShift();
        workers = new HashMap<>();
        _dayOfWeek = shift.getDayOfWeek();
        _active = shift.getActive();
        _localDate = LocalDate.of(Integer.parseInt(_date.split("\\.")[2]), Integer.parseInt(_date.split("\\.")[1]), Integer.parseInt(_date.split("\\.")[0]));
        _branch = Branch.getBranch(shift.getBranchId());
        needQuartermaster = shift.getNeedQM();
        if(shift.getManagerId() != -1)
            shiftManager = HeadOfBranch.getWorker(shift.getManagerId());
        else
            shiftManager = null;
        Map<String, List<Integer>> workersMap = shift.getWorkers();
        for (Map.Entry<String, List<Integer>> entry : workersMap.entrySet()) {
            List<Worker> workersList = new LinkedList<>();
            for (int id : entry.getValue()) {
                workersList.add(HeadOfBranch.getWorker(id));
            }
            workers.put(entry.getKey(), workersList);
        }
    }

    public String getDate() {
        return _date;
    }

    public boolean getDayShift() {
        return _dayShift;
    }

    public void addWorker(Worker worker, String role) {
        if (workers.containsKey(role)) {
            workers.get(role).add(worker);
        } else {
            workers.put(role, new LinkedList<>());
            workers.get(role).add(worker);
        }
        ShiftDTO shiftDTO = new ShiftDTO();
        shiftDTO.setDate(_date);
        shiftDTO.setDayShift(_dayShift);
        shiftDTO.setBranchId(_branch.getID());

        ShiftDAO.insertWorkerToShift(shiftDTO, role, worker.getID());
    }

    public int getDayOfWeek() {
        return _dayOfWeek;
    }

    public boolean getActive() {
        return _active;
    }

    public Worker getShiftManager() {
        return shiftManager;
    }

    public Map<String, List<Worker>> getWorkers() {
        return workers;
    }


    public LocalDate getLocalDate() {
        return _localDate;
    }


    public Branch getBranch() {
        return _branch;
    }

    public void setNeedQuartermaster(boolean b) {
        needQuartermaster = b;
        ShiftDTO shiftDTO = new ShiftDTO();
        shiftDTO.setDate(_date);
        shiftDTO.setDayShift(_dayShift);
        shiftDTO.setBranchId(_branch.getID());
        shiftDTO.setNeedQM(b);
        ShiftDAO.updateNeedQM(shiftDTO);
    }

    public boolean getNeedQuartermaster() {
        return needQuartermaster;
    }

//    public boolean isWorkerInShift(int id) {
//        for (Map.Entry<String, List<Worker>> entry : workers.entrySet()) {
//            for (Worker worker : entry.getValue()) {
//                if(worker.getID() == id) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
}
