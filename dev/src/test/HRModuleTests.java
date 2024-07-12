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

    @Test
    public void testremoveLicense() {
        Worker worker = HeadOfBranch.getWorker(1);
        License license = License.D;
        WorkerManager.addLicense(worker, license);
        WorkerManager.removeLicense(worker, license);
        Assertions.assertFalse(worker.getLicenses().contains(license));
    }
    @Test
    public void testSetHourlyWage() {
        Worker worker = HeadOfBranch.getWorker(1);
        int wage = 50;
        worker.setHWage(wage);
        Assertions.assertEquals(worker.getHWage(), wage);
    }


    @Test
    public void testSetConstraints() {
        Worker worker = HeadOfBranch.getWorker(1);
        Constraints oldConstraint = worker.getCons(1, true);
        worker.addConstraints(1,true,Constraints.want);
        Assertions.assertEquals(worker.getCons(1,true), Constraints.want);
        worker.addConstraints(1,true,oldConstraint);
    }

    @Test
    public void setFullTimeJob() {
        Worker worker = HeadOfBranch.getWorker(1);
        worker.setFullTimeJob(true);
        Assertions.assertTrue(worker.getFullTimeJob());
    }
    @Test
    public void testSetVacationDays() {
        Worker worker = HeadOfBranch.getWorker(1);
        int days = 10;
        worker.setVacationDays(days);
        Assertions.assertEquals(worker.getCurrVacationDays(), days);
    }
    @Test
    public void testSetBankNum() {
        Worker worker = HeadOfBranch.getWorker(1);
        int bankNum = 123456;
        worker.setBankNum(bankNum);
        Assertions.assertEquals(worker.getBankNum(), bankNum);
    }

}
