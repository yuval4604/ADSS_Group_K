package workers.DomainLayer;

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
    private boolean isHR;
    private Constraints[][] _prefrenceList;

    public Worker(String name,int id,int bankNum,int globalWage,int hourlyWage,String dateOfStart,boolean fullTimeJob,int totalVacationDays,int currentVacationDays) {
        _name = name;
        _fullTimeJob = fullTimeJob;
        _id = id;
        _bankNumber = bankNum;
        _globalWage = globalWage;
        _hourlyWage = hourlyWage;
        _dateOfStart = dateOfStart;
        _totalVacationDays = totalVacationDays;
        _currentVacationDays = currentVacationDays;
        _prefrenceList = new Constraints[7][2];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                _prefrenceList[i][j] = Constraints.can;
            }

        }
        if(name.equals("Admin")) {
            isHR = true;
        }
        else {
            isHR = false;
        }
    }
    public String getName() {
        return _name;
    }
    public int getID() {
        return _id;
    }
    public Constraints getCons(int dayOfWeek,boolean dayShift) {
        return _prefrenceList[dayOfWeek][dayShift ? 0 : 1];
    }
    public int getBankNum() {
        return _bankNumber;
    }
    public String getDateOfStart() {
        return _dateOfStart;
    }
    public Constraints[][] getCons() {
        return _prefrenceList;
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
        if(dayShift) {
            _prefrenceList[day][0] = cons;

        }
        else {
            _prefrenceList[day][1] = cons;
        }
        return true;
    }
    public boolean getIsHR() {
        return isHR;
    }
    public boolean useVacationDays(int days) {
        if (days < _currentVacationDays) {
            _currentVacationDays = _currentVacationDays - days;
            return true;
        }
        return false;
    }



}
