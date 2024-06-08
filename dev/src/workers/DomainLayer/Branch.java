package workers.DomainLayer;

import java.util.LinkedList;
import java.util.List;

public class Branch {
    private String _name;
    private int _id;
    private List<Worker> _workers;
    private List<Shift> _shifts;
    private String _address;
    private Worker _branchManager;

    public Branch(String name, int id, String address, Worker branchManager) {
        _name = name;
        _id = id;
        _workers = new LinkedList<>();
        _shifts = new LinkedList<>();
        _address = address;
        _branchManager = branchManager;
    }

    public String getName() {
        return _name;
    }

    public int getID() {
        return _id;
    }

    public List<Worker> getWorkers() {
        return _workers;
    }

    public List<Shift> getShifts() {
        return _shifts;
    }

    public String getAddress() {
        return _address;
    }

    public Worker getBranchManager() {
        return _branchManager;
    }

    public void addWorker(Worker worker) {
        _workers.add(worker);
    }

    public void addShift(Shift shift) {
        _shifts.add(shift);
    }

    public void removeWorker(Worker worker) {
        _workers.remove(worker);
    }
    public void setBranchManager(Worker branchManager) {
        _branchManager = branchManager;
    }
}
