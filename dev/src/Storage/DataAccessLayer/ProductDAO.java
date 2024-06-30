package Storage.DataAccessLayer;

import Storage.DomainLayer.Product;

import java.util.List;
import java.util.Map;

public class ProductDAO {

    public Product getProduct(int catalogNumber) {
        return null;
    }

    public void addProduct(Product product) {
    }

    public void updateProduct(Product product) {
    }

    public Product deleteProduct(int catalogNumber) {
        return null;
    }

    public List<Product> getProductsByCategories(List<String> categories) {
        return null;
    }

    public List<Product> getAllProducts() {
        return null;
    }

    public Map<Integer, Integer> expiredCount() {
        return null;
    }

    public String alertOnMinimalQuantity() {
        return null;
    }
}
