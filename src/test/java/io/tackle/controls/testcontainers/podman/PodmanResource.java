package io.tackle.controls.testcontainers.podman;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.io.IOException;
import java.util.Map;

/**
 * Dismissed in favor of using Maven profiles and the exec plugin
 */
public class PodmanResource implements QuarkusTestResourceLifecycleManager {

    private Process podmanService;

    @Override
    public Map<String, String> start() {
        ProcessBuilder processBuilder = new ProcessBuilder("podman", "system", "service", "-t", "0");
        try {
            podmanService = processBuilder.start();
            System.out.println("Podman started");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void stop() {
        if (null != podmanService) {
            podmanService.destroy();
            System.out.println("Podman stopped");
        }
    }
}
