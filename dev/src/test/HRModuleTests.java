package src.test;

import org.junit.jupiter.api.*;

import src.workers.DataAcsessLayer.*;
import src.workers.DomainLayer.*;

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
    public void testSetConstraints() {
        Worker worker = HeadOfBranch.getWorker(1);
        Constraints oldConstraint = worker.getCons(1, true);
        worker.addConstraints(1,true,Constraints.want);
        Assertions.assertEquals(worker.getCons(1,true), Constraints.want);
        worker.addConstraints(1,true,oldConstraint);
    }

    @Test
    public void testChangeGlobalWage() {
        Worker worker = HeadOfBranch.getWorker(1);
        int oldWage = worker.getGWage();
        worker.setWage(100);
        Assertions.assertEquals(worker.getGWage(), 100);
        worker.setWage(oldWage);
    }

    @Test
    public void testRemoveLicense() {
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

    @Test
    public void testCreateBranch() {
        Branch branch = new Branch("test", 10000, "", null);
        try {
            Assertions.assertEquals(BranchDAO.getBranch(10000).getName(), "test");
        } catch (Exception e) {
            Assertions.fail();
        }
        BranchDAO.deleteBranch(10000);
    }

    @Test
    public void testDeleteBranch() {
        Branch branch = new Branch("test", 10000, "", null);
        try {
            Assertions.assertEquals(BranchDAO.getBranch(10000).getName(), "test");
        } catch (Exception e) {
            Assertions.fail();
        }
        BranchDAO.deleteBranch(10000);
        try {
            Assertions.assertNull(BranchDAO.getBranch(10000));
        } catch (Exception e) {
        }
    }

    @Test
    public void testAddWorkerToBranch() {
        Branch branch = new Branch("test", 10000, "", null);
        Worker worker = new Worker("test", 10000, 1, 1,1,"",true,1,1,false);
        branch.addWorker(worker);
        Assertions.assertTrue(branch.getWorkers().contains(worker));
        BranchDAO.deleteBranchWorker(10000, 10000);
        BranchDAO.deleteBranch(10000);
        WorkerManager.removeWorker(10000, true);
    }

    @Test
    public void testRemoveWorkerFromBranch() {
        Branch branch = new Branch("test", 10000, "", null);
        Worker worker = new Worker("test", 10000, 1, 1,1,"",true,1,1,false);
        branch.addWorker(worker);
        Assertions.assertTrue(branch.getWorkers().contains(worker));
        branch.removeWorker(worker);
        Assertions.assertFalse(branch.getWorkers().contains(worker));
        BranchDAO.deleteBranch(10000);
        WorkerManager.removeWorker(10000, true);
    }

    @Test
    public void testCreateShift() {
        Branch branch = new Branch("test", 10000, "", null);
        Shift shift = new Shift(null, "01.01.2000", true, 1, true, branch);
        try {
            Assertions.assertEquals(ShiftDAO.getShift("01.01.2000", true,10000).getDate(), "01.01.2000");
        } catch (Exception e) {
            Assertions.fail();
        }
        BranchDAO.deleteBranchShift(10000, "01.01.2000", true);
        BranchDAO.deleteBranch(10000);
        ShiftDAO.deleteShift("01.01.2000", true, 10000);
    }

    @Test
    public void testAddWorker() {
        Worker worker = new Worker("test", 10000, 1, 1,1,"",true,1,1,false);
        try {
            Assertions.assertEquals(WorkerDAO.getWorker(10000).getName(),"test");
        } catch (Exception e) {
            Assertions.fail();
        }
        WorkerManager.removeWorker(10000, true);
    }

    @Test
    public void testCreateHeadOfBranch() {
        HeadOfBranch worker = new HeadOfBranch("test", 10000, 1, 1,1,"",true,1,1,null);
        try {
            Assertions.assertTrue(HeadOfBranchDAO.getHeadOfBranch(10000) != null);
        } catch (Exception e) {
            Assertions.fail();
        }
        WorkerManager.removeWorker(10000, true);
        HeadOfBranchDAO.deleteHeadOfBranch(10000);
        HeadOfBranchDAO.deleteRoleList("Shift-Manager",10000);
    }

    @Test
    public void testRemoveWorker() {
        Worker worker = new Worker("test", 10000, 1, 1,1,"",true,1,1,false);
        try {
            Assertions.assertEquals(WorkerDAO.getWorker(10000).getName(),"test");
        } catch (Exception e) {
            Assertions.fail();
        }
        WorkerManager.removeWorker(10000, true);
        try {
            WorkerDAO.getWorker(10000);
            Assertions.fail();
        } catch (Exception e) {
        }
    }

    @Test
    public void testChangePassword() {
        String oldPassword = LoginDAO.getPassword(1);
        facade.login(1,oldPassword);
        facade.changePassword(oldPassword, "AlfredoBedo1234");
        Assertions.assertEquals(LoginDAO.getPassword(1), "AlfredoBedo1234");
        facade.changePassword("AlfredoBedo1234", oldPassword);
        facade.logOut();
    }

    @Test
    public void testFireWorker() {
        Worker worker = new Worker("test", 10000, 1, 1,1,"",true,1,1,false);
        try {
            Assertions.assertEquals(WorkerDAO.getWorker(10000).getName(),"test");
        } catch (Exception e) {
            Assertions.fail();
        }
        facade.login(0, LoginDAO.getPassword(0));
        facade.fireWorker(10000);
        try {
            Assertions.assertNull(WorkerDAO.getWorker(10000));
        } catch (Exception e) {
        }
        WorkerDAO.rdeleteWorker(10000);
    }
}

