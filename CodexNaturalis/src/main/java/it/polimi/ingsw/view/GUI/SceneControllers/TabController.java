package it.polimi.ingsw.view.GUI.SceneControllers;

/**
 * Interface for controllers managing tabs in GUI.
 * Provides a method to update the tab content.
 */
public interface TabController {
    /**
     * Updates the content of the tab.
     * Implementing classes should define how the tab content is updated.
     */
    void update();
}