package workers.PresentationLayer;
import workers.DomainLayer.Connector;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose the admin's password");
        String pass = scanner.nextLine();
        Connector connector = new Connector(pass);
        LoginManager lm = new LoginManager(connector);
        ActionManager ac = new ActionManager(connector);
        boolean isLoggedIn = false;
        boolean terminated = false;


        while(!terminated) {
            if(!isLoggedIn) {
                isLoggedIn = lm.login();
                if(isLoggedIn) {
                    System.out.println("Connected successfully");
                }
                else {
                    System.out.println("Connection failed. Please try again");
                }
            }
            else{
                System.out.println("Choose command:\n1) logout\n2) change password\n3) Show your information\n4) Show your prefrences \n5) Set bank number\n6) use vacation days\n7) set your prefrences\nHR Only Commands:\n8) Set a worker's global wage\n9) Set a worker's hourly wage\n10) Set if a worker is full time job employee or not\n11) Set a worker's vacation days\n12) Reset a worker's vacation days\n13) add a worker\n14) work on shift\n15) change a worker's optional roles\n16) set half a day off\n17) set a full day off");
                int result = scanner.nextInt(); // TODO try & catch

                switch(result) {
                    case 1:
                        lm.logOut();
                        System.out.println("Disconnected successfully");
                        break;
                    case 2:
                        break;

                }
            }


        }
    }
}