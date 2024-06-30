package Storage.DomainLayer;

import Storage.DataAccessLayer.Repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class DomainManager {

    private Map<Integer, Product> productMap;
    private Repository repo;

    public static List<String> categories;
    public static List<String> subCategories;
    public static List<String> sizes;

    public DomainManager(Repository repo) {
        this.repo = repo;
        this.productMap = new HashMap<Integer, Product>();
    }

    public DomainManager(Map<Integer, Product> productMap, Repository repo) {
        this.productMap = productMap;
        this.repo = repo;
    }

    public void addProduct(Product product) throws Exception {
        try {
            if (this.productMap.containsKey(product.getCatalogNumber())) {
                throw new IllegalArgumentException("Product with this catalog number already exists");
            }
            this.repo.addProduct(product);
            this.productMap.put(product.getCatalogNumber(), product);
        } catch (Exception e) {
            throw e;
        }
    }

    public Product getProduct(int catalogNumber) throws Exception {
        try {
            if(this.productMap.containsKey(catalogNumber))
                return this.productMap.get(catalogNumber);
            Product p = this.repo.getProduct(catalogNumber);
            if(p != null)
                this.productMap.put(catalogNumber, p);
            return p;
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<Integer, Product> getProductMap() {
        return this.productMap;
    }

    public void removeProduct(Product product) throws Exception {
        try {
            if(product == null)
                throw new IllegalArgumentException("Product does not exist");
            if (!this.productMap.containsKey(product.getCatalogNumber()) || this.repo.getProduct(product.getCatalogNumber()) == null) {
                throw new IllegalArgumentException("Product with this catalog number does not exist");
            }
            this.productMap.remove(product.getCatalogNumber());
            this.repo.deleteProduct(product.getCatalogNumber());
        } catch (Exception e) {
            throw e;
        }
    }

    // in the presentaion we will force the user to choose all the higher level categories of a chosen category
    public List<Product> getProductsByCategories(List<String> categoriesList) throws Exception {
        try {
            for (String category : categoriesList) {
                String[] divided = category.split(",");
                if (divided.length == 0 || divided.length > 3)
                    throw new IllegalArgumentException("invalid entry");
                if (categories.contains(divided[0])) {
                    if (divided.length > 1) {
                        if(!subCategories.contains(divided[1])) throw new NoSuchElementException("Sub Category doesn't exist");
                        if (divided.length > 2)
                            if (!sizes.contains(divided[2])) throw new NoSuchElementException("sub category does not have this size");
                        } else throw new NoSuchElementException("Size doesn't exist");
                } else throw new NoSuchElementException("Category doesn't exist");
            }
            return this.repo.getProductsByCategories(categoriesList);
        } catch (Exception e) {
            throw e;
        }
    }

    public void moveProductToStore(int catalogNumber, LocalDate expirationDate, int quantity) throws Exception {
        try {
            Product product = this.productMap.remove(catalogNumber);
            if(product == null) product = this.repo.getProduct(catalogNumber);
            if(product == null) throw new IllegalArgumentException("Product does not exist");
            product.moveProductToStore(expirationDate, quantity);
            this.productMap.put(catalogNumber, product);
            this.repo.updateExpiration(product.getCatalogNumber(), expirationDate, product.getStoreQuantity(), product.getStorageQuantity());
        } catch (Exception e) {
            throw e;
        }
    }

    public void subtractFromStore(int catalogNumber, Map<LocalDate, Integer> products) throws Exception {
        try {
            Product product = this.productMap.remove(catalogNumber);
            if(product == null) product = this.repo.getProduct(catalogNumber);
            if(product == null) throw new IllegalArgumentException("Product does not exist");
            for (Map.Entry<LocalDate, Integer> entry : products.entrySet()) {
                if (product.getStoreQuantity() < entry.getValue())
                    throw new IllegalArgumentException("Not enough products in store");
                for (int i = 0; i < entry.getValue(); i++) {
                    product.removeOne(false, entry.getKey());
                }
                this.repo.updateExpiration(product.getCatalogNumber(), entry.getKey(), product.getStoreQuantity(), product.getStorageQuantity());
            }
            this.productMap.put(catalogNumber, product);
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<Integer, Integer> expiredCount() throws Exception {
        try {
            return this.repo.expiredCount();
        } catch (Exception e) {
            throw e;
        }
    }


    public String alertOnMinimalQuantity() throws Exception {
        try {
            return this.repo.alertOnMinimalQuantity();
        } catch (Exception e) {
            throw e;
        }
    }

    public void addToProduct(LocalDate expirationDate, int catalogNumber, int inStore, int inStorage) throws Exception {
        try {
            Product product = this.productMap.remove(catalogNumber);
            if(product == null) product = this.repo.getProduct(catalogNumber);
            if(product == null) throw new IllegalArgumentException("Product does not exist");
            product.addByExpirationDate(inStore, inStorage, expirationDate);
            this.productMap.put(catalogNumber, product);
            this.repo.updateExpiration(product.getCatalogNumber(), expirationDate, product.getStoreQuantity(), product.getStorageQuantity());
        } catch (Exception e) {
            throw e;
        }
    }

    public void moveToExpired(int catalogNumber, LocalDate expirationDate, int inStore, int inStorage) throws Exception {
        try {
            Product product = this.productMap.remove(catalogNumber);
            if(product == null) product = this.repo.getProduct(catalogNumber);
            if(product == null) throw new IllegalArgumentException("Product does not exist");
            product.moveToExpired(inStore, inStorage, expirationDate);
            this.productMap.put(catalogNumber, product);
            this.repo.updateExpiration(product.getCatalogNumber(), expirationDate, product.getStoreQuantity(), product.getStorageQuantity());
            this.repo.updateExpired(product.getCatalogNumber(), expirationDate, product.getExpiredProducts().get(expirationDate) + inStore + inStorage);
        } catch (Exception e) {
            throw e;
        }
    }

    public void setDiscount(int catalogNumber, double discount) throws SQLException {
        try {
            Product p = this.productMap.remove(catalogNumber);
            if(p == null) p = this.repo.getProduct(catalogNumber);
            if(p == null) throw new IllegalArgumentException("Product does not exist");
            p.setDiscount(discount);
            this.productMap.put(catalogNumber, p);
            this.repo.updateProduct(catalogNumber, Map.of("discount", String.valueOf(discount)));
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateBuyPriceForProduct(int catalogNumber, double buyPrice) {
        try {
            Product p = this.productMap.remove(catalogNumber);
            if(p == null) p = this.repo.getProduct(catalogNumber);
            if(p == null) throw new IllegalArgumentException("Product does not exist");
            p.setBuyPrice(buyPrice);
            this.productMap.put(catalogNumber, p);
            this.repo.updateProduct(catalogNumber, Map.of("buyPrice", String.valueOf(buyPrice)));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateSalePriceForProduct(int catalogNumber, double salePrice) {
        try {
            Product p = this.productMap.remove(catalogNumber);
            if(p == null) p = this.repo.getProduct(catalogNumber);
            if(p == null) throw new IllegalArgumentException("Product does not exist");
            p.setSalePrice(salePrice);
            this.productMap.put(catalogNumber, p);
            this.repo.updateProduct(catalogNumber, Map.of("salePrice", String.valueOf(salePrice)));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateSupplierDiscountForProduct(int catalogNumber, double supplierDiscount) {
        try {
            Product p = this.productMap.remove(catalogNumber);
            if(p == null) p = this.repo.getProduct(catalogNumber);
            if(p == null) throw new IllegalArgumentException("Product does not exist");
            p.setSupplierDiscount(supplierDiscount);
            this.productMap.put(catalogNumber, p);
            this.repo.updateProduct(catalogNumber, Map.of("supplierDiscount", String.valueOf(supplierDiscount)));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateManufacturerForProduct(int catalogNumber, String manufacturer) {
        try {
            Product p = this.productMap.remove(catalogNumber);
            if(p == null) p = this.repo.getProduct(catalogNumber);
            if(p == null) throw new IllegalArgumentException("Product does not exist");
            p.setManufacturer(manufacturer);
            this.productMap.put(catalogNumber, p);
            this.repo.updateProduct(catalogNumber, Map.of("manufacturer", manufacturer));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateAisleForProduct(int catalogNumber, String aisle) {
        try {
            Product p = this.productMap.remove(catalogNumber);
            if(p == null) p = this.repo.getProduct(catalogNumber);
            if(p == null) throw new IllegalArgumentException("Product does not exist");
            p.setAisle(aisle);
            this.productMap.put(catalogNumber, p);
            this.repo.updateProduct(catalogNumber, Map.of("aisle", aisle));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateMinimalQuantityForProduct(int catalogNumber, int minimalQuantity) {
        try {
            Product p = this.productMap.remove(catalogNumber);
            if(p == null) p = this.repo.getProduct(catalogNumber);
            if(p == null) throw new IllegalArgumentException("Product does not exist");
            p.setMinimalQuantity(minimalQuantity);
            this.productMap.put(catalogNumber, p);
            this.repo.updateProduct(catalogNumber, Map.of("minimalQuantity", String.valueOf(minimalQuantity)));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
