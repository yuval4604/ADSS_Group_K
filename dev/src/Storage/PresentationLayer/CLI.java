package Storage.PresentationLayer;

import java.util.Scanner;

public class CLI {

    private static PresentationController presentationController;


    public static void main(String[] args){
        System.out.println("Welcome to the Storage, what would you like to do:\n" +
                "1. Add products.\n" +
                "2. Move products.\n" +
                "3. decrease products.\n" +
                "4. decrease damaged products.\n" +
                "5. Produce products report.\n" +
                "6. Produce damaged and expired report.\n" +
                "7. Check for supplies below minimal quantity.\n" +
                "8. Check for expired products.\n" +
                "9. Exit program.");
        Scanner scan = new Scanner(System.in);
        String choice = scan.nextLine();

        while(choice != "9"){
            switch (choice){
                case "1":{
                    presentationController.addProduct();
                    break;
                }
                case "2":{

                    break;
                }
                case "3":{

                    break;
                }
                case "4":{

                    break;
                }
                case "5":{

                    break;
                }
                case "6":{

                    break;
                }
                case "7":{

                    break;
                }
                case "8":{

                    break;
                }
                case "9":{

                    break;
                }
                default:
                    break;
            }
        }
    }
}
