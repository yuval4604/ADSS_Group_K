package workers.DataAcsessLayer;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftDAO {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    private static final String DB_URL = "jdbc:sqlite:workers/DataAcsessLayer/WorkersDB.db"; //+ Paths.get("workers/DataAcsessLayer/WorkersDB.db").toAbsolutePath().toString().replace("\\", "/");


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
                    " PRIMARY KEY ( date, dayShift,branchID), " +
                    " FOREIGN KEY (managerID) REFERENCES Workers(id), " +
                    " FOREIGN KEY (branchID) REFERENCES Workers(id))";
            stmt.executeUpdate(sql);
            String sql1 = "CREATE TABLE Roles" +
                    "(role VARCHAR(30), " +
                    " workerID INTEGER, " +
                    "branchID INTEGER," +
                    "date VARCHAR(30), " +
                    " dayShift BIT(1), " +
                    "PRIMARY KEY(role,workerID), " +
                    "FOREIGN KEY(date,dayShift,branchID) REFERENCES Shifts(date, dayShift,branchID))";
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
            String sql = "UPDATE INTO SHIFT SET active = " + shiftDTO.getActive() + " WHERE date = " + shiftDTO.getDate() + " AND dayShift = " + shiftDTO.getDayShift() + " AND branchId = " + shiftDTO.getBranchId();
            stmt.executeUpdate(sql);
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
            String sql = "UPDATE INTO SHIFT SET managerID = " + shiftDTO.getManagerId() + " WHERE date = " + shiftDTO.getDate() + " AND dayShift = " + shiftDTO.getDayShift() + " AND branchId = " + shiftDTO.getBranchId();
            stmt.executeUpdate(sql);
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
            String sql = "UPDATE INTO SHIFT SET branchID = " + shiftDTO.getBranchId() + " WHERE date = " + shiftDTO.getDate() + " AND dayShift = " + shiftDTO.getDayShift() + " AND branchId = " + shiftDTO.getBranchId();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void deleteShift(String date, boolean dayShift, int branchId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "DELETE FROM Shifts WHERE date = " + date + " AND dayShift = " + dayShift + " AND branchId = " + branchId;
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static ShiftDTO getShift(String date, boolean dayShift, int branchId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "SELECT * FROM Shifts WHERE date = " + date + " AND dayShift = " + dayShift + " AND branchId = " + branchId;
            ResultSet rs = stmt.executeQuery(sql);
            ShiftDTO shiftDTO = new ShiftDTO();
            shiftDTO.setDate(rs.getString("date"));
            shiftDTO.setDayShift(rs.getBoolean("dayShift"));
            shiftDTO.setActive(rs.getBoolean("active"));
            shiftDTO.setDayOfWeek(rs.getInt("dayOfWeek"));
            shiftDTO.setManagerId(rs.getInt("managerID"));
            shiftDTO.setBranchId(rs.getInt("branchID"));

            String sql1 = "SELECT * FROM Roles WHERE date = " + date + " AND dayShift = " + dayShift + " AND branchId = " + branchId;
            ResultSet rs1 = stmt.executeQuery(sql1);
            Map<String, List<Integer>> workers = new HashMap<>();
            while (rs1.next()) {
                String role = rs1.getString("role");
                int workerId = rs1.getInt("workerID");
                if (workers.containsKey(role)) {
                    workers.get(role).add(workerId);
                }
                else {
                    workers.put(role, List.of(workerId));
                }
            }
            shiftDTO.setWorkers(workers);

            return shiftDTO;

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static  void setBranchID(ShiftDTO shiftDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "UPDATE INTO SHIFT SET branchID = " + shiftDTO.getBranchId() + " WHERE date = " + shiftDTO.getDate() + " AND dayShift = " + shiftDTO.getDayShift();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
