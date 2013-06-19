package com.everysport.api.sdk.request;

public interface Listable<T> {

    public T limit(Object limit);

    public T offset(Object offset);
}
