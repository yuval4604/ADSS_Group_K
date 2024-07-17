package src.workers.PresentationLayer;

import src.workers.DomainLayer.Facade;

import java.util.Scanner;

public class LoginManager {
    private Facade _facade;
    private Scanner scanner;
    public LoginManager(Facade facade) {
        _facade = facade;
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
        boolean ans = _facade.login(id,password);
        if (ans)
        {
            if(!_facade.hasChangedPassword())
            {
                System.out.println("You must change your password");
                while (!changePass());
            }
        }
        return ans;
    }
    public void logOut() {
        _facade.logOut();
    }

    public boolean changePass() {
        System.out.println("Enter old password: ");
        String oldPass = scanner.nextLine();
        System.out.println("Enter new password: the password must contains upper case lower case and number and be at least 8 characters");
        String newPass = scanner.nextLine();
        boolean res = _facade.changePassword(oldPass,newPass);
        if(res) {
            System.out.println("password changed successfully");
            return true;
        }
        else {
            System.out.println("Error: no permission to do that Or wrong password");
            return false;
        }
    }
}
