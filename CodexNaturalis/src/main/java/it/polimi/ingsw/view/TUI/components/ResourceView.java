package it.polimi.ingsw.view.TUI.components;

import it.polimi.ingsw.model.enums.Resource;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.min;

public class ResourceView {
    Map<Resource, Integer> resourceCount;

    public ResourceView() {
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
        out.append(" \n");
        return out.toString();
    }
}
