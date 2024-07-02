package workers.DomainLayer;

import workers.DataAcsessLayer.BranchDAO;
import workers.DataAcsessLayer.BranchDTO;

import java.util.LinkedList;
import java.util.List;

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
            BranchDTO bdto = new BranchDTO();
            bdto.setName(name);
            bdto.setId(id);
            bdto.setAddress(address);
            if (headOfBranch == null)
                bdto.setHeadOfBranchId(0);
            else
                bdto.setHeadOfBranchId(headOfBranch.getID());
            BranchDAO.insertBranch(bdto);

    }

    public Branch(BranchDTO bdto) {
        _name = bdto.getName();
        _id = bdto.getId();
        _address = bdto.getAddress();
        _headOfBranch = HeadOfBranch.getWorker(bdto.getHeadOfBranchId());
        _workers = new LinkedList<>();
        _shifts = new LinkedList<>();
    }

    public static Branch getBranch(int branchID, Worker headOfBranch) {
        try {
            BranchDTO bdto = BranchDAO.getBranch(branchID);
            return new Branch(bdto);
        }
        catch (Exception e) {
            return null;
        }
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
        BranchDAO.insertBranchWorker(_id, worker.getID());
    }

    public void addShift(Shift shift) {
        _shifts.add(shift);
        BranchDAO.insertBranchShift(_id, shift.getDate(), shift.getDayShift());
    }

    public void removeWorker(Worker worker) {
        _workers.remove(worker);
        BranchDAO.deleteBranchWorker(_id, worker.getID());
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
