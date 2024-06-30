package Storage.DataAccessLayer;

import Storage.DomainLayer.Product;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Repository {

    private ProductDAO dao;

    public Repository() {
        this.dao = new ProductDAO();
    }

    public Repository(ProductDAO dao) {
        this.dao = dao;
    }

    public Product getProduct(int catalogNumber) throws SQLException {
        return dao.getProduct(catalogNumber);
    }

    public void addProduct(Product product) throws SQLException {
        dao.addProduct(product);
    }

    public void updateProduct(int catalogNumber, Map<String, String> updates) throws SQLException {
        dao.updateProduct(catalogNumber, updates);
    }

    public void deleteProduct(int catalogNumber) throws SQLException {
        dao.deleteProduct(catalogNumber);
    }

    public List<Product> getProductsByCategories(List<String> categories) throws SQLException {
        List<String[]> categoriesList = new ArrayList<>();
        for (String category : categories) {
            categoriesList.add(category.split(","));
        }
        return dao.getProductsByCategories(categoriesList);
    }

    public List<Product> getAllProducts() throws SQLException {
        return dao.getAllProducts();
    }

    public Map<Integer, Integer> expiredCount() throws SQLException {
        return dao.expiredCount();
    }

    public String alertOnMinimalQuantity() throws SQLException {
        return dao.alertOnMinimalQuantity();
    }

    public void updateExpiration(int catalogNumber, LocalDate expirationDate, int storeQuantity, int storageQuantity) throws SQLException {
        dao.updateExpiration(catalogNumber, expirationDate, storeQuantity, storageQuantity);
    }

    public void updateExpired(int catalogNumber, LocalDate expirationDate, int newAmount) throws SQLException {
        this.dao.updateExpired(catalogNumber, expirationDate, newAmount);
    }
}
