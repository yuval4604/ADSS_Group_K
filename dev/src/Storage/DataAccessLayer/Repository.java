package Storage.DataAccessLayer;

import Storage.DomainLayer.Product;

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

    public Product getProduct(int catalogNumber) {
        return dao.getProduct(catalogNumber);
    }

    public void addProduct(Product product) {
        dao.addProduct(product);
    }

    public void updateProduct(Product product) {
        dao.updateProduct(product);
    }

    public Product deleteProduct(int catalogNumber) {
        return dao.deleteProduct(catalogNumber);
    }

    public List<Product> getProductsByCategories(List<String> categories) {
        List<String[]> categoriesList = new ArrayList<>();
        for (String category : categories) {
            categoriesList.add(category.split(","));
        }
        return dao.getProductsByCategories(categoriesList);
    }

    public List<Product> getAllProducts() {
        return dao.getAllProducts();
    }

    public Map<Integer, Integer> expiredCount() {
        return dao.expiredCount();
    }

    public String alertOnMinimalQuantity() {
        return dao.alertOnMinimalQuantity();
    }
}
