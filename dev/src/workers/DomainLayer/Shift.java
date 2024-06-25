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

    private int _dayOfWeek;
    private boolean _dayShift; // true - morning / false - evening
    private Map<String, List<Worker>> workers; // map contains a role and a list of workers
    private Branch _branch;

    public Shift(Worker sManager, String date, boolean dayShift, int dayOfWeek, boolean active, Branch branch) {
        shiftManager = sManager;
        _date = date;
        _dayShift = dayShift;
        workers = new HashMap<>();
        _dayOfWeek = dayOfWeek;
        _active = active;
        _localDate = LocalDate.of(Integer.parseInt(date.split("\\.")[2]), Integer.parseInt(date.split("\\.")[1]), Integer.parseInt(date.split("\\.")[0]));
        _branch = branch;
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


}