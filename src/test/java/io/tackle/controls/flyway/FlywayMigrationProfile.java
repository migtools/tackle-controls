package io.tackle.controls.flyway;

import io.quarkus.test.junit.QuarkusTestProfile;
import io.tackle.commons.testcontainers.PostgreSQLDatabaseTestResource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FlywayMigrationProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Collections.singletonMap("quarkus.oidc.enabled","false");
    }

    @Override
    public Set<Class<?>> getEnabledAlternatives() {
        return Collections.emptySet();
    }

    @Override
    public String getConfigProfile() {
        return "flyway";
    }

    @Override
    public List<TestResourceEntry> testResources() {
        Map<String, String> args = new HashMap<>(3);
        args.put(PostgreSQLDatabaseTestResource.DB_NAME,"controls_db");
        args.put(PostgreSQLDatabaseTestResource.USER,"controls");
        args.put(PostgreSQLDatabaseTestResource.PASSWORD,"controls");
        return Collections.singletonList(new TestResourceEntry(PostgreSQLDatabaseTestResource.class, args));
    }

    @Override
    public Set<String> tags() {
        return Collections.singleton("flyway");
    }
    
    @Override
    public boolean disableGlobalTestResources() {
        return true;
    }
}
