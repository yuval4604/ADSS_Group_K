package workers.DataAcsessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class HeadOfBranchDAO {
    private static final String DB_URL = "../WorkersDB.db";

    public static void createHeadOfBranchTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {

            String sql = "CREATE TABLE HeadOfBranch " +
                    "(ID INTEGER not NULL, " +
                    "BranchID INTEGER," +
                    "LastDayForPrefs INTEGER," +
                    " PRIMARY KEY ( ID )," +
                    " FOREIGN KEY (ID) REFERENCES Workers(ID), " +
                    " FOREIGN KEY (BranchID) REFERENCES Branches(ID))";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createMinimalWorkersTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {

            String sql = "CREATE TABLE minimalWorkers " +
                    "(BranchID INTEGER," +
                    "Role VARCHAR(255)," +
                    "Amount INTEGER," +
                    " PRIMARY KEY ( BranchID, Role )," +
                    " FOREIGN KEY (BranchID) REFERENCES Branches(ID))";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createRoleListTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {

            String sql = "CREATE TABLE RoleList " +
                    "(Role VARCHAR(255)," +
                    "WorkerID INTEGER," +
                    " PRIMARY KEY ( WorkerID, Role )," +
                    " FOREIGN KEY (WorkerID) REFERENCES Workers(WorkerID))";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addHeadOfBranch(HeadOfBranchDTO headOfBranch) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "INSERT INTO HeadOfBranch (ID, BranchID, LastDayForPrefs) VALUES (" +
                    headOfBranch.getID() + "," +
                    headOfBranch.getBranchID() + "," +
                    headOfBranch.getLastDayForPrefs() + ")";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteHeadOfBranch(int ID) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "DELETE FROM HeadOfBranch WHERE ID = " + ID;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addMinimalWorkers(int branchID, String role, int amount) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "INSERT INTO minimalWorkers (BranchID, Role, Amount) VALUES (" +
                    branchID + ",'" +
                    role + "'," +
                    amount + ")";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteMinimalWorkers(int branchID, String role) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "DELETE FROM minimalWorkers WHERE BranchID = " + branchID + " AND Role = '" + role + "'";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addRoleList(String role, int workerID) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "INSERT INTO RoleList (Role, WorkerID) VALUES ('" +
                    role + "'," +
                    workerID + ")";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteRoleList(String role, int workerID) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "DELETE FROM RoleList WHERE Role = '" + role + "' AND WorkerID = " + workerID;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void updateHeadOfBranchBID(HeadOfBranchDTO headOfBranch) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE HeadOfBranch SET BranchID = " + headOfBranch.getBranchID() + " WHERE ID = " + headOfBranch.getId();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateHeadOfBranchLDP(HeadOfBranchDTO headOfBranch) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE HeadOfBranch SET  LastDayForPrefs = " + headOfBranch.getLastDayForPrefs() + " WHERE ID = " + headOfBranch.getId();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateMinimalWorkers(int branchID, String role, int amount) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE minimalWorkers SET Amount = " + amount + " WHERE BranchID = " + branchID + " AND Role = '" + role + "'";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateRoleList(String role, int workerID) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE RoleList SET Role = '" + role + "' WHERE WorkerID = " + workerID;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HeadOfBranchDTO getHeadOfBranch(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM HeadOfBranch WHERE ID = " + id;
            return new HeadOfBranchDTO(stmt.executeQuery(sql).getInt("ID"),
                    stmt.executeQuery(sql).getInt("BranchID"),
                    stmt.executeQuery(sql).getInt("LastDayForPrefs"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getMinimalWorkersAmount(int branchID, String role) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM minimalWorkers WHERE BranchID = " + branchID + " AND Role = '" + role + "'";
            return stmt.executeQuery(sql).getInt("Amount");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<Integer> getRoleListWorkerID(String role) {
        List<Integer> workerIDs = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM RoleList WHERE Role = '" + role + "'";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                workerIDs.add(rs.getInt("WorkerID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return workerIDs;
    }

    public static List<String> getRoleListRole(int workerID) {
        List<String> roles = new LinkedList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM RoleList WHERE WorkerID = " + workerID;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                roles.add(rs.getString("Role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roles;
    }
}
