package workers.DataAcsessLayer;

import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class LoginDAO {
    private static final String DB_URL = "jdbc:sqlite:" + Paths.get("WorkersDB.db").toAbsolutePath().toString().replace("\\", "/");

    public static void createTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS Users (ID INTEGER PRIMARY KEY, password TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkLogin(int id, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE ID = '" + id + "' AND password = '" + password + "'");
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void insertUser(int id, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO Users (ID, password) VALUES ('" + id + "', '" + password + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Users WHERE ID = '" + id + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePassword(int id, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("UPDATE Users SET password = '" + password + "' WHERE ID = '" + id + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
