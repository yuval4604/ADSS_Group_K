package Storage.PresentationLayer;

import Storage.DataAccessLayer.Repository;
import Storage.DomainLayer.DomainManager;
import Storage.DomainLayer.Product;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CLI {

    private static PresentationController presentationController;
    private static final int DAY_OF_WEEK = 7;
    private static final int DAY_OF_MONTH = 1;
    private static boolean isDamageReportAlerted = false;
    private static boolean isProductReportAlerted = false;
    public static void main(String[] args) throws Exception {
        resetFactory();
        Scanner scan = new Scanner(System.in);
        String choice = "";
        String info;
        while(!choice.equals("24")){
            if(!isDamageReportAlerted && LocalDate.now().getDayOfMonth() == DAY_OF_MONTH){
                System.out.println("It is time to produce the damage report. Please choose option 18.");
                isDamageReportAlerted = true;
            }
            else if(isDamageReportAlerted && LocalDate.now().getDayOfMonth() != DAY_OF_MONTH){
                isDamageReportAlerted = false;
            }
            if(!isProductReportAlerted && LocalDate.now().getDayOfWeek().getValue() == DAY_OF_WEEK){
                System.out.println("It is time to produce the product report. Please choose option 17, and enter the categories you wish to produce a report for.");
                isProductReportAlerted = true;
            }
            else if(isProductReportAlerted && LocalDate.now().getDayOfWeek().getValue() != DAY_OF_WEEK){
                isProductReportAlerted = false;
            }
            info = "";
            System.out.println("Welcome to the Storage, what would you like to do:\n" +
                    "1. Get product information.\n" +
                    "2. Add products.\n" +
                    "3. Add to product.\n" +
                    "4. Move products to store.\n" +
                    "5. Decrease products quantity.\n" +
                    "6. Remove damage products.\n" +
                    "7. Remove expired products.\n" +
                    "8. Update buy price for product.\n" +
                    "9. Update sale price for product.\n" +
                    "10. Update supplier discount for product.\n" +
                    "11. Update manufacturer for product.\n" +
                    "12. Update aisle for product.\n" +
                    "13. Update minimal quantity for product.\n" +
                    "14. Update discount for product.\n" +
                    "15. Update discount for categories.\n" +
                    "16. Remove products.\n" +
                    "17. Produce products report.\n" +
                    "18. Produce damaged and expired report.\n" +
                    "19. Check for products below minimal quantity.\n" +
                    "20. Check for expired products.\n" +
                    "21. Add category.\n" +
                    "22. Add sub category.\n" +
                    "23. Add size.\n" +
                    "24. Exit program.");
            choice = scan.nextLine();
            switch (choice){
                case "1":{
                    System.out.println("Please enter the catalog number of the product you wish to get information for: ");
                    System.out.println(presentationController.parseGetProductMessage(scan.nextLine()));
                    break;
                }
                case "2":{
                    System.out.println("Please enter the catalog number of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the name of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the category of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the sub category of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the size of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the buy price of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the sale price of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the discount of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the supplier discount of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the manufacturer of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the aisle of this product: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the minimal quantity of this product: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseAddProductMessage(info));
                    break;
                }
                case "3":{
                    System.out.println("Please enter the catalog number of the product you wish to add to: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the expiration date of the product you wish to add: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the quantity in store of the product you wish to add: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the quantity in storage of the product you wish to add: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseAddToProductMessage(info));
                    break;
                }
                case "4":{
                    System.out.println("Please enter the catalog number of the product you wish to move: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the date of the product you wish to move: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the quantity of the product you wish to move: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseMoveProductToStore(info));
                    break;
                }
                case "5":{
                    System.out.println("Please enter the catalog number of the product you wish to subtract from store: ");
                    info += scan.nextLine() + ",";
                    String next = "";
                    int i = 0;
                    while(!next.equals(".")){
                        System.out.println("Please enter the date of the product you wish to subtract from store: ");
                        next = scan.nextLine();
                        info += next + ",";
                        System.out.println("Please enter the quantity of the product you wish to subtract from store: ");
                        next = scan.nextLine();
                        info += next + ",";
                        i++;
                        System.out.println("If you wish to stop please enter '.'.Otherwise press any other key.");
                        next = scan.nextLine();
                    }
                    if(i != 0) {
                        info = info.substring(0, info.length() - 1);
                        System.out.println(presentationController.parseSubtractFromStore(info));
                    }
                    else
                        System.out.println("No products were subtracted from store.");
                    break;
                }
                case "6":{
                    System.out.println("Please enter the catalog number of the product you wish to remove damage from: ");
                    info += scan.nextLine() + ",";
                    String next = "";
                    while(!next.equals(".")){
                        System.out.println("Please enter the date of the product you wish to remove damage from: ");
                        next = scan.nextLine();
                        info += next + ",";
                        System.out.println("Please enter the quantity in store of the product you wish to remove damage from: ");
                        next = scan.nextLine();
                        info += next + ",";
                        System.out.println("Please enter the quantity in storage of the product you wish to remove damage from: ");
                        next = scan.nextLine();
                        info += next + ",";
                        System.out.println("If you wish to stop, please enter '.'. Otherwise, press any other key");
                        next = scan.nextLine();
                    }
                    info = info.substring(0,info.length()-1);
                    System.out.println(presentationController.parseUpdateDamageForProductMessage(info));
                    break;
                }
                case "7":{
                    System.out.println("Please enter the catalog number of the product you wish to move to expired: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the date of the product you wish to move to expired: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the quantity in store of the product you wish to move to expired: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the quantity in storage of the product you wish to move to expired: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseMoveToExpiredMessage(info));
                    break;
                }
                case "8":{
                    System.out.println("Please enter the catalog number of the product you wish to update the buy price for: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the new buy price for the product: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseUpdateBuyPriceForProductMessage(info));
                    break;
                }
                case "9":{
                    System.out.println("Please enter the catalog number of the product you wish to update the sale price for: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the new sale price for the product: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseUpdateSalePriceForProductMessage(info));
                    break;
                }
                case "10":{
                    System.out.println("Please enter the catalog number of the product you wish to update the supplier discount for: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the new supplier discount for the product: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseUpdateSupplierDiscountForProductMessage(info));
                    break;
                }
                case "11":{
                    System.out.println("Please enter the catalog number of the product you wish to update the manufacturer for: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the new manufacturer for the product: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseUpdateManufacturerForProductMessage(info));
                    break;
                }
                case "12":{
                    System.out.println("Please enter the catalog number of the product you wish to update the aisle for: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the new aisle for the product: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseUpdateAisleForProductMessage(info));
                    break;
                }
                case "13":{
                    System.out.println("Please enter the catalog number of the product you wish to update the minimal quantity for: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the new minimal quantity for the product: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseUpdateMinimalQuantityForProductMessage(info));
                    break;
                }
                case "14":{
                    System.out.println("Please enter the catalog number of the product you wish to update the discount for: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the new discount for the product: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseUpdateDiscountForProductMessage(info));
                    break;
                }
                case "15":{
                    System.out.println("Please enter the new discount for the categories: ");
                    info += scan.nextLine() + ";";
                    info += getCategoriesFromUser(scan);
                    System.out.println(presentationController.parseUpdateDiscountForCategoryMessage(info));
                    break;
                }
                case "16":{
                    System.out.println("Please enter the catalog number of the product you wish to remove: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseRemoveProductMessage(info));
                    break;
                }
                case "17":{
                    info = getCategoriesFromUser(scan);
                    System.out.println(presentationController.parseProduceProductReportMessage(info));
                    break;
                }
                case "18":{
                    System.out.println(presentationController.parseProduceDamageReportMessage());
                    break;
                }
                case "19":{
                    System.out.println("The products that need to be ordered are:");
                    System.out.println(presentationController.alertOnMinimalQuantity());
                    break;
                }
                case "20":{
                    System.out.println("The expired products are: ");
                    System.out.println(presentationController.expiredCount());
                    break;
                }
                case "21":{
                    System.out.println("Please enter the name of the category you wish to add: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseAddCategoryMessage(info));
                    break;
                }
                case "22":{
                    System.out.println("Please enter the name of the sub category you wish to add: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseAddSubCategoryMessage(info));
                    break;
                }
                case "23":{
                    System.out.println("Please enter the name of the size you wish to add: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseAddSizeMessage(info));
                    break;
                }
                case "24":
                    break;
                default:
                    System.out.println("Invalid choice, please choose again.");
                    break;
            }
        }
        System.out.println("Goodbye :)");
    }

    public static String getCategoriesFromUser(Scanner scan) throws Exception{
        String info = "";
        String next = "";
        while(!next.equals(".")){
            System.out.println("If you wish to select all of the categories, please enter 'all'.\nIf not, please enter the category of the product you wish to produce a report for: ");
            System.out.println(getCategories());
            next = scan.nextLine();
            if(!next.equals("all")) {
                info += next + ",";
                System.out.println("Please enter the sub category you wish to produce a report for: ");
                System.out.println(getSubCategories());
                System.out.println("If you wish to get the entire category, please enter 'all'.");
                next = scan.nextLine();
                if (!next.equals("all")) {
                    info += next + ",";
                    System.out.println("Please enter the size you wish to produce a report for: ");
                    System.out.println(getSizes());
                    System.out.println("If you wish to get the entire sub category, please enter 'all'.");
                    next = scan.nextLine();
                    if (!next.equals("all"))
                        info += next + ";";
                    else
                        info += ";";
                } else
                    info += ";";
                System.out.println("If you are done choosing please press '.'. Otherwise, press any other key.");
                next = scan.nextLine();
            }
            else {
                String categories = "";
                for(String category : DomainManager.categories){
                    categories += category + ";";
                }
                return categories.substring(0, categories.length());
            }
        }
        return info.substring(0,info.length()-1);
    }

    public static String getCategories(){
        String categories = "";
        for(String category : DomainManager.categories){
            categories += category + "\n";
        }
        return categories.substring(0,categories.length()-1);
    }

    public static String getSubCategories(){
        String subCategories = "";
        for(String subCategory : DomainManager.subCategories){
            subCategories += subCategory + "\n";
        }
        return subCategories.substring(0,subCategories.length()-1);
    }

    public static String getSizes(){
        String sizes = "";
        for(String size : DomainManager.sizes){
            sizes += size + "\n";
        }
        return sizes.substring(0,sizes.length()-1);
    }

    public static void resetFactory() throws Exception{
        Map<Integer, Product> products = new HashMap<>();
        Repository repo = new Repository("data_layer.db");
        presentationController = new PresentationController(products, repo);
    }

}
