package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.ApiBaseResponse;
import com.everysport.api.sdk.EverysportClient;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public class GenericRequest extends AbstractRequest<GenericRequest> implements Listable<GenericRequest>
{
	public GenericRequest(EverysportClient client) {
		setClient(client);
	}

	public <V, T extends ApiBaseResponse> EverysportResponse<List<V>> list(String path, Class<T> deserializationType, String jsonProperty) throws IOException {
		return genericList(path, deserializationType, jsonProperty);
	}

	public <V, T extends ApiBaseResponse> EverysportResponse<V> get(String path, Class<T> deserializationType, String jsonProperty) throws IOException {
		return genericGet(path, deserializationType, jsonProperty);
	}
}
