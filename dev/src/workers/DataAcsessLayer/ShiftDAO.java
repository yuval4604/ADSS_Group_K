package workers.DataAcsessLayer;
import java.sql.*;

public class ShiftDAO {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    private static final String DB_URL = "jdbc:sqlite:./src/workers/DataAcsessLayer/shifts.db";


    public static void createShiftTable() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE Shifts " +
                    "(date VARCHAR(30), " +
                    " dayShift BIT(1), " +
                    " active BIT(1), " +
                    " dayOfWeek INTEGER, " +
                    " managerID INTEGER, " +
                    " branchID INTEGER, " +
                    " PRIMARY KEY ( date, dayShift), " +
                    " FOREIGN KEY (managerID) REFERENCES Branches(id), " + // TODO: change table of FK
                    " FOREIGN KEY (branchID) REFERENCES Workers(id))"; // Same here
            stmt.executeUpdate(sql);
            String sql1 = "CREATE TABLE Roles" +
                    "(role VARCHAR(30), " +
                    " workerID INTEGER, " +
                    "FOREIGN KEY(role) REFERENCES Shifts(date, dayShift)"; // TODO: fix FK
            stmt.executeUpdate(sql1);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertShift(ShiftDTO shiftDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "INSERT INTO Shifts (date, dayShift, active, dayOfWeek, managerID, branchID) " +
                    "VALUES ('" + shiftDTO.getDate() + "', " + shiftDTO.getDayShift() + ", " + shiftDTO.getActive() + ", " + shiftDTO.getDayOfWeek() + ", " + shiftDTO.getManagerId() + ", " + shiftDTO.getBranchId() + ")";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateShiftActive(ShiftDTO shiftDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "UPDATE INTO SHIFT SET active = " + shiftDTO.getActive() + " WHERE date = " + shiftDTO.getDate() + " AND dayShift = " + shiftDTO.getDayShift();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateShiftManager(ShiftDTO shiftDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "UPDATE INTO SHIFT SET managerID = " + shiftDTO.getManagerId() + " WHERE date = " + shiftDTO.getDate() + " AND dayShift = " + shiftDTO.getDayShift();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateShiftBranch(ShiftDTO shiftDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "UPDATE INTO SHIFT SET branchID = " + shiftDTO.getBranchId() + " WHERE date = " + shiftDTO.getDate() + " AND dayShift = " + shiftDTO.getDayShift();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteShift(ShiftDTO shiftDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "DELETE FROM Shifts WHERE date = " + shiftDTO.getDate() + " AND dayShift = " + shiftDTO.getDayShift();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void getShift(String date, boolean dayShift) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "SELECT * FROM Shifts WHERE date = " + date + " AND dayShift = " + dayShift;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString("date") + " " + rs.getBoolean("dayShift") + " " + rs.getBoolean("active") + " " + rs.getInt("dayOfWeek") + " " + rs.getInt("managerID") + " " + rs.getInt("branchID"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
