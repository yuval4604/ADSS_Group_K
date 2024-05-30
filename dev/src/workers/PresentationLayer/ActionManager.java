package workers.PresentationLayer;
import workers.DomainLayer.Connector;
import workers.DomainLayer.Constraints;

import java.util.Scanner;

public class ActionManager {
    private final Connector _connector;
    private final Scanner scanner;

    public ActionManager(Connector connector) {
        _connector = connector;
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
        _connector.setBank(bankNum);

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
        if(!_connector.isFullTime(id)) {
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
        boolean res = _connector.setGlobalWage(id,wage);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("changed successfully");
            _connector.setHourlyWage(id,0);
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
        if(_connector.isFullTime(id)) {
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
        boolean res = _connector.setHourlyWage(id,wage);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("changed successfully");
            _connector.setGlobalWage(id,0);
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
            res = _connector.setFullTimeJob(id,true);
            System.out.println("update the global wage of this employee: ");
            setWage();
        }
        else if(c.equals("f")){
            res = _connector.setFullTimeJob(id,false);
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
        boolean res = _connector.setVacationDays(id,days);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("changed successfully\nDo you want to reset the vacation days too? : y/n");
            String choice = scanner.nextLine();
            if(choice.equals("y")) {
                _connector.ResetVacationDays(id);
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
        boolean res = _connector.ResetVacationDays(id);
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
        boolean res = _connector.useVacationDays(days);
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
        String list = _connector.getAvailableWorkersOfRole(res);
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

        boolean res = _connector.addWorkerToShift(id,role);
        if(res) {
            System.out.println("Worker has been added!");
        }
        else {
            System.out.println("Error: Something went wrong :(");
        }
    }
    public void selectShift() {
        System.out.println("Enter the desired date: ");
        String date = scanner.nextLine();
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
            return;
        }
        boolean res = _connector.selectShift(date,dayShift);
        if(res) {
            System.out.println("selected successfully");
        }
        else {
            System.out.println("Error: shift does not exist");
        }
    }

    public void createShift() {
        System.out.println("Enter the shift manager's id: ");
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
        System.out.println("Choose the shift's date: ");
        String date = scanner.nextLine();
        if(date.length() != 10 || date.charAt(2) != '.' || date.charAt(5) != '.'){
            System.out.println("date not at format dd.mm.yyyy");
            return;
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

        boolean res = _connector.createShift(id, date, dayShift, dayOfWeek);
        if(res) {
            System.out.println("created successfully");
        }
        else {
            System.out.println("Error: shift already exists or wrong info");
        }
    }
    public void showWorkerInfo() {
        String res = _connector.showWorkerInfo();
        System.out.println(res);
    }
    public void showWorkerConstraints() {
        String res = _connector.showWorkerConstraints();
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
        boolean res = _connector.setHalfDayShiftOff(date,dayShift,dayOfWeek);
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
        boolean res = _connector.setAllDayOff(date,dayOfWeek);
        if(res) {
            System.out.println("updated successfully");
        }
        else {
            System.out.println("Error: no permission to do that Or wrong information");
        }
    }

    public void addConstraints() {
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
        _connector.addConstraints(day,dayShift,cons);

    }

    public void workOnShift()
    {
        System.out.println("create or select shift? : c/s");
        String choice = scanner.nextLine();
        if(choice.equals("c"))
        {
            createShift();
        }
        else if(choice.equals("s"))
        {
            selectShift();
            if(_connector.isInactive())
            {
                System.out.println("Shift is inactive");
                return;
            }
        }
        else
        {
            System.out.println("Didnt choose one of the options");
        }
        boolean keepWorking = true;
        while (keepWorking)
        {
            System.out.println("choose command:\n1) add worker to shift\n2) remove a worker from the shift\n3) show available workers of role\n4) show shift\n5) back");
            int result;
            try {
                result = scanner.nextInt();
                if(result < 1 || result > 5) {
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
                case 4 -> System.out.println(_connector.showShift());
                case 5 -> keepWorking = false;
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

        boolean res = _connector.removeWorkerFromShift(id,role);
        if(res) {
            System.out.println("Worker has been added!");
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
        System.out.println("Enter Worker's initial password: ");
        String password = scanner.nextLine();
        boolean res = _connector.addWorker(name, id, bankNum, fullTime, globalWage, hourlyWage, dateOfStart, totalVacationDays, totalVacationDays,password);
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
            boolean res = _connector.addRole(id, role);
            if(res) {
                System.out.println("Worker's role has been added!");
            }
            else {
                System.out.println("Error: Something went wrong :(");
            }
        } else if (aORr.equals("r")) {
            System.out.println("Enter Worker's role to remove: ");
            String role = scanner.nextLine();
            boolean res = _connector.removeRole(id, role);
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
        String roles = _connector.getRoles();
        System.out.println(roles);
    }

    public boolean isHR() {
        return _connector.getIsHR();
    }

    public void load() {
        _connector.load();
    }
}
