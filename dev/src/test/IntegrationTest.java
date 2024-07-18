package src.test;

import org.junit.jupiter.api.*;
import src.workers.DataAcsessLayer.ShiftDAO;
import src.workers.DomainLayer.Facade;
import src.workers.PresentationLayer.ActionManager;
import src.workers.PresentationLayer.Main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;

public class IntegrationTest {
    @Test
    public void testIntegration() {
        Facade facade = new Facade();
        facade.login(22, "Benjy123");
        String day = "" + LocalDate.now().getDayOfMonth();
        String month = "" + LocalDate.now().getMonthValue();
        String date = (day.length() == 1) ? ("0" + day) : day + ".";
        date += (month.length() == 1) ? ("0" + month) : month;
        date += "." + LocalDate.now().getYear();
        facade.createShift(22, date, true, LocalDate.now().getDayOfWeek().getValue());
        facade.createShift(22, date, false, LocalDate.now().getDayOfWeek().getValue());

        ActionManager actionManager = new ActionManager(facade);
        String in = "1\n1\n1\n2\n1\n3\n24";
        InputStream inStream = new ByteArrayInputStream(in.getBytes());
        System.setIn(inStream);
        Assertions.assertTrue(actionManager.storage());
        ShiftDAO.deleteShift(date, true, 0);
        ShiftDAO.deleteShift(date, false, 0);
    }
}
