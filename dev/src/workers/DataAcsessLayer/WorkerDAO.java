package workers.DataAcsessLayer;

import java.sql.*;
import java.time.LocalDate;

public class WorkerDAO  {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/workerdb";

    public static void createWorkerTable() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {

            String sql = "CREATE TABLE Workers " +
                    "(ID INTEGER not NULL, " +
                    " Name VARCHAR(255), " +
                    " BankNumber INTEGER " +
                    " GWage INTEGER, " +
                    " HWage INTEGER, " +
                    " DateOfStart DATE, " +
                    " FullTime BIT(1), " +
                    " TotalVacationDays INTEGER, " +
                    " CurrentVacationDays INTEGER, " +
                    " isBM BIT(1), " +
                    " Changed BIT(1), " +
                    " CurrPref SET(14), " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
            String sql2 = "CREATE TABLE Prefs " +
                    "(ID INTEGER not NULL," +
                    "SUNDAY BIT(1)," +
                    "MONDAY BIT(1)," +
                    "TUEDAY BIT(1)," +
                    "WEDDAY BIT(1)," +
                    "THURDAY BIT(1)," +
                    "FRIDAY BIT(1)," +
                    "SUNNIGHT BIT(1)," +
                    "MONNIGHT BIT(1)," +
                    "TUENIGHT BIT(1)," +
                    "WEDNIGHT BIT(1)," +
                    "THURNIGHT BIT(1)," +
                    "FRINIGHT BIT(1)," +
                    "                     PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addWorker(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "INSERT INTO Workers (ID, Name, BankNumber, GWage, HWage, DateOfStart, FullTime, TotalVacationDays, CurrentVacationDays, isBM, Changed, CurrPref) " +
                    "VALUES (" + worker.getID() + ", '" + worker.getName() + "', " + worker.getBankAccount() + ", " + worker.getGWage() + ", " + worker.getHWage() + ", '" + worker.getStartDate() + "', " + worker.getFTime() + ", " + worker.getTVDays() + ", " + worker.getCVDays() + ", " + worker.getIsHeadOfBranch() + ", " + worker.getChange() + ", " + ")";
            stmt.executeUpdate(sql);
            String sql2 = "INSERT INTO Prefs (ID, SUNDAY, MONDAY, TUEDAY, WEDDAY, THURDAY, FRIDAY, SUNNIGHT, MONNIGHT, TUENIGHT, WEDNIGHT, THURNIGHT, FRINIGHT) " +
                    "VALUES (" + worker.getID() + ", " + worker.getPref()[0][0] + ", " + worker.getPref()[1][0] + ", " + worker.getPref()[2][0] + ", " + worker.getPref()[3][0] + ", " + worker.getPref()[4][0] + ", " + worker.getPref()[5][0] + ", " + worker.getPref()[0][1] + ", " + worker.getPref()[1][1] + ", " + worker.getPref()[2][1] + ", " + worker.getPref()[3][1] + ", " + worker.getPref()[4][1] + ", " + worker.getPref()[5][1] + ")";
                    stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static WorkerDTO getWorkerByID(int id) throws SQLException{
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
            worker.setStartDate(LocalDate.of(Integer.parseInt(rs.getString("DateOfStart").split("\\.")[2]), Integer.parseInt(rs.getString("DateOfStart").split("\\.")[1]), Integer.parseInt(rs.getString("DateOfStart").split("\\.")[0])));
            worker.setFTime(rs.getBoolean("FullTime"));
            worker.setTVDays(rs.getInt("TotalVacationDays"));
            worker.setCVDays(rs.getInt("CurrentVacationDays"));
            worker.setIsHeadOfBranch(rs.getBoolean("isBM"));
            worker.setChange(rs.getBoolean("Changed"));
            String sql2 = "SELECT * FROM Prefs WHERE ID = " + id;
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

    public static void updateWorker(WorkerDTO worker) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
        ) {
            String sql = "UPDATE Workers SET Name = '" + worker.getName() + "', BankNumber = " + worker.getBankAccount() + ", GWage = " + worker.getGWage() + ", HWage = " + worker.getHWage() + ", DateOfStart = '" + worker.getStartDate() + "', FullTime = " + worker.getFTime() + ", TotalVacationDays = " + worker.getTVDays() + ", CurrentVacationDays = " + worker.getCVDays() + ", isBM = " + worker.getIsHeadOfBranch() + ", Changed = " + worker.getChange() + ", CurrPref = " + " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql);
            String sql2 = "UPDATE Prefs SET SUNDAY = " + worker.getPref()[0][0] + ", MONDAY = " + worker.getPref()[1][0] + ", TUEDAY = " + worker.getPref()[2][0] + ", WEDDAY = " + worker.getPref()[3][0] + ", THURDAY = " + worker.getPref()[4][0] + ", FRIDAY = " + worker.getPref()[5][0] + ", SUNNIGHT = " + worker.getPref()[0][1] + ", MONNIGHT = " + worker.getPref()[1][1] + ", TUENIGHT = " + worker.getPref()[2][1] + ", WEDNIGHT = " + worker.getPref()[3][1] + ", THURNIGHT = " + worker.getPref()[4][1] + ", FRINIGHT = " + worker.getPref()[5][1] + " WHERE ID = " + worker.getID();
            stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
