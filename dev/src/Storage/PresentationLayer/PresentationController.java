package Storage.PresentationLayer;

import Storage.DomainLayer.Facades.DomainFacade;
import Storage.DomainLayer.Product;
import Storage.ServiceLayer.ServiceController;

import java.util.Date;
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

    public void addProduct(int catalogNumber, String name, String category, String subCategory, String size, Map<Date, Integer> expirationDates, double buyPrice, double salePrice, double discount, int storageQuantity, int storeQuantity, int damageQuantity, String manufacturer, String aisle, int minimalQuantity) {
        this.serviceController.addProduct(catalogNumber, name, category, subCategory, size, expirationDates, buyPrice, salePrice, discount, storageQuantity, storeQuantity, damageQuantity, manufacturer, aisle, minimalQuantity);
    }

    public String produceProductReport(List<String> categories) {
        return this.serviceController.produceProductReport(categories);
    }

    public String produceDamageReport(List<String> categories) {
        return this.serviceController.produceDamageReport(categories);
    }

    public void removeProduct(int catalogNumber) {
        this.serviceController.removeProduct(catalogNumber);
    }

    public String alertOnMinimalQuantity() {
        return this.serviceController.alertOnMinimalQuantity();
    }

    public Product getProduct(int catalogNumber) {
        return this.serviceController.getProduct(catalogNumber);
    }

    public void updateDiscount(int catalogNumber, double discount) {
        this.serviceController.updateDiscountForProduct(catalogNumber, discount);
    }

    public void updateDiscountForCategory(List<String> categories, double discount) {
        this.serviceController.updateDiscountForCategory(categories, discount);
    }

    public void updateDamageForProduct(int catalogNumber, int inStore, int inStorage, Date expirationDate) {
        this.serviceController.updateDamageForProduct(catalogNumber, inStore, inStorage, expirationDate);
    }

    public void parse(String str){
        String[] commands = str.split(" ");
        switch (commands[0]){
            case "addProduct":
                this.addProduct(Integer.parseInt(commands[1]), commands[2], commands[3], commands[4], commands[5], null, Double.parseDouble(commands[6]), Double.parseDouble(commands[7]), Double.parseDouble(commands[8]), Integer.parseInt(commands[9]), Integer.parseInt(commands[10]), Integer.parseInt(commands[11]), commands[12], commands[13], Integer.parseInt(commands[14]));
                break;

            default:
                break;
        }
    }
}
