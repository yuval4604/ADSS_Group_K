package workers.PresentationLayer;
import workers.DomainLayer.Connector;

import java.util.Scanner;

public class ActionManager {
    private Connector _connector;
    private Scanner scanner;

    public ActionManager(Connector connector) {
        _connector = connector;
        scanner = new Scanner(System.in);
    }


    public void setBankNumber() {

        System.out.println("Enter new bank number:");
        int bankNum = scanner.nextInt();
        if(bankNum < 0) {
            System.out.println("Can't have negative bank number");
            return;
        }
        _connector.setBank(bankNum);

    }
    public void setWage() {
        System.out.println("Enter Worker's id:");
        int id = scanner.nextInt();
        System.out.println("Enter new global wage:");
        int wage = scanner.nextInt();
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
        int id = scanner.nextInt();
        System.out.println("Enter new hourly wage: ");
        int wage = scanner.nextInt();
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
        int id = scanner.nextInt();
        System.out.println("full time job?: choose t/f");
        String charr = scanner.nextLine();
        boolean fullTime;
        if(charr.equals("t")) {
            fullTime = true;
            System.out.println("update the global wage of this employee: ");
            setWage();
        }
        else if(charr.equals("f")){
            fullTime = false;
            System.out.println("update the hourly wage of this employee: ");
            setHWage();
        }
        else {
            System.out.println("Didnt enter information correctly");
            return;
        }

        boolean res = _connector.setFullTimeJob(id,fullTime);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("changed successfully");
        }

    }
    public void setVacationDays() {
        System.out.println("Enter Worker's id: ");
        int id = scanner.nextInt();
        System.out.println("How many vication days?: ");
        int days = scanner.nextInt();
        if(days < 0) {
            System.out.println("Can't have negative vacation days");
            return;
        }
        boolean res = _connector.setVacationDays(id,days);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("changed successfully");
        }
    }
    public void ResetVacationDays() {
        System.out.println("Enter Worker's id: ");
        int id = scanner.nextInt();
        boolean res = _connector.ResetVacactionDays(id);
        if(!res) {
            System.out.println("User does not exist");
        }
        else {
            System.out.println("Reset successfully");
        }
    }
    public void useVacationDays() {
        System.out.println("How many vication days?: ");
        int days = scanner.nextInt();
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
        int id = scanner.nextInt();
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
        int id = scanner.nextInt();
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
        int dayOfWeek = scanner.nextInt() - 1;

        boolean res = _connector.createShift(id, date, dayShift, dayOfWeek);
        if(res) {
            System.out.println("created successfully");
        }
        else {
            System.out.println("Error: shift already exists or wrong info");
        }
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
            //TODO: check if inactive shift
        }
        else
        {
            System.out.println("Didnt choose one of the options");
        }
        boolean keepWorking = true;
        while (keepWorking)
        {
            System.out.println("choose command:\n1) add worker to shift\n2) show available workers of role\n3) exit");
            int result = scanner.nextInt();
            switch (result)
            {
                case 1:
                    addWorkerToShift();
                    break;
                case 2:
                    ShowAvailableWorkersOfRole();
                    break;
                case 3:
                    keepWorking = false;
                    break;
                default:
                    System.out.println("Didnt choose one of the options");
            }
        }
    }

    public void addAWorker()
    {
        System.out.println("Enter Worker's name: ");
        String name = scanner.nextLine();
        System.out.println("Enter Worker's id: ");
        int id = scanner.nextInt();
        System.out.println("Enter Worker's bank number: ");
        int bankNum = scanner.nextInt();
        System.out.println("Is the worker full time job?: choose t/f");
        String charr = scanner.nextLine();
        boolean fullTime;
        int globalWage = 0 ,hourlyWage = 0;
        if(charr.equals("t")) {
            fullTime = true;
            System.out.println("Enter Worker's global wage: ");
            globalWage = scanner.nextInt();
        }
        else if(charr.equals("f")){
            fullTime = false;
            System.out.println("Enter Worker's hourly wage: ");
            hourlyWage = scanner.nextInt();
        }
        else {
            System.out.println("Didnt enter information correctly");
            return;
        }
        System.out.println("Enter Worker's date of start: ");
        String dateOfStart = scanner.nextLine();
        System.out.println("Enter Worker's total vacation days: ");
        int totalVacationDays = scanner.nextInt();
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
        int id = scanner.nextInt();
        System.out.println("do you want to add or remove a role from him? choose a/r ");
        String aORr = scanner.nextLine();
        if(aORr.equals("a")){
            System.out.println("Enter Worker's new role: ");
            String role = scanner.nextLine();
            boolean res = _connector.addRole(id, role);
            if(res) {
                System.out.println("Worker's role has been changed!");
            }
            else {
                System.out.println("Error: Something went wrong :(");
            }
        } else if (aORr.equals("r")) {
            System.out.println("Enter Worker's role to remove: ");
            String role = scanner.nextLine();
            boolean res = _connector.removeRole(id, role);
            if(res) {
                System.out.println("Worker's role has been changed!");
            }
            else {
                System.out.println("Error: Something went wrong :(");
            }
        } else {
            System.out.println("Didnt choose one of the options");

        }

    }

}
