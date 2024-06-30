package Storage.DataAccessLayer;

import Storage.DomainLayer.Product;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ProductDAO {


    /*public ProductDAO(){
        try (var conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                var meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }*/

    public static void main(String[] args){
        final String URL = "jdbc:sqlite:" + Paths.get("data_layer.db").toAbsolutePath().toString().replace("\\","/");
        try (var conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                var meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

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

    public List<Product> getProductsByCategories(List<String[]> categories) {
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
