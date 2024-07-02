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
    private String URL;
    private Connection conn;

    public ProductDAO(String dataBaseName) throws SQLException {
        URL = "jdbc:sqlite:" + Paths.get(dataBaseName).toAbsolutePath().toString().replace("\\", "/");
        conn = DriverManager.getConnection(URL);
        if(conn == null)
            throw new SQLException("Connection failed");
        conn.setAutoCommit(false);
    }


    public Product getProduct(int catalogNumber) throws SQLException {
        try {
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
                    conn.commit();
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
        try {
            if (conn != null) {
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
                conn.commit();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateProduct(int catalogNumber, Map<String, String> map) throws SQLException {
        try {
            if (conn != null) {
                String query = "UPDATE product SET ";
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    query += entry.getKey() + " = " + entry.getValue() + ", ";
                }
                query = query.substring(0, query.length() - 2) + " WHERE catalogNumber = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, catalogNumber);
                stmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void deleteProduct(int catalogNumber) throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM product WHERE catalogNumber = ?");
                stmt.setInt(1, catalogNumber);
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM expirationDates WHERE catalogNumber = ?");
                stmt.setInt(1, catalogNumber);
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM expiredProducts WHERE catalogNumber = ?");
                stmt.setInt(1, catalogNumber);
                stmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Product> getProductsByCategories(List<String[]> categories) throws SQLException {
        try {
            if (conn != null) {
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
                conn.commit();
                return products;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public List<Product> getAllExpiredProducts() throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM product where (catalogNumber in (SELECT catalogNumber FROM expiredProducts)) OR (damagedQuantity > 0) ");
                ResultSet rs = stmt.executeQuery();
                List<Product> products = new ArrayList<>();
                List<Integer> SNN = new ArrayList<>();
                while (rs.next()) {
                    Product p = new Product(rs.getInt("catalogNumber"), rs.getString("name"), rs.getString("category"), rs.getString("subCategory"), rs.getString("size"), rs.getDouble("buyPrice"), rs.getDouble("salePrice"), rs.getDouble("discount"), rs.getDouble("supplierDiscount"), rs.getString("manufacturer"), rs.getString("aisle"), rs.getInt("minimalQuantity"));
                    p.setStorageQuantity(rs.getInt("storageQuantity"));
                    p.setStoreQuantity(rs.getInt("storeQuantity"));
                    p.setDamagedQuantity(rs.getInt("damagedQuantity"));
                    stmt = conn.prepareStatement("SELECT * FROM expirationDates WHERE catalogNumber = ?");
                    SNN.add(rs.getInt("catalogNumber"));
                    stmt.setInt(1, rs.getInt("catalogNumber"));
                    ResultSet rs2 = stmt.executeQuery();
                    Map<LocalDate, Map.Entry<Integer, Integer>> expirationDates = new HashMap<>();
                    while (rs2.next()) {
                        expirationDates.put(LocalDate.parse(rs2.getString("expirationDate")), Map.entry(rs2.getInt("storageQuantity"), rs2.getInt("storeQuantity")));
                    }
                    p.setExpirationDates(expirationDates);
                    stmt = conn.prepareStatement("SELECT * FROM expiredProducts WHERE catalogNumber = ?");
                    stmt.setInt(1, rs.getInt("catalogNumber"));
                    rs2 = stmt.executeQuery();
                    Map<LocalDate, Integer> expiredProducts = new HashMap<>();
                    while (rs2.next()) {
                        expiredProducts.put(LocalDate.parse(rs2.getString("expiredDate")), rs2.getInt("quantity"));
                    }
                    p.setExpiredProducts(expiredProducts);
                    products.add(p);
                }
                conn.commit();
                emptyExpiredAndDamagedProducts(SNN);
                return products;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    private void emptyExpiredAndDamagedProducts(List<Integer> snn) {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM expiredProducts");
                stmt.executeUpdate();
                conn.commit();
                stmt = conn.prepareStatement("UPDATE product SET damagedQuantity = 0 WHERE catalogNumber = ?");
                for (int i : snn) {
                    stmt.setInt(1, i);
                    stmt.executeUpdate();
                    conn.commit();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Integer> expiredCount () throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT catalogNumber, SUM(quantity) FROM expiredProducts GROUP BY catalogNumber");
                ResultSet rs = stmt.executeQuery();
                Map<Integer, Integer> expiredCount = new HashMap<>();
                while (rs.next()) {
                    expiredCount.put(rs.getInt("catalogNumber"), rs.getInt("SUM(quantity)"));
                }
                conn.commit();
                return expiredCount;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public String alertOnMinimalQuantity () throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT name FROM product WHERE storageQuantity + storeQuantity < minimalQuantity");
                ResultSet rs = stmt.executeQuery();
                String alert = "";
                while (rs.next()) {
                    alert += rs.getString("name") + "\n";
                }
                conn.commit();
                if(alert.equals(""))
                    return "No products are below the minimal quantity";
                return alert.substring(0, alert.length() - 1);
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }


    public void updateExpiration ( int catalogNumber, LocalDate expirationDate,int storeQuantity,
    int storageQuantity) throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM expirationDates WHERE catalogNumber = ? AND expirationDate = ?");
                stmt.setInt(1, catalogNumber);
                stmt.setString(2, expirationDate.toString());
                ResultSet rs = stmt.executeQuery();
                if (!rs.next()) {
                    stmt = conn.prepareStatement("INSERT INTO expirationDates (catalogNumber, expirationDate, storageQuantity, storeQuantity) VALUES (?, ?, ?, ?)");
                    stmt.setInt(1, catalogNumber);
                    stmt.setString(2, expirationDate.toString());
                    stmt.setInt(3, storageQuantity);
                    stmt.setInt(4, storeQuantity);
                    stmt.executeUpdate();
                } else {
                    stmt = conn.prepareStatement("UPDATE expirationDates SET storageQuantity = ?, storeQuantity = ? WHERE catalogNumber = ? AND expirationDate = ?");
                    stmt.setInt(3, catalogNumber);
                    stmt.setString(4, expirationDate.toString());
                    stmt.setInt(1, storageQuantity);
                    stmt.setInt(2, storeQuantity);
                    stmt.executeUpdate();
                }
                conn.commit();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void updateExpired ( int catalogNumber, LocalDate expirationDate,int newAmount) throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE expiredProducts SET quantity = ? WHERE catalogNumber = ? AND expiredDate = ?");
                stmt.setInt(2, catalogNumber);
                stmt.setString(3, expirationDate.toString());
                stmt.setInt(1, newAmount);
                stmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<String> getCategories() throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM categories");
                ResultSet rs = stmt.executeQuery();
                List<String> categories = new ArrayList<>();
                while (rs.next()) {
                    categories.add(rs.getString("category"));
                }
                conn.commit();
                return categories;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void addCategory(String category) throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO categories (category) VALUES (?)");
                stmt.setString(1, category);
                stmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<String> getSubCategories() throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM subCategories");
                ResultSet rs = stmt.executeQuery();
                List<String> subCategories = new ArrayList<>();
                while (rs.next()) {
                    subCategories.add(rs.getString("subCategory"));
                }
                conn.commit();
                return subCategories;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void addSubCategory(String subCategory) throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO subCategories (subCategory) VALUES (?)");
                stmt.setString(1, subCategory);
                stmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<String> getSizes() throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sizes");
                ResultSet rs = stmt.executeQuery();
                List<String> sizes = new ArrayList<>();
                while (rs.next()) {
                    sizes.add(rs.getString("size"));
                }
                conn.commit();
                return sizes;
            }
        } catch (SQLException e) {
            throw e;
        }
        return null;
    }

    public void addSize(String size) throws SQLException {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO sizes (size) VALUES (?)");
                stmt.setString(1, size);
                stmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    public void deleteAll() {
        try {
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM product");
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM expirationDates");
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM expiredProducts");
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM categories");
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM subCategories");
                stmt.executeUpdate();
                stmt = conn.prepareStatement("DELETE FROM sizes");
                stmt.executeUpdate();
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

