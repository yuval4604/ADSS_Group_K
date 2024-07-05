package workers.DataAcsessLayer;

import workers.DomainLayer.Worker;

import java.util.LinkedList;
import java.util.List;

public class BranchDTO {
    private String name;
    private int id;
    private List<Integer> workers;
    private List<String> shifts;
    private String address;
    private int headOfBranchId;

    public String getName() {
        return name;
    }
    public int getId() {
        return id;
    }
    public List<Integer> getWorkers() {
        if (workers == null)
            workers = new LinkedList<>();
        return workers;
    }
    public List<String> getShifts() {
        if (shifts == null)
            shifts = new LinkedList<>();
        return shifts;
    }
    public String getAddress() {
        return address;
    }
    public int getHeadOfBranchId() {
        return headOfBranchId;
    }
    public List<Worker> getWorkersList() {
        return null;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setHeadOfBranchId(int headOfBranchId) {
        this.headOfBranchId = headOfBranchId;
    }
    public void setWorkers(List<Integer> workers) {
        this.workers = workers;
    }
    public void setShifts(List<String> lshifts) {
        this.shifts = lshifts;
    }
}
