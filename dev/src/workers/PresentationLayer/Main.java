package src.workers.PresentationLayer;
import src.workers.DomainLayer.Facade;

import java.util.Scanner;

public class Main {
    public static void printCommands(boolean isHR, boolean isBM) {
        System.out.println("commands:\n-1) Exit\n0) command list \n1) logout\n2) change password\n3) Show your information\n4) Show your Constraints \n5) Set bank number\n6) use vacation days\n7) set your Constraints\n8) show your roles\n9) show branch\n10)show your shifts\n11)altar on going shift\n12) add license\n13) file storage reports and logs");
        if(isBM)
            System.out.println("BM and HR Only Commands:\n14) Set a worker's global wage\n15) Set a worker's hourly wage\n16) Set if a worker is full time job employee or not\n17) Set a worker's vacation days\n18) Reset a worker's vacation days\n19) add a worker to your branch\n20) work on shift of your branch\n21) change a worker's optional roles\n22) set half a day off for your branch\n23) set a full day off for your branch\n24) set the deadline for your branch workers constraints\n25) set your branch's minimal workers for a shift\n26) show your branch's information\n27) remove a worker from your branch");
        if(isHR)
            System.out.println("HR Only Commands:\n28) add a worker\n29) add branch\n30) remove branch\n31) enter a worker's resignation\n32) fire s worker\n33) create a head of branch\n34)set the head of branch\n35) show branches  ");
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Facade facade = new Facade();
        LoginManager lm = new LoginManager(facade);
        ActionManager ac = new ActionManager(facade);
        boolean isLoggedIn = false;
        boolean terminated = false;

        boolean isHR = false;
        boolean isBM = false;
        boolean seen = false;
        while(!terminated) {
            if(!isLoggedIn) {
                System.out.println("Welcome to the workers system");
                if(!seen) {
                    System.out.println("Do you want to login or exit the app? l/e");
                    String choice = scanner.nextLine();
                    if (choice.equals("e")) {
                        System.out.println("GoodBye!");
                        return;
                    } else if (!choice.equals("l")) {
                        System.out.println("you didn't choose any of the option you are moved to login in");
                    }
                    seen = true;
                }
                isLoggedIn = lm.login();
                if(isLoggedIn) {
                    facade.checkUpdateDay();
                    System.out.println("Connected successfully");
                    isHR = ac.isHR();
                    isBM = ac.isBM();
                    printCommands(isHR, isBM);
                }
                else {
                    System.out.println("Connection failed. Please try again");
                }
            }
            else{
                seen = false;
                System.out.println("\nchoose command: (0 for command list)");
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
                    case 0 -> printCommands(isHR, isBM);
                    case 1 -> {
                        lm.logOut();
                        isLoggedIn = false;
                        seen = false;
                        System.out.println("Disconnected successfully");
                    }
                    case 2 -> lm.changePass();
                    case 3 -> ac.showWorkerInfo();
                    case 4 -> ac.showWorkerConstraints();
                    case 5 -> ac.setBankNumber();
                    case 6 -> ac.useVacationDays();
                    case 7 -> ac.addConstraints();
                    case 8 -> ac.showWorkerRoles();
                    case 9 -> ac.getBranch();
                    case 10 ->ac.showWorkerShifts();
                    case 11 -> ac.altarOnGoingShift();
                    case 12 -> ac.addLicense();
                    case 13 -> ac.storage();
                    case 14 -> {
                        if (isBM) {
                            ac.setWage(-1);
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 15 -> {
                        if (isBM) {
                            ac.setHWage(-1);
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 16 -> {
                        if (isBM) {
                            ac.setFullTimeJob();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 17 -> {
                        if (isBM) {
                            ac.setVacationDays();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 18 -> {
                        if (isBM) {
                            ac.ResetVacationDays();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 19 -> {
                        if(isBM)
                            ac.addWorkerToBranch();
                        else
                            System.out.println("Error: no permission to do that");
                    }
                    case 20 -> {
                        if (isBM) {
                            ac.workOnShift();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 21 -> {
                        if (isBM) {
                            ac.changeWorkerRoles();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 22 -> {
                        if (isBM) {
                            ac.setHalfDayOff();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 23 -> {
                        if (isBM) {
                            ac.setAllDayOff();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 24 -> {
                        if (isBM) {
                            ac.setLastDayForConstraints();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 25 -> {
                        if (isBM) {
                            ac.setMinimalWorkers();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 26 -> {
                        if (isBM) {
                            ac.showBranch();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 27 ->{
                        if(isBM)
                            ac.removeWorkerFromBranch();
                        else
                            System.out.println("Error: no permission to do that");
                    }
                    case 28 -> {
                        if (isHR) {
                            ac.addAWorker();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 29 -> {
                        if(isHR)
                            ac.addBranch();
                        else
                            System.out.println("Error: no permission to do that");
                    }
                    case 30 -> {
                        if(isHR)
                            ac.removeBranch();
                        else
                            System.out.println("Error: no permission to do that");
                    }
                    case 31 -> {
                        if(isHR)
                            ac.endContranct30DaysFromNow();
                        else
                            System.out.println("Error: no permission to do that");
                    }
                    case 32 -> {
                        if(isHR)
                            ac.fireWorker();
                        else
                            System.out.println("Error: no permission to do that");
                    }
                    case 33 -> {
                        if(isHR)
                            ac.createBM();
                        else
                            System.out.println("Error: no permission to do that");
                    }
                    case 34 -> {
                        if (isHR) {
                            ac.setHeadOfBranch();
                        } else {
                            System.out.println("Error: no permission to do that");
                        }
                    }
                    case 35 -> {
                        if(isHR)
                            ac.showBranches();
                        else
                            System.out.println("Error: no permission to do that");
                    }
                    case -1 -> terminated = true;
                    default -> System.out.println("Invalid command");
                }
            }


        }
    }
}