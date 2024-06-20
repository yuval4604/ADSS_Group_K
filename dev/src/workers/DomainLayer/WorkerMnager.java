package workers.DomainLayer;

public class WorkerMnager {
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
        worker.createNewConstraints();
    }

}
