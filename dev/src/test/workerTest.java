import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import workers.DomainLayer.*;

public class workerTest {
    @BeforeAll
    void init() {
        Connector connector = new Connector("1234");
        HeadOfHR hr = new HeadOfHR();
        Worker worker1 = new Worker("worker1",1,1,10000,0,"11/11/11",true,10,10);
        Worker worker2 = new Worker("worker2",2,2,100000,0,"11/11/11",true,10,10);
        Worker worker3 = new Worker("worker3",3,3,1000000,0,"11/11/11",true,10,10);
        Worker worker4 = new Worker("worker4",4,4,0,10,"11/11/11",false,10,10);
        hr.addWorker(worker1);
        hr.addWorker(worker2);
        hr.addWorker(worker3);
        hr.addWorker(worker4);
    }
    @Test
    void shouldWork() {
        Assertions.assertEquals("1","1");
    }
}
