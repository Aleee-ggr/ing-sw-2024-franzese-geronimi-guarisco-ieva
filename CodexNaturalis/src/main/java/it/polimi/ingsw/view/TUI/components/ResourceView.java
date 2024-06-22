package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.enums.Resource;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;

/**
 * Represents a view component displaying resource counts for a player in the TUI.
 */
public class ResourceView implements Component {
    public static final int width = 19;
    private Map<Resource, Integer> resourceCount;
    private String username;

    /**
     * Constructs a ResourceView object initialized with a username.
     *
     * @param username The username of the player whose resources are being displayed.
     */
    public ResourceView(String username) {
        this.username = username;
        resourceCount = new HashMap<>();
        for (Resource res : Resource.values()) {
            if (res == Resource.NONE || res == Resource.NONCOVERABLE) {
                continue;
            }
            resourceCount.put(res, 0);
        }
    }

    /**
     * Sets the username for the ResourceView object.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the resource counts displayed in the ResourceView.
     *
     * @param resourceCount A map containing resource counts for various resource types.
     */
    public void setResourceCount(Map<Resource, Integer> resourceCount) {
        this.resourceCount =new HashMap<>(resourceCount);
    }

    /**
     * Converts the ResourceView object to its string representation.
     * This representation includes the formatted layout of resource counts,
     * aligned with the specified width.
     *
     * @return The string representation of the ResourceView.
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        out.append(" %s resources".formatted(username.substring(0, Math.min(username.length(), 7))))
                .append(" ".repeat(width - Math.min(username.length(), 7) - 11))
                .append("\n");

        for (Resource res : Resource.values()) {
            if (res == Resource.NONE || res == Resource.NONCOVERABLE ) {
                continue;
            }

            out.append(" ")
                    .append(res.toChar())
                    .append(" ")
                    .append(String.format("%2d ",resourceCount.get(res)))
                    .append(" ".repeat(width-6))
                    .append("\n");
        }
        return out.toString();
    }
}
