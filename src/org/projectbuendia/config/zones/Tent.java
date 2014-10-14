package org.projectbuendia.config.zones;

import java.util.HashMap;

/**
 * Created by wwadewitte on 10/14/14.
 */
public final class Tent {
    private HashMap<Integer, Portal> portals = new HashMap<Integer, Portal>();
    private final String name;

    public final String getName() {
        return name;
    }

    public Tent (String name) {
        this.name = name;
    }
}
