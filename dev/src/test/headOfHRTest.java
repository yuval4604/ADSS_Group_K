import org.junit.jupiter.api.*;

import workers.DomainLayer.*;

public class headOfHRTest {
    private Facade facade;
    @BeforeEach
    void setUp() {
        facade = new Facade("1234");
        facade.createBM("a",0,1,true,10000,0,"11/11/11",10,10);
        facade.addWorker("worker1",1,1,true,10000,0,"11/11/11",10,10);
        facade.addWorker("worker2",2,2,false,10000,0,"11/11/11",10,10);
        facade.addWorker("worker3",3,3,false,10000,0,"11/11/11",10,10);
        facade.addWorker("worker4",4,4,true,10000,0,"11/11/11",10,10);
        facade.addBranch("branch1",1,"a",0);

        facade.addRole(1,"Shift-Manager");
        facade.addRole(2,"c");
        facade.addRole(3,"b");
        facade.addRole(4,"a");
        facade.login(0,"a");
        facade.addWorkerToBranch(1);
        facade.addWorkerToBranch(2);
        facade.addWorkerToBranch(3);
        facade.addWorkerToBranch(4);

        facade.createShift(1,"01.01.2021",true,1);

    }


    @Test
    void createShiftTest() {
        Assertions.assertTrue(facade.createShift(1,"12.12.21",true,1),"Shift added successfully");
        Assertions.assertFalse(facade.createShift(1,"12.12.21",true,1),"Shift already exist");
    }
    @Test
    void selectShiftTest() {
        facade.createShift(1,"12.12.21",true,1);
        Assertions.assertTrue(facade.selectShift("12.12.21",true),"Does exist");
        Assertions.assertFalse(facade.selectShift("11.12.21",true),"Does not exist");
    }
    @Test
    void setHourlyWage() {
        facade.setHourlyWage(2,1000);
        facade.setHourlyWage(3,100);
        facade.login(2,"worker2");
        Assertions.assertEquals(facade.showWorkerInfo().contains("Hourly wage:1000"),true);
        facade.login(3,"worker3");
        Assertions.assertEquals(facade.showWorkerInfo().contains("Hourly wage:100"),true);
    }

    @Test
    void setFullTimeJob() {
        facade.setFullTimeJob(4,true);
        facade.setFullTimeJob(1,false);
        Assertions.assertTrue(facade.isFullTime(4));
        Assertions.assertTrue(facade.isFullTime(1));
    }

    @Test
    void setVacationDays() {
        facade.logOut();
        facade.login(0,"a");
        facade.setVacationDays(4,5);
        facade.setVacationDays(1,5);
        facade.login(4,"worker4");
        String workerInfo = facade.showWorkerInfo();
        Assertions.assertTrue(workerInfo.contains("Total vacation days:5"));
        facade.login(1,"worker1");
        workerInfo = facade.showWorkerInfo();
        Assertions.assertTrue(workerInfo.contains("Total vacation days:5"));
    }

    @Test
    void resetVacationDays() {
        facade.setVacationDays(4,10);
        facade.ResetVacationDays(4);
        facade.setVacationDays(1,10);
        facade.ResetVacationDays(1);
        facade.login(4,"a");
        Assertions.assertEquals(facade.showWorkerInfo().contains("Total vacation days:10"),true);
        facade.login(1,"a");
        Assertions.assertEquals(facade.showWorkerInfo().contains("Total vacation days:10"),true);
    }

    @Test
    void setWorkerGlobal() {

        facade.setGlobalWage(4,1000);
        facade.setGlobalWage(1,100);
        facade.login(4,"a");
        Assertions.assertEquals(facade.showWorkerInfo().contains("Global wage:1000"),true);
        Assertions.assertEquals(facade.showWorkerInfo().contains("Global wage:1000"),true);
    }

    @Test
    void addWorkerToShift() {
        facade.createShift(1,"12.12.21",true,1);
        Assertions.assertTrue(facade.showShift().contains("worker1"));
        facade.addWorkerToShift(2,"c");
        String shift = facade.showShift();
        Assertions.assertTrue(shift.contains("worker2"));
        facade.addWorkerToShift(3,"b");
        Assertions.assertTrue(facade.showShift().contains("worker3"));
        facade.addWorkerToShift(4,"a");
        Assertions.assertTrue(facade.showShift().contains("worker4"));
    }

    @Test
    void setHalfDayShiftOff() {
        facade.setHalfDayShiftOff("02.01.2021",true,1);
        facade.selectShift("02.01.2021",true);
        Assertions.assertTrue(facade.isInactive());
        facade.login(1,"a");
        Assertions.assertTrue(facade.getCons(1,true).equals(Constraints.inactive));
        facade.login(2,"b");
        Assertions.assertTrue(facade.getCons(1,true).equals(Constraints.inactive));
        facade.login(3,"c");
        Assertions.assertTrue(facade.getCons(1,true).equals(Constraints.inactive));
        facade.login(4,"a");
        Assertions.assertTrue(facade.getCons(1,true).equals(Constraints.inactive));

    }

    @Test
    void addWorker() {
        facade.logOut();
        facade.login(0,"1234");
        facade.addWorker("worker5",5,5,true,0,0,"11.11.11",10,10);
        Assertions.assertTrue(facade.login(5,"worker5"));
    }

    @Test
    void addRole() {
        facade.addRole(1,"d");
        facade.selectShift("01.01.2021",true);
        Assertions.assertTrue(facade.getAvailableWorkersOfRole("d").contains("worker1"));
    }

    @Test
    void setAllDayShiftOff() {
        facade.setAllDayOff("03.01.2021",1);
        facade.selectShift("03.01.2021",true);
        Assertions.assertTrue(facade.isInactive());
        facade.selectShift("03.01.2021",false);
        Assertions.assertTrue(facade.isInactive());
    }

    @Test
    void removeRole() {
        facade.addRole(1,"d");
        facade.removeRole(1,"d");
        Assertions.assertFalse(facade.getAvailableWorkersOfRole("d").contains("worker1"));
    }

    @Test
    void removeWorkerFromShift() {
        facade.createShift(1,"12.12.21",true,1);
        facade.addWorkerToShift(3,"b");
        Assertions.assertTrue(facade.showShift().contains("worker3"));
        facade.removeWorkerFromShift(3,"b");
        Assertions.assertFalse(facade.showShift().contains("worker3"));
    }
}
