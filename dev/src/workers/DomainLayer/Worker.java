package workers.DomainLayer;

import workers.DataAcsessLayer.WorkerDAO;
import workers.DataAcsessLayer.WorkerDTO;

import java.util.LinkedList;
import java.util.List;

public class Worker {
    private String _name;
    private int _id;
    private int _bankNumber;
    private int _globalWage;
    private int _hourlyWage;
    private String _dateOfStart;
    private boolean _fullTimeJob;
    private int _totalVacationDays;
    private int _currentVacationDays;
    private boolean _isBM;
    private Constraints[][] _prefList;
    private List<Constraints[][]> _constraints;
    private boolean _changedPassword;
    protected String branch;
    private List<License> _licenses;

    public Worker(WorkerDTO worker)
    {
        _name = worker.getName();
        _id = worker.getID();
        _bankNumber = worker.getBankAccount();
        _globalWage = worker.getGWage();
        _hourlyWage = worker.getHWage();
        _dateOfStart = worker.getStartDate();
        _fullTimeJob = worker.getFTime();
        _totalVacationDays = worker.getTVDays();
        _currentVacationDays = worker.getCVDays();
        _prefList = new Constraints[7][2];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                _prefList[i][j] = Constraints.valueOf(worker.getPref()[i][j]);
            }

        }
        _isBM = worker.getIsHeadOfBranch();
        _changedPassword = worker.getChange();
        _licenses = new LinkedList<License>();
        for (String l : worker.getLicenses()) {
            if(l.equals(""))
                continue;
            _licenses.add(License.valueOf(l));
        }
    }

    public Worker(String name,int id,int bankNum,int globalWage,int hourlyWage,String dateOfStart,boolean fullTimeJob,int totalVacationDays,int currentVacationDays,boolean isBM) {
        _name = name;
        _fullTimeJob = fullTimeJob;
        _id = id;
        _bankNumber = bankNum;
        _globalWage = globalWage;
        _hourlyWage = hourlyWage;
        _dateOfStart = dateOfStart;
        _totalVacationDays = totalVacationDays;
        _currentVacationDays = currentVacationDays;
        _prefList = new Constraints[7][2];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                _prefList[i][j] = Constraints.can;
            }

        }
        _prefList[6][0] = Constraints.inactive;
        _prefList[6][1] = Constraints.inactive;
        _isBM = isBM;
        _changedPassword = false;
        _licenses = new LinkedList<License>();
        _constraints = new LinkedList<Constraints[][]>();

            WorkerDTO workerDTO = new WorkerDTO();
            workerDTO.setStartDate(_dateOfStart);
            workerDTO.setBankAccount(_bankNumber);
            workerDTO.setBranchName(branch);
            workerDTO.setFTime(_fullTimeJob);
            workerDTO.setGWage(_globalWage);
            workerDTO.setHWage(_hourlyWage);
            workerDTO.setID(_id);
            workerDTO.setName(_name);
            workerDTO.setTVDays(_totalVacationDays);
            workerDTO.setCVDays(_currentVacationDays);
            workerDTO.setIsHeadOfBranch(_isBM);
            workerDTO.setChange(_changedPassword);
            List<String> licenses = new LinkedList<String>();
            for (License l : _licenses) {
                licenses.add(l.name());
            }
            workerDTO.setLicenses(licenses);
            WorkerDAO.addWorker(workerDTO);

    }
    public String getName() {
        return _name;
    }
    public int getID() {
        return _id;
    }
    public Constraints getCons(int dayOfWeek,boolean dayShift) {
        return _prefList[dayOfWeek][dayShift ? 0 : 1];
    }
    public int getBankNum() {
        return _bankNumber;
    }
    public String getDateOfStart() {
        return _dateOfStart;
    }
    public Constraints[][] getCons() {
        return _prefList;
    }
    public int getGWage() {
        return _globalWage;
    }
    public int getHWage() {
        return _hourlyWage;
    }
    public boolean getFullTimeJob() {
        return _fullTimeJob;
    }
    public int getTotalVacationDays() {
        return _totalVacationDays;
    }
    public int getCurrVacationDays() {
        return _currentVacationDays;
    }

    public List<License> getLicenses() {
        return _licenses;
    }

    public void setBankNum(int newBankNum) {
        _bankNumber = newBankNum;
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setBankAccount(_bankNumber);
        workerDTO.setID(_id);
        WorkerDAO.updateWorkerBankNumber(workerDTO);
    }
    public void setWage(int newWage) {
        _globalWage = newWage;
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setGWage(_globalWage);
        workerDTO.setID(_id);
        WorkerDAO.updateWorkerGWage(workerDTO);
    }
    public void setHWage(int newHourly) {
        _hourlyWage = newHourly;
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setHWage(_hourlyWage);
        workerDTO.setID(_id);
        WorkerDAO.updateWorkerHWage(workerDTO);
    }
    public void setFullTimeJob(boolean isFull) {
        _fullTimeJob = isFull;
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setFTime(_fullTimeJob);
        workerDTO.setID(_id);
        WorkerDAO.updateWorkerFullTime(workerDTO);
    }
    public void setVacationDays(int vic) {
        _totalVacationDays = vic;
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setTVDays(_totalVacationDays);
        workerDTO.setID(_id);
        WorkerDAO.updateWorkerTotalVacationDays(workerDTO);
    }

    public boolean addConstraints(int day,boolean dayShift, Constraints cons) {
        if(day == 6)
            return false;
        if(dayShift) {
            _prefList[day][0] = cons;

        }
        else {
            _prefList[day][1] = cons;
        }
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setID(_id);
        String[][] prefList = new String[7][2];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                prefList[i][j] = _prefList[i][j].toString();
            }
        }
        workerDTO.setPref(prefList);
        WorkerDAO.updateWorkerPrefs(workerDTO);
        return true;
    }

    public String getBranch() {
        return branch;
    }

    public boolean getIsBM() {
        return _isBM;
    }



    public boolean getHasChangedPassword() {
        return _changedPassword;
    }

    public void createNewConstraints()
    {
        _prefList = new Constraints[7][2];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                _prefList[i][j] = (i == 6) ? Constraints.inactive : Constraints.can;
            }

        }
    }
    public void backupConstraints() {
        _constraints.add(_prefList);
    }

    public void setBranch(String branch) {
        this.branch = branch;
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setBranchName(branch);
        workerDTO.setID(_id);
        WorkerDAO.updateWorkerBranchName(workerDTO);
    }

    public void setChangedPassword() {
        _changedPassword = true;
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setChange(_changedPassword);
        workerDTO.setID(_id);
        WorkerDAO.updateWorkerChanged(workerDTO);
    }

    public void setCurrvacationDays(int days) {
        _currentVacationDays = days;
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setCVDays(_currentVacationDays);
        workerDTO.setID(_id);
        WorkerDAO.updateWorkerCurrentVacationDays(workerDTO);
    }

    public boolean addLicense(License license) {
        boolean b = _licenses.add(license);
        if(b) {
            WorkerDTO workerDTO = new WorkerDTO();
            workerDTO.setID(_id);
            List<String> licenses = new LinkedList<String>();
            for (License l : _licenses) {
                licenses.add(l.name());
            }
            workerDTO.setLicenses(licenses);
            WorkerDAO.updateWorkerLicenses(workerDTO);
        }
        return b;
    }
}
