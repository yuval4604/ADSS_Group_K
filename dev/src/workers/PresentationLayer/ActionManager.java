package workers.PresentationLayer;
import workers.DomainLayer.Facade;
import workers.DomainLayer.Constraints;
import workers.DomainLayer.HeadOfBranch;

import java.time.LocalDate;
import java.util.Scanner;

public class ActionManager {
    private final Facade _facade;
    private final Scanner scanner;

    public ActionManager(Facade facade) {
        _facade = facade;
        scanner = new Scanner(System.in);
    }


    public void setBankNumber() {

        System.out.println("Enter new bank number:");
        int bankNum;
        try {
             bankNum = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid bank number");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        if(bankNum < 0) {
            System.out.println("Can't have negative bank number");
            return;
        }
        _facade.setBank(bankNum);

    }
    public void setWage() {
        System.out.println("Enter Worker's id:");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        if(!_facade.isFullTime(id)) {
            System.out.println("Can't do that...\nWorker is not full time");
            return;
        }
        System.out.println("Enter new global wage:");
        int wage;
        try {
            wage = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid wage");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        if(wage <= 0) {
            System.out.println("Can't have negative global wage");
            return;
        }
        boolean res = _facade.setGlobalWage(id,wage);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("changed successfully");
            _facade.setHourlyWage(id,0);
        }
    }
    public void setHWage() {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        if(_facade.isFullTime(id)) {
            System.out.println("Can't do that...\nWorker is full time");
            return;
        }
        System.out.println("Enter new hourly wage: ");
        int wage;
        try {
            wage = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid wage");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        if(wage <= 0) {
            System.out.println("Can't have negative hourly wage");
            return;
        }
        boolean res = _facade.setHourlyWage(id,wage);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("changed successfully");
            _facade.setGlobalWage(id,0);
        }
    }

    public void setFullTimeJob() {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("full time job?: choose t/f");
        String c = scanner.nextLine();
        boolean res;
        if(c.equals("t")) {
            res = _facade.setFullTimeJob(id,true);
            System.out.println("update the global wage of this employee: ");
            setWage();
        }
        else if(c.equals("f")){
            res = _facade.setFullTimeJob(id,false);
            System.out.println("update the hourly wage of this employee: ");
            setHWage();
        }
        else {
            System.out.println("Didnt enter information correctly");
            return;
        }


        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("changed successfully");
        }

    }
    public void setVacationDays() {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("How many vacation days?: ");
        int days;
        try {
            days = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid days");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        if(days < 0) {
            System.out.println("Can't have negative vacation days");
            return;
        }
        boolean res = _facade.setVacationDays(id,days);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("changed successfully\nDo you want to reset the vacation days too? : y/n");
            String choice = scanner.nextLine();
            if(choice.equals("y")) {
                _facade.ResetVacationDays(id);
            }
        }
    }
    public void ResetVacationDays() {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        boolean res = _facade.ResetVacationDays(id);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("Reset successfully");
        }
    }
    public void useVacationDays() {
        System.out.println("How many vacation days?: ");
        int days;
        try {
            days = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid days");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        if(days < 0) {
            System.out.println("Can't use negative vacation days");
            return;
        }
        boolean res = _facade.useVacationDays(days);
        if(!res) {
            System.out.println("Not enough vacation days");
        }
        else {
            System.out.println("used successfully. enjoy your vacation :)");
        }
    }

    public void ShowAvailableWorkersOfRole() {
        System.out.println("Which role?: ");
        String res = scanner.nextLine();
        String list = _facade.getAvailableWorkersOfRole(res);
        System.out.println(list);
    }
    public void addWorkerToShift() {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("Which role?: ");
        String role = scanner.nextLine();

        boolean res = _facade.addWorkerToShift(id,role);
        if(res) {
            System.out.println("Worker has been added!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }
    public boolean selectShift() {
        System.out.println("Enter the desired date: ");
        String date = scanner.nextLine();
        try{
            LocalDate ld = LocalDate.of(Integer.parseInt(date.substring(6,10)),Integer.parseInt(date.substring(3,5)),Integer.parseInt(date.substring(0,2)));
            if(ld.isBefore(LocalDate.now())){
                System.out.println("date is in the past");
                return false;
            }
        }
        catch (Exception e){
            System.out.println("Invalid date try dd.mm.yyyy");
            return false;
        }
        System.out.println("Day shift or night shift? : d/n");
        String shiftTime = scanner.nextLine();
        boolean dayShift;
        if(shiftTime.equals("d")) {
            dayShift = true;
        }
        else if(shiftTime.equals("n")){
                dayShift = false;
        }
        else {
            System.out.println("Didnt choose one of the options");
            return false;
        }
        boolean res = _facade.selectShift(date,dayShift);
        if(res) {
            System.out.println("selected successfully");
            return true;
        }
        else {
            System.out.println("Error: shift does not exist");
            return false;
        }
    }

    public boolean createShift() {
        System.out.println(_facade.getShiftManagers());
        System.out.println("Enter the shift manager's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return false;
        }
        scanner.nextLine();
        System.out.println("Choose the shift's date: ");
        String date = scanner.nextLine();
        if(date.length() != 10 || date.charAt(2) != '.' || date.charAt(5) != '.'){
            System.out.println("date not at format dd.mm.yyyy");
            return false;
        }
        try{
            LocalDate ld = LocalDate.of(Integer.parseInt(date.substring(6,10)),Integer.parseInt(date.substring(3,5)),Integer.parseInt(date.substring(0,2)));
            if(ld.isBefore(LocalDate.now())){
                System.out.println("date is in the past");
                return false;
            }
        }
        catch (Exception e){
            System.out.println("Invalid date");
            return false;
        }
        System.out.println("Day shift or night shift? : d/n");
        String shiftTime = scanner.nextLine();
        boolean dayShift;
        if (shiftTime.equals("d")) {    
            dayShift = true;
        } else if (shiftTime.equals("n")) {
            dayShift = false;
        } else {
            System.out.println("Didnt choose one of the options");
            return false;
        }
        System.out.println("day of week?: choose 1 to 7");
        int dayOfWeek;
        try {
            dayOfWeek = scanner.nextInt() - 1;
            if(dayOfWeek < 0 || dayOfWeek > 6) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            System.out.println("Invalid day of week");
            scanner.nextLine();
            return false;
        }
        scanner.nextLine();

        boolean res = _facade.createShift(id, date, dayShift, dayOfWeek);
        if(res) {
            System.out.println("created successfully");
            return true;
        }
        else {
            System.out.println("Error: shift already exists or wrong info");
            return false;
        }
    }
    public void showWorkerInfo() {
        String res = _facade.showWorkerInfo();
        System.out.println(res);
    }
    public void showWorkerConstraints() {
        String res = _facade.showWorkerConstraints();
        System.out.println(res);
    }
    public void setHalfDayOff() {
        System.out.println("Choose the shift's date: ");
        String date = scanner.nextLine();
        System.out.println("Day shift or night shift? : d/n");
        String shiftTime = scanner.nextLine();
        boolean dayShift;
        if (shiftTime.equals("d")) {
            dayShift = true;
        } else if (shiftTime.equals("n")) {
            dayShift = false;
        } else {
            System.out.println("Didnt choose one of the options");
            return;
        }
        System.out.println("day of week?: choose 1 to 7");
        int dayOfWeek;
        try {
            dayOfWeek = scanner.nextInt() - 1;
            if(dayOfWeek < 0 || dayOfWeek > 6) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            System.out.println("Invalid day of week");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        boolean res = _facade.setHalfDayShiftOff(date,dayShift,dayOfWeek);
        if(res) {
            System.out.println("updated successfully");
        }
        else {
            System.out.println("Error: no permission to do that Or wrong information");
        }
    }
    public void setAllDayOff() {
        System.out.println("Choose the shift's date: ");
        String date = scanner.nextLine();
        System.out.println("day of week?: choose 1 to 7");
        int dayOfWeek;
        try {
            dayOfWeek = scanner.nextInt() - 1;
            if(dayOfWeek < 0 || dayOfWeek > 6) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            System.out.println("Invalid day of week");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        boolean res = _facade.setAllDayOff(date,dayOfWeek);
        if(res) {
            System.out.println("updated successfully");
        }
        else {
            System.out.println("Error: no permission to do that Or wrong information");
        }
    }

    public void addConstraints() {
        if(_facade.lastDayToSetConstraints() < LocalDate.now().getDayOfWeek().getValue()) {
            System.out.println("Can't set constraints for past days");
            return;
        }
        System.out.println("Enter the day: 1-7");
        int day;
        try {
            day = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid days");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("Day shift or night shift? : d/n");
        String shiftTime = scanner.nextLine();
        boolean dayShift;
        if (shiftTime.equals("d")) {
            dayShift = true;
        } else if (shiftTime.equals("n")) {
            dayShift = false;
        } else {
            System.out.println("Didnt choose one of the options");
            return;
        }
        System.out.println("Choose Constraint? Choose a number: 1/2/3 for want/can/cant");
        int choice;
        try {
            choice = scanner.nextInt();
            if(choice < 1 || choice > 3) {
                throw new Exception();
            }
        }
        catch (Exception e) {
            System.out.println("Invalid option");
            scanner.nextLine();
            return;
        }

        scanner.nextLine();
        Constraints cons;
        if (choice == 1) {
            cons = Constraints.want;
        } else if (choice == 2) {
            cons = Constraints.can;
        } else{
            cons = Constraints.cant;
        }
        _facade.addConstraints(day - 1,dayShift,cons);

    }

    public void workOnShift()
    {
        boolean cOrS = false;
        while(!cOrS) {
            System.out.println("create or select shift? : c/s");
            String choice = scanner.nextLine();
            if (choice.equals("c")) {
                cOrS = createShift();
            } else if (choice.equals("s")) {
                cOrS = selectShift();
                if (_facade.isInactive()) {
                    System.out.println("Shift is inactive");
                    return;
                }
            } else {
                System.out.println("Didnt choose one of the options");
            }
        }
        boolean keepWorking = true;
        while (keepWorking)
        {
            System.out.println("choose command:\n1) add worker to shift\n2) remove a worker from the shift\n3) show available workers of role\n4) show shift\n5) check if the current shift has enough workers\n6) back");
            int result;
            try {
                result = scanner.nextInt();
                if(result < 1 || result > 6) {
                    throw new Exception();
                }
            }
            catch (Exception e) {
                System.out.println("Invalid command");
                scanner.nextLine();
                continue;
            }
            scanner.nextLine();
            switch (result) {
                case 1 -> addWorkerToShift();
                case 2 -> removeWorkerFromShift();
                case 3 -> ShowAvailableWorkersOfRole();
                case 4 -> System.out.println(_facade.showShift());
                case 5 -> checkIfRoleHasMinimalWorkers();
                case 6 -> {
                    if(_facade.checkIfRoleHasMinimalWorkers())
                    {
                        System.out.println("Shift is ready to go!");
                        keepWorking = false;
                    }
                    else
                    {
                        System.out.println("Shift is not ready to go!");
                    }
                }
                default -> System.out.println("Didnt choose one of the options");
            }
        }
    }

    private void removeWorkerFromShift() {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("Which role?: ");
        String role = scanner.nextLine();

        boolean res = _facade.removeWorkerFromShift(id,role);
        if(res) {
            System.out.println("Worker has been removed!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }

    public void addAWorker()
    {
        System.out.println("Enter Worker's name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("Enter Worker's bank number: ");
        int bankNum;
        try {
            bankNum = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid bank number");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("Is the worker full time job?: choose t/f");
        String c = scanner.nextLine();
        boolean fullTime;
        int globalWage = 0 ,hourlyWage = 0;
        if(c.equals("t")) {
            fullTime = true;
            System.out.println("Enter Worker's global wage: ");
            try{
                globalWage = scanner.nextInt();
            }
            catch (Exception e) {
                System.out.println("Invalid wage");
                scanner.nextLine();
                return;
            }
            scanner.nextLine();
        }
        else if(c.equals("f")){
            fullTime = false;
            System.out.println("Enter Worker's hourly wage: ");
            try{
                hourlyWage = scanner.nextInt();
            }
            catch (Exception e) {
                System.out.println("Invalid wage");
                scanner.nextLine();
                return;
            }
            scanner.nextLine();
        }
        else {
            System.out.println("Didnt enter information correctly");
            return;
        }
        System.out.println("Enter Worker's date of start: ");
        String dateOfStart = scanner.nextLine();
        System.out.println("Enter Worker's total vacation days: ");
        int totalVacationDays;
        try {
            totalVacationDays = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid days");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        boolean res = _facade.addWorker(name, id, bankNum, fullTime, globalWage, hourlyWage, dateOfStart, totalVacationDays, totalVacationDays);
        if(res) {
            System.out.println("Worker has been added!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }

    public void changeWorkerRoles()
    {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("do you want to add or remove a role from him? choose a/r ");
        String aORr = scanner.nextLine();
        if(aORr.equals("a")){
            System.out.println("Enter Worker's new role: ");
            String role = scanner.nextLine();
            boolean res = _facade.addRole(id, role);
            if(res) {
                System.out.println("Worker's role has been added!");
            }
            else {
                System.out.println("Error: Something went wrong :(");
            }
        } else if (aORr.equals("r")) {
            System.out.println("Enter Worker's role to remove: ");
            String role = scanner.nextLine();
            boolean res = _facade.removeRole(id, role);
            if(res) {
                System.out.println("Worker's role has been removed!");
            }
            else {
                System.out.println("Error: Something went wrong :(");
            }
        } else {
            System.out.println("Didnt choose one of the options");

        }

    }

    public void showWorkerRoles()
    {
        String roles = _facade.getRoles();
        System.out.println(roles);
    }

    public boolean isBM() {
        return _facade.getIsBM();
    }

    public void getBranch() {
        System.out.println(_facade.getBranch());
    }

    public void setLastDayForConstraints() {
        System.out.println("Enter the day: 1-7");
        int day;
        try {
            day = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid days");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        boolean res = _facade.setLastDayForConstraints(day);
        if(res) {
            System.out.println("updated successfully");
        }
        else {
            System.out.println("Error: no permission to do that Or wrong information");
        }
    }

    public void showWorkerShifts() {
        try {
            System.out.println("Choose A starting date from this order: dd.mm.yyyy");
            String date = scanner.nextLine();
            System.out.println("Choose An ending date from this order: dd.mm.yyyy");
            String date1 = scanner.nextLine();
            System.out.println(_facade.showWorkerShifts(date,date1));
        }
        catch (Exception e) {
            System.out.println("Invalid information");
            return;
        }

    }
    public void fireWorker() {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        try {
            boolean res = _facade.fireWorker(id);
            if(res) {
                System.out.println("Worker has been fired!");
            }
            else {
                System.out.println("Error: Something went wrong :(");
            }
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
    }
    public void endContranct30DaysFromNow() {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        try {
            boolean res = _facade.endContranct30DaysFromNow(id);
            if(res) {
                System.out.println("Worker's contract has been ended!");
            }
            else {
                System.out.println("Error: Something went wrong :(");
            }
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
    }
    public void setMinimalWorkers() {
        System.out.println("Enter Role: ");
        String role;
        try {
            role = scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        System.out.println("Enter number of minimal worker in said role: ");
        int num;
        try {
            num = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        boolean res = _facade.setMinimalWorkers(role,num);
        if(res) {
            System.out.println("Minimal workers has been set!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }

    public void checkIfRoleHasMinimalWorkers() {
        boolean res = _facade.checkIfRoleHasMinimalWorkers();
        if(res) {
            System.out.println("All roles have reached the mimnimal requirement of workers");
        }
        else {
            System.out.println("some roles did not reached the mimnimal requirement of workers");
        }
    }

    public void showBranch() {
        System.out.println(_facade.showBranch());
    }

    public void showBranches() {
        System.out.println(_facade.showBranches());
    }

    public void setHeadOfBranch() {
        System.out.println("Enter Branch's id: ");
        int branchID;
        try {
            branchID = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        System.out.println("Enter Worker's id: ");
        int workerID;
        try {
            workerID = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        boolean res = _facade.setHeadOfBranch(branchID,workerID);
        if(res) {
            System.out.println("Branch manager has been set!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }

    public void removeWorkerFromBranch() {
        System.out.println("Enter Worker's id: ");
        int workerID;
        try {
            workerID = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        boolean res = _facade.removeWorkerFromBranch(workerID);
        if(res) {
            System.out.println("Worker has been removed from branch!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }

    public void addWorkerToBranch() {
        System.out.println("Enter Worker's id: ");
        int workerID;
        try {
            workerID = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        boolean res = _facade.addWorkerToBranch(workerID);
        if(res) {
            System.out.println("Worker has been added to branch!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }

    public void removeBranch() {
        System.out.println("Enter Branch's id: ");
        int branchID;
        try {
            branchID = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        boolean res = _facade.removeBranch(branchID);
        if(res) {
            System.out.println("Branch has been removed!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }

    public void addBranch() {
        System.out.println("Enter Branch's id: ");
        int branchID;
        try {
            branchID = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        System.out.println("Enter Branch's name: ");
        String branchName;
        try {
            branchName = scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        System.out.println("Enter Branch's address: ");
        String adress = scanner.nextLine();
        System.out.println("Enter Branch's BM id: ");
        int BMID;
        try {
            BMID = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            return;
        }
        boolean res = _facade.addBranch(branchName, branchID, adress, BMID);
        if(res) {
            System.out.println("Branch has been added!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }

    public void altarOnGoingShift() {
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        System.out.println("Enter Worker's new role: ");
        String role = scanner.nextLine();
        System.out.println("Enter branch's id: ");
        int bID;
        try {
            bID = scanner.nextInt();
            scanner.nextLine();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        boolean res = _facade.altarOnGoingShift(id,role,bID);
        if(res) {
            System.out.println("changed successfully");
        }
        else {
            System.out.println("Error: something went wrong");
        }
    }

    public boolean isHR() {
        return _facade.getIsHR();
    }

    public void createBM() {
        System.out.println("Enter Worker's name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Worker's id: ");
        int id;
        try {
            id = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("Enter Worker's bank number: ");
        int bankNum;
        try {
            bankNum = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid bank number");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        System.out.println("Is the worker full time job?: choose t/f");
        String c = scanner.nextLine();
        boolean fullTime;
        int globalWage = 0 ,hourlyWage = 0;
        if(c.equals("t")) {
            fullTime = true;
            System.out.println("Enter Worker's global wage: ");
            try{
                globalWage = scanner.nextInt();
            }
            catch (Exception e) {
                System.out.println("Invalid wage");
                scanner.nextLine();
                return;
            }
            scanner.nextLine();
        }
        else if(c.equals("f")){
            fullTime = false;
            System.out.println("Enter Worker's hourly wage: ");
            try{
                hourlyWage = scanner.nextInt();
            }
            catch (Exception e) {
                System.out.println("Invalid wage");
                scanner.nextLine();
                return;
            }
            scanner.nextLine();
        }
        else {
            System.out.println("Didnt enter information correctly");
            return;
        }
        System.out.println("Enter Worker's date of start: ");
        String dateOfStart = scanner.nextLine();
        System.out.println("Enter Worker's total vacation days: ");
        int totalVacationDays;
        try {
            totalVacationDays = scanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid days");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();
        boolean res = _facade.createBM(name, id, bankNum, fullTime, globalWage, hourlyWage, dateOfStart, totalVacationDays, totalVacationDays);
        if(res) {
            System.out.println("Worker has been added!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }

    }

    public void addLicense() {
        System.out.println("Enter Worker's license: ");
        String license = scanner.nextLine();
        boolean res = _facade.addLicense(license);
        if(res) {
            System.out.println("License has been added!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }
}
