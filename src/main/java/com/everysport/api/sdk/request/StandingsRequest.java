package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.Group;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public interface StandingsRequest extends Sortable<StandingsRequest> {

    public StandingsRequest type(Object... type);

    public StandingsRequest round(Object... round);

    public StandingsRequest size(Object... size);

    public StandingsRequest group(Object... group);

    public EverysportResponse<List<Group>> get(Object leagueId) throws IOException;
}
