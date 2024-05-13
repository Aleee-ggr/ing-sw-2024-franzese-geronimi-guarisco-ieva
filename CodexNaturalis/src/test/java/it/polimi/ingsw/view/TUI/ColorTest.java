package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.enums.Resource;
import org.junit.Test;

public class ColorTest {
    @Test
    public void testColor() {
        for (Resource resource : Resource.values()) {
            System.out.println(resource.toColorBlock());
        }
    }
}
