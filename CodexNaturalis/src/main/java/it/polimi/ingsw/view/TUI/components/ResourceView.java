package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.enums.Resource;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;

public class ResourceView implements Component {
    public static final int width = 73;
    private Map<Resource, Integer> resourceCount;
    private String username;

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setResourceCount(Map<Resource, Integer> resourceCount) {
        this.resourceCount =new HashMap<>(resourceCount);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        out.append(" %s:\tresources\n".formatted(username));

        for (Resource res : Resource.values()) {
            if (res == Resource.NONE || res == Resource.NONCOVERABLE ) {
                continue;
            }

            out.append(" ")
                    .append(res.toChar())
                    .append(" ")
                    .append(String.format("%2d ",resourceCount.get(res)))
                    .append("੦".repeat(min(65, resourceCount.get(res))))
                    .append(" ".repeat(min(1, resourceCount.get(res))))
                    .append("\n");
        }
        return out.toString();
    }
}
