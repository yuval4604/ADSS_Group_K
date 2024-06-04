package Storage.ServiceLayer;

import Storage.DomainLayer.DomainManager;
import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Facades.DomainFacade;
import Storage.DomainLayer.Product;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Controller {
    private DomainManager manager;

    public Controller(Map<Integer, Product> productMap, DomainFacade facade) {
        this.manager = new DomainManager(productMap, facade);
    }

    public void addProduct(int catalogNumber, String name, Category category, SubCategory subCategory, SubSubCategory size, Map<Date, Integer> expirationDates, double buyPrice, double salePrice, double discount, int storageQuantity, int storeQuantity, int damageQuantity, String manufacturer, String aisle, int minimalQuantity) {
        if(this.manager.getProduct(catalogNumber) != null)
            throw new IllegalArgumentException("Product with this catalog number already exists");
        if(!(Category.contains(category.toString()) && SubCategory.contains(subCategory.toString()) && SubSubCategory.contains(size.toString())))
            throw new IllegalArgumentException("Invalid category");
        Product product = new Product(catalogNumber, name, category, subCategory, size, expirationDates, buyPrice, salePrice, discount, storageQuantity, storeQuantity, damageQuantity, manufacturer, aisle, minimalQuantity);
        this.manager.addProduct(product);
    }

    public Product getProduct(int catalogNumber) { return this.manager.getProduct(catalogNumber); }

    // returns a string with the properties of the products in the given categories
    public String produceProductReport(List<String> categories){
        List<Product> info = manager.getProductsByCategories(categories);
        String report = "";
        for(Product product : info){
            report += product.productReportToString() + "\n";
        }
        return report;
    }

    // returns a string with the damage properties of the products in the given categories
    public String produceDamageReport(List<String> categories){
        List<Product> info = manager.getProductsByCategories(categories);
        String report = "";
        for(Product product : info){
            report += product.damagedReportToString() + "\n";
        }
        return report;
    }

    // removes the product from the system
    public void removeProduct(int catalogNumber) {
        Product p = this.getProduct(catalogNumber);
        this.manager.removeProduct(p);
    }

    // returns a string with the names of the products that are below the minimal quantity
    public String alertOnMinimalQuantity() {
        Category[] categories = Category.values();
        List<String> categoriesList = new java.util.ArrayList<String>();
        for(Category category : categories){
            categoriesList.add(category.toString());
        }
        List<Product> info = manager.getProductsByCategories(categoriesList);
        String alert = "";
        for(Product product : info){
            if(product.getStorageQuantity() + product.getStoreQuantity() <= product.getMinimalQuantity()){
                alert += product.getName() + "\n";
            }
        }
        return alert;
    }

    // updates the discount of the given product
    public void updateDiscountForProduct(int catalogNumber, double discount){
        this.getProduct(catalogNumber).setDiscount(discount);
    }

    // updates the discount of all products in the given categories (the strings are like in getProductByCategories)
    public void updateDiscountForCategory(List<String> categories, double discount){
        List<Product> info = manager.getProductsByCategories(categories);
        for(Product product : info){
            product.setDiscount(discount);
        }
    }

    // updates the damage amount of the given product
    public void updateDamageForProduct(int catalogNumber, int inStore, int inStorage, Date expirationDate){
        this.getProduct(catalogNumber).moveToDamage(inStore, inStorage, expirationDate);
    }
}
