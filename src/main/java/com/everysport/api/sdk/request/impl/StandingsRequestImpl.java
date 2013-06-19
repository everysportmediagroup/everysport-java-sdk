package com.everysport.api.sdk.request.impl;

import com.everysport.api.domain.api.Group;
import com.everysport.api.domain.api.StandingsResponse;
import com.everysport.api.sdk.EverysportClient;
import com.everysport.api.sdk.request.AbstractRequest;
import com.everysport.api.sdk.request.Sortable;
import com.everysport.api.sdk.request.StandingsRequest;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public class StandingsRequestImpl extends AbstractRequest<StandingsRequest> implements StandingsRequest, Sortable<StandingsRequest> {

    public StandingsRequestImpl(EverysportClient client) {
        setClient(client);
    }

    public EverysportResponse<List<Group>> get(Object leagueId) throws IOException {
		return genericGet("/leagues/" + leagueId + "/standings", StandingsResponse.class, "Groups");
    }
}
