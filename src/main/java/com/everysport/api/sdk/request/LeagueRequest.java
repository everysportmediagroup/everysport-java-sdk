package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.League;
import com.everysport.api.domain.api.TeamClass;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public interface LeagueRequest extends Listable<LeagueRequest>
{
	LeagueRequest sport(Object ... sportIds);

    LeagueRequest teamClass(TeamClass ... teamClass);

	EverysportResponse<List<League>> list() throws IOException;

	EverysportResponse<League> get(Object leagueId) throws IOException;
}
