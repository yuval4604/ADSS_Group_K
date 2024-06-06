package Storage.PresentationLayer;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Facades.CategoryFacade;
import Storage.DomainLayer.Facades.DomainFacade;
import Storage.DomainLayer.Facades.SubCategoryFacade;
import Storage.DomainLayer.Facades.SubSubCategoryFacade;
import Storage.DomainLayer.Product;

import java.time.LocalDate;
import java.util.HashMap;
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

    public static void setDomain(){
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

    public static void addProducts(){
        Map<LocalDate, Integer> expirationDates1 = new HashMap<>();
        expirationDates1.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(1, "beef number 1 100g", "Meat", "Beef", "Small", expirationDates1, 10, 20, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates2 = new HashMap<>();
        expirationDates2.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(2, "beef number 2 200g", "Meat", "Beef", "Medium", expirationDates2, 20, 40, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates3 = new HashMap<>();
        expirationDates3.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(3, "beef number 3 300g", "Meat", "Beef", "Large", expirationDates3, 30, 60, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates4 = new HashMap<>();
        expirationDates4.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(4, "chicken number 1 100g", "Meat", "Chicken", "Small", expirationDates4, 10, 20, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates5 = new HashMap<>();
        expirationDates5.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(5, "chicken number 2 200g", "Meat", "Chicken", "Medium", expirationDates5, 20, 40, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates6 = new HashMap<>();
        expirationDates6.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(6, "chicken number 3 300g", "Meat", "Chicken", "Large", expirationDates6, 30, 60, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates7 = new HashMap<>();
        expirationDates7.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(7, "fish number 1 100g", "Meat", "Pork", "Small", expirationDates7, 10, 20, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates8 = new HashMap<>();
        expirationDates8.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(8, "fish number 2 200g", "Meat", "Pork", "Medium", expirationDates8, 20, 40, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates9 = new HashMap<>();
        expirationDates9.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(9, "fish number 3 300g", "Meat", "Pork", "Large", expirationDates9, 30, 60, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates10 = new HashMap<>();
        expirationDates10.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(10, "grapes number 1 100g", "Fruits", "Grapes", "Small", expirationDates10, 10, 20, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates11 = new HashMap<>();
        expirationDates11.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(11, "grapes number 2 200g", "Fruits", "Grapes", "Medium", expirationDates11, 20, 40, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates12 = new HashMap<>();
        expirationDates12.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(12, "grapes number 3 300g", "Fruits", "Grapes", "Large", expirationDates12, 30, 60, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates13 = new HashMap<>();
        expirationDates13.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(13, "apples number 1 100g", "Fruits", "Apples", "Small", expirationDates13, 10, 20, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates14 = new HashMap<>();
        expirationDates14.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(14, "apples number 2 200g", "Fruits", "Apples", "Medium", expirationDates14, 20, 40, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates15 = new HashMap<>();
        expirationDates15.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(15, "apples number 3 300g", "Fruits", "Apples", "Large", expirationDates15, 30, 60, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates16 = new HashMap<>();
        expirationDates16.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(16, "bananas number 1 100g", "Fruits", "Bananas", "Small", expirationDates16, 10, 20, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates17 = new HashMap<>();
        expirationDates17.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(17, "bananas number 2 200g", "Fruits", "Bananas", "Medium", expirationDates17, 20, 40, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
        Map<LocalDate, Integer> expirationDates18 = new HashMap<>();
        expirationDates18.put(LocalDate.of(2024, 7, 10), 100);
        presentationController.addProduct(18, "bananas number 3 300g", "Fruits", "Bananas", "Large", expirationDates18, 30, 60, 0, 0, 100, 0, 0, "Tnuva", "1", 10);
    }
}
