package workers.PresentationLayer;
import workers.DomainLayer.Connector;

import java.util.Scanner;

public class Main {
    public static void printCommands(boolean ishr) {
        if(ishr) {
            System.out.println("commands:\n0) command list \n1) logout\n2) change password\n3) Show your information\n4) Show your Constraints \n5) Set bank number\n6) use vacation days\n7) set your Constraints\n8) show your roles\nHR Only Commands:\n9) Set a worker's global wage\n10) Set a worker's hourly wage\n11) Set if a worker is full time job employee or not\n12) Set a worker's vacation days\n13) Reset a worker's vacation days\n14) add a worker\n15) work on shift\n16) change a worker's optional roles\n17) set half a day off\n18) set a full day off\n-1) Exit");
        }
        else {
            System.out.println("commands:\n0) command list \n1) logout\n2) change password\n3) Show your information\n4) Show your Constraints \n5) Set bank number\n6) use vacation days\n7) set your Constraints\n8) show your roles\n-1) Exit");
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the admin's password");
        String pass = scanner.nextLine();
        Connector connector = new Connector(pass);
        LoginManager lm = new LoginManager(connector);
        ActionManager ac = new ActionManager(connector);
        boolean isLoggedIn = false;
        boolean terminated = false;

        boolean ishr = false;
        while(!terminated) {
            if(!isLoggedIn) {
                isLoggedIn = lm.login();
                if(isLoggedIn) {
                    System.out.println("Connected successfully");
                    ishr = ac.isHR();
                    printCommands(ishr);
                }
                else {
                    System.out.println("Connection failed. Please try again");
                }
            }
            else{
                System.out.println("\nchoose command:");
                int result = scanner.nextInt(); // TODO try & catch
                scanner.nextLine();
                switch(result) {
                    case 0:
                        printCommands(ishr);
                        break;
                    case 1:
                        lm.logOut();
                        isLoggedIn = false;
                        System.out.println("Disconnected successfully");
                        break;
                    case 2:
                        lm.changePass();
                        break;
                    case 3:
                        ac.showWorkerInfo();
                        break;
                    case 4:
                        ac.showWorkerConstraints();
                        break;
                    case 5:
                        ac.setBankNumber();
                        break;
                    case 6:
                        ac.useVacationDays();
                        break;
                    case 7:
                        ac.addConstraints();
                        break;
                    case 8:
                        ac.showWorkerRoles();
                        break;
                    case 9:
                        if(ishr) {
                            ac.setWage();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case 10:
                        if(ishr) {
                            ac.setHWage();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case 11:
                        if(ishr) {
                            ac.setFullTimeJob();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case 12:
                        if(ishr) {
                            ac.setVacationDays();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case 13:
                        if(ishr) {
                            ac.ResetVacationDays();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case 14:
                        if(ishr) {
                            ac.addAWorker();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case 15:
                        if(ishr) {
                            ac.workOnShift();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case 16:
                        if(ishr) {
                            ac.changeWorkerRoles();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case 17:
                        if(ishr) {
                            ac.setHalfDayOff();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case 18:
                        if(ishr) {
                            ac.setAllDayOff();
                        }
                        else {
                            System.out.println("Error: no permission to do that");
                        }
                        break;
                    case -1:
                        terminated = true;
                        break;
                    default:
                        System.out.println("Invalid command");

                }
            }


        }
    }
}