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
                System.out.println("Enter id");
                int id= scanner.nextInt();
                System.out.println("Enter password");
                String password = scanner.nextLine();
                isLoggedIn = lm.login(id,password);
                if(isLoggedIn) {
                    System.out.println("Connected successfully");
                }
                else {
                    System.out.println("Connection failed. Please try again");
                }
            }
            else{
                System.out.println("Choose command:\n1) logout\n2) //TODO");
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