package io.tackle.controls.testcontainers.postresql;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.Map;

public class PostgreSQLResource implements QuarkusTestResourceLifecycleManager {

    private final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:10.6")
            .withDatabaseName("controls_db")
            .withUsername("controls")
            .withPassword("controls")
/*            .withClasspathResourceMapping("db/migration/V20200928__create_business-service.sql",
                    "/docker-entrypoint-initdb.d/init.sql",
                    BindMode.READ_ONLY)*/
//            .withInitScript("import-test.sql")
            ;

    @Override
    public Map<String, String> start() {
        postgreSQLContainer.start();
        return Collections.singletonMap("quarkus.datasource.jdbc.url", postgreSQLContainer.getJdbcUrl());
    }

    @Override
    public void stop() {
        postgreSQLContainer.close();
    }

/*    @Override
    public int order() {
        return 10;
    }*/

}
