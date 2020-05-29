package io.github.selcukes.grid;

import java.util.concurrent.TimeUnit;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.grid.internal.utils.configuration.GridNodeConfiguration;
import org.openqa.selenium.remote.server.SeleniumServer;

public class LocalNode implements Bootable {

    private SelfRegisteringRemote node;
    private LocalHub hub;

    public LocalNode(LocalHub hub, GridNodeConfiguration cfg) {
        this.hub = hub;
        node = new SelfRegisteringRemote(cfg);
        SeleniumServer server = new SeleniumServer(node.getConfiguration());
        node.setRemoteServer(server);
    }

    @Override
    public void start() {
        if (node.startRemoteServer()) {
            node.sendRegistrationRequest();
        }
        int attempt = 1;
        boolean registered = false;
        while (attempt++ <= 10) {
            if (hub.isNodeRegistered()) {
                registered = true;
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (!registered) {
            throw new IllegalStateException("Node registration failed");
        }
    }

    @Override
    public void stop() {
        node.stopRemoteServer();
    }
}
