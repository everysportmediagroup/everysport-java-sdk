package com.everysport.api.sdk;

import com.everysport.api.domain.api.ApiBaseResponse;
import com.everysport.api.domain.api.Event;

import java.util.List;

public class TestResponse extends ApiBaseResponse
{
	private List<Event> events;

	public List<Event> getEvents()
	{
		return events;
	}

	public void setEvents(List<Event> events)
	{
		this.events = events;
	}
}
