import org.junit.jupiter.api.*;

import workers.DomainLayer.*;

public class headOfHRTest {
    private Connector connector;
    @BeforeEach
    void setUp() {
        connector = new Connector("1234");
        connector.addWorker("worker1",1,1,true,10000,0,"11/11/11",10,10);
        connector.addWorker("worker2",2,2,false,10000,0,"11/11/11",10,10);
        connector.addWorker("worker3",3,3,false,10000,0,"11/11/11",10,10);
        connector.addWorker("worker4",4,4,true,10000,0,"11/11/11",10,10);

        connector.addRole(1,"Shift-Manager");
        connector.addRole(2,"c");
        connector.addRole(3,"b");
        connector.addRole(4,"a");
        connector.login(-1,"1234");

        connector.createShift(1,"01.01.2021",true,1);

    }


    @Test
    void createShiftTest() {
        Assertions.assertTrue(connector.createShift(1,"12/12/21",true,1),"Shift added successfully");
        Assertions.assertFalse(connector.createShift(1,"12/12/21",true,1),"Shift already exist");
    }
    @Test
    void selectShiftTest() {
        connector.createShift(1,"12/12/21",true,1);
        Assertions.assertTrue(connector.selectShift("12/12/21",true),"Does exist");
        Assertions.assertFalse(connector.selectShift("11/12/21",true),"Does not exist");
    }
    @Test
    void setHourlyWage() {
        connector.setHourlyWage(2,1000);
        connector.setHourlyWage(3,100);
        connector.login(2,"b");
        Assertions.assertEquals(connector.showWorkerInfo().contains("Hourly wage:1000"),true);
        connector.login(3,"c");
        Assertions.assertEquals(connector.showWorkerInfo().contains("Hourly wage:100"),true);
    }

    @Test
    void setFullTimeJob() {
        connector.setFullTimeJob(4,true);
        connector.setFullTimeJob(1,false);
        Assertions.assertTrue(connector.isFullTime(4));
        Assertions.assertTrue(connector.isFullTime(1));
    }

    @Test
    void setVacationDays() {
        connector.setVacationDays(4,5);
        connector.setVacationDays(1,5);
        connector.login(4,"a");
        Assertions.assertEquals(connector.showWorkerInfo().contains("Total vacation days:5"),true);
        connector.login(1,"a");
        Assertions.assertEquals(connector.showWorkerInfo().contains("Total vacation days:5"),true);
    }

    @Test
    void resetVacationDays() {
        connector.setVacationDays(4,10);
        connector.ResetVacationDays(4);
        connector.setVacationDays(1,10);
        connector.ResetVacationDays(1);
        connector.login(4,"a");
        Assertions.assertEquals(connector.showWorkerInfo().contains("Total vacation days:10"),true);
        connector.login(1,"a");
        Assertions.assertEquals(connector.showWorkerInfo().contains("Total vacation days:10"),true);
    }

    @Test
    void setWorkerGlobal() {

        connector.setGlobalWage(4,1000);
        connector.setGlobalWage(1,100);
        connector.login(4,"a");
        Assertions.assertEquals(connector.showWorkerInfo().contains("Global wage:1000"),true);
        Assertions.assertEquals(connector.showWorkerInfo().contains("Global wage:1000"),true);
    }

    @Test
    void addWorkerToShift() {
        connector.createShift(1,"12/12/21",true,1);
        Assertions.assertTrue(connector.showShift().contains("worker1"));
        connector.addWorkerToShift(2,"c");
        String shift = connector.showShift();
        Assertions.assertTrue(shift.contains("worker2"));
        connector.addWorkerToShift(3,"b");
        Assertions.assertTrue(connector.showShift().contains("worker3"));
        connector.addWorkerToShift(4,"a");
        Assertions.assertTrue(connector.showShift().contains("worker4"));
    }

    @Test
    void setHalfDayShiftOff() {
        connector.setHalfDayShiftOff("02.01.2021",true,1);
        connector.selectShift("02.01.2021",true);
        Assertions.assertTrue(connector.isInactive());
        connector.login(1,"a");
        Assertions.assertTrue(connector.getCons(1,true).equals(Constraints.inactive));
        connector.login(2,"b");
        Assertions.assertTrue(connector.getCons(1,true).equals(Constraints.inactive));
        connector.login(3,"c");
        Assertions.assertTrue(connector.getCons(1,true).equals(Constraints.inactive));
        connector.login(4,"a");
        Assertions.assertTrue(connector.getCons(1,true).equals(Constraints.inactive));

    }

    @Test
    void addWorker() {
        connector.addWorker("worker5",5,5,true,0,0,"11/11/11",10,10);
        Assertions.assertTrue(connector.login(5,"a"));
    }

    @Test
    void addRole() {
        connector.addRole(1,"d");
        connector.selectShift("01.01.2021",true);
        Assertions.assertTrue(connector.getAvailableWorkersOfRole("d").contains("worker1"));
    }

    @Test
    void setAllDayShiftOff() {
        connector.setAllDayOff("03.01.2021",1);
        connector.selectShift("03.01.2021",true);
        Assertions.assertTrue(connector.isInactive());
        connector.selectShift("03.01.2021",false);
        Assertions.assertTrue(connector.isInactive());
    }

    @Test
    void removeRole() {
        connector.addRole(1,"d");
        connector.removeRole(1,"d");
        Assertions.assertFalse(connector.getAvailableWorkersOfRole("d").contains("worker1"));
    }

    @Test
    void removeWorkerFromShift() {
        connector.createShift(1,"12/12/21",true,1);
        connector.addWorkerToShift(3,"b");
        Assertions.assertTrue(connector.showShift().contains("worker3"));
        connector.removeWorkerFromShift(3,"b");
        Assertions.assertFalse(connector.showShift().contains("worker3"));
    }
}
