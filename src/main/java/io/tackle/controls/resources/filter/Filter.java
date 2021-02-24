package io.tackle.controls.resources.filter;

import java.util.List;
import java.util.Map;

public class Filter {
    private String query;
    private Map<String, Object> queryParameters;
    private Map<String, List<String>> rawQueryParams;

    public static Filter withQuery(String query) {
        Filter filter = new Filter();
        filter.query = query;
        return filter;
    }

    public Filter andParameters(Map<String, Object> queryParameters) {
        this.queryParameters = queryParameters;
        return this;
    }

    public Filter andRawQueryParams(Map<String, List<String>> rawQueryParams) {
        this.rawQueryParams = rawQueryParams;
        return this;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, Object> getQueryParameters() {
        return queryParameters;
    }

    public Map<String, List<String>> getRawQueryParams() {
        return rawQueryParams;
    }
}
