package workers.DomainLayer;

public class WorkerManager {
    public static boolean useVacationDays(Worker worker, int days) {
        if (days < worker.getCurrVacationDays()) {
            worker.setCurrvacationDays(worker.getCurrVacationDays() - days);
            return true;
        }
        return false;
    }

    public static void ResetVacationDays(Worker worker) {
        worker.setCurrvacationDays(worker.getTotalVacationDays());
    }

    public static boolean inBranch(Worker worker, String name) {
        return worker.getBranch().equals(name);
    }

    public static void checkUpdateDay(Worker worker) {
        worker.backupConstraints();
        //worker.createNewConstraints();
    }

    public static String getWorkerLicense(Worker worker) {
        String licenses = "";
        for (License license : worker.getLicenses()) {
            licenses += license.toString() + ", ";
        }
        if(licenses.length() > 0)
            licenses = licenses.substring(0, licenses.length() - 2);
        else
            licenses = "None";
        return licenses;
    }

    public static boolean addLicense(Worker worker, License license) {
        return worker.addLicense(license);
    }

}
