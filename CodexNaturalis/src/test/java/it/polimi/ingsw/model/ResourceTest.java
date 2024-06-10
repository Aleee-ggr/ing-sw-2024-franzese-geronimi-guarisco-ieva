package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enums.Resource;
import org.junit.Test;

import java.util.Set;

import static it.polimi.ingsw.model.enums.Resource.*;
import static org.junit.Assert.assertEquals;

public class ResourceTest {
    Set<Resource> centerResources = Set.of(FUNGI, PLANT, INSECT, ANIMAL);
    Set<Resource> cornerResources = Set.of(FUNGI, PLANT, INSECT, ANIMAL, INKWELL, MANUSCRIPT, QUILL, NONCOVERABLE);

    @Test
    public void colorTest() {
        for (Resource resource : Resource.values()) {
            if (centerResources.contains(resource)) {
                assertEquals(resource.toColorChar('◆'), resource.toColorCenter());
            } else {
                assertEquals(" ", resource.toColorCenter());
            }

            if (cornerResources.contains(resource)) {
                assertEquals(resource.toColorChar('█'), resource.toColorBlock());
            } else {
                assertEquals(" ", resource.toColorBlock());
            }
        }

    }
}
