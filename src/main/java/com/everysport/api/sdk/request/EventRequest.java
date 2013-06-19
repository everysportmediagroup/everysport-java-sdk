package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.Event;
import com.everysport.api.domain.api.EventStatus;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public interface EventRequest extends Listable<EventRequest>
{

    public EventRequest league(Object ... leagueIds);

    public EventRequest sport(Object ... sportIds);

    public EventRequest status(EventStatus... status);

    public EventRequest fromDate(Object... fromDate);

    public EventRequest toDate(Object... toDate);

    public EventRequest round(Object... round);

    public EventRequest fields(Object... fields);

    public EventRequest sort(Object... sort);

    public EventRequest team(Object... team);

    public EverysportResponse<List<Event>> list() throws IOException;

    public EverysportResponse<Event> get(Object eventId) throws IOException;

}
