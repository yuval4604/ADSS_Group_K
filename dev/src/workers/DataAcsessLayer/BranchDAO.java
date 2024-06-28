package workers.DataAcsessLayer;

import java.sql.*;

public class BranchDAO {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;


    private static final String DB_URL = "jdbc:sqlite:WorkersDB.db";
    public static void createBranchTable() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE Branches " +
                    "(id INTEGER PRIMARY KEY, " +
                    " Name VARCHAR(30), " +
                    " Address VARCHAR(30), " +
                    " HeadOfBranchId INTEGER, " +
                    " FOREIGN KEY (headOfBranchId) REFERENCES Workers(Id))";
            stmt.executeUpdate(sql);
            String sql1 = "CREATE TABLE branchWorkers" +
                    "(BranchID INTEGER, " +
                    " WorkerID INTEGER, " +
                    " PRIMARY KEY (branchID, workerID), " +
                    " FOREIGN KEY (branchID) REFERENCES Branches(id), " +
                    " FOREIGN KEY (workerID) REFERENCES Workers(id))";
            stmt.executeUpdate(sql1);
            String sql2 = "CREATE TABLE branchShifts" +
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
            String sql = "INSERT INTO Branches (id, Name, Address, HeadOfBranchId) " +
                    "VALUES (" + branchDTO.getId() + ", '" + branchDTO.getName() + "', '" + branchDTO.getAddress() + "', " + branchDTO.getHeadOfBranchId() + ")";
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBranchId(BranchDTO branchDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "UPDATE Branches SET id = " + branchDTO.getId() + " WHERE id = " + branchDTO.getId();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateBranchName(BranchDTO branchDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "UPDATE Branches SET Name = '" + branchDTO.getName() + "' WHERE id = " + branchDTO.getId();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateBranchAddress(BranchDTO branchDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "UPDATE Branches SET Address = '" + branchDTO.getAddress() + "' WHERE id = " + branchDTO.getId();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateBranchHeadOfBranchId(BranchDTO branchDTO) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        )
        {
            String sql = "UPDATE Branches SET HeadOfBranchId = " + branchDTO.getHeadOfBranchId() + " WHERE id = " + branchDTO.getId();
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
                branchDTO.getShifts().add(rs2.getInt("BranchID"));
            }

            return branchDTO;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
