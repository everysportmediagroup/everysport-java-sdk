package com.everysport.api.sdk.response;

import com.everysport.api.domain.api.Credit;
import com.everysport.api.sdk.request.AbstractRequest;

import java.util.Map;

public class EverysportResponse<T> {

    private AbstractRequest<T> request;
    private String rawBody;
    private T entity;
    private Map<String, Object> metadata;
    private Credit credit;

    public EverysportResponse() {
    }

    public String getRawBody() {
        return rawBody;
    }

    public void setRawBody(String rawBody) {
        this.rawBody = rawBody;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public AbstractRequest<T> getRequest() {
        return request;
    }

    public void setRequest(AbstractRequest<T> request) {
        this.request = request;
    }

}
