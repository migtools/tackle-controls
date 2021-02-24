package io.tackle.controls.resources.filter;

import io.tackle.controls.resources.ListFilteredResource;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

public class FilterBuilder {

    private final UriInfo uriInfo;
    private List<String> filterableFields;

    FilterBuilder(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public static FilterBuilder withUriInfo(UriInfo uriInfo) {
        return new FilterBuilder(uriInfo);
    }

    public FilterBuilder andAcceptedFilters(List<String> filterableFields) {
        this.filterableFields = filterableFields;
        return this;
    }

    public Filter build() {
        final StringBuilder queryBuilder = new StringBuilder();
        final Map<String, Object> queryParameters = new HashMap<>();
        final Map<String, List<String>> rawQueryParams = new HashMap<>();
        uriInfo.getQueryParameters(true).forEach((key, values) -> {
            // change this to use filter?
            if (!(ListFilteredResource.QUERY_PARAM_SIZE.equals(key) ||
                    ListFilteredResource.QUERY_PARAM_PAGE.equals(key) ||
                    ListFilteredResource.QUERY_PARAM_SORT.equals(key) ||
                    ListFilteredResource.QUERY_PARAM_FILTER.equals(key))) {
                // if the filter name is not one of the fields annotated as Filterable
                // then the request is bad and the user should not repeat it without modifications
                // https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html#sec10.4.1
                if (!filterableFields.contains(key)) throw new WebApplicationException("Malformed syntax for a filter name", BAD_REQUEST);
                if (queryBuilder.length() != 0) queryBuilder.append(" and ");
                else queryBuilder.append("WHERE ");
                queryBuilder.append("( ");
                AtomicBoolean isFirst = new AtomicBoolean(true);
                values.forEach(value -> {
                    if (!isFirst.compareAndSet(true, false)) queryBuilder.append(" or ");
                    String randomParameterKey = key.replace('.', '_') + ThreadLocalRandom.current().nextInt(0, 1001);
                    // https://github.com/quarkusio/quarkus/issues/15088#issuecomment-783454416
                    // Due to the need of generating queries on our own, where parameters must have a prefix
                    queryBuilder.append(String.format("lower(%s.%s) LIKE lower(:%s)", ListFilteredResource.DEFAULT_SQL_ROOT_TABLE_ALIAS, key, randomParameterKey));
                    queryParameters.put(randomParameterKey, String.format("%%%s%%", value));
                    rawQueryParams.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
                });
                queryBuilder.append(" )");
            }
        });
        return Filter.withQuery(queryBuilder.toString()).andParameters(queryParameters).andRawQueryParams(rawQueryParams);
    }
}
