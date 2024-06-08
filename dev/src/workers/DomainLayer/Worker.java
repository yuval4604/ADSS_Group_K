package workers.DomainLayer;

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
    private List<String> branches;

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
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                _prefList[i][j] = Constraints.can;
            }

        }
        _prefList[6][0] = Constraints.inactive;
        _prefList[6][1] = Constraints.inactive;
        _isBM = isBM;
        _changedPassword = false;
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

    public void setBankNum(int newBankNum) {
        _bankNumber = newBankNum;
    }
    public void setWage(int newWage) {
        _globalWage = newWage;
    }
    public void setHWage(int newHourly) {
        _hourlyWage = newHourly;
    }
    public void setFullTimeJob(boolean isFull) {
        _fullTimeJob = isFull;
    }
    public void setVacationDays(int vic) {
        _totalVacationDays = vic;
    }
    public void ResetVacationDays() {
        _currentVacationDays = _totalVacationDays;
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
        return true;
    }
    public boolean getIsBM() {
        return _isBM;
    }
    public boolean useVacationDays(int days) {
        if (days < _currentVacationDays) {
            _currentVacationDays = _currentVacationDays - days;
            return true;
        }
        return false;
    }


    public boolean hasChangedPassword() {
        return _changedPassword;
    }

    public void checkUpdateDay() {
        _constraints.add(_prefList);
        _prefList = new Constraints[7][2];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                _prefList[i][j] = (i == 6) ? Constraints.inactive : Constraints.can;
            }

        }
    }

    public void addOptionalBranches(String name) {
        branches.add(name);
    }

    public boolean inBranch(String name) {
        return branches.contains(name);
    }

    public String getBranches() {
        StringBuilder sb = new StringBuilder();
        for (String name : branches) {
            sb.append(name);
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
}
