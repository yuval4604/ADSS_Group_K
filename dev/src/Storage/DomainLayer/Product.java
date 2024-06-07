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
    private Map<LocalDate, Map.Entry<Integer,Integer>> expirationDates;//First in pair is storage, Second is store
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
        this.expiredProducts = new HashMap<>();
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

    public Map<LocalDate, Map.Entry<Integer,Integer>> getExpirationDates() {
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
        if(discount > 1 || discount < 0) throw new IllegalArgumentException("Discount is out of bounds");
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
        if(expirationDate.isBefore(LocalDate.now())) throw new IllegalArgumentException("Expiration date is in the past");
        this.storeQuantity += amountForStore;
        this.storageQuantity += amountForStorage;
        expirationDates.put(expirationDate, expirationDates.getOrDefault(expirationDate, new AbstractMap.SimpleEntry<Integer, Integer>(0, 0)));
        Map.Entry<Integer,Integer> baseValues = expirationDates.remove(expirationDate);
        Map.Entry<Integer,Integer> newValues = new AbstractMap.SimpleEntry(baseValues.getKey()+ amountForStorage, baseValues.getValue() + amountForStore);
        expirationDates.put(expirationDate,newValues);
    }

    public void removeOne(boolean isStorage, LocalDate expirationDate) {
        if (!expirationDates.containsKey(expirationDate)) {
            throw new IllegalArgumentException("No such expiration date");
        }
        if (isStorage) {
            if(expirationDates.get(expirationDate).getKey() == 0) throw new IllegalArgumentException("Not enough quantity to remove");
            if(storageQuantity == 0) throw new IllegalArgumentException("Not enough quantity to remove from storage");
            storageQuantity--;
            expirationDates.put(expirationDate, new AbstractMap.SimpleEntry<>(expirationDates.get(expirationDate).getKey() - 1,expirationDates.get(expirationDate).getValue()));
        } else {
            if(expirationDates.get(expirationDate).getValue() == 0) throw new IllegalArgumentException("Not enough quantity to remove");
            if(storeQuantity == 0) throw new IllegalArgumentException("Not enough quantity to remove from store");
            storeQuantity--;
            expirationDates.put(expirationDate, new AbstractMap.SimpleEntry<>(expirationDates.get(expirationDate).getKey(),expirationDates.get(expirationDate).getValue() - 1));
        }
        if (expirationDates.get(expirationDate).getKey() == 0 && expirationDates.get(expirationDate).getValue() == 0) {
            expirationDates.remove(expirationDate);
        }
    }

    public void moveToDamage(int[] inStore, int[] inStorage, LocalDate[] expirationDate){
        int storage = 0;
        for(int amount: inStorage)
            storage += amount;
        int store = 0;
        for(int amount: inStore)
            store += amount;
        boolean flag = true;
        for(int i = 0; i < inStorage.length; i++){
            if(!this.expirationDates.containsKey(expirationDate[i]))
                flag = false;
            if(this.expirationDates.get(expirationDate[i]).getKey() < inStorage[i] || this.expirationDates.get(expirationDate[i]).getValue() < inStore[i])
                flag = false;
        }
        if(this.storageQuantity >= storage && this.storeQuantity >= store && flag){
            this.storageQuantity -= storage;
            this.storeQuantity -= store;
            this.damageQuantity += storage + store;
            for(int i = 0; i < inStorage.length; i++){
                Map.Entry<Integer,Integer> oldValues = this.expirationDates.remove(expirationDate[i]);
                this.expirationDates.put(expirationDate[i], new AbstractMap.SimpleEntry<Integer,Integer>(oldValues.getKey() - inStorage[i], oldValues.getValue() - inStore[i]));
            }
        }
        else throw new IllegalArgumentException("Not enough quantity to move to damage");
    }

    public void moveToExpired(int inStore, int inStorage, LocalDate expirationDate){
        if(!expirationDate.isBefore(LocalDate.now())) throw new IllegalArgumentException("Expiration date is in the future");
        if(!this.expirationDates.containsKey(expirationDate)) throw new IllegalArgumentException("No such expiration date");
        if(this.expirationDates.get(expirationDate).getKey() >= inStorage && this.expirationDates.get(expirationDate).getValue() >= inStore){
            this.storeQuantity -= inStore;
            this.storageQuantity -= inStorage;
            Map.Entry<Integer,Integer> oldValues = this.expirationDates.remove(expirationDate);
            this.expirationDates.put(expirationDate, new AbstractMap.SimpleEntry<Integer,Integer>(oldValues.getKey() - inStorage, oldValues.getValue() - inStore));
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
                "Sale price: " + getDiscountedPrice() + "\n" +
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

    public void moveProductToStore(LocalDate expirationDate, int quantity) {
        if(this.expirationDates.get(expirationDate) == null) throw new IllegalArgumentException("No such expiration date");
        if (quantity > this.expirationDates.get(expirationDate).getKey()) {
            throw new IllegalArgumentException("Not enough quantity to move to store");
        }
        Map.Entry<Integer,Integer> oldValues = this.expirationDates.remove(expirationDate);
        this.expirationDates.put(expirationDate, new AbstractMap.SimpleEntry<Integer,Integer>(oldValues.getKey() - quantity, oldValues.getValue() + quantity));
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

        for(Map.Entry<LocalDate, Map.Entry<Integer,Integer>> entry : expirationDates.entrySet()){
            if(entry.getKey().isBefore(now))
                count += entry.getValue().getKey() + entry.getValue().getValue();
        }
        return count;
    }
}
