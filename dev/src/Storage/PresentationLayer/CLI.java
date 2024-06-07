package Storage.PresentationLayer;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Facades.CategoryFacade;
import Storage.DomainLayer.Facades.DomainFacade;
import Storage.DomainLayer.Facades.SubCategoryFacade;
import Storage.DomainLayer.Facades.SubSubCategoryFacade;
import Storage.DomainLayer.Product;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CLI {

    private static PresentationController presentationController;

    public static void main(String[] args) throws Exception {
        resetFactory();
        String choice = "";
        Scanner scan = new Scanner(System.in);
        String info;
        while(!choice.equals("15")){
            info = "";
            System.out.println("Welcome to the Storage, what would you like to do:\n" +
                    "1. Get product information.\n" +
                    "2. Add products.\n" +
                    "3. Add to product.\n" +
                    "4. Move products to store.\n" +
                    "5. Decrease products quantity.\n" +
                    "6. Remove damage products.\n" +
                    "7. Remove expired products.\n" +
                    "8. Update discount for product.\n" +
                    "9. Update discount for categories.\n" +
                    "10. Remove products.\n" +
                    "11. Produce products report.\n" +
                    "12. Produce damaged and expired report.\n" +
                    "13. Check for products below minimal quantity.\n" +
                    "14. Check for expired products.\n" +
                    "15. Exit program.");
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
                        System.out.println("If you wish to subtract more products from store, please enter 'y'. Otherwise, please enter '.'");
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
                        System.out.println("If you wish to subtract more products from store, please enter 'y'. Otherwise, please enter '.'");
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
                    System.out.println("Please enter the catalog number of the product you wish to update the discount for: ");
                    info += scan.nextLine() + ",";
                    System.out.println("Please enter the new discount for the product: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseUpdateDiscountForProductMessage(info));
                    break;
                }
                case "9":{
                    System.out.println("Please enter the new discount for the categories: ");
                    info += scan.nextLine() + ";";
                    info += getCategoriesFromUser(scan);
                    System.out.println(presentationController.parseUpdateDiscountForCategoryMessage(info));
                    break;
                }
                case "10":{
                    System.out.println("Please enter the catalog number of the product you wish to remove: ");
                    info += scan.nextLine();
                    System.out.println(presentationController.parseRemoveProductMessage(info));
                    break;
                }
                case "11":{
                    info = getCategoriesFromUser(scan);
                    System.out.println(presentationController.parseProduceProductReportMessage(info));
                    break;
                }
                case "12":{
                    System.out.println(presentationController.parseProduceDamageReportMessage());
                    break;
                }
                case "13":{
                    System.out.println("The products that need to be ordered are:");
                    System.out.println(presentationController.alertOnMinimalQuantity());
                    break;
                }
                case "14":{
                    System.out.println("The expired products are: ");
                    System.out.println(presentationController.expiredCount());
                    break;
                }
                case "15":{
                    break;
                }
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
                    System.out.println(getSubSubCategories());
                    System.out.println("If you wish to get the entire sub category, please enter 'all'.");
                    next = scan.nextLine();
                    if (!next.equals("all"))
                        info += next + ";";
                    else
                        info += ";";
                } else
                    info += ";";
                System.out.println("If you are done choosing please press '.'");
                next = scan.nextLine();
            }
            else {
                String categories = "";
                for(Category category : Category.values()){
                    categories += category.toString() + ";";
                }
                return categories.substring(0, categories.length());
            }
        }
        return info.substring(0,info.length()-1);
    }

    public static String getCategories(){
        String categories = "";
        for(Category category : Category.values()){
            categories += category.toString() + "\n";
        }
        return categories.substring(0,categories.length()-1);
    }

    public static String getSubCategories(){
        String subCategories = "";
        for(SubCategory subCategory : SubCategory.values()){
            subCategories += subCategory.toString() + "\n";
        }
        return subCategories.substring(0,subCategories.length()-1);
    }

    public static String getSubSubCategories(){
        String subSubCategories = "";
        for(SubSubCategory subSubCategory : SubSubCategory.values()){
            subSubCategories += subSubCategory.toString() + "\n";
        }
        return subSubCategories.substring(0,subSubCategories.length()-1);
    }

    public static void resetFactory() throws Exception{
        SubSubCategoryFacade ssc1small = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc1medium = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc1large = new SubSubCategoryFacade();
        Map<SubSubCategory, SubSubCategoryFacade> ssc1 = new HashMap<>();
        ssc1.put(SubSubCategory.Small, ssc1small);
        ssc1.put(SubSubCategory.Medium, ssc1medium);
        ssc1.put(SubSubCategory.Large, ssc1large);
        SubCategoryFacade sc1 = new SubCategoryFacade();
        sc1.setSubSubCategories(ssc1);
        SubSubCategoryFacade ssc2small = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc2medium = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc2large = new SubSubCategoryFacade();
        Map<SubSubCategory, SubSubCategoryFacade> ssc2 = new HashMap<>();
        ssc1.put(SubSubCategory.Small, ssc2small);
        ssc1.put(SubSubCategory.Medium, ssc2medium);
        ssc1.put(SubSubCategory.Large, ssc2large);
        SubCategoryFacade sc2 = new SubCategoryFacade();
        sc2.setSubSubCategories(ssc2);
        SubSubCategoryFacade ssc3small = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc3medium = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc3large = new SubSubCategoryFacade();
        Map<SubSubCategory, SubSubCategoryFacade> ssc3 = new HashMap<>();
        ssc1.put(SubSubCategory.Small, ssc3small);
        ssc1.put(SubSubCategory.Medium, ssc3medium);
        ssc1.put(SubSubCategory.Large, ssc3large);
        SubCategoryFacade sc3 = new SubCategoryFacade();
        sc3.setSubSubCategories(ssc3);
        Map<SubCategory, SubCategoryFacade> s1 = new HashMap<>();
        s1.put(SubCategory.Beef, sc1);
        s1.put(SubCategory.Chicken, sc1);
        s1.put(SubCategory.Pork, sc1);
        CategoryFacade c1 = new CategoryFacade();
        c1.setSubCategories(s1);
        SubSubCategoryFacade ssc4small = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc4medium = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc4large = new SubSubCategoryFacade();
        Map<SubSubCategory, SubSubCategoryFacade> ssc4 = new HashMap<>();
        ssc4.put(SubSubCategory.Small, ssc4small);
        ssc4.put(SubSubCategory.Medium, ssc4medium);
        ssc4.put(SubSubCategory.Large, ssc4large);
        SubCategoryFacade sc4 = new SubCategoryFacade();
        sc4.setSubSubCategories(ssc4);
        SubSubCategoryFacade ssc5small = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc5medium = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc5large = new SubSubCategoryFacade();
        Map<SubSubCategory, SubSubCategoryFacade> ssc5 = new HashMap<>();
        ssc5.put(SubSubCategory.Small, ssc5small);
        ssc5.put(SubSubCategory.Medium, ssc5medium);
        ssc5.put(SubSubCategory.Large, ssc5large);
        SubCategoryFacade sc5 = new SubCategoryFacade();
        sc5.setSubSubCategories(ssc5);
        SubSubCategoryFacade ssc6small = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc6medium = new SubSubCategoryFacade();
        SubSubCategoryFacade ssc6large = new SubSubCategoryFacade();
        Map<SubSubCategory, SubSubCategoryFacade> ssc6 = new HashMap<>();
        ssc6.put(SubSubCategory.Small, ssc6small);
        ssc6.put(SubSubCategory.Medium, ssc6medium);
        ssc6.put(SubSubCategory.Large, ssc6large);
        SubCategoryFacade sc6 = new SubCategoryFacade();
        sc6.setSubSubCategories(ssc6);
        Map<SubCategory, SubCategoryFacade> s2 = new HashMap<>();
        s2.put(SubCategory.Grapes, sc4);
        s2.put(SubCategory.Apples, sc5);
        s2.put(SubCategory.Bananas, sc6);
        CategoryFacade c2 = new CategoryFacade();
        c2.setSubCategories(s2);
        Map<Integer, Product> products = new HashMap<>();
        DomainFacade facade = new DomainFacade();
        Map<Category, CategoryFacade> categories = new HashMap<>();
        categories.put(Category.Meat, c1);
        categories.put(Category.Fruits, c2);
        facade.setCategories(categories);
        presentationController = new PresentationController(products, facade);
    }

    public static void productFactory() throws Exception{
        presentationController.parseAddProductMessage("1,beef number,1 100g,Meat,Beef,Small,10,20,0,0,Tnuva,1,10");
        presentationController.parseAddProductMessage("2,beef number 2 200g,Meat,Beef,Medium,20,40,0,0,Tnuva,1,10");
        presentationController.parseAddProductMessage("3,beef number 3 300g,Meat,Beef,Large,30,60,0,0,Tnuva,1,10");
        presentationController.parseAddProductMessage("4,chicken number 1 100g,Meat,Chicken,Small,10,20,0,0,Tnuva,1,10");
        presentationController.parseAddProductMessage("5,chicken number 2 200g,Meat,Chicken,Medium,20,40,0,0,Tnuva,1,10");
        presentationController.parseAddProductMessage("6,chicken number 3 300g,Meat,Chicken,Large,30,60,0,0,Tnuva,1,10");
        presentationController.parseAddProductMessage("7,fish number 1 100g,Meat,Pork,Small,15,30,0,0.15,Tnuva,1,10");
        presentationController.parseAddProductMessage("8,fish number 2 200g,Meat,Pork,Medium,30,60,0,0.15,Tnuva,1,10");
        presentationController.parseAddProductMessage("9,fish number 3 300g,Meat,Pork,Large,45,90,0,0.15,Tnuva,1,10");
        presentationController.parseAddProductMessage("10,grapes number 1 100g,Fruits,Grapes,Small,5,10,0,0,Tnuva,7,10");
        presentationController.parseAddProductMessage("11,grapes number 2 200g,Fruits,Grapes,Medium,10,20,0,0,Tnuva,7,10");
        presentationController.parseAddProductMessage("12,grapes number 3 300g,Fruits,Grapes,Large,15,30,0,0,Tnuva,7,10");
        presentationController.parseAddProductMessage("13,apples number 1 100g,Fruits,Apples,Small,3,7,0,0,Tnuva,8,10");
        presentationController.parseAddProductMessage("14,apples number 2 200g,Fruits,Apples,Medium,6,14,0,0,Tnuva,8,10");
        presentationController.parseAddProductMessage("15,apples number 3 300g,Fruits,Apples,Large,9,21,0,0,Tnuva,8,10");
        presentationController.parseAddProductMessage("16,bananas number 1 100g,Fruits,Bananas,Small,5,10,0,0,Tnuva,8,10");
        presentationController.parseAddProductMessage("17,bananas number 2 200g,Fruits,Bananas,Medium,10,20,0,0,Tnuva,8,10");
        presentationController.parseAddProductMessage("18,bananas number 3 300g,Fruits,Bananas,Large,15,30,0,0,Tnuva,8,10");
    }
}
