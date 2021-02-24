package io.tackle.controls.testcontainers.keycloak;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.SelinuxContext;
import org.testcontainers.containers.wait.strategy.Wait;

import java.util.Collections;
import java.util.Map;

public class KeycloakResource implements QuarkusTestResourceLifecycleManager {

    private static final String KEYCLOAK_IMAGE = "jboss/keycloak:12.0.2";
    public static final String TACKLE_KEYCLOAK_TEST_URL = "TACKLE_KEYCLOAK_TEST_URL";

    public static GenericContainer<?> keycloak =
            new GenericContainer<>(KEYCLOAK_IMAGE)
                    .withExposedPorts(8080, 8443)
                    //this check was not reliable
//                    .waitingFor(Wait.forHttp("/auth/realms/master"))
                    .waitingFor(Wait.forLogMessage(".* started in .*", 1))
                    .withEnv("KEYCLOAK_USER", "admin")
                    .withEnv("KEYCLOAK_PASSWORD", "admin")
                    .withEnv("KEYCLOAK_IMPORT", "/tmp/quarkus-realm.json")
                    .withClasspathResourceMapping("keycloak/quarkus-realm.json", "/tmp/quarkus-realm.json", BindMode.READ_WRITE, SelinuxContext.SINGLE)
                    .withEnv("DB_VENDOR", "h2");

    @Override
    public Map<String, String> start() {
        // used System.out due to lack of logger in QuarkusTestResourceLifecycleManager as reported in
        // https://github.com/quarkusio/quarkus/blob/6cdd2078f1e99eddc4e739f28c7d7808ce8af12b/test-framework/common/src/main/java/io/quarkus/test/common/QuarkusTestResourceLifecycleManager.java#L20-L21
        final String keycloakExternalUrl = System.getenv(TACKLE_KEYCLOAK_TEST_URL);
        if (keycloakExternalUrl != null) {
            System.out.printf("Keycloak externally provided with %s=%s\n", TACKLE_KEYCLOAK_TEST_URL, keycloakExternalUrl);
            return Collections.singletonMap("quarkus.oidc.auth-server-url", keycloakExternalUrl);
        }
        System.out.printf("%s starting...\n", KEYCLOAK_IMAGE);
        keycloak.start();
        System.out.printf("%s started\n", KEYCLOAK_IMAGE);
        return Collections.singletonMap("quarkus.oidc.auth-server-url", "https://localhost:" + keycloak.getMappedPort(8443) + "/auth/realms/quarkus");
    }

    @Override
    public void stop() {
        keycloak.stop();
    }
}
