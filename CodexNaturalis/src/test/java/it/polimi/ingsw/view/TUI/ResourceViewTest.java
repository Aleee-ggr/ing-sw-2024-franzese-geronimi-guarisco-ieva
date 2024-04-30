package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.enums.Resource;
import it.polimi.ingsw.view.TUI.components.ResourceView;
import org.junit.Test;

public class ResourceViewTest {
    @Test
    public void noResource() {
        ResourceView view = new ResourceView("username");
        System.out.println(view);
    }

    @Test
    public void F10P15() {
        ResourceView view = new ResourceView("username");
        view.setResource(Resource.FUNGI, 10);
        view.setResource(Resource.PLANT, 15);
        System.out.println(view);
    }

    @Test
    public void Overflow() {
        ResourceView view = new ResourceView("username");
        view.setResource(Resource.FUNGI, 64);
        view.setResource(Resource.PLANT, 999);
        System.out.println(view);
    }
}
