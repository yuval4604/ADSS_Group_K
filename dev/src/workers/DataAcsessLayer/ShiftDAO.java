package workers.DataAcsessLayer;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShiftDAO {


    private static final String DB_URL = "jdbc:sqlite:" + Paths.get("WorkersDB.db").toAbsolutePath().toString().replace("\\", "/");


    public static void createShiftTable() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Shifts " +
                    "(date VARCHAR(30), " +
                    " dayShift BIT(1), " +
                    " active BIT(1), " +
                    " dayOfWeek INTEGER, " +
                    " managerID INTEGER, " +
                    " branchID INTEGER, " +
                    "needQM BIT(1)," +
                    " PRIMARY KEY ( date, dayShift,branchID), " +
                    " FOREIGN KEY (managerID) REFERENCES Workers(id), " +
                    " FOREIGN KEY (branchID) REFERENCES Workers(id))";
            stmt.executeUpdate(sql);
            String sql1 = "CREATE TABLE IF NOT EXISTS Roles" +
                    "(role VARCHAR(30), " +
                    " workerID INTEGER, " +
                    "branchID INTEGER," +
                    "date VARCHAR(30), " +
                    " dayShift BIT(1), " +
                    "PRIMARY KEY(role,workerID, date, dayShift, branchID), " +
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
        ) {
            int needQM = shiftDTO.getNeedQM() ? 1 : 0;
            int active = shiftDTO.getActive() ? 1 : 0;
            int dayShift = shiftDTO.getDayShift() ? 1 : 0;
            if (shiftDTO.getManagerId() == -1) {
                String sql = "INSERT INTO Shifts (date, dayShift, active, dayOfWeek, branchID, needQM) " +
                        "VALUES ('" + shiftDTO.getDate() + "', " + dayShift + ", " + active + ", " + shiftDTO.getDayOfWeek() + ", " + shiftDTO.getBranchId() + ", " + needQM + ")";
                stmt.executeUpdate(sql);

            } else {
                String sql = "INSERT INTO Shifts (date, dayShift, active, dayOfWeek, managerID, branchID, needQM) " +
                        "VALUES ('" + shiftDTO.getDate() + "', " + dayShift + ", " + active + ", " + shiftDTO.getDayOfWeek() + ", " + shiftDTO.getManagerId() + ", " + shiftDTO.getBranchId() + ", " + needQM + ")";
                stmt.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateNeedQM(ShiftDTO shiftDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "UPDATE Shifts SET needQM = " + shiftDTO.getNeedQM() + " WHERE date = '" + shiftDTO.getDate() + "' AND dayShift = " + shiftDTO.getDayShift() + " AND branchId = " + shiftDTO.getBranchId();
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
            String sql = "DELETE FROM Shifts WHERE date = '" + date + "' AND dayShift = " + dayShift + " AND branchId = " + branchId;
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "DELETE FROM Roles WHERE date = '" + date + "' AND dayShift = " + dayShift + " AND branchId = " + branchId;
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
            int dayShiftInt = dayShift ? 1 : 0;
            String sql = "SELECT * FROM Shifts WHERE date = '" + date + "' AND dayShift = " + dayShiftInt + " AND branchId = " + branchId;
            ResultSet rs = stmt.executeQuery(sql);
            ShiftDTO shiftDTO = new ShiftDTO();
            shiftDTO.setDate(rs.getString("date"));
            shiftDTO.setDayShift(rs.getBoolean("dayShift"));
            shiftDTO.setActive(rs.getBoolean("active"));
            shiftDTO.setDayOfWeek(rs.getInt("dayOfWeek"));
            shiftDTO.setManagerId(rs.getInt("managerID"));
            shiftDTO.setBranchId(rs.getInt("branchID"));
            shiftDTO.setNeedQM(rs.getBoolean("needQM"));


            String sql1 = "SELECT * FROM Roles WHERE date = '" + date + "' AND dayShift = " + dayShift + " AND branchId = " + branchId;
            ResultSet rs1 = stmt.executeQuery(sql1);
            Map<String, List<Integer>> workers = new HashMap<>();
            while (rs1.next()) {
                String role = rs1.getString("role");
                int workerId = rs1.getInt("workerID");
                if (workers.containsKey(role)) {
                    workers.get(role).add(workerId);
                }
                else {
                    workers.put(role, new LinkedList<>());
                    workers.get(role).add(workerId);
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

    public static void insertWorkerToShift(ShiftDTO shiftDTO, String role, int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            int dayShift = shiftDTO.getDayShift() ? 1 : 0;
            String sql = "INSERT INTO Roles (role, workerID, branchID, date, dayShift) " +
                    "VALUES ('" + role + "', " + id + ", " + shiftDTO.getBranchId() + ", '" + shiftDTO.getDate() + "', " + dayShift + ")";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteWorkerFromShift(String date, boolean dayShift, int bID, int wID) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            int dayShiftInt = dayShift ? 1 : 0;
            String sql = "DELETE FROM Roles WHERE date = '" + date + "' AND dayShift = " + dayShiftInt + " AND workerID = " + wID + " AND branchID = " + bID;
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
