package workers.DataAcsessLayer;

import workers.DomainLayer.Shift;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HeadOfBranchDAO {
    private static final String DB_URL = "jdbc:sqlite:" +  Paths.get("WorkersDB.db").toAbsolutePath().toString().replace("\\", "/");

    public static void createHeadOfBranchTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {

            String sql = "CREATE TABLE IF NOT EXISTS HeadOfBranch " +
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

            String sql = "CREATE TABLE IF NOT EXISTS minimalWorkers " +
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

            String sql = "CREATE TABLE IF NOT EXISTS RoleList " +
                    "(Role VARCHAR(255)," +
                    "WorkerID INTEGER," +
                    " PRIMARY KEY ( WorkerID, Role )," +
                    " FOREIGN KEY (WorkerID) REFERENCES Workers(ID))";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createNoticeTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {

            String sql = "CREATE TABLE IF NOT EXISTS Notice " +
                    "(WorkerID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "Date VARCHAR(255)," +
                    " FOREIGN KEY (WorkerID) REFERENCES Workers(ID))";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNotice(int workerID, String date) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "INSERT INTO Notice (WorkerID, Date) VALUES (" +
                    workerID + ",'" +
                    date + "')";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteNotice(int workerID) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "DELETE FROM Notice WHERE WorkerID = " + workerID;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, String> getNotices() {
        Map<Integer, String> notices = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM Notice";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                notices.put(rs.getInt("WorkerID"), rs.getString("Date"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notices;
    }

    public static void addHeadOfBranch(HeadOfBranchDTO headOfBranch) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            if(headOfBranch.getID() == 0)
            {
                String sql = "INSERT OR IGNORE INTO HeadOfBranch (ID, LastDayForPrefs) VALUES (" +
                        headOfBranch.getID() + "," +
                        headOfBranch.getLastDayForPrefs() + ")";
                stmt.executeUpdate(sql);
            }
            else if(headOfBranch.getBranchID() == -1) {
                String sql = "INSERT INTO HeadOfBranch (ID, LastDayForPrefs) VALUES (" +
                        headOfBranch.getID() + "," +
                        headOfBranch.getLastDayForPrefs() + ")";
                stmt.executeUpdate(sql);
            }
            else {
                String sql = "INSERT INTO HeadOfBranch (ID, BranchID, LastDayForPrefs) VALUES (" +
                        headOfBranch.getID() + "," +
                        headOfBranch.getBranchID() + "," +
                        headOfBranch.getLastDayForPrefs() + ")";
                stmt.executeUpdate(sql);
            }
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
            if(headOfBranch.getBranchID() == -1) {
                String sql = "UPDATE HeadOfBranch SET BranchID = NULL WHERE ID = " + headOfBranch.getID();
                stmt.executeUpdate(sql);
                return;
            }
            String sql = "UPDATE HeadOfBranch SET BranchID = " + headOfBranch.getBranchID() + " WHERE ID = " + headOfBranch.getID();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateHeadOfBranchLDP(HeadOfBranchDTO headOfBranch) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE HeadOfBranch SET  LastDayForPrefs = " + headOfBranch.getLastDayForPrefs() + " WHERE ID = " + headOfBranch.getID();
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

    public static HeadOfBranchDTO getHeadOfBranch(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM HeadOfBranch WHERE ID = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                HeadOfBranchDTO headOfBranch = new HeadOfBranchDTO();
                headOfBranch.setID(rs.getInt("ID"));
                headOfBranch.setBranchID(rs.getInt("BranchID"));
                headOfBranch.setLastDayForPrefs(rs.getInt("LastDayForPrefs"));
                return headOfBranch;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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


    public static Map<String, Integer> getMinimalWorkers(int branchID) {
        Map<String, Integer> minimalWorkers = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM minimalWorkers WHERE BranchID = " + branchID;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                minimalWorkers.put(rs.getString("Role"), rs.getInt("Amount"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minimalWorkers;
    }


    public static ShiftDTO getShift(String date, boolean dayShift, int id) {
        return ShiftDAO.getShift(date, dayShift, id);
    }

    public static void deleteRoles(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "DELETE FROM RoleList WHERE WorkerID = " + id;
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
