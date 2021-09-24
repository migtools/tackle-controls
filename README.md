# Tackle Controls Application

This is the application that manages the entities related to the controls service for the Tackle Application Inventory application.  
This includes Business Services, Stakeholders, Stakeholder Groups, Tag Types and Tags.  

This project uses Quarkus, the Supersonic Subatomic Java Framework.  
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/.  

## Quarkus extensions used

### [REST Data Panache](https://quarkus.io/guides/rest-data-panache)

REST Data Panache (using the [active record pattern](https://quarkus.io/guides/hibernate-orm-panache#solution-1-using-the-active-record-pattern)) provides:

1. sorting
1. pagination

### [Flyway](https://flywaydb.org)

[Quarkus Flyway extension](https://quarkus.io/guides/flyway) provides:
1. DB production management: SQL scripts must be placed in [`src/main/resources/db/migration/`](src/main/resources/db/migration) folder, following [Quarkus Flyway default location](https://quarkus.io/guides/flyway#quarkus-flyway_quarkus.flyway.locations)
1. DB test data import: SQL scripts must be placed in [`src/main/resources/db/test-data/`](src/main/resources/db/test-data) folder, leveraging the `test` profile property `%test.quarkus.flyway.locations=db/migration,db/test-data`

File names convention is based on [Flyway "Versioned Migrations" docs](https://flywaydb.org/documentation/concepts/migrations#versioned-migrations) and follows the pattern:
```sql
V<yyyymmdd>__<SQL action>_<resource>.sql
```
where:

* `yyyymmdd` is the date the file has been added (e.g. `20200928`).  
  In case of multiple files added the same day, add a `.X` suffix (e.g. `20210104.1`)
* `SQL action` is the "SQL action" executed inside the SQL script (e.g. `create`, `alter`, `insert`)
* `resource` is the resource managed by the SQL scripts (e.g. `business-service`)

## Development

### Required components

#### PostgreSQL

First start a PostreSQL container in [Podman](https://podman.io/) executing:
```Shell
$ podman run -it --rm=true --memory-swappiness=0 \
            --name postgres-controls -e POSTGRES_USER=controls \
            -e POSTGRES_PASSWORD=controls -e POSTGRES_DB=controls_db \
            -p 5432:5432 postgres:10.6
```
If running on Fedora 32 or below, remove the `--memory-swappiness` switch, executing:
```Shell
$ podman run -it --rm=true \
            --name postgres-controls -e POSTGRES_USER=controls \
            -e POSTGRES_PASSWORD=controls -e POSTGRES_DB=controls_db \
            -p 5432:5432 postgres:10.6
```
It works the same with Docker just replacing `podman` with `docker` in the above command.  
To connect to the running PostgreSQL instance, first retrieve the `CONTAINER ID` value executing:

```shell
$ podman ps
CONTAINER ID  IMAGE                            COMMAND     CREATED         STATUS             PORTS                                           NAMES
36f92b030807  docker.io/library/postgres:10.6  postgres    22 minutes ago  Up 22 minutes ago  0.0.0.0:5432->5432/tcp                          postgres-controls
```
In the example `CONTAINER ID` is `36f92b030807` and use it in the next command:

```shell
$ podman exec -it <CONTAINER ID> bash
```
Once the container's terminal will be available, connect to the database running:

```shell
root@36f92b030807:/# psql -U controls -d controls_db
```

#### Keycloak

```Shell
$ podman run -it --name keycloak --rm \
            -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin -e KEYCLOAK_IMPORT=/tmp/keycloak/quarkus-realm.json \
            -e DB_VENDOR=h2 -p 8180:8080 -p 8543:8443 -v ./src/main/resources/keycloak:/tmp/keycloak:Z \
            jboss/keycloak:12.0.4
```

### Run the application in dev mode

You can run your application in dev mode that enables live coding using:
```Shell
$ ./mvnw quarkus:dev
```

### Call endpoints in dev mode

To do calls to application's endpoint while running it in dev mode, execute the following commands:
```Shell
$ export access_token=$(\
    curl -X POST http://localhost:8180/auth/realms/quarkus/protocol/openid-connect/token \
    --user backend-service:secret \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token' \
 )
$ curl -X GET 'http://localhost:8080/controls/business-service?description=ser&sort=name' \
  -H 'Accept: application/json' -H "Authorization: Bearer "$access_token |jq .
```

### Insert test data

To help the development, there's a script that inserts three stakeholders and three business services.  
To run it, execute the command:
```shell
$ ./src/main/resources/import-curl-locally.sh http://localhost:8080 http://localhost:8180
```

### Add a resource

For creating the `Foo` resource, follow these steps:

1. add `Foo.java` bean class in [`src/main/java/io/tackle/controls/entities/`](src/main/java/io/tackle/controls/entities/)
   adapting this template class:
   ```java
    package io.tackle.controls.entities;

    import io.tackle.commons.entities.AbstractEntity;
    import org.hibernate.annotations.ResultCheckStyle;
    import org.hibernate.annotations.SQLDelete;
    import org.hibernate.annotations.Where;
    import javax.persistence.Entity;
    import javax.persistence.Table;

    @Entity
    @Table(name = "foo")
    @SQLDelete(sql = "UPDATE foo SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
    @Where(clause = "deleted = false")
    public class Foo extends AbstractEntity {
        <<add fields>>
    }
   ```
   When adding fields to the entity, remember to add the `@Filterable` annotation (from `io.tackle.commons.annotations` package) to the fields accepted as entity filters in REST endpoint.  
   For example, if the entity's got a `description` field that we want to be able to filter by, add the annotation in this way:
   ```java
   @Filterable
   public String description;
   ```
   In case the field represents a relational association entity, then add the field `filterName` to the `@Filterable` annotation in order to specify, with dot notation, the related entity's field the filter will be applied to.  
   For example, the [`BusinessService`](src/main/java/io/tackle/controls/entities/BusinessService.java) entity has an association with `Stakeholder` entity through the `owner` field.  
   Since it has been decided the `Stakeholder`'s field to filter by is the `displayName`, the `owner` field has been annotated in this way:
   ```java
   @Filterable(filterName = "owner.displayName")
   public Stakeholder owner;
   ```
   so that a REST request to filter Business Service entities can use the `owner.displayName` as query parameter.
1. add `FooResource.java` resource class in [`src/main/java/io/tackle/controls/resources/`](src/main/java/io/tackle/controls/resources/)
   adapting this template class:
   ```java
    package io.tackle.controls.resources;
    
    import io.quarkus.hibernate.orm.rest.data.panache.PanacheEntityResource;
    import io.quarkus.panache.common.Page;
    import io.quarkus.panache.common.Sort;
    import io.quarkus.rest.data.panache.MethodProperties;
    import io.quarkus.rest.data.panache.ResourceProperties;
    import io.tackle.controls.entities.Foo;
    
    import java.util.List;
   
    @ResourceProperties(hal = true)
    public interface FooResource extends PanacheEntityResource<Foo, Long> {
       @MethodProperties(exposed = false)
       List<Foo> list(Page page, Sort sort);
    }
   ```
   in this way the REST Data Panache extension will be used for providing all endpoints but the "list" one because we need filtering for lists which is not provided (yet?) from that Quarkus extension.
1. to add the "list" endpoints (both plain JSON and HAL-JSON), add `FooListFilteredResource.java` class in [`src/main/java/io/tackle/controls/resources/`](src/main/java/io/tackle/controls/resources/) adapting this template class:
   ```java
   package io.tackle.controls.resources;

   import io.tackle.commons.resources.ListFilteredResource;
   import io.tackle.controls.entities.Foo;
   import org.jboss.resteasy.links.LinkResource;
   
   import javax.ws.rs.DefaultValue;
   import javax.ws.rs.GET;
   import javax.ws.rs.Path;
   import javax.ws.rs.Produces;
   import javax.ws.rs.QueryParam;
   import javax.ws.rs.core.Context;
   import javax.ws.rs.core.Response;
   import javax.ws.rs.core.UriInfo;
   import java.util.List;
   
   @Path("foo")
   public class FooListFilteredResource implements ListFilteredResource<Foo> {
   
       @Override
       public Class<Foo> getPanacheEntityType() {
           return Foo.class;
       }
   
       @GET
       @Path("")
       @Produces({"application/json"})
       @LinkResource(
               entityClassName = "io.tackle.controls.entities.Foo",
               rel = "list"
       )
       public Response list(@QueryParam(QUERY_PARAM_SORT) @DefaultValue(DEFAULT_VALUE_SORT) List var1,
                            @QueryParam(QUERY_PARAM_PAGE) @DefaultValue(DEFAULT_VALUE_PAGE) int var2,
                            @QueryParam(QUERY_PARAM_SIZE) @DefaultValue(DEFAULT_VALUE_SIZE) int var3,
                            @Context UriInfo var4) throws Exception {
           return ListFilteredResource.super.list(var1, var2, var3, var4, false);
       }
   
       @Path("")
       @GET
       @Produces({"application/hal+json"})
       public Response listHal(@QueryParam(QUERY_PARAM_SORT) @DefaultValue(DEFAULT_VALUE_SORT) List var1,
                               @QueryParam(QUERY_PARAM_PAGE) @DefaultValue(DEFAULT_VALUE_PAGE) int var2,
                               @QueryParam(QUERY_PARAM_SIZE) @DefaultValue(DEFAULT_VALUE_SIZE) int var3,
                               @Context UriInfo var4) throws Exception {
           return ListFilteredResource.super.list(var1, var2, var3, var4, true);
       }
   }
   ```
1. start the application in dev mode following [Running the application in dev mode](#running-the-application-in-dev-mode)
1. open a browser to http://localhost:8080/controls/q/swagger-ui/ to trigger code reload
1. check the application's log in terminal to retrieve Hibernate output about `Foo` entity's table, keys and indexes creation, something like:
   ```sql
   Hibernate: 
    
    create table foo (
       id int8 not null,
        << all the fields declared in Foo bean >>
        primary key (id)
    )
   ```
1. copy all the SQL instructions needed to manage the new `Foo` resource
1. create a `Vyyyymmdd__create_foo.sql` file (e.g. `V20200928__create_business-service.sql`) in [`src/main/resources/db/migration/`](src/main/resources/db/migration) folder (refer to [Flyway](#flyway) paragraph)
1. paste inside the `Vyyyymmdd__create_foo.sql` file all the previously copied SQL DDL instructions
1. for tests execution, add test data into the database creating a `Vyyyymmdd__insert_foo.sql` file (e.g. `V20201210__insert_business-service.sql`) in [`src/main/resources/db/test-data/`](src/main/resources/db/test-data) folder (refer to [Flyway](#flyway) paragraph)
1. create a new test `FooTest` class into [`src/test/java/io/tackle/controls/resources`](src/test/java/io/tackle/controls/resources) folder and add test for the REST endpoints.
   A "reference" test class and methods is [`BusinessServiceTest`](src/test/java/io/tackle/controls/resources/BusinessServiceTest.java)
1. create a new test `NativeFooIT` class into [`src/test/java/io/tackle/controls/resources`](src/test/java/io/tackle/controls/resources) folder adapting this template class:
   ```java
   package io.tackle.controls.resources;

   import io.quarkus.test.junit.NativeImageTest;
   
   @NativeImageTest
   public class NativeFooIT extends FooTest {}
   ```

### Add natural unique key

When an entity's field is a natural key (e.g. the job function's role) that must be unique, then a [PostgreSQL partial index](https://www.postgresql.org/docs/10/indexes-partial.html) can be defined in order to guarantee uniqueness and preserve the soft delete approach.  
If the `Foo` entity has a `name` field that is a natural key and must be unique, then the partial index can be defined in a Flyway script in this way:
```sql
create unique INDEX UK<unique_id>
on foo (name)
where (deleted = false);
```
so that the value of the `name` column in the `foo` table has the unique constraint only if `deleted = false`.  
In this way it's guaranteed that a value, previously used in an already soft-deleted entity, can be used again.  

### Change a resource

1. start the application in dev mode following [Running the application in dev mode](#running-the-application-in-dev-mode)
1. change `%dev.quarkus.hibernate-orm.database.generation = update` property
1. modify resource
1. open a browser to http://localhost:8080/controls/swagger-ui/, this will trigger code reload
1. create a `Vyyyymmdd__alter_foo.sql` file (e.g. `V20210104.1__alter_business-service.sql`) in [`src/main/resources/db/migration/`](src/main/resources/db/migration) folder (refer to [Flyway](#flyway) paragraph)
1. copy Hibernate SQL
   ```sql
   Hibernate: 
    
    alter table if exists foo 
       add column updateTime timestamp

   Hibernate:

    alter table if exists foo 
       add column updateUser varchar(255)
   ```
   
An example of how to rename/remove/add columns copying data from existing into new columns is provided in [`V20210128__alter_stakeholder.sql`](src/main/resources/db/migration/V20210128__alter_stakeholder.sql) file.

### Check generated code

If you want to check the code generated by Quarkus extensions, both when running in dev mode or while executing tests, add the following properties:

```Shell
-Dquarkus.debug.transformed-classes-dir=target/dump -Dquarkus.debug.generated-classes-dir=target/dump
```

to dump the generated code into the `dump` folder.  
More information available in [Dump the Generated Classes to the File System](https://quarkus.io/guides/writing-extensions#dump-the-generated-classes-to-the-file-system) guide.

## Test

### Tests environment

The tests can be run without having to start any external components.  
At the same time, there're situations, for example during development, when you run multiple times in a short time the tests and so it would be good to speed up the tests' execution.  
If the `TACKLE_KEYCLOAK_TEST_URL` environment variable is provided, the tests won't start the Keycloak testcontainer saving its startup time during tests execution.  
Keycloak can be started following the instructions above in [Keycloak](#keycloak) paragraph.  
Once Keycloak is started, set the `TACKLE_KEYCLOAK_TEST_URL` environment variable executing:
```shell
$ export TACKLE_KEYCLOAK_TEST_URL=https://localhost:8543/auth/realms/quarkus
```
In this way the tests will use this provided Keycloak instance instead of starting a new one.  

### Testing JVM mode

`$ ./mvnw test`

> :bulb: In case of `DockerClientException: Could not pull image:` exception when using Podman, just try to re-run the test command.

### Testing native mode

`$ ./mvnw verify -Pnative -Dquarkus-profile=test`  
where the `quarkus-profile=test` property is mandatory to force the build of the native image to use the `test` profile, otherwise the default `prod` profile would be used. 

If you want to just execute (again) the native tests without building again the native image, use the following command:  
`$ ./mvnw test-compile failsafe:integration-test@tackle-controls-IT -Pnative`

## Package and run locally

Before starting locally the application, start the [Required components](#required-components).

### JVM mode
The application can be packaged using the `$ ./mvnw package -Dquarkus-profile=local` command.  
It produces the `quarkus-run.jar` file in the `/target/quarkus-app` directory.  
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/` subfolders.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

### Native mode

You can create a native executable using: `$ ./mvnw package -Dquarkus-profile=local -Pnative -Dquarkus.native.container-build=true`  
If you have GraalVM installed locally, you can run the native executable build without the `-Dquarkus.native.container-build=true` option.  
You can then run the application in native mode executing: `$ ./target/controls-*-runner`  

### Test coverage

To get the report of test coverage of the application's code it's a matter of activating the `coverage` Maven profile during `verify` Maven goal execution like in the following command:  
```shell
$ ./mvnw verify -Pjacoco
```

at the end the report will be available opening in a browser the `target/site/jacoco-ut/index.html` file.  

## Kubernetes

### Minikube

This is base on using Podman and CRI-O locally.
Something very similar can be done using Docker.

#### Start Minikube

```Shell
$ minikube start --driver=podman --container-runtime=cri-o --feature-gates="LocalStorageCapacityIsolation=false"
```
Once started you can open its dashboard executing
```Shell
$ minikube dashboard
```

#### Deploy the application

First build the container image for the application executing:
```Shell
$ ./mvnw package -Pcontainer-image # eventually with -Pnative to deploy the native application
```
If it's the first deployment then create (just once) a dedicated namespace with
```shell
$ kubectl create namespace tackle
```
then you can deploy applying the files generated during the build running the command:
```Shell
$ kubectl apply -f target/kubernetes/minikube.yml -n tackle
```
If there are no changes to the resources in Kubernetes and you just need to have the latest image deployed, you can execute
```Shell
$ kubectl rollout restart deployment controls -n tackle
```

#### Call endpoints

```shell
$ export access_token=$(\
    curl -X POST $(minikube service --url=true keycloak -n tackle)/auth/realms/quarkus/protocol/openid-connect/token \
    --user backend-service:secret \
    -H 'content-type: application/x-www-form-urlencoded' \
    -d 'username=alice&password=alice&grant_type=password' | jq --raw-output '.access_token' \
 )
$ curl -X GET "$(minikube service --url=true controls -n tackle)/controls/business-service?description=ser&sort=name" \
  -H 'Accept: application/json' -H "Authorization: Bearer "$access_token |jq .
```

#### Insert test data

Execute the script:
```shell
$ ./src/main/resources/import-curl-remotely.sh http://192.169.17.2
```

### Kubernetes

To deploy the application you built locally into a Kubernetes instance, you can push your image to a container repository to make it available for deployment into the Kubernetes instance.  
First create the container image (JVM or native mode, native in the example) providing the name of the repository you want to later push the image to: in this case I'm going to use [quay.io](https://quay.io/):
```shell
$ ./mvnw package -Pnative -Pcontainer-image -Dquarkus.container-image.registry=quay.io
```
and then push the image to quay.io repository executing:
```shell
$ podman push quay.io/<your quay user>/controls:<version>-<type>
```
where
* `version` = the version of the current project
* `type` = the package type (i.e. `jar` or `native`)

### Openshift

*TBD*

## Performance testing

```Shell
$ ab -n 1000 -c 20 -H 'Accept: application/hal+json' 'http://<host>/controls/business-service?name=service&sort=name&size=1&page=1'
```

## Database management {#db-mgmt}

> :rotating_light: Backup and Restore instructions are provided for development purposes and **NOT for production** usage :rotating_light:

**This description is meant to support developers and contributors developing tackle-controls. A description how to backup/restore/move an operator-backed installation of Tackle (Application Assessment), consisting of `tackle-application-inventory`, `tackle-controls`, `tackle-pathfinder` and others can be found [here](./docs/MovingTackleDBs.md)**

### Backup

1. get the name of the PostgreSQL pod:
   ```shell
   $ kubectl get pods -l app.kubernetes.io/name=controls-postgres -n tackle
   ```
1. retrieve the database's user using pod's name (e.g. `controls-postgres-5b6cc47f66-fw48p`):
   ```shell
   $ kubectl exec controls-postgres-5b6cc47f66-fw48p -n tackle -- printenv POSTGRES_USER
   ```
1. dump the database **data only** (excluding `flyway_schema_history` table) using pod's name and database's user (e.g. `controls-postgres-5b6cc47f66-fw48p` and `controls`):
   ```shell
   $ kubectl exec controls-postgres-5b6cc47f66-fw48p -n tackle  -- /bin/bash -c "pg_dump -a -T flyway_schema_history -U controls controls_db" > $(date +%Y%m%d%H%M%S)_controls_db_data.sql
   ```

### Restore

1. get the name of the PostgreSQL pod:
   ```shell
   $ kubectl get pods -l app.kubernetes.io/name=controls-postgres -n tackle
   ```
1. retrieve the database's user using pod's name (e.g. `controls-postgres-5b6cc47f66-fw48p`):
   ```shell
   $ kubectl exec controls-postgres-5b6cc47f66-fw48p -n tackle -- printenv POSTGRES_USER
   ```
1. insert the values in the database using pod's name and database's user (e.g. `controls-postgres-5b6cc47f66-fw48p` and `controls`):
   ```shell
   $ cat 20210323195709_controls_db_data.sql | kubectl exec -i controls-postgres-5b6cc47f66-fw48p -n tackle -- psql -U controls -d controls_db
   ```
