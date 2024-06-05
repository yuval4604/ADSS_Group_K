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
        int id;
        try {
            id = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid id");
            scanner.nextLine();
            return false;
        }
        scanner.nextLine();
        System.out.println("Enter password");
        String password = scanner.nextLine();
        boolean ans = _connector.login(id,password);
        if (ans)
        {
            if(_connector.hasChangedPassword())
            {
                System.out.println("You must change your password");
                changePass();
            }
        }
        return ans;
    }
    public void logOut() {
        _connector.logOut();
    }

    public void changePass() {
        System.out.println("Enter old password: ");
        String oldPass = scanner.nextLine();
        System.out.println("Enter new password: ");
        String newPass = scanner.nextLine();
        boolean res = _connector.changePassword(oldPass,newPass);
        if(res) {
            System.out.println("password changed successfully");
        }
        else {
            System.out.println("Error: no permission to do that Or wrong password");
        }
    }
}
