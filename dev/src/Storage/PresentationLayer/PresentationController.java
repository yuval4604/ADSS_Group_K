package Storage.PresentationLayer;


import Storage.DataAccessLayer.Repository;
import Storage.DomainLayer.Product;
import Storage.ServiceLayer.ServiceController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresentationController {
    private ServiceController serviceController;

    public PresentationController(Map<Integer, Product> productMap, Repository repo) {
        this.serviceController = new ServiceController(productMap, repo);
    }

    public PresentationController(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    private String addProduct(int catalogNumber, String name, String category, String subCategory, String size, double buyPrice, double salePrice, double discount, double supplierDiscount, String manufacturer, String aisle, int minimalQuantity) throws Exception {
        try {
            this.serviceController.addProduct(catalogNumber, name, category, subCategory, size, buyPrice, salePrice, discount, supplierDiscount, manufacturer, aisle, minimalQuantity);
            return "Product was added to store.";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }


    private String addToProduct(LocalDate expirationDate, int catalogNumber, int inStore, int inStorage) throws Exception {
        try {
            this.serviceController.addToProduct(expirationDate, catalogNumber, inStore, inStorage);
            return "The product batch was registered.";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String produceProductReport(List<String> categories) throws Exception {
        try {
            return this.serviceController.produceProductReport(categories);
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String produceDamageReport() throws Exception{
        try {
            return this.serviceController.produceDamageReport();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String removeProduct(int catalogNumber) throws Exception{
        try{
            this.serviceController.removeProduct(catalogNumber);
            return "Product was removed from store.";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    public String alertOnMinimalQuantity() throws Exception {
        try {
            return this.serviceController.alertOnMinimalQuantity();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    public String expiredCount() throws Exception {
        String str = "";
        try {
            Map<Integer, Integer> map = this.serviceController.expiredCount();
            for(Map.Entry<Integer,Integer> entry : map.entrySet())
                if(entry.getValue() > 0)
                    str += "Catalog number: " + entry.getKey() + ", quantity: " + entry.getValue() + "\n";
            if(!str.equals(""))
                return str.substring(0, str.length()-1);
            return str;
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String getProduct(int catalogNumber) throws Exception{
        try{
            return this.serviceController.getProduct(catalogNumber);
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String updateDiscountForProduct(int catalogNumber, double discount) throws Exception {
        try {
            this.serviceController.updateDiscountForProduct(catalogNumber, discount);
            return "Discount was updated.";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String updateDiscountForCategory(List<String> categories, double discount) throws Exception{
        try{
            this.serviceController.updateDiscountForCategory(categories, discount);
            return "Discount was updated.";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String updateDamageForProduct(int catalogNumber, int inStore[], int inStorage[], LocalDate expirationDate[]) throws Exception{
        try{
            this.serviceController.updateDamageForProduct(catalogNumber, inStore, inStorage, expirationDate);
            return "Damage was updated.";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String moveProductToStore(int catalogNumber, LocalDate expirationDate, int quantity) throws Exception{
        try {
            this.serviceController.moveProductToStore(catalogNumber, expirationDate, quantity);
            return "Products were moved to store.";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String subtractFromStore(int catalogNumber, Map<LocalDate,Integer> products) throws Exception{
        try {
            this.serviceController.subtractFromStore(catalogNumber, products);
            return "Products were subtracted from store.";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private String moveToExpired(int catalogNumber, LocalDate expirationDate, int inStore, int inStorage) throws Exception{
        try {
            this.serviceController.moveToExpired(catalogNumber, expirationDate, inStore, inStorage);
            return "Products were moved to expired.";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    private void updateMinimalQuantityForProduct(int catalogNumber, int minimalQuantity) {
        this.serviceController.updateMinimalQuantityForProduct(catalogNumber, minimalQuantity);
    }

    private void updateAisleForProduct(int catalogNumber, String aisle) {
        this.serviceController.updateAisleForProduct(catalogNumber, aisle);
    }

    private void updateManufacturerForProduct(int catalogNumber, String manufacturer) {
        this.serviceController.updateManufacturerForProduct(catalogNumber, manufacturer);
    }

    private void updateSupplierDiscountForProduct(int catalogNumber, double supplierDiscount) {
        this.serviceController.updateSupplierDiscountForProduct(catalogNumber, supplierDiscount);
    }

    private void updateSalePriceForProduct(int catalogNumber, double salePrice) {
        this.serviceController.updateSalePriceForProduct(catalogNumber, salePrice);
    }

    private void updateBuyPriceForProduct(int catalogNumber, double buyPrice) {
        this.serviceController.updateBuyPriceForProduct(catalogNumber, buyPrice);
    }

    public String parseAddProductMessage(String str) throws Exception{
        try {
            String[] parts = str.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            String name = parts[1];
            String category = parts[2];
            String subCategory = parts[3];
            String size = parts[4];
            double buyPrice = Double.parseDouble(parts[5]);
            double salePrice = Double.parseDouble(parts[6]);
            double discount = Double.parseDouble(parts[7]);
            double supplierDiscount = Double.parseDouble(parts[8]);
            String manufacturer = parts[9];
            String aisle = parts[10];
            int minimalQuantity = Integer.parseInt(parts[11]);
            return this.addProduct(catalogNumber, name, category, subCategory, size, buyPrice, salePrice, discount, supplierDiscount, manufacturer, aisle, minimalQuantity);
        } catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseRemoveProductMessage(String str) throws Exception{
        try {
            int catalogNumber = Integer.parseInt(str);
            return this.removeProduct(catalogNumber);
        } catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseUpdateDiscountForProductMessage(String str) throws Exception{
        try {
            String[] parts = str.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            double discount = Double.parseDouble(parts[1]) / 100;
            return this.updateDiscountForProduct(catalogNumber, discount);
        } catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseUpdateDamageForProductMessage(String str) throws Exception{
        try {
            String[] parts = str.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            int[] inStore = new int[(parts.length - 1) / 3];
            int[] inStorage = new int[(parts.length - 1) / 3];
            LocalDate[] expirationDate = new LocalDate[(parts.length - 1) / 3];
            for (int i = 1; i < parts.length; i += 3) {
                expirationDate[i - 1] = LocalDate.parse(parts[i]);
                inStore[i - 1] = Integer.parseInt(parts[i + 1]);
                inStorage[i - 1] = Integer.parseInt(parts[i + 2]);
            }
            return this.updateDamageForProduct(catalogNumber, inStore, inStorage, expirationDate);
        } catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseGetProductMessage(String str) throws Exception{
        try {
            int catalogNumber = Integer.parseInt(str);
            return this.getProduct(catalogNumber);
        } catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseProduceProductReportMessage(String str) throws Exception{
        try {
            String[] parts = str.split(";");
            List<String> categories = new java.util.LinkedList<String>();
            for (String category : parts) {
                categories.add(category);
            }
            return this.produceProductReport(categories);
        } catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseProduceDamageReportMessage() throws Exception{
        return this.produceDamageReport();
    }

    public String parseUpdateDiscountForCategoryMessage(String str) throws Exception{
        try {
            String[] parts = str.split(";");
            double discount = Double.parseDouble(parts[0]);
            List<String> categories = new java.util.LinkedList<String>();
            for (int i = 1; i < parts.length; i++) {
                categories.add(parts[i]);
            }
            return this.updateDiscountForCategory(categories, discount);
        } catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseMoveProductToStore(String str) throws Exception{
        try {
            String[] parts = str.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            LocalDate expirationDate = LocalDate.parse(parts[1]);
            int quantity = Integer.parseInt(parts[2]);
            return this.moveProductToStore(catalogNumber, expirationDate, quantity);
        }
        catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseSubtractFromStore(String str) throws Exception{
        try {
            String[] parts = str.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            Map<LocalDate, Integer> products = new HashMap<>();
            LocalDate date;
            int quantity;
            for (int i = 1; i < parts.length; i += 2) {
                date = LocalDate.parse(parts[i]);
                quantity = Integer.parseInt(parts[i + 1]);
                products.put(date, quantity);
            }
            return this.subtractFromStore(catalogNumber, products);
        }
        catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseAddToProductMessage(String str) throws Exception{
        try {
            String[] parts = str.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            LocalDate expirationDate = LocalDate.parse(parts[1]);
            int inStore = Integer.parseInt(parts[2]);
            int inStorage = Integer.parseInt(parts[3]);
            return this.addToProduct(expirationDate, catalogNumber, inStore, inStorage);
        } catch (Exception e) {
            return "give us good info please!";
        }
    }

    public String parseMoveToExpiredMessage(String str) throws Exception{
        try {
            String[] parts = str.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            LocalDate expirationDate = LocalDate.parse(parts[1]);
            int inStore = Integer.parseInt(parts[2]);
            int inStorage = Integer.parseInt(parts[3]);
            return this.moveToExpired(catalogNumber, expirationDate, inStore, inStorage);
        } catch (Exception e) {
            return "give us good info please!";
        }
    }

    public boolean parseUpdateBuyPriceForProductMessage(String info) {
        try {
            String[] parts = info.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            double buyPrice = Double.parseDouble(parts[1]);
            this.updateBuyPriceForProduct(catalogNumber, buyPrice);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean parseUpdateSalePriceForProductMessage(String info) {
        try {
            String[] parts = info.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            double salePrice = Double.parseDouble(parts[1]);
            this.updateSalePriceForProduct(catalogNumber, salePrice);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean parseUpdateSupplierDiscountForProductMessage(String info) {
        try {
            String[] parts = info.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            double supplierDiscount = Double.parseDouble(parts[1]);
            this.updateSupplierDiscountForProduct(catalogNumber, supplierDiscount);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean parseUpdateManufacturerForProductMessage(String info) {
        try {
            String[] parts = info.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            String manufacturer = parts[1];
            this.updateManufacturerForProduct(catalogNumber, manufacturer);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean parseUpdateAisleForProductMessage(String info) {
        try {
            String[] parts = info.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            String aisle = parts[1];
            this.updateAisleForProduct(catalogNumber, aisle);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean parseUpdateMinimalQuantityForProductMessage(String info) {
        try {
            String[] parts = info.split(",");
            int catalogNumber = Integer.parseInt(parts[0]);
            int minimalQuantity = Integer.parseInt(parts[1]);
            this.updateMinimalQuantityForProduct(catalogNumber, minimalQuantity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
