package Storage.DomainLayer;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Product {

    private int catalogNumber;
    private String name;
    private Category category;
    private SubCategory subCategory;
    private SubSubCategory size;
    private Map<LocalDate, Integer> expirationDates;
    private Map<LocalDate, Integer> expiredProducts;
    private double buyPrice;
    private double salePrice;
    private double discount; // [0,1]

    private double supplierDiscount; // [0,1]
    private int storageQuantity;

    private int storeQuantity;
    private int damageQuantity;
    private String manufacturer;

    private int minimalQuantity;

    private String aisle;
    public Product(int catalogNumber, String name, Category category, SubCategory subCategory, SubSubCategory size, double buyPrice, double salePrice, double discount, double supplierDiscount, String manufacturer, String aisle, int minimalQuantity) {
        this.catalogNumber = catalogNumber;
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.size = size;
        this.expirationDates = new HashMap<>();
        this.buyPrice = buyPrice;
        this.salePrice = salePrice;
        this.discount = discount;
        this.supplierDiscount = supplierDiscount;
        this.storageQuantity = 0;
        this.storeQuantity = 0;
        this.damageQuantity = 0;
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

    public Map<LocalDate, Integer> getExpirationDates() {
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

    public double getSupplierDiscount() { return supplierDiscount; }

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

    public int getMinimalQuantity() {
        return minimalQuantity;
    }

    public double getDiscountedPrice(){
        return salePrice * (1 - discount);
    }

    public void setStoreQuantity(int storeQuantity) {
        this.storeQuantity = storeQuantity;
    }

    public void setMinimalQuantity(int minimalQuantity) {
        this.minimalQuantity = minimalQuantity;
    }

    public void addByExpirationDate(int amountForStore, int amountForStorage, LocalDate expirationDate) {
        //if(expirationDate.isBefore(LocalDate.now())) throw new IllegalArgumentException("Expiration date is in the past");
        this.storeQuantity += amountForStore;
        this.storageQuantity += amountForStorage;
        expirationDates.put(expirationDate, expirationDates.getOrDefault(expirationDate, 0) + amountForStore + amountForStorage);
    }

    public void removeOne(boolean isStorage, LocalDate expirationDate) {
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

    public void moveToDamage(int inStore, int inStorage, LocalDate expirationDate){
        if(this.storeQuantity >= inStore && this.storageQuantity >= inStorage){
            this.storeQuantity -= inStore;
            this.storageQuantity -= inStorage;
            this.damageQuantity += inStore + inStorage;
            int quantity = this.expirationDates.remove(expirationDate) - inStorage - inStore;
            this.expirationDates.put(expirationDate, quantity);
        }
        else throw new IllegalArgumentException("Not enough quantity to move to damage");
    }

    public void moveToExpired(int inStore, int inStorage, LocalDate expirationDate){
        if(this.storeQuantity >= inStore && this.storageQuantity >= inStorage){
            this.storeQuantity -= inStore;
            this.storageQuantity -= inStorage;
            int quantity = this.expirationDates.remove(expirationDate) - inStorage - inStore;
            this.expirationDates.put(expirationDate, quantity);
            this.expiredProducts.put(expirationDate, inStorage + inStorage);
        }
        else throw new IllegalArgumentException("Not enough quantity to move to expired");
    }

    public String toString(){
        return "Catalog number: " + catalogNumber + "\n" +
                "Name: " + name + "\n" +
                "Category: " + category + "\n" +
                "Sub category: " + subCategory + "\n" +
                "Size: " + size + "\n" +
                "Buy price: " + buyPrice + "\n" +
                "Sale price: " + salePrice + "\n" +
                "Discount: " + discount * 100 + "% " + "\n" +
                "Supplier discount: " + supplierDiscount * 100 + "% " + "\n" +
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
                "Storage quantity: " + storageQuantity + "\n" +
                "Store quantity: " + storeQuantity + "\n" +
                "Manufacturer: " + manufacturer + "\n" +
                "Discount: " + discount * 100 + "% " + "\n" +
                "Supplier discount: " + supplierDiscount * 100 + "% " + "\n" +
                "Aisle: " + aisle + "\n" +
                "Minimal quantity: " + minimalQuantity + "\n";
    }

    public String damagedReportToString(){
        int expiredSum = 0;
        for(LocalDate expiredDate : this.expiredProducts.keySet())
            expiredSum += this.expiredProducts.get(expiredDate);
        return "Catalog number: " + catalogNumber + "\n" +
                "Name: " + name + "\n" +
                "Category: " + category + "\n" +
                "Sub category: " + subCategory + "\n" +
                "Size: " + size + "\n" +
                "Aisle: " + aisle + "\n" +
                "Expired quantity: " + expiredSum + "\n" +
                "Damaged quantity: " + damageQuantity  + "\n";
    }

    public void moveProductToStore(int quantity) {
        if (this.storageQuantity < quantity) {
            throw new IllegalArgumentException("Not enough quantity to move to store");
        }
        this.storageQuantity -= quantity;
        this.storeQuantity += quantity;
    }

    public int expiredCount(){
        int count = 0;
        LocalDate now;
        if(LocalDateTime.now().getMonthValue() < 10 && LocalDateTime.now().getDayOfMonth() < 10)
            now =  LocalDate.parse(LocalDateTime.now().getYear() + "-0" + LocalDateTime.now().getMonthValue() + "-0" + LocalDateTime.now().getDayOfMonth());
        else{
            if(LocalDateTime.now().getMonthValue() < 10)
                now =  LocalDate.parse(LocalDateTime.now().getYear() + "-0" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth());
            else{
                if(LocalDateTime.now().getDayOfMonth() < 10)
                    now =  LocalDate.parse(LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-0" + LocalDateTime.now().getDayOfMonth());
                else
                    now =  LocalDate.parse(LocalDateTime.now().getYear() + "-" + LocalDateTime.now().getMonthValue() + "-" + LocalDateTime.now().getDayOfMonth());
            }
        }

        for(Map.Entry<LocalDate, Integer> entry : expirationDates.entrySet()){
            if(entry.getKey().isBefore(now))
                count+= entry.getValue();
        }
        return count;
    }
}
