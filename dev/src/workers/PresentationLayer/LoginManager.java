package workers.PresentationLayer;

import workers.DomainLayer.Connector;

import java.util.Scanner;

public class LoginManager {
    private Connector _connector;
    private Scanner scanner;
    public LoginManager(Connector connector) {
        _connector = connector;
        scanner = new Scanner(System.in);
    }
    public boolean login() {
        System.out.println("Enter id");
        int id= scanner.nextInt();
        System.out.println("Enter password");
        String password = scanner.nextLine();
        return _connector.login(id,password);

    }
    public void logOut() {
        _connector.logOut();
    }
}
