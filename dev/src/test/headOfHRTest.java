import org.junit.jupiter.api.*;

import workers.DomainLayer.*;

public class headOfHRTest {
    private Connector connector;
    private HeadOfHR hr;
    private Worker worker1;
    private Worker worker2;
    private Worker worker3;
    private Worker worker4;
    @BeforeEach
    void setUp() {
        connector = new Connector("1234");
        hr = new HeadOfHR();
        worker1 = new Worker("worker1",1,1,10000,0,"11/11/11",true,10,10);
        worker2 = new Worker("worker2",2,2,100000,0,"11/11/11",true,10,10);
        worker3 = new Worker("worker3",3,3,1000000,0,"11/11/11",true,10,10);
        worker4 = new Worker("worker4",4,4,0,10,"11/11/11",false,10,10);
        hr.addWorker(worker1);
        hr.addWorker(worker2);
        hr.addWorker(worker3);
        hr.addWorker(worker4);
        hr.addRole(1,"Shift-Manager");
        hr.addRole(2,"c");
        hr.addRole(3,"b");
        hr.addRole(4,"a");

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
        connector.setHourlyWage(4,1000);
        Assertions.assertEquals(worker1.getHWage(),1000);
        connector.setHourlyWage(1,100);
        Assertions.assertEquals(worker1.getHWage(),100);
    }

    @Test
    void setFullTimeJob() {
        connector.setFullTimeJob(4,true);
        Assertions.assertTrue(worker4.getFullTimeJob());
        connector.setFullTimeJob(1,false);
        Assertions.assertTrue(worker1.getFullTimeJob());
    }

    @Test
    void setVacationDays() {
        connector.setVacationDays(4,5);
        Assertions.assertEquals(worker4.getTotalVacationDays(),5);
        connector.setVacationDays(1,5);
        Assertions.assertEquals(worker1.getTotalVacationDays(),5);
    }

    @Test
    void resetVacationDays() {
        connector.setVacationDays(4,5);
        connector.ResetVacationDays(4);
        Assertions.assertEquals(worker4.getCurrVacationDays(),10);
        connector.setVacationDays(1,5);
        connector.ResetVacationDays(1);
        Assertions.assertEquals(worker1.getCurrVacationDays(),10);
    }

    @Test
    void setWorkerGlobal() {
        connector.setGlobalWage(4,1000);
        Assertions.assertEquals(worker4.getGWage(),1000);
        connector.setGlobalWage(1,100);
        Assertions.assertEquals(worker1.getGWage(),100);
    }

    @Test
    void addWorkerToShift() {
        connector.createShift(1,"12/12/21",true,1);
        Assertions.assertTrue(connector.showShift().contains(worker1.getName()));
        connector.addWorkerToShift(1,"b");
        Assertions.assertTrue(connector.showShift().contains(worker2.getName()));
        connector.addWorkerToShift(1,"c");
        Assertions.assertTrue(connector.showShift().contains(worker3.getName()));
        connector.addWorkerToShift(1,"a");
        Assertions.assertTrue(connector.showShift().contains(worker4.getName()));
    }

    @Test
    void setHalfDayShiftOff() {
        connector.setHalfDayShiftOff("01.01.2021",true,1);
        connector.selectShift("01.01.2021",true);
        Assertions.assertTrue(connector.isInactive());
        Assertions.assertFalse(worker1.getCons(1,true).equals(Constraints.inactive));
        Assertions.assertFalse(worker2.getCons(1,true).equals(Constraints.inactive));
        Assertions.assertFalse(worker3.getCons(1,true).equals(Constraints.inactive));
        Assertions.assertFalse(worker4.getCons(1,true).equals(Constraints.inactive));
    }

    @Test
    void addWorker() {
        connector.addWorker("worker5",5,5,true,0,0,"11/11/11",10,10,"a");
        Assertions.assertTrue(connector.login(5,"a"));
    }

    @Test
    void addRole() {
        connector.addRole(1,"d");
        Assertions.assertTrue(connector.getAvailableWorkersOfRole("d").contains("worker1"));
    }

    @Test
    void setAllDayShiftOff() {
        connector.setAllDayOff("01.01.2021",1);
        connector.selectShift("01.01.2021",true);
        Assertions.assertTrue(connector.isInactive());
        connector.selectShift("01.01.2021",false);
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
        connector.addWorkerToShift(1,"b");
        connector.removeWorkerFromShift(1,"worker2");
        Assertions.assertFalse(connector.showShift().contains(worker2.getName()));
    }
}
