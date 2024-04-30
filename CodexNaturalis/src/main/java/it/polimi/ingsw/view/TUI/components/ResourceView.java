package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.enums.Resource;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;

public class ResourceView {
    public static final int width = 73;
    private Map<Resource, Integer> resourceCount;
    private final String username;

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

    public void setResourceCount(Map<Resource, Integer> resourceCount) {
        this.resourceCount =new HashMap<>(resourceCount);
    }

    public void setResource(Resource resource, Integer count) {
        this.resourceCount.put(resource, count);
    }

    public String[] toStringArray() {
        return toString().split("\n");
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
                    .append("੦".repeat(min(65, resourceCount.get(res))))
                    .append(" ".repeat(min(1, resourceCount.get(res))))
                    .append(resourceCount.get(res))
                    .append("\n");
        }
        return out.toString();
    }
}
