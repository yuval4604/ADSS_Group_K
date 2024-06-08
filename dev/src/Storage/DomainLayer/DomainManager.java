package Storage.DomainLayer;

import Storage.DomainLayer.Enums.Category;
import Storage.DomainLayer.Enums.SubCategory;
import Storage.DomainLayer.Enums.SubSubCategory;
import Storage.DomainLayer.Facades.DomainFacade;

import java.time.LocalDate;
import java.util.*;

public class DomainManager {

    private Map<Integer, Product> productMap;

    private DomainFacade domainFacade;

    public DomainManager(DomainFacade domainFacade) {
        this.domainFacade = domainFacade;
        this.productMap = new HashMap<Integer, Product>();
    }

    public DomainManager(Map<Integer, Product> productMap, DomainFacade domainFacade) {
        this.productMap = productMap;
        this.domainFacade = domainFacade;
    }

    public void addProduct(Product product) throws Exception {
        try {
            if (this.productMap.containsKey(product.getCatalogNumber())) {
                throw new IllegalArgumentException("Product with this catalog number already exists");
            }
            this.domainFacade.addProduct(product);
            this.productMap.put(product.getCatalogNumber(), product);
        } catch (Exception e) {
            throw e;
        }
    }

    public Product getProduct(int catalogNumber) throws Exception {
        try {
            if (!this.productMap.containsKey(catalogNumber)) {
                return null;
            }
            return this.productMap.get(catalogNumber);
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<Integer, Product> getProductMap() {
        return this.productMap;
    }

    public void removeProduct(Product product) throws Exception {
        try {
            if (!this.productMap.containsKey(product.getCatalogNumber())) {
                throw new IllegalArgumentException("Product with this catalog number does not exist");
            }
            this.productMap.remove(product.getCatalogNumber());
            this.domainFacade.removeProduct(product);
        } catch (Exception e) {
            throw e;
        }
    }

    // in the presentaion we will force the user to choose all the higher level categories of a chosen category
    public List<Product> getProductsByCategories(List<String> categories) throws Exception {
        try {
            List<Product> products = new LinkedList<>();
            for (String category : categories) {
                String[] divided = category.split(",");
                if (divided.length == 0 || divided.length > 3)
                    throw new IllegalArgumentException("invalid entry");
                if (Category.contains(divided[0])) {
                    if (divided.length == 1)
                        products.addAll(domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getAllProducts());
                    else if (SubCategory.contains(divided[1])) {
                        if (domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategories().containsKey(SubCategory.valueOf(divided[1]))) {
                            if (divided.length == 2)
                                products.addAll(domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategoryFacade(SubCategory.valueOf(divided[1])).getAllProducts());
                            else if (SubSubCategory.contains(divided[2])) {
                                if (domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategoryFacade(SubCategory.valueOf(divided[1])).getSubSubCategories().containsKey(SubSubCategory.valueOf(divided[2])))
                                    products.addAll(domainFacade.getCategoryFacade(Category.valueOf(divided[0])).getSubCategoryFacade(SubCategory.valueOf(divided[1])).
                                            getSubSubCategoryFacade(SubSubCategory.valueOf(divided[2])).getAllProducts());
                                else throw new NoSuchElementException("sub category does not have this size");
                            } else throw new NoSuchElementException("Size doesn't exist");
                        } else throw new NoSuchElementException("category does not have this sub category");
                    } else throw new NoSuchElementException("Sub Category doesn't exist");
                } else throw new NoSuchElementException("Category doesn't exist");
            }
            return products;
        } catch (Exception e) {
            throw e;
        }
    }

    public void moveProductToStore(int catalogNumber, LocalDate expirationDate, int quantity) throws Exception {
        try {
            Product product = this.productMap.get(catalogNumber);
            product.moveProductToStore(expirationDate, quantity);
        } catch (Exception e) {
            throw e;
        }
    }

    public void subtractFromStore(int catalogNumber, Map<LocalDate, Integer> products) throws Exception {
        try {
            Product product = this.productMap.get(catalogNumber);
            for (Map.Entry<LocalDate, Integer> entry : products.entrySet()) {
                if (product.getStoreQuantity() < entry.getValue())
                    throw new IllegalArgumentException("Not enough products in store");
                for (int i = 0; i < entry.getValue(); i++) {
                    product.removeOne(false, entry.getKey());
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<Integer, Integer> expiredCount() throws Exception {
        Map<Integer, Integer> expiredProducts = new HashMap<>();
        for (Integer catalogNumber : productMap.keySet()) {
            expiredProducts.put(catalogNumber, productMap.get(catalogNumber).expiredCount());
        }
        return expiredProducts;
    }


    public String alertOnMinimalQuantity() throws Exception {
        String alert = "";
        for (Product product : this.productMap.values()) {
            if (product.getStorageQuantity() + product.getStoreQuantity() <= product.getMinimalQuantity()) {
                alert += product.getName() + "\n";
            }
        }
        if (this.productMap.values().size() > 0 && !alert.equals(""))
            alert = alert.substring(0, alert.length() - 1);
        return alert;
    }

    public void addToProduct(LocalDate expirationDate, int catalogNumber, int inStore, int inStorage) throws Exception {
        try {
            Product product = this.productMap.get(catalogNumber);
            product.addByExpirationDate(inStore, inStorage, expirationDate);
        } catch (Exception e) {
            throw e;
        }
    }

    public void moveToExpired(int catalogNumber, LocalDate expirationDate, int inStore, int inStorage) throws Exception {
        try {
            Product product = this.productMap.get(catalogNumber);
            product.moveToExpired(inStore, inStorage, expirationDate);
        } catch (Exception e) {
            throw e;
        }
    }

    public void setDiscount(int catalogNumber, double discount) {
        try {
            this.productMap.get(catalogNumber).setDiscount(discount);
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateBuyPriceForProduct(int catalogNumber, double buyPrice) {
        try {
            this.productMap.get(catalogNumber).setBuyPrice(buyPrice);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateSalePriceForProduct(int catalogNumber, double salePrice) {
        try {
            this.productMap.get(catalogNumber).setSalePrice(salePrice);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateSupplierDiscountForProduct(int catalogNumber, double supplierDiscount) {
        try {
            this.productMap.get(catalogNumber).setSupplierDiscount(supplierDiscount);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateManufacturerForProduct(int catalogNumber, String manufacturer) {
        try {
            this.productMap.get(catalogNumber).setManufacturer(manufacturer);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateAisleForProduct(int catalogNumber, String aisle) {
        try {
            this.productMap.get(catalogNumber).setAisle(aisle);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateMinimalQuantityForProduct(int catalogNumber, int minimalQuantity) {
        try {
            this.productMap.get(catalogNumber).setMinimalQuantity(minimalQuantity);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
