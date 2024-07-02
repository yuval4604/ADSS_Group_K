package workers.DomainLayer;

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
