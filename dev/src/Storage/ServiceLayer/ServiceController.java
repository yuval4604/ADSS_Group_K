package Storage.ServiceLayer;

import Storage.DomainLayer.DomainManager;
import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Facades.DomainFacade;
import Storage.DomainLayer.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ServiceController {
    private DomainManager manager;

    public ServiceController(Map<Integer, Product> productMap, DomainFacade facade) {
        this.manager = new DomainManager(productMap, facade);
    }

    public void addProduct(int catalogNumber, String name, String category, String subCategory, String size, double buyPrice, double salePrice, double discount, double supplierDiscount, String manufacturer, String aisle, int minimalQuantity) throws Exception {
        try {
            if (this.manager.getProduct(catalogNumber) != null)
                throw new IllegalArgumentException("Product with this catalog number already exists");
            if (!(Category.contains(category) && SubCategory.contains(subCategory) && SubSubCategory.contains(size)))
                throw new IllegalArgumentException("Invalid category");
            Product product = new Product(catalogNumber, name, Category.valueOf(category), SubCategory.valueOf(subCategory), SubSubCategory.valueOf(size), buyPrice, salePrice, discount, supplierDiscount, manufacturer, aisle, minimalQuantity);
            this.manager.addProduct(product);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public String getProduct(int catalogNumber) throws Exception {
        try{
            Product p = this.manager.getProduct(catalogNumber);
            if(p == null)
                throw new IllegalArgumentException("Product with this catalog number does not exist");
            return p.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // returns a string with the properties of the products in the given categories
    public String produceProductReport(List<String> categories) throws Exception{
        try {
            List<Product> info = manager.getProductsByCategories(categories);
            String report = "";
            for (Product product : info) {
                report += product.productReportToString() + "\n";
            }
            return report;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // returns a string with the damage properties of the products in the given categories
    public String produceDamageReport() throws Exception{
        try {
            Product[] info = (Product[]) manager.getProductMap().keySet().toArray();
            String report = "";
            for (Product product : info) {
                report += product.damagedReportToString() + "\n";
            }
            return report;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // removes the product from the system
    public void removeProduct(int catalogNumber) throws Exception{
        try {
            Product p = this.manager.getProduct(catalogNumber);
            this.manager.removeProduct(p);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // returns a string with the names of the products that are below the minimal quantity
    public String alertOnMinimalQuantity() throws Exception{
        return this.manager.alertOnMinimalQuantity();
    }

    // updates the discount of the given product
    public void updateDiscountForProduct(int catalogNumber, double discount) throws Exception{
        try {
            this.manager.setDiscount(catalogNumber, discount);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // updates the discount of all products in the given categories (the strings are like in getProductByCategories)
    public void updateDiscountForCategory(List<String> categories, double discount) throws Exception{
        try {
            List<Product> info = manager.getProductsByCategories(categories);
            for (Product product : info) {
                product.setDiscount(discount);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // updates the damage amount of the given product
    public void updateDamageForProduct(int catalogNumber, int inStore[], int inStorage[], LocalDate expirationDate[]) throws Exception{
        try {
            this.manager.getProduct(catalogNumber).moveToDamage(inStore, inStorage, expirationDate);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void moveProductToStore(int catalogNumber, LocalDate expirationDate, int quantity) throws Exception {
        try {
            this.manager.moveProductToStore(catalogNumber, expirationDate, quantity);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void subtractFromStore(int catalogNumber, Map<LocalDate,Integer> products) throws Exception{
        try {
            this.manager.subtractFromStore(catalogNumber, products);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Map<Integer,Integer> expiredCount() throws Exception{
        return this.manager.expiredCount();
    }

    public void addToProduct(LocalDate expirationDate, int catalogNumber, int inStore, int inStorage) throws Exception {
        try {
            this.manager.addToProduct(expirationDate, catalogNumber, inStore, inStorage);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void moveToExpired(int catalogNumber, LocalDate expirationDate, int inStore, int inStorage) throws Exception{
        try {
            this.manager.moveToExpired(catalogNumber, expirationDate, inStore, inStorage);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
