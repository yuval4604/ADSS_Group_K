package Storage.DataAccessLayer;

import Storage.DomainLayer.Product;

import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductDAO {
    private final String URL = "jdbc:sqlite:" + Paths.get("data_layer.db").toAbsolutePath().toString().replace("\\","/");


    public Product getProduct(int catalogNumber) {
        try (var conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM product WHERE catalogNumber = ?");
                stmt.setInt(1, catalogNumber);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Product p = new Product(rs.getInt("catalogNumber"), rs.getString("name"), rs.getString("category"), rs.getString("subCategory"), rs.getString("size"), rs.getDouble("buyPrice"), rs.getDouble("salePrice"), rs.getDouble("discount"), rs.getDouble("supplierDiscount"), rs.getString("manufacturer"), rs.getString("aisle"), rs.getInt("minimalQuantity"));
                    p.setStorageQuantity(rs.getInt("storageQuantity"));
                    p.setStoreQuantity(rs.getInt("storeQuantity"));
                    p.setDamagedQuantity(rs.getInt("damagedQuantity"));
                    stmt = conn.prepareStatement("SELECT * FROM expirationDates WHERE catalogNumber = ?");
                    stmt.setInt(1, catalogNumber);
                    Map<LocalDate, Map.Entry<Integer, Integer>> expirationDates = new HashMap<>();
                    while(rs.next()){
                        expirationDates.put(LocalDate.parse(rs.getString("expiredDate")), Map.entry(rs.getInt("storageQuantity"), rs.getInt("storeQuantity")));
                    }
                    p.setExpirationDates(expirationDates);
                    stmt = conn.prepareStatement("SELECT * FROM expiredProducts WHERE catalogNumber = ?");
                    stmt.setInt(1, catalogNumber);
                    Map<LocalDate, Integer> expiredProducts = new HashMap<>();
                    while(rs.next()){
                        expiredProducts.put(LocalDate.parse(rs.getString("expiredDate")), rs.getInt("quantity"));
                    }
                    p.setExpiredProducts(expiredProducts);
                    return p;
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addProduct(Product product) {
        try (var conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO product (catalogNumber, name, category, subCategory, size, buyPrice, salePrice, discount, supplierDiscount, storageQuantity, storeQuantity, damagedQuantity, manufacturer, minimalQuantity, aisle) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                stmt.setInt(1, product.getCatalogNumber());
                stmt.setString(2, product.getName());
                stmt.setString(3, product.getCategory());
                stmt.setString(4, product.getSubCategory());
                stmt.setString(5, product.getSize());
                stmt.setDouble(6, product.getBuyPrice());
                stmt.setDouble(7, product.getSalePrice());
                stmt.setDouble(8, product.getDiscount());
                stmt.setDouble(9, product.getSupplierDiscount());
                stmt.setInt(10, product.getStorageQuantity());
                stmt.setInt(11, product.getStoreQuantity());
                stmt.setInt(12, product.getDamagedQuantity());
                stmt.setString(13, product.getManufacturer());
                stmt.setInt(14, product.getMinimalQuantity());
                stmt.setString(15, product.getAisle());
                stmt.executeUpdate();

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
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
