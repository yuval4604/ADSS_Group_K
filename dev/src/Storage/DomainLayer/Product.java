package Storage.DomainLayer;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class Product {

    private int catalogNumber;
    private String name;
    private Category category;
    private SubCategory subCategory;
    private SubSubCategory size;
    private Map<Date, Integer> expirationDates;
    private double buyPrice;
    private double salePrice;
    private double discount; // [0,1]
    private int storageQuantity;
    private int storeQuantity;
    private int damageQuantity;
    private String manufacturer;

    private int minimalQuantity;

    private String aisle;
    public Product(int catalogNumber, String name, Category category, SubCategory subCategory, SubSubCategory size, Map<Date, Integer> expirationDates, double buyPrice, double salePrice, double discount, int storageQuantity, int storeQuantity, int damageQuantity, String manufacturer, String aisle, int minimalQuantity) {
        this.catalogNumber = catalogNumber;
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.size = size;
        this.expirationDates = expirationDates;
        this.buyPrice = buyPrice;
        this.salePrice = salePrice;
        this.discount = discount;
        this.storageQuantity = storageQuantity;
        this.storeQuantity = storeQuantity;
        this.damageQuantity = damageQuantity;
        this.manufacturer = manufacturer;
        this.aisle = aisle;
        this.minimalQuantity = minimalQuantity;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public SubCategory getSubCategory() {
        return subCategory;
    }

    public SubSubCategory getSize() {
        return size;
    }

    public Map<Date, Integer> getExpirationDates() {
        return Collections.unmodifiableMap(expirationDates);
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public double getDiscount() {
        return discount;
    }

    public int getStorageQuantity() {
        return storageQuantity;
    }

    public int getStoreQuantity() {
        return storeQuantity;
    }

    public int getDamageQuantity() {
        return damageQuantity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getAisle() { return aisle; }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setAisle(String aisle) { this.aisle = aisle; }

    public void addOne(boolean isStorage, Date expirationDate) {
        if (isStorage) {
            storageQuantity++;
        } else {
            storeQuantity++;
        }
        expirationDates.put(expirationDate, expirationDates.getOrDefault(expirationDate, 0) + 1);
    }

    public void removeOne(boolean isStorage, Date expirationDate) {
        if (!expirationDates.containsKey(expirationDate)) {
            throw new IllegalArgumentException("No such expiration date");
        }
        if(expirationDates.get(expirationDate) == 0) throw new IllegalArgumentException("Not enough quantity to remove");
        if (isStorage) {
            if(storageQuantity == 0) throw new IllegalArgumentException("Not enough quantity to remove from storage");
            storageQuantity--;
        } else {
            if(storeQuantity == 0) throw new IllegalArgumentException("Not enough quantity to remove from store");
            storeQuantity--;
        }
        expirationDates.put(expirationDate, expirationDates.get(expirationDate) - 1);
        if (expirationDates.get(expirationDate) == 0) {
            expirationDates.remove(expirationDate);
        }
    }

    public void moveToDamage(int inStore, int inStorage){
        if(this.storeQuantity >= inStore && this.storageQuantity >= inStorage){
            this.storeQuantity -= inStore;
            this.storageQuantity -= inStorage;
            this.damageQuantity += inStore + inStorage;
        }
        else throw new IllegalArgumentException("Not enough quantity to move to damage");
    }

    public String toString(){
        return "Catalog number: " + catalogNumber + "\n" +
                "Name: " + name + "\n" +
                "Category: " + category + "\n" +
                "Sub category: " + subCategory + "\n" +
                "Size: " + size + "\n" +
                "Buy price: " + buyPrice + "\n" +
                "Sale price: " + salePrice + "\n" +
                "Discount: " + discount + "\n" +
                "Storage quantity: " + storageQuantity + "\n" +
                "Store quantity: " + storeQuantity + "\n" +
                "Manufacturer: " + manufacturer + "\n" +
                "Aisle: " + aisle + "\n" +
                "Minimal quantity: " + minimalQuantity + "\n";
    }

    public String productReportToString(){
        return "Catalog number: " + catalogNumber + "\n" +
                "Name: " + name + "\n" +
                "Category: " + category + "\n" +
                "Sub category: " + subCategory + "\n" +
                "Size: " + size + "\n" +
                "Buy price: " + buyPrice + "\n" +
                "Sale price: " + salePrice + "\n" +
                "Discount: " + discount + "\n" +
                "Storage quantity: " + storageQuantity + "\n" +
                "Store quantity: " + storeQuantity + "\n" +
                "Manufacturer: " + manufacturer + "\n" +
                "Aisle: " + aisle + "\n" +
                "Minimal quantity: " + minimalQuantity + "\n";
    }

    public String damagedReportToString(){
        return "Catalog number: " + catalogNumber + "\n" +
                "Name: " + name + "\n" +
                "Category: " + category + "\n" +
                "Sub category: " + subCategory + "\n" +
                "Size: " + size + "\n" +
                "Aisle: " + aisle + "\n" +
                "Damaged quantity: " + damageQuantity + "\n";
    }
}
