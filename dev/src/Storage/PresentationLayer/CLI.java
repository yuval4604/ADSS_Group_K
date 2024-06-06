package Storage.PresentationLayer;

import java.util.Map;
import java.util.Scanner;

public class CLI {

    private static PresentationController presentationController;

    public static void main(String[] args){
        System.out.println("Welcome to the Storage, what would you like to do:\n" +
                "1. Add products.\n" +
                "2. Move products.\n" +
                "3. decrease products quantity.\n" +
                "4. decrease damaged or expired products.\n" +
                "5. Produce products report.\n" +
                "6. Produce damaged and expired report.\n" +
                "7. Check for supplies below minimal quantity.\n" +
                "8. Check for expired products.\n" +
                "9. Exit program.");
        Scanner scan = new Scanner(System.in);
        String choice = scan.nextLine();

        while(!choice.equals("9")){
            switch (choice){
                case "1":{
                    System.out.println("Please enter the information of the new product batch:");
                    String info = "";
                    choice = scan.nextLine();
                    while(!choice.equals("finish")){
                        info += choice + ",";
                        choice = scan.nextLine();
                    }
                    info = info.substring(0,info.length());
                    presentationController.parseAddProductMessage(info);
                    break;
                }
                case "2":{
                    System.out.println("Please enter the information of the product batch you wish to move:");
                    String info = "";
                    choice = scan.nextLine();
                    while(!choice.equals("finish")){
                        info += choice + ",";
                        choice = scan.nextLine();
                    }
                    info = info.substring(0,info.length());
                    presentationController.parseMoveProductToStore(choice);
                    break;
                }
                case "3":{
                    System.out.println("Please enter the information of the product batch you wish to decrease:");
                    String info = "";
                    choice = scan.nextLine();
                    while(!choice.equals("finish")){
                        info += choice + ",";
                        choice = scan.nextLine();
                    }
                    info = info.substring(0,info.length());
                    presentationController.parseSubtractFromStore(info);
                    break;
                }
                case "4":{
                    System.out.println("Please enter the information of damaged batch you removed:");
                    String info = "";
                    choice = scan.nextLine();
                    while(!choice.equals("finish")){
                        info += choice + ",";
                        choice = scan.nextLine();
                    }
                    info = info.substring(0,info.length());
                    presentationController.parseUpdateDamageForProductMessage(info);
                    break;
                }
                case "5":{
                    System.out.println("Please enter the information of the product batch:");
                    String info = "";
                    choice = scan.nextLine();
                    while(!choice.equals("finish")){
                        info += choice + ",";
                        choice = scan.nextLine();
                    }
                    info = info.substring(0,info.length());
                    presentationController.parseProduceProductReportMessage(info);
                    break;
                }
                case "6":{
                    System.out.println("Please enter the information of the product batch:");
                    String info = "";
                    choice = scan.nextLine();
                    while(!choice.equals("finish")){
                        info += choice + ",";
                        choice = scan.nextLine();
                    }
                    info = info.substring(0,info.length());
                    presentationController.parseProduceDamageReportMessage(info);
                    break;
                }
                case "7":{
                    System.out.println("The products that need to be ordered are:");
                    presentationController.alertOnMinimalQuantity();
                    break;
                }
                case "8":{
                    System.out.println("The expired products are: ");
                    Map<Integer,Integer> expired = presentationController.expiredCount();
                    for(Map.Entry<Integer,Integer> entry : expired.entrySet())
                        System.out.println("Catalog number: " + entry.getKey() + ", quantity: " + entry.getValue());
                    break;
                }
                case "9":{

                    break;
                }
                default:
                    System.out.println("Invalid choice, please choose again.");
                    break;
            }
            System.out.println("Welcome to the Storage, what would you like to do:");
            choice = scan.nextLine();
        }
    }
}
