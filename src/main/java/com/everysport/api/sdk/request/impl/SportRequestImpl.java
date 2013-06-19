package com.everysport.api.sdk.request.impl;

import com.everysport.api.domain.api.Sport;
import com.everysport.api.domain.api.SportsResponse;
import com.everysport.api.sdk.EverysportClient;
import com.everysport.api.sdk.request.AbstractRequest;
import com.everysport.api.sdk.request.SportRequest;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public class SportRequestImpl extends AbstractRequest<SportRequestImpl> implements SportRequest {

    private static final String PATH = "/sports";

    public SportRequestImpl(EverysportClient client) {
        setClient(client);
    }

    public EverysportResponse<List<Sport>> list() throws IOException {
		return genericGet(PATH, SportsResponse.class, "Sports");
    }

}
