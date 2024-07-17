package src.workers.DomainLayer;

import src.workers.DataAcsessLayer.WorkerDAO;

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

    public static boolean inBranch(Worker worker, int id) {
        return worker.getBranch()  == id;
    }

    public static void checkUpdateDay(Worker worker) {
        worker.backupConstraints();
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

    public static void removeWorker(int id) {
        WorkerDAO.deleteWorker(id);
    }

    public static void removeWorker(int id, boolean b) {
        if (b)
            WorkerDAO.rdeleteWorker(id);
        else
            WorkerDAO.deleteWorker(id);

    }

    public static void removeLicense(Worker worker, License license) {
        worker.removeLicense(license);
    }
}
