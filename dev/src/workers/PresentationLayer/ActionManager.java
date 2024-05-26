package workers.PresentationLayer;
import java.util.List;
import workers.DomainLayer.Connector;
import workers.DomainLayer.Worker;

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
        }
        else if(charr.equals("f")){
            fullTime = false;
        }
        else {
            System.out.println("Didnt enter information correctly");
            return;
            // TODO - add error
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
        List<Worker>[] list = _connector.getAvailableWorkersOfRole(res);
        System.out.println("For the role, " + res + " , the workers who want to work this shift are: ");
        for(Worker worker : list[0]) {
            System.out.println(worker.getName() + ", " + worker.getID());
        }
        System.out.println("For the role, " + res + " , the workers who can work this shift are: ");
        for(Worker worker : list[1]) {
            System.out.println(worker.getName() + ", " + worker.getID());
        }
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

}
