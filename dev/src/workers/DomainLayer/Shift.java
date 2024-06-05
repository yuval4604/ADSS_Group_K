package workers.DomainLayer;

import javax.management.relation.Role;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;


public class Shift {
    private boolean _active;
    private Worker shiftManager;
    private String _date; // dd-mm-yyyy
    private LocalDate _localDate;

    private Map<String,Integer> minimalWorkers;
    private int _dayOfWeek;
    private boolean _dayShift; // true - morning / false - evening
    private Map<String, List<Worker>> workers; // map contains a role and a list of workers
    public Shift(Worker sManager,String date, boolean dayShift,int dayOfWeek, boolean active) {
        shiftManager = sManager;
        _date = date;
        _dayShift = dayShift;
        workers = new HashMap<>();
        _dayOfWeek = dayOfWeek;
        _active = active;
        _localDate = LocalDate.of(Integer.parseInt(date.split(".")[2]),Integer.parseInt(date.split(".")[1]),Integer.parseInt(date.split(".")[0]));
        minimalWorkers = new HashMap<>();
    }

    public String getDate() {
        return _date;
    }
    public boolean getDayShift() {
        return _dayShift;
    }

    public void addWorker(Worker worker,String role) {
        if(workers.containsKey(role)) {
            workers.get(role).add(worker);
        }
        else {
            workers.put(role,new LinkedList<>());
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

    public boolean notIn(Worker worker) {
        for (Map.Entry<String, List<Worker>> entry : workers.entrySet()) {
            if(entry.getValue().contains(worker)) {
                return false;
            }
        }
        return true;
    }

    public LocalDate getLocalDate() {
        return _localDate;
    }

    public Map<String,Integer> getMinimalWorkers() {
        return minimalWorkers;
    }


    public void altarRole(int id, String role) {
        for (Map.Entry<String, List<Worker>> entry : workers.entrySet()) {
            for (Worker worker : entry.getValue()) {
                if(worker.getID() == id) {
                    entry.getValue().remove(worker);
                    workers.get(role).add(worker);
                    return;
                }
            }
        }
    }
}
