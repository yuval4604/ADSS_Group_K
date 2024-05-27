package workers.DomainLayer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Shift {
    private boolean _active;
    private Worker shiftManager;
    private String _date; // dd-mm-yyyy
    private int _dayOfWeek;
    private boolean _dayShift; // true - morning / false - evening
    private Map<String, List<Worker>> workers; // map contains a role and a list of workers
    public Shift(Worker smanager,String date, boolean dayShift,int dayOfWeek, boolean active) {
        shiftManager = smanager;
        _date = date;
        _dayShift = dayShift;
        workers = new HashMap<>();
        _dayOfWeek = dayOfWeek;
        _active = active;
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
}
