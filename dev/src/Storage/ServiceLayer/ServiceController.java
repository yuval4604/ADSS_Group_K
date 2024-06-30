package Storage.ServiceLayer;

import Storage.DataAccessLayer.Repository;
import Storage.DomainLayer.DomainManager;
import Storage.DomainLayer.Product;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ServiceController {
    private DomainManager manager;

    public ServiceController(Map<Integer, Product> productMap, Repository repository) {
        this.manager = new DomainManager(productMap, repository);
    }

    public void addProduct(int catalogNumber, String name, String category, String subCategory, String size, double buyPrice, double salePrice, double discount, double supplierDiscount, String manufacturer, String aisle, int minimalQuantity) throws Exception {
        try {
            if (this.manager.getProduct(catalogNumber) != null)
                throw new IllegalArgumentException("Product with this catalog number already exists");
            if (!(DomainManager.categories.contains(category) && DomainManager.subCategories.contains(subCategory) && DomainManager.sizes.contains(size)))
                throw new IllegalArgumentException("Invalid category");
            Product product = new Product(catalogNumber, name, category, subCategory, size, buyPrice, salePrice, discount, supplierDiscount, manufacturer, aisle, minimalQuantity);
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
            Collection<Product> info = manager.getProductMap().values();
            String report = "";
            for (Product product : info) {
                if(product.getDamageQuantity() > 0 || product.expiredCount() > 0)
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
            Product p = this.manager.getProduct(catalogNumber);
            if (p == null)
                throw new IllegalArgumentException("Product with this catalog number does not exist");
            p.moveToDamage(inStore, inStorage, expirationDate);
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

    public void updateBuyPriceForProduct(int catalogNumber, double buyPrice) {
        try {
            this.manager.updateBuyPriceForProduct(catalogNumber, buyPrice);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateSalePriceForProduct(int catalogNumber, double salePrice) {
        try {
            this.manager.updateSalePriceForProduct(catalogNumber, salePrice);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateSupplierDiscountForProduct(int catalogNumber, double supplierDiscount) {
        try {
            this.manager.updateSupplierDiscountForProduct(catalogNumber, supplierDiscount);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateManufacturerForProduct(int catalogNumber, String manufacturer) {
        try {
            this.manager.updateManufacturerForProduct(catalogNumber, manufacturer);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateAisleForProduct(int catalogNumber, String aisle) {
        try {
            this.manager.updateAisleForProduct(catalogNumber, aisle);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateMinimalQuantityForProduct(int catalogNumber, int minimalQuantity) {
        try {
            this.manager.updateMinimalQuantityForProduct(catalogNumber, minimalQuantity);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void addCategory(String category) {
        try {
            this.manager.addCategory(category);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void addSubCategory(String subCategory) {
        try {
            this.manager.addSubCategory(subCategory);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void addSize(String size) {
        try {
            this.manager.addSize(size);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
