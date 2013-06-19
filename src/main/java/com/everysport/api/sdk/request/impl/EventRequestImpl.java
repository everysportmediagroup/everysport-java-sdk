package com.everysport.api.sdk.request.impl;

import com.everysport.api.domain.api.Event;
import com.everysport.api.domain.api.EventResponse;
import com.everysport.api.domain.api.EventStatus;
import com.everysport.api.domain.api.EventsResponse;
import com.everysport.api.sdk.EverysportClient;
import com.everysport.api.sdk.request.AbstractRequest;
import com.everysport.api.sdk.request.EventRequest;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public class EventRequestImpl extends AbstractRequest<EventRequestImpl> implements EventRequest {

    public EventRequestImpl(EverysportClient client) {
        setClient(client);
    }

    public static EventRequest getInstance(EverysportClient client) {
         return new EventRequestImpl(client);
    }

    public EverysportResponse<List<Event>> list() throws IOException {
		return genericList("/events", EventsResponse.class, "Events");
    }

    public EverysportResponse<Event> get(Object eventId) throws IOException {
		return genericGet("/events/"+eventId, EventResponse.class, "Event");
    }

}
