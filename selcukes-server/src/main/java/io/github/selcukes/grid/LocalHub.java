package io.github.selcukes.grid;

import org.openqa.grid.internal.utils.configuration.GridHubConfiguration;
import org.openqa.grid.web.Hub;

public class LocalHub implements Bootable {

    private Hub hub;

    public LocalHub(GridHubConfiguration cfg) {
        hub = new Hub(cfg);
    }

    @Override
    public void start() {
        hub.start();
    }

    public boolean isNodeRegistered() {
        return !hub.getRegistry().getAllProxies().isEmpty();
    }

    @Override
    public void stop() {
        hub.stop();
    }
}
