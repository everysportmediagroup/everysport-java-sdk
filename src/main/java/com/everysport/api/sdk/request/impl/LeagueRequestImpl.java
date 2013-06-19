package com.everysport.api.sdk.request.impl;

import com.everysport.api.domain.api.League;
import com.everysport.api.domain.api.LeagueResponse;
import com.everysport.api.domain.api.LeaguesResponse;
import com.everysport.api.sdk.EverysportClient;
import com.everysport.api.sdk.request.AbstractRequest;
import com.everysport.api.sdk.request.LeagueRequest;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public class LeagueRequestImpl extends AbstractRequest<LeagueRequest> implements LeagueRequest
{

	public LeagueRequestImpl(EverysportClient client) {
		setClient(client);
	}

	public EverysportResponse<List<League>> list() throws IOException {
		return genericList("/leagues", LeaguesResponse.class, "Leagues");
	}

	public EverysportResponse<League> get(Object leagueId) throws IOException {
		return genericGet("/leagues/"+leagueId, LeagueResponse.class, "League");
	}
}
