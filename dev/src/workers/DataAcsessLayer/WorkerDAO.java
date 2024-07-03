package workers.DataAcsessLayer;

import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class WorkerDAO  {
    private static final String DB_URL = "jdbc:sqlite:" + Paths.get("WorkersDB.db").toAbsolutePath().toString().replace("\\", "/");

    public static void createWorkerTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS Workers " +
                    "(ID INTEGER not NULL, " +
                    " Name VARCHAR(255), " +
                    " BankNumber INTEGER, " +
                    " GWage INTEGER, " +
                    " HWage INTEGER, " +
                    " DateOfStart DATE, " +
                    " FullTime BIT(1), " +
                    " TotalVacationDays INTEGER, " +
                    " CurrentVacationDays INTEGER, " +
                    " isBM BIT(1), " +
                    " Changed BIT(1), " +
                    "BranchName VARCHAR(255)," +
                    "Licenses VARCHAR(255)," +
                    " FOREIGN KEY (BranchName) REFERENCES Branches(Name)," +
                    " PRIMARY KEY ( ID ))";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createPrefsTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {

            String sql = "CREATE TABLE IF NOT EXISTS Prefs " +
                    "(ID INTEGER not NULL, " +
                    " SUNDAY VARCHAR(255), " +
                    " MONDAY VARCHAR(255), " +
                    " TUEDAY VARCHAR(255), " +
                    " WEDDAY VARCHAR(255), " +
                    " THURDAY VARCHAR(255), " +
                    " FRIDAY VARCHAR(255), " +
                    " SUNNIGHT VARCHAR(255), " +
                    " MONNIGHT VARCHAR(255), " +
                    " TUENIGHT VARCHAR(255), " +
                    " WEDNIGHT VARCHAR(255), " +
                    " THURNIGHT VARCHAR(255), " +
                    " FRINIGHT VARCHAR(255), " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addWorker(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            if(worker.getBranchName() == null)
                worker.setBranchName("");
            if(worker.getID() == 0)
            {
                String sql = "INSERT OR IGNORE INTO Workers (ID, Name, BankNumber, GWage, HWage, DateOfStart, FullTime, TotalVacationDays, CurrentVacationDays, isBM, Changed, BranchName, Licenses) " +
                        "VALUES (" + worker.getID() + ", '" + worker.getName() + "', " + worker.getBankAccount() + ", " + worker.getGWage() + ", " + worker.getHWage() + ", '" + worker.getStartDate() + "', " + worker.getFTime() + ", " + worker.getTVDays() + ", " + worker.getCVDays() + ", " + worker.getIsHeadOfBranch() + ", " + worker.getChange() + ", '" + worker.getBranchName() + "', '" + worker.getLicensesString() + "')";
                stmt.executeUpdate(sql);
            }
            else {
                String sql = "INSERT INTO Workers (ID, Name, BankNumber, GWage, HWage, DateOfStart, FullTime, TotalVacationDays, CurrentVacationDays, isBM, Changed, BranchName, Licenses) " +
                        "VALUES (" + worker.getID() + ", '" + worker.getName() + "', " + worker.getBankAccount() + ", " + worker.getGWage() + ", " + worker.getHWage() + ", '" + worker.getStartDate() + "', " + worker.getFTime() + ", " + worker.getTVDays() + ", " + worker.getCVDays() + ", " + worker.getIsHeadOfBranch() + ", " + worker.getChange() + ", '" + worker.getBranchName() + "', '" + worker.getLicensesString() + "')";
                stmt.executeUpdate(sql);
            }
            if(worker.getPref() == null)
            {
                String[][] pref = new String[6][2];
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 2; j++) {
                        pref[i][j] = "can";
                    }
                }
                worker.setPref(pref);
            }
            if(worker.getID() == 0){
                String sql2 = "INSERT OR IGNORE INTO Prefs (ID, SUNDAY, MONDAY, TUEDAY, WEDDAY, THURDAY, FRIDAY, SUNNIGHT, MONNIGHT, TUENIGHT, WEDNIGHT, THURNIGHT, FRINIGHT) " +
                        "VALUES (" + worker.getID() + ", '" + worker.getPref()[0][0] + "', '" + worker.getPref()[1][0] + "', '" + worker.getPref()[2][0] + "', '" + worker.getPref()[3][0] + "', '" + worker.getPref()[4][0] + "', '" + worker.getPref()[5][0] + "', '" + worker.getPref()[0][1] + "', '" + worker.getPref()[1][1] + "', '" + worker.getPref()[2][1] + "', '" + worker.getPref()[3][1] + "', '" + worker.getPref()[4][1] + "', '" + worker.getPref()[5][1] + "')";
                stmt.executeUpdate(sql2);
                return;
            }
            String sql2 = "INSERT INTO Prefs (ID, SUNDAY, MONDAY, TUEDAY, WEDDAY, THURDAY, FRIDAY, SUNNIGHT, MONNIGHT, TUENIGHT, WEDNIGHT, THURNIGHT, FRINIGHT) " +
                    "VALUES (" + worker.getID() + ", '" + worker.getPref()[0][0] + "', '" + worker.getPref()[1][0] + "', '" + worker.getPref()[2][0] + "', '" + worker.getPref()[3][0] + "', '" + worker.getPref()[4][0] + "', '" + worker.getPref()[5][0] + "', '" + worker.getPref()[0][1] + "', '" + worker.getPref()[1][1] + "', '" + worker.getPref()[2][1] + "', '" + worker.getPref()[3][1] + "', '" + worker.getPref()[4][1] + "', '" + worker.getPref()[5][1] + "')";
                    stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void deleteWorker(int id) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "DELETE FROM Workers WHERE ID = " + id;
            stmt.executeUpdate(sql);
            String sql2 = "DELETE FROM Prefs WHERE ID = " + id;
            stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static WorkerDTO getWorker(int id) throws SQLException{
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM Workers WHERE ID = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            WorkerDTO worker = new WorkerDTO();
            worker.setID(rs.getInt("ID"));
            worker.setName(rs.getString("Name"));
            worker.setBankAccount(rs.getInt("BankNumber"));
            worker.setGWage(rs.getInt("GWage"));
            worker.setHWage(rs.getInt("HWage"));
            worker.setStartDate(rs.getString("DateOfStart"));
            worker.setFTime(rs.getBoolean("FullTime"));
            worker.setTVDays(rs.getInt("TotalVacationDays"));
            worker.setCVDays(rs.getInt("CurrentVacationDays"));
            worker.setIsHeadOfBranch(rs.getBoolean("isBM"));
            worker.setChange(rs.getBoolean("Changed"));
            worker.setBranchName(rs.getString("BranchName"));
            String[] licenses = rs.getString("Licenses").split(", ");
            List<String> licensesList = new LinkedList<>();
            for (String license : licenses) {
                licensesList.add(license);
            }
            worker.setLicenses(licensesList);
            String sql2 = "SELECT * " +
                    "FROM Prefs " +
                    "WHERE ID = " + id;
            ResultSet rs2 = stmt.executeQuery(sql2);
            String[][] pref = new String[6][2];
            pref[0][0] = rs2.getString("SUNDAY");
            pref[1][0] = rs2.getString("MONDAY");
            pref[2][0] = rs2.getString("TUEDAY");
            pref[3][0] = rs2.getString("WEDDAY");
            pref[4][0] = rs2.getString("THURDAY");
            pref[5][0] = rs2.getString("FRIDAY");
            pref[0][1] = rs2.getString("SUNNIGHT");
            pref[1][1] = rs2.getString("MONNIGHT");
            pref[2][1] = rs2.getString("TUENIGHT");
            pref[3][1] = rs2.getString("WEDNIGHT");
            pref[4][1] = rs2.getString("THURNIGHT");
            pref[5][1] = rs2.getString("FRINIGHT");
            worker.setPref(pref);
            return worker;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateWorkerBankNumber(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Workers SET BankNumber = " + worker.getBankAccount() +  " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerGWage(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Workers SET GWage = " + worker.getGWage() + " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerHWage(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Workers SET HWage = " + worker.getHWage() + " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerFullTime(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Workers SET FullTime = " + worker.getFTime() + " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerTotalVacationDays(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Workers SET TotalVacationDays = " + worker.getTVDays() + " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerCurrentVacationDays(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Workers SET CurrentVacationDays = " + worker.getCVDays() + " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerChanged(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Workers SET Changed = " + worker.getChange() + " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerBranchName(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Workers SET BranchName = " + worker.getBranchName() + " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerPrefs(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Prefs SET SUNDAY = '" + worker.getPref()[0][0] + "', MONDAY = '" + worker.getPref()[1][0] + "', TUEDAY = '" + worker.getPref()[2][0] + "', WEDDAY = '" + worker.getPref()[3][0] + "', THURDAY = '" + worker.getPref()[4][0] + "', FRIDAY = '" + worker.getPref()[5][0] + "', SUNNIGHT = '" + worker.getPref()[0][1] + "', MONNIGHT = '" + worker.getPref()[1][1] + "', TUENIGHT = '" + worker.getPref()[2][1] + "', WEDNIGHT = '" + worker.getPref()[3][1] + "', THURNIGHT = '" + worker.getPref()[4][1] + "', FRINIGHT = '" + worker.getPref()[5][1] + "' WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateWorkerLicenses(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String licenses = "";
            for (String license : worker.getLicenses()) {
                licenses += license + ", ";
            }
            if(licenses.length() > 0)
                licenses = licenses.substring(0, licenses.length() - 2);
            else
                licenses = "None";
            String sql = "UPDATE Workers SET Licenses = '" + licenses + "' WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String[][] getPrefs(int id) {
        String[][] prefs = new String[6][2];
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "SELECT * FROM Prefs WHERE ID = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            prefs[0][0] = rs.getString("SUNDAY");
            prefs[1][0] = rs.getString("MONDAY");
            prefs[2][0] = rs.getString("TUEDAY");
            prefs[3][0] = rs.getString("WEDDAY");
            prefs[4][0] = rs.getString("THURDAY");
            prefs[5][0] = rs.getString("FRIDAY");
            prefs[0][1] = rs.getString("SUNNIGHT");
            prefs[1][1] = rs.getString("MONNIGHT");
            prefs[2][1] = rs.getString("TUENIGHT");
            prefs[3][1] = rs.getString("WEDNIGHT");
            prefs[4][1] = rs.getString("THURNIGHT");
            prefs[5][1] = rs.getString("FRINIGHT");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prefs;
    }

}
