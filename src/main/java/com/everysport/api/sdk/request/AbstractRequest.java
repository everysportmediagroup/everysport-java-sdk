package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.ApiBaseResponse;
import com.everysport.api.domain.api.EventStatus;
import com.everysport.api.domain.api.TeamClass;
import com.everysport.api.sdk.EverysportClient;
import com.everysport.api.sdk.response.EverysportResponse;
import com.everysport.api.sdk.utils.UrlUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRequest<T> implements Cloneable {

    private Class type;
    private String property;
    private String path;
    private Map<String, String> params;
    private EverysportClient client;
    private String requestUri;
    private String body;

    protected <V, T extends ApiBaseResponse> EverysportResponse<List<V>> genericList(String path, Class<T> deserializationType, String jsonProperty) throws IOException {
        setType(deserializationType);
        setPath(path);
        setProperty(jsonProperty);
        return (EverysportResponse<List<V>>) getClient().<List<V>, T>get(this);
    }

    protected <V, T extends ApiBaseResponse> EverysportResponse<V> genericGet(String path, Class<T> deserializationType, String jsonProperty) throws IOException {
        setType(deserializationType);
        setPath(path);
        setProperty(jsonProperty);
        return (EverysportResponse<V>) getClient().<V, T>get(this);
    }

    protected void setClient(EverysportClient client) {
        this.client = client;
    }

    protected EverysportClient getClient() {
        return client;
    }

    public T limit(Object limit) {
        assertEqualOrGreaterThan(1, "limit", limit);
        addParam("limit", limit);
        return (T) this;
    }

    public T offset(Object offset) {
        assertEqualOrGreaterThan(0, "offset", offset);
        addParam("offset", offset);
        return (T) this;
    }

    public T sort(Object... sort) {
        addParam("sort", sort);
        return (T) this;
    }

    public T league(Object... leagueIds) {
        assertEqualOrGreaterThan(1, "league", leagueIds);
        addParam("league", join(leagueIds));
        return (T) this;
    }

    public T sport(Object... sportIds) {
        assertEqualOrGreaterThan(1, "sport", sportIds);
        addParam("sport", join(sportIds));
        return (T) this;
    }

    public T teamClass(TeamClass... teamClass) {
        addParam("teamClass", join(teamClass));
        return (T) this;
    }

    public T type(Object... type) {
        addParam("type", join(type));
        return (T) this;
    }

    public T round(Object... round) {
        addParam("round", join(round));
        return (T) this;
    }

    public T fields(Object... fields) {
        addParam("fields", join(fields));
        return (T) this;
    }

    public T size(Object... size) {
        addParam("size", join(size));
        return (T) this;
    }

    public T group(Object... group) {
        addParam("group", join(group));
        return (T) this;
    }

    public T status(EventStatus... status) {
        addParam("status", join(status));
        return (T) this;
    }

    public T fromDate(Object... fromDate) {
        addParam("fromDate", join(fromDate));
        return (T) this;
    }

    public T toDate(Object... toDate) {
        addParam("toDate", join(toDate));
        return (T) this;
    }

    public T team(Object... teamIds) {
        assertEqualOrGreaterThan(1, "team", teamIds);
        addParam("team", join(teamIds));
        return (T) this;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public T withPath(String path) {
        this.path = path;
        return (T) this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getParam(String key) {

        if (params == null) {
            return null;
        }

        return params.get(key);
    }

    public void addParam(String key, Object... value) {
        addParam(key, join(value));
    }

    public T withParam(String key, Object... value) {
        addParam(key, join(value));
        return (T) this;
    }

    private void addParam(String key, Object value) {

        if (params == null) {
            params = new HashMap();
        }

        params.put(key, String.valueOf(value));
    }

    public String generateUri() {

        if (path == null) {
            requestUri = null;
        }
        if (path.indexOf('?') != -1) {
            requestUri = path;
        }

        StringBuilder stringBuilder = null;
        if (params != null && params.size() > 0) {
            stringBuilder = new StringBuilder();
            UrlUtils.addParams(params, stringBuilder);
        }

        if (stringBuilder != null && stringBuilder.length() > 0) {
            requestUri = path + '?' + stringBuilder.toString();
        } else {
            requestUri = path;
        }
        return requestUri;
    }

    private String join(Object[] objects) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : objects) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(String.valueOf(o));
        }
        return stringBuilder.toString();
    }

    public Class getType() {
        return type;
    }

    public void setType(Class type) {
        this.type = type;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public T withBody(String body) {
        this.body = body;
        return (T) this;
    }

    @Override
    public AbstractRequest clone() throws CloneNotSupportedException {

        AbstractRequest<T> clone = (AbstractRequest<T>) super.clone();
		if (params == null)
		{
			clone.setParams(new HashMap<String, String>());
		} else
		{
			clone.setParams(new HashMap<>(params));
		}

        return clone;

    }


    /**
     * Check to see if {@code check} is equal or greater than {@code value}.
     * If assert fails throw an IllegalArgumentException with
     * a message stating that the {@code paramName} must be a number
     * equal or greater than value.
     *
     * @param value
     * @param paramName
     * @param check
     * @throws IllegalArgumentException
     */
    protected void assertEqualOrGreaterThan(long value, String paramName, Object... check) throws IllegalArgumentException {
        for (Object o : check) {
            if (Long.valueOf(o.toString()) < value) {
                throw new IllegalArgumentException(String.format("'%s' must be a number equal or greater than %d", paramName, value));
            }
        }
    }
}
