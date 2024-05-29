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



    }


    @Test
    void createShiftTest() {
        Assertions.assertTrue(connector.createShift(1,"12/12/21",true,1),"Shift added successfully");
        Assertions.assertFalse(connector.createShift(1,"12/12/21",true,1),"Shift already exist");
    }
    void selectShiftTest() {
        connector.createShift(1,"12/12/21",true,1);
        Assertions.assertTrue(connector.selectShift("12/12/21",true),"Does exist");
        Assertions.assertFalse(connector.selectShift("11/12/21",true),"Does not exist");
    }
    void setHourlyWage() {
        connector.setHourlyWage(4,1000);
        Assertions.assertEquals(worker1.getHWage(),1000);
        connector.setHourlyWage(1,100);
        Assertions.assertEquals(worker1.getHWage(),100);
    }

}
