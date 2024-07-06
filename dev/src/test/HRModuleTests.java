import org.junit.jupiter.api.*;

import workers.DomainLayer.*;

import java.util.List;

public class HRModuleTests {
    private Facade facade;

    @BeforeEach
    public void setUp() {
        facade = new Facade();
    }

    @Test
    public void testResetVacationDays() {
        Worker worker = HeadOfBranch.getWorker(1);
        WorkerManager.useVacationDays(worker, 5);
        WorkerManager.ResetVacationDays(worker);
        Assertions.assertEquals(worker.getCurrVacationDays(), worker.getTotalVacationDays());
    }

    @Test
    public void testUseVacationDays() {
        Worker worker = HeadOfBranch.getWorker(1);
        WorkerManager.useVacationDays(worker, 5);
        Assertions.assertEquals(worker.getCurrVacationDays(), worker.getTotalVacationDays() - 5);
        WorkerManager.ResetVacationDays(worker);
    }

    @Test
    public void testInBranch() {
        Worker worker = HeadOfBranch.getWorker(1);
        Assertions.assertTrue(WorkerManager.inBranch(worker, 1));
    }

    @Test
    public void testaddLicense() {
        Worker worker = HeadOfBranch.getWorker(1);
        License license = License.D;
        WorkerManager.addLicense(worker, license);
        Assertions.assertTrue(worker.getLicenses().contains(license));
        WorkerManager.removeLicense(worker, license);
    }
}
