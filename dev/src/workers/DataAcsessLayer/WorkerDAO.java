package workers.DataAcsessLayer;

import java.sql.*;

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
                    "VALUES (" + worker.getID() + ", " + worker.getPref().get("SUNDAY") + ", " + worker.getPref().get("MONDAY") + ", " + worker.getPref().get("TUEDAY") + ", " + worker.getPref().get("WEDDAY") + ", " + worker.getPref()[4][0] + ", " + worker.getPref()[5][0] + ", " + worker.getPref()[0][1] + ", " + worker.getPref()[1][1] + ", " + worker.getPref()[2][1] + ", " + worker.getPref()[3][1] + ", " + worker.getPref()[4][1] + ", " + worker.getPref()[5][1] + ")";
                    stmt.executeUpdate(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
