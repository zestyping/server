package org.projectbuendia.config.zones;

import java.util.HashMap;

/**
 * Created by wwadewitte on 10/14/14.
 */
public final class Zone {
    private HashMap<Integer, Tent> tents = new HashMap<Integer, Tent>();

    private final String name;

    public final String getName() {
        return name;
    }
    public Zone (String name) {
        this.name = name;
    }
}
