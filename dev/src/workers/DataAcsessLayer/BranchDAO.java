package workers.DataAcsessLayer;

import java.nio.file.Paths;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class BranchDAO {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    private static final String DB_URL = "jdbc:sqlite:" + Paths.get("WorkersDB.db").toAbsolutePath().toString().replace("\\", "/");
    public static void createBranchTable() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Branches " +
                    "(id INTEGER PRIMARY KEY, " +
                    " Name VARCHAR(30), " +
                    " Address VARCHAR(30), " +
                    " HeadOfBranchId INTEGER, " +
                    " FOREIGN KEY (headOfBranchId) REFERENCES Workers(Id))";
            stmt.executeUpdate(sql);
            String sql1 = "CREATE TABLE IF NOT EXISTS branchWorkers" +
                    "(BranchID INTEGER, " +
                    " WorkerID INTEGER, " +
                    " PRIMARY KEY (branchID, workerID), " +
                    " FOREIGN KEY (branchID) REFERENCES Branches(id), " +
                    " FOREIGN KEY (workerID) REFERENCES Workers(id))";
            stmt.executeUpdate(sql1);
            String sql2 = "CREATE TABLE IF NOT EXISTS branchShifts" +
                    "(BranchID INTEGER, " +
                    " Date VARCHAR(30), " +
                    " DayShift BIT(1), " +
                    " PRIMARY KEY (BranchID, Date, DayShift), " +
                    " FOREIGN KEY (BranchID) REFERENCES Branches(Id)," +
                    " FOREIGN KEY (date, dayShift, branchID) REFERENCES Shifts(date, dayShift, branchID))";
            stmt.executeUpdate(sql2);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public static void insertBranch(BranchDTO branchDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            if(branchDTO.getHeadOfBranchId() == 0) {
                String sql = "INSERT OR IGNORE INTO Branches (id, Name, Address) " +
                        "VALUES (" + branchDTO.getId() + ", '" + branchDTO.getName() + "', '" + branchDTO.getAddress() + "')";
                stmt.executeUpdate(sql);
                return;
            }
            String sql = "INSERT INTO Branches (id, Name, Address, HeadOfBranchId) " +
                    "VALUES (" + branchDTO.getId() + ", '" + branchDTO.getName() + "', '" + branchDTO.getAddress() + "', " + branchDTO.getHeadOfBranchId() + ")";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteBranch(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "DELETE FROM Branches WHERE id = " + id;
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static BranchDTO getBranch(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "SELECT * FROM Branches WHERE id = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            BranchDTO branchDTO = new BranchDTO();
            branchDTO.setId(rs.getInt("id"));
            branchDTO.setName(rs.getString("Name"));
            branchDTO.setAddress(rs.getString("Address"));
            branchDTO.setHeadOfBranchId(rs.getInt("HeadOfBranchId"));

            String sql1 = "SELECT * FROM branchWorkers WHERE BranchID = " + id;
            ResultSet rs1 = stmt.executeQuery(sql1);
            while (rs1.next()) {
                branchDTO.getWorkers().add(rs1.getInt("WorkerID"));
            }

            String sql2 = "SELECT * FROM branchShifts WHERE BranchID = " + id;
            ResultSet rs2 = stmt.executeQuery(sql2);
            while (rs2.next()) {
                String shift = rs2.getString("Date");
                shift += ","  + rs2.getBoolean("DayShift");
                shift += "," + rs2.getInt("BranchID");
                branchDTO.getShifts().add(shift);
            }

            return branchDTO;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertBranchWorker(int branchId, int workerId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "INSERT INTO branchWorkers (BranchID, WorkerID) " +
                    "VALUES (" + branchId + ", " + workerId + ")";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBranchWorker(int branchId, int workerId) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "DELETE FROM branchWorkers WHERE BranchID = " + branchId + " AND WorkerID = " + workerId;
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertBranchShift(int branchId, String date, boolean dayShift) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "INSERT INTO branchShifts (BranchID, Date, DayShift) " +
                    "VALUES (" + branchId + ", '" + date + "', " + dayShift + ")";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<Integer> getAllBranchesIDs() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "SELECT id FROM Branches";
            ResultSet rs = stmt.executeQuery(sql);
            List<Integer> ids = new LinkedList<>();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
            return ids;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
