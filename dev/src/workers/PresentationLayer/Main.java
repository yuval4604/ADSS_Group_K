package workers.PresentationLayer;
import workers.DomainLayer.Connector;

import java.util.Scanner;

public class Main {
    public static void printCommands(boolean isHR) {
        if(isHR) {
            System.out.println("commands:\n0) command list \n1) logout\n2) change password\n3) Show your information\n4) Show your Constraints \n5) Set bank number\n6) use vacation days\n7) set your Constraints\n8) show your roles\nHR Only Commands:\n9) Set a worker's global wage\n10) Set a worker's hourly wage\n11) Set if a worker is full time job employee or not\n12) Set a worker's vacation days\n13) Reset a worker's vacation days\n14) add a worker\n15) work on shift\n16) change a worker's optional roles\n17) set half a day off\n18) set a full day off\n-1) Exit");
        }
        else {
            System.out.println("commands:\n0) command list \n1) logout\n2) change password\n3) Show your information\n4) Show your Constraints \n5) Set bank number\n6) use vacation days\n7) set your Constraints\n8) show your roles\n-1) Exit");
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the password of the admin");
        String pass = scanner.nextLine();
        Connector connector = new Connector(pass);
        connector.checkUpdateDay();
        LoginManager lm = new LoginManager(connector);
        ActionManager ac = new ActionManager(connector);
        System.out.println("do you want to load the system with data? (y/n)");
        String ans = scanner.nextLine();
        if(ans.equals("y")) {
            ac.load();
        }
        boolean isLoggedIn = false;
        boolean terminated = false;

        boolean isHR = false;
        while(!terminated) {
            if(!isLoggedIn) {
                System.out.println("Welcome to the workers system");
                isLoggedIn = lm.login();
                if(isLoggedIn) {
                    System.out.println("Connected successfully");
                    isHR = ac.isHR();
                    printCommands(isHR);
                }
                else {
                    System.out.println("Connection failed. Please try again");
                }
            }
            else{
                System.out.println("\nchoose command:");
                int result;
                try {
                     result = scanner.nextInt();
                } catch (Exception e) {
                    System.out.println("Invalid command");
                    scanner.nextLine();
                    continue;
                }
                scanner.nextLine();
                switch (result) {
                    case 0 -> printCommands(isHR);
                    case 1 -> {
                        lm.logOut();
                        isLoggedIn = false;
                        System.out.println("Disconnected successfully");
                    }
                    case 2 -> lm.changePass();
                    case 3 -> ac.showWorkerInfo();
                    case 4 -> ac.showWorkerConstraints();
                    case 5 -> ac.setBankNumber();
                    case 6 -> ac.useVacationDays();
                    case 7 -> ac.addConstraints();
                    case 8 -> ac.showWorkerRoles();
                    case 9 -> {
                        if (isHR) {
                            ac.setWage();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 10 -> {
                        if (isHR) {
                            ac.setHWage();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 11 -> {
                        if (isHR) {
                            ac.setFullTimeJob();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 12 -> {
                        if (isHR) {
                            ac.setVacationDays();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 13 -> {
                        if (isHR) {
                            ac.ResetVacationDays();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 14 -> {
                        if (isHR) {
                            ac.addAWorker();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 15 -> {
                        if (isHR) {
                            ac.workOnShift();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 16 -> {
                        if (isHR) {
                            ac.changeWorkerRoles();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 17 -> {
                        if (isHR) {
                            ac.setHalfDayOff();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 18 -> {
                        if (isHR) {
                            ac.setAllDayOff();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case -1 -> terminated = true;
                    default -> System.out.println("Invalid command");
                }
            }


        }
    }
}