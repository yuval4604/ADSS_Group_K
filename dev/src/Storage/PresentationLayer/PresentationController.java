package Storage.PresentationLayer;

import Storage.DomainLayer.Facades.DomainFacade;
import Storage.DomainLayer.Product;
import Storage.ServiceLayer.ServiceController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PresentationController {
    private ServiceController serviceController;

    public PresentationController(Map<Integer, Product> productMap, DomainFacade facade) {
        this.serviceController = new ServiceController(productMap, facade);
    }

    public PresentationController(ServiceController serviceController) {
        this.serviceController = serviceController;
    }

    private void addProduct(int catalogNumber, String name, String category, String subCategory, String size, Map<LocalDate, Integer> expirationDates, double buyPrice, double salePrice, double discount, double supplierDiscount, int storageQuantity, int storeQuantity, int damageQuantity, String manufacturer, String aisle, int minimalQuantity) {
        this.serviceController.addProduct(catalogNumber, name, category, subCategory, size, expirationDates, buyPrice, salePrice, discount, supplierDiscount, storageQuantity, storeQuantity, damageQuantity, manufacturer, aisle, minimalQuantity);
    }

    private String produceProductReport(List<String> categories) {
        return this.serviceController.produceProductReport(categories);
    }

    private String produceDamageReport(List<String> categories) {
        return this.serviceController.produceDamageReport(categories);
    }

    private void removeProduct(int catalogNumber) {
        this.serviceController.removeProduct(catalogNumber);
    }

    public String alertOnMinimalQuantity() {
        return this.serviceController.alertOnMinimalQuantity();
    }

    private String getProduct(int catalogNumber) {
        return this.serviceController.getProduct(catalogNumber);
    }

    private void updateDiscountForProduct(int catalogNumber, double discount) {
        this.serviceController.updateDiscountForProduct(catalogNumber, discount);
    }

    private void updateDiscountForCategory(List<String> categories, double discount) {
        this.serviceController.updateDiscountForCategory(categories, discount);
    }

    private void updateDamageForProduct(int catalogNumber, int inStore, int inStorage, LocalDate expirationDate) {
        this.serviceController.updateDamageForProduct(catalogNumber, inStore, inStorage, expirationDate);
    }

    private void moveProductToStore(int catalogNumber, int quantity) {
        this.serviceController.moveProductToStore(catalogNumber, quantity);
    }

    private void subtractFromStore(int catalogNumber, Map<LocalDate,Integer> products) {
        this.serviceController.substractFromStore(catalogNumber, products);
    }

    public void parseAddProductMessage(String str){
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
        int storageQuantity = Integer.parseInt(parts[9]);
        int storeQuantity = Integer.parseInt(parts[10]);
        int damageQuantity = Integer.parseInt(parts[11]);
        String manufacturer = parts[12];
        String aisle = parts[13];
        int minimalQuantity = Integer.parseInt(parts[14]);
        Map<LocalDate, Integer> expirationDates = null;
        if(parts.length > 15){
            expirationDates = new HashMap<>();
            for(int i = 15; i < parts.length; i+=2){
                expirationDates.put(LocalDate.parse(parts[i]), Integer.parseInt(parts[i+1]));
            }
        }
        this.addProduct(catalogNumber, name, category, subCategory, size, expirationDates, buyPrice, salePrice, discount, supplierDiscount, storageQuantity, storeQuantity, damageQuantity, manufacturer, aisle, minimalQuantity);
    }

    public void parseRemoveProductMessage(String str){
        int catalogNumber = Integer.parseInt(str);
        this.removeProduct(catalogNumber);
    }

    public void parseUpdateDiscountForProductMessage(String str){
        String[] parts = str.split(",");
        int catalogNumber = Integer.parseInt(parts[0]);
        double discount = Double.parseDouble(parts[1]);
        this.updateDiscountForProduct(catalogNumber, discount);
    }

    public void parseUpdateDamageForProductMessage(String str){
        String[] parts = str.split(",");
        int catalogNumber = Integer.parseInt(parts[0]);
        int inStore = Integer.parseInt(parts[1]);
        int inStorage = Integer.parseInt(parts[2]);
        LocalDate expirationDate = LocalDate.parse(parts[3]);
        this.updateDamageForProduct(catalogNumber, inStore, inStorage, expirationDate);
    }

    public void parseGetProductMessage(String str){
        int catalogNumber = Integer.parseInt(str);
        this.getProduct(catalogNumber);
    }

    public void parseProduceProductReportMessage(String str){
        String[] parts = str.split("|");
        List<String> categories = new java.util.LinkedList<String>();
        for(String category : parts){
            categories.add(category);
        }
        this.produceProductReport(categories);
    }

    public void parseProduceDamageReportMessage(String str){
        String[] parts = str.split("|");
        List<String> categories = new java.util.LinkedList<String>();
        for(String category : parts){
            categories.add(category);
        }
        this.produceDamageReport(categories);
    }

    public void parseUpdateDiscountForCategory(String str){
        String[] parts = str.split("|");
        double discount = Double.parseDouble(parts[0]);
        List<String> categories = new java.util.LinkedList<String>();
        for(int i = 1; i < parts.length; i++){
            categories.add(parts[i]);
        }
        this.updateDiscountForCategory(categories, discount);
    }

    public void parseMoveProductToStore(String str){
        String[] parts = str.split(",");
        int catalogNumber = Integer.parseInt(parts[0]);
        int quantity = Integer.parseInt(parts[1]);
        this.moveProductToStore(catalogNumber, quantity);
    }

    public void parseSubtractFromStore(String str){
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
