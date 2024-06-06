package Storage.PresentationLayer;

import Storage.DomainLayer.Facades.DomainFacade;
import Storage.DomainLayer.Product;
import Storage.ServiceLayer.ServiceController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PresentationController {
    private ServiceController serviceController;

    public PresentationController(Map<Integer, Product> productMap, DomainFacade facade) {
        this.serviceController = new ServiceController(productMap, facade);
    }

    public PresentationController(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    private void addProduct(int catalogNumber, String name, String category, String subCategory, String size, double buyPrice, double salePrice, double discount, double supplierDiscount, String manufacturer, String aisle, int minimalQuantity) throws Exception {
        this.serviceController.addProduct(catalogNumber, name, category, subCategory, size, buyPrice, salePrice, discount, supplierDiscount, manufacturer, aisle, minimalQuantity);
    }

    private String produceProductReport(List<String> categories) throws Exception {
        return this.serviceController.produceProductReport(categories);
    }

    private String produceDamageReport(List<String> categories) throws Exception{
        return this.serviceController.produceDamageReport(categories);
    }

    private void removeProduct(int catalogNumber) throws Exception{
        this.serviceController.removeProduct(catalogNumber);
    }

    public String alertOnMinimalQuantity() throws Exception {
        return this.serviceController.alertOnMinimalQuantity();
    }

    public Map<Integer,Integer> expiredCount() throws Exception { return this.serviceController.expiredCount(); }

    private String getProduct(int catalogNumber) throws Exception{
        return this.serviceController.getProduct(catalogNumber);
    }

    private void updateDiscountForProduct(int catalogNumber, double discount) throws Exception {
        this.serviceController.updateDiscountForProduct(catalogNumber, discount);
    }

    private void updateDiscountForCategory(List<String> categories, double discount) throws Exception{
        this.serviceController.updateDiscountForCategory(categories, discount);
    }

    private void updateDamageForProduct(int catalogNumber, int inStore, int inStorage, LocalDate expirationDate) throws Exception{
        this.serviceController.updateDamageForProduct(catalogNumber, inStore, inStorage, expirationDate);
    }

    private void moveProductToStore(int catalogNumber, int quantity) throws Exception{
        this.serviceController.moveProductToStore(catalogNumber, quantity);
    }

    private void subtractFromStore(int catalogNumber, Map<LocalDate,Integer> products) throws Exception{
        this.serviceController.subtractFromStore(catalogNumber, products);
    }

    public void parseAddProductMessage(String str) throws Exception{
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
        int minimalQuantity = Integer.parseInt(parts[14]);
        this.addProduct(catalogNumber, name, category, subCategory, size, buyPrice, salePrice, discount, supplierDiscount, manufacturer, aisle, minimalQuantity);
    }

    public void parseRemoveProductMessage(String str) throws Exception{
        int catalogNumber = Integer.parseInt(str);
        this.removeProduct(catalogNumber);
    }

    public void parseUpdateDiscountForProductMessage(String str) throws Exception{
        String[] parts = str.split(",");
        int catalogNumber = Integer.parseInt(parts[0]);
        double discount = Double.parseDouble(parts[1]);
        this.updateDiscountForProduct(catalogNumber, discount);
    }

    public void parseUpdateDamageForProductMessage(String str) throws Exception{
        String[] parts = str.split(",");
        int catalogNumber = Integer.parseInt(parts[0]);
        int inStore = Integer.parseInt(parts[1]);
        int inStorage = Integer.parseInt(parts[2]);
        LocalDate expirationDate = LocalDate.parse(parts[3]);
        this.updateDamageForProduct(catalogNumber, inStore, inStorage, expirationDate);
    }

    public void parseGetProductMessage(String str) throws Exception{
        int catalogNumber = Integer.parseInt(str);
        this.getProduct(catalogNumber);
    }

    public void parseProduceProductReportMessage(String str) throws Exception{
        String[] parts = str.split("|");
        List<String> categories = new java.util.LinkedList<String>();
        for(String category : parts){
            categories.add(category);
        }
        this.produceProductReport(categories);
    }

    public void parseProduceDamageReportMessage(String str) throws Exception{
        String[] parts = str.split("|");
        List<String> categories = new java.util.LinkedList<String>();
        for(String category : parts){
            categories.add(category);
        }
        this.produceDamageReport(categories);
    }

    public void parseUpdateDiscountForCategory(String str) throws Exception{
        String[] parts = str.split("|");
        double discount = Double.parseDouble(parts[0]);
        List<String> categories = new java.util.LinkedList<String>();
        for(int i = 1; i < parts.length; i++){
            categories.add(parts[i]);
        }
        this.updateDiscountForCategory(categories, discount);
    }

    public void parseMoveProductToStore(String str) throws Exception{
        String[] parts = str.split(",");
        int catalogNumber = Integer.parseInt(parts[0]);
        int quantity = Integer.parseInt(parts[1]);
        this.moveProductToStore(catalogNumber, quantity);
    }

    public void parseSubtractFromStore(String str) throws Exception{
        String[] parts = str.split(",");
        int catalogNumber = Integer.parseInt(parts[0]);
        Map<LocalDate, Integer> products = new HashMap<>();
        LocalDate date;
        int quantity;
        for(int i = 1; i < parts.length; i+=2){
            date = LocalDate.parse(parts[i]);
            quantity = Integer.parseInt(parts[i+1]);
            products.put(date,quantity);
        }
        this.subtractFromStore(catalogNumber,products);

    }
}
