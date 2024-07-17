package src.test;

import org.junit.jupiter.api.*;
import src.workers.DomainLayer.Facade;
import src.workers.PresentationLayer.ActionManager;
import src.workers.PresentationLayer.Main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class IntegrationTest {
    @Test
    public void testIntegration() {
        ActionManager actionManager = new ActionManager(new Facade());
        String in = "1\n1\n1\n2\n1\n3\n24";
        InputStream inStream = new ByteArrayInputStream(in.getBytes());
        System.setIn(inStream);
        Assertions.assertTrue(actionManager.storage());
    }
}
