package Storage.DataAccessLayer;

import Storage.DomainLayer.Product;

import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductDAO {
    private final String URL = "jdbc:sqlite:" + Paths.get("data_layer.db").toAbsolutePath().toString().replace("\\", "/");

    public void rollback() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                conn.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Product getProduct(int catalogNumber) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
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
                    while (rs.next()) {
                        expirationDates.put(LocalDate.parse(rs.getString("expirationDate")), Map.entry(rs.getInt("storageQuantity"), rs.getInt("storeQuantity")));
                    }
                    p.setExpirationDates(expirationDates);
                    stmt = conn.prepareStatement("SELECT * FROM expiredProducts WHERE catalogNumber = ?");
                    stmt.setInt(1, catalogNumber);
                    Map<LocalDate, Integer> expiredProducts = new HashMap<>();
                    while (rs.next()) {
                        expiredProducts.put(LocalDate.parse(rs.getString("expiredDate")), rs.getInt("quantity"));
                    }
                    p.setExpiredProducts(expiredProducts);
                    return p;
                }
                return null;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void addProduct(Product product) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO product (catalogNumber, name, category, " +
                        "subCategory, size, buyPrice, salePrice, discount, supplierDiscount, storageQuantity, storeQuantity," +
                        " damagedQuantity, manufacturer, minimalQuantity, aisle) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
            throw e;
        }
    }

    public void updateProduct(int catalogNumber, Map<String, String> map) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                String query = "UPDATE product SET ";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    query += entry.getKey() + " = " + entry.getValue() + ", ";
                }
                query = query.substring(0, query.length() - 2) + " WHERE catalogNumber = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, catalogNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void deleteProduct(int catalogNumber) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM product WHERE catalogNumber = ?");
                stmt.setInt(1, catalogNumber);
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM expirationDates WHERE catalogNumber = ?");
                stmt.setInt(1, catalogNumber);
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM expiredProducts WHERE catalogNumber = ?");
                stmt.setInt(1, catalogNumber);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Product> getProductsByCategories(List<String[]> categories) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                String query = "SELECT * FROM product WHERE ";
                for (String[] categoryList : categories) {
                    switch (categoryList.length) {
                        case 1:
                            query += "(category = '" + categoryList[0] + "') OR ";
                            break;
                        case 2:
                            query += "(category = '" + categoryList[0] + "' AND subCategory = '" + categoryList[1] + "') OR ";
                            break;
                        case 3:
                            query += "(category = '" + categoryList[0] + "' AND subCategory = '" + categoryList[1] + "' AND size = '" + categoryList[2] + "') OR ";
                            break;
                    }
                }
                query = query.substring(0, query.length() - 4);
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                System.out.println(rs.getFetchSize());
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    Product p = new Product(rs.getInt("catalogNumber"), rs.getString("name"), rs.getString("category"), rs.getString("subCategory"), rs.getString("size"), rs.getDouble("buyPrice"), rs.getDouble("salePrice"), rs.getDouble("discount"), rs.getDouble("supplierDiscount"), rs.getString("manufacturer"), rs.getString("aisle"), rs.getInt("minimalQuantity"));
                    p.setStorageQuantity(rs.getInt("storageQuantity"));
                    p.setStoreQuantity(rs.getInt("storeQuantity"));
                    p.setDamagedQuantity(rs.getInt("damagedQuantity"));
                    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM expirationDates WHERE catalogNumber = ?");
                    pstmt.setInt(1, rs.getInt("catalogNumber"));
                    Map<LocalDate, Map.Entry<Integer, Integer>> expirationDates = new HashMap<>();
                    ResultSet rs2 = pstmt.executeQuery();
                    while (rs2.next()) {
                        expirationDates.put(LocalDate.parse(rs2.getString("expirationDate")), Map.entry(rs2.getInt("storageQuantity"), rs2.getInt("storeQuantity")));
                    }
                    p.setExpirationDates(expirationDates);
                    pstmt = conn.prepareStatement("SELECT * FROM expiredProducts WHERE catalogNumber = ?");
                    pstmt.setInt(1, rs.getInt("catalogNumber"));
                    Map<LocalDate, Integer> expiredProducts = new HashMap<>();
                    rs2 = pstmt.executeQuery();
                    while (rs2.next()) {
                        expiredProducts.put(LocalDate.parse(rs2.getString("expiredDate")), rs2.getInt("quantity"));
                    }
                    p.setExpiredProducts(expiredProducts);
                    products.add(p);
                }
                return products;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public List<Product> getAllProducts() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM product");
                ResultSet rs = stmt.executeQuery();
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    Product p = new Product(rs.getInt("catalogNumber"), rs.getString("name"), rs.getString("category"), rs.getString("subCategory"), rs.getString("size"), rs.getDouble("buyPrice"), rs.getDouble("salePrice"), rs.getDouble("discount"), rs.getDouble("supplierDiscount"), rs.getString("manufacturer"), rs.getString("aisle"), rs.getInt("minimalQuantity"));
                    p.setStorageQuantity(rs.getInt("storageQuantity"));
                    p.setStoreQuantity(rs.getInt("storeQuantity"));
                    p.setDamagedQuantity(rs.getInt("damagedQuantity"));
                    stmt = conn.prepareStatement("SELECT * FROM expirationDates WHERE catalogNumber = ?");
                    stmt.setInt(1, rs.getInt("catalogNumber"));
                    Map<LocalDate, Map.Entry<Integer, Integer>> expirationDates = new HashMap<>();
                    while (rs.next()) {
                        expirationDates.put(LocalDate.parse(rs.getString("expirationDate")), Map.entry(rs.getInt("storageQuantity"), rs.getInt("storeQuantity")));
                    }
                    p.setExpirationDates(expirationDates);
                    stmt = conn.prepareStatement("SELECT * FROM expiredProducts WHERE catalogNumber = ?");
                    stmt.setInt(1, rs.getInt("catalogNumber"));
                    Map<LocalDate, Integer> expiredProducts = new HashMap<>();
                    while (rs.next()) {
                        expiredProducts.put(LocalDate.parse(rs.getString("expiredDate")), rs.getInt("quantity"));
                    }
                    p.setExpiredProducts(expiredProducts);
                    products.add(p);
                }
                return products;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public Map<Integer, Integer> expiredCount () throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("SELECT catalogNumber, SUM(quantity) FROM expiredProducts GROUP BY catalogNumber");
                ResultSet rs = stmt.executeQuery();
                Map<Integer, Integer> expiredCount = new HashMap<>();
                while (rs.next()) {
                    expiredCount.put(rs.getInt("catalogNumber"), rs.getInt("SUM(quantity)"));
                }
                return expiredCount;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public String alertOnMinimalQuantity () throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("SELECT name FROM product WHERE storageQuantity + storeQuantity < minimalQuantity");
                ResultSet rs = stmt.executeQuery();
                String alert = "";
                while (rs.next()) {
                    alert += rs.getString("name") + "\n";
                }
                return alert;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }


    public void updateExpiration ( int catalogNumber, LocalDate expirationDate,int storeQuantity,
    int storageQuantity) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);

                PreparedStatement stmt = conn.prepareStatement("UPDATE expirationDates SET storageQuantity = ?, storeQuantity = ? WHERE catalogNumber = ? AND expirationDate = ?");
                stmt.setInt(3, catalogNumber);
                stmt.setString(4, expirationDate.toString());
                stmt.setInt(1, storageQuantity);
                stmt.setInt(2, storeQuantity);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateExpired ( int catalogNumber, LocalDate expirationDate,int newAmount) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);

                PreparedStatement stmt = conn.prepareStatement("UPDATE expiredProducts SET quantity = ? WHERE catalogNumber = ? AND expiredDate = ?");
                stmt.setInt(2, catalogNumber);
                stmt.setString(3, expirationDate.toString());
                stmt.setInt(1, newAmount);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<String> getCategories() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM categories");
                ResultSet rs = stmt.executeQuery();
                List<String> categories = new ArrayList<>();
                while (rs.next()) {
                    categories.add(rs.getString("category"));
                }
                return categories;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void addCategory(String category) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO categories (category) VALUES (?)");
                stmt.setString(1, category);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<String> getSubCategories() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM subCategories");
                ResultSet rs = stmt.executeQuery();
                List<String> subCategories = new ArrayList<>();
                while (rs.next()) {
                    subCategories.add(rs.getString("subCategory"));
                }
                return subCategories;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void addSubCategory(String subCategory) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO subCategories (subCategory) VALUES (?)");
                stmt.setString(1, subCategory);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<String> getSizes() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sizes");
                ResultSet rs = stmt.executeQuery();
                List<String> sizes = new ArrayList<>();
                while (rs.next()) {
                    sizes.add(rs.getString("size"));
                }
                return sizes;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void addSize(String size) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                conn.setAutoCommit(false);
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO sizes (size) VALUES (?)");
                stmt.setString(1, size);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw e;
        }
    }
}

