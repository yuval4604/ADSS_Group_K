package workers.DomainLayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Branch {
    private String _name;
    private int _id;
    private List<Worker> _workers;
    private List<Shift> _shifts;
    private String _address;
    private Worker _headOfBranch;

    public Branch(String name, int id, String address, Worker headOfBranch) {
        _name = name;
        _id = id;
        _workers = new LinkedList<>();
        _shifts = new LinkedList<>();
        _address = address;
        _headOfBranch = headOfBranch;
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

    public Worker getHeadOfBranch() {
        return _headOfBranch;
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
    public void setHeadOfBranch(Worker headOfBranch) {
        ((HeadOfBranch)headOfBranch).takeOffBranch();
        _headOfBranch = headOfBranch;
    }

//    public Map<String, Integer> getMinimalWorkersForShift() {
//        return ((HeadOfBranch) _headOfBranch).getMinimalWorkersForShift();
//    }

    public Worker getBM() {
        return _headOfBranch;
    }
}
