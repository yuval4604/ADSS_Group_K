package Storage.DomainLayer;

import Storage.DataAccessLayer.Repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainManager {

    private Map<Integer, Product> productMap;
    private Repository repo;

    public static List<String> categories;
    public static List<String> subCategories;
    public static List<String> sizes;

    public DomainManager(Repository repo) throws SQLException {
        this.productMap = new HashMap<Integer, Product>();
        this.repo = repo;
        categories = repo.getCategories();
        subCategories = repo.getSubCategories();
        sizes = repo.getSizes();
        if(categories == null || categories.size() == 0) categories = new ArrayList<>();
        if(subCategories == null || subCategories.size() == 0) subCategories = new ArrayList<>();
        if(sizes == null ||  sizes.size() == 0) sizes = new ArrayList<>();
    }

    public DomainManager(Map<Integer, Product> productMap, Repository repo) throws SQLException {
        this.productMap = productMap;
        this.repo = repo;
        categories = repo.getCategories();
        subCategories = repo.getSubCategories();
        sizes = repo.getSizes();
        if(categories == null || categories.size() == 0) categories = new ArrayList<>();
        if(subCategories == null || subCategories.size() == 0) subCategories = new ArrayList<>();
        if(sizes == null ||  sizes.size() == 0) sizes = new ArrayList<>();
    }


    public void addProduct(Product product) throws Exception {
        try {
            if(categories == null || subCategories == null || sizes == null)
                throw new IllegalArgumentException("Categories not initialized");
            if(product == null)
                throw new IllegalArgumentException("Product is null");
            if (this.productMap.containsKey(product.getCatalogNumber())) {
                throw new IllegalArgumentException("Product with this catalog number already exists");
            }
            if (!(categories.contains(product.getCategory()) && subCategories.contains(product.getSubCategory()) && sizes.contains(product.getSize()))) {
                throw new IllegalArgumentException("Invalid category");
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

    public void removeProduct(int catalogNumber) throws Exception {
        try {
            if (!this.productMap.containsKey(catalogNumber) || this.repo.getProduct(catalogNumber) == null) {
                throw new IllegalArgumentException("Product with this catalog number does not exist");
            }
            this.productMap.remove(catalogNumber);
            this.repo.deleteProduct(catalogNumber);
        } catch (Exception e) {
            throw e;
        }
    }

    // in the presentaion we will force the user to choose all the higher level categories of a chosen category
    public List<Product> getProductsByCategories(List<String> categoriesList) throws Exception {
        try {
            checkGoodCategories(categoriesList);
            return this.repo.getProductsByCategories(categoriesList);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean checkGoodCategories(List<String> categoriesList) {
        for (String category : categoriesList) {
            String[] divided = category.split(",");
            switch (divided.length){
                case 1:
                    if(!categories.contains(divided[0]))
                        throw new IllegalArgumentException("Invalid category");
                    break;
                case 2:
                    if(!categories.contains(divided[0]) || !subCategories.contains(divided[1]))
                        throw new IllegalArgumentException("Invalid category");
                    break;
                case 3:
                    if(!categories.contains(divided[0]) || !subCategories.contains(divided[1]) || !sizes.contains(divided[2]))
                        throw new IllegalArgumentException("Invalid category");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid category");

            }
        }
        return true;
    }

    public void moveProductToStore(int catalogNumber, LocalDate expirationDate, int quantity) throws Exception {
        try {
            Product product = this.productMap.remove(catalogNumber);
            if(product == null) product = this.repo.getProduct(catalogNumber);
            if(product == null) throw new IllegalArgumentException("Product does not exist");
            try {
                product.moveProductToStore(expirationDate, quantity);
            } catch (Exception e) {
                throw e;
            }
            finally {
                this.productMap.put(catalogNumber, product);
                this.repo.updateExpiration(product.getCatalogNumber(), expirationDate, product.getStoreQuantity(), product.getStorageQuantity());
            }
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
            this.repo.updateProduct(catalogNumber, Map.of("storeQuantity", String.valueOf(product.getStoreQuantity()), "storageQuantity", String.valueOf(product.getStorageQuantity())));
            this.repo.updateExpiration(product.getCatalogNumber(), expirationDate, product.getExpirationDates().get(expirationDate).getKey(), product.getExpirationDates().get(expirationDate).getValue());
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
            Product p = this.productMap.get(catalogNumber);
            if(p == null) p = this.repo.getProduct(catalogNumber);
            if(p == null) throw new IllegalArgumentException("Product does not exist");
            p.setDiscount(discount);
            this.productMap.remove(catalogNumber);
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

    public void addCategory(String category) {
        try {
            if(categories.contains(category)) throw new IllegalArgumentException("Category already exists");
            categories.add(category);
            this.repo.addCategory(category);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void addSubCategory(String subCategory) {
        try {
            if(subCategories.contains(subCategory)) throw new IllegalArgumentException("Sub Category already exists");
            subCategories.add(subCategory);
            this.repo.addSubCategory(subCategory);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void addSize(String size) {
        try {
            if(sizes.contains(size)) throw new IllegalArgumentException("Size already exists");
            sizes.add(size);
            this.repo.addSize(size);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateDamageForProduct(int catalogNumber, int[] inStore, int[] inStorage, LocalDate[] expirationDate) {
        try {
            Product p = this.productMap.remove(catalogNumber);
            if(p == null) p = this.repo.getProduct(catalogNumber);
            if(p == null) throw new IllegalArgumentException("Product does not exist");
            p.moveToDamage(inStore, inStorage, expirationDate);
            this.productMap.put(catalogNumber, p);
            for (int i = 0; i < inStore.length; i++) {
                this.repo.updateExpiration(p.getCatalogNumber(), expirationDate[i], p.getExpirationDates().get(expirationDate[i]).getKey(), p.getExpirationDates().get(expirationDate[i]).getValue());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public void updateDiscountForCategory(List<String> categories, double discount) {
        try {
            checkGoodCategories(categories);
            for (Product p : this.repo.getProductsByCategories(categories)) {
                p.setDiscount(discount);
                this.repo.updateProduct(p.getCatalogNumber(), Map.of("discount", String.valueOf(discount)));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String produceDamageReport() {
        try {
            return this.repo.produceDamageReport();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public String produceProductReport(List<String> categories) throws Exception {
        try {
            List<Product> products = getProductsByCategories(categories);
            StringBuilder sb = new StringBuilder();
            for (Product p : products) {
                sb.append(p.productReportToString());
                sb.append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleteAll() {
        try {
            this.repo.deleteAll();
            this.productMap.clear();
            this.categories = new ArrayList<>();
            this.subCategories = new ArrayList<>();
            this.sizes = new ArrayList<>();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
