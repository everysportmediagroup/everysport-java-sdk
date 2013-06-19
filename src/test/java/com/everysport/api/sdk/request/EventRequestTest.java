package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.EventResponse;
import com.everysport.api.domain.api.EventsResponse;
import com.everysport.api.sdk.request.impl.EventRequestImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertSame;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class EventRequestTest extends BaseRequestTest {

    private EventRequest req;

    @Before
    public void setup() throws IOException {
        super.setup();
        req = new EventRequestImpl(client);
    }


    @Test
    public void checkParams() {

        Set<String> availableParams = new HashSet();
        availableParams.add("league");
        availableParams.add("status");
        availableParams.add("fromDate");
        availableParams.add("toDate");
        availableParams.add("round");
        availableParams.add("limit");
        availableParams.add("offset");
        availableParams.add("sort");
        availableParams.add("team");
        availableParams.add("sport");

        Method[] methods = new EventRequestImpl(client).getClass().getMethods();

        Set<String> methodNames = new HashSet();
        for (Method method : methods) {
            methodNames.add(method.getName());
        }

        //Todo Nicer to use Hamcrest and assertThat ... contains ...
        for (String param : availableParams) {
            assertTrue("Missing '" + param +"' as an available parameter.", methodNames.contains(param));
        }
    }

    @Test
    public void properArgumentsForList() throws IOException {

        req.list();

        assertEquals("/events", ((EventRequestImpl) req).getPath());
        assertEquals("Events", ((EventRequestImpl) req).getProperty());
        assertSame(EventsResponse.class, ((EventRequestImpl) req).getType());

    }

    @Test
    public void properArgumentsForGet() throws IOException {

        req.get(1);

        assertEquals("/events/1", ((EventRequestImpl) req).getPath());
        assertEquals("Event", ((EventRequestImpl) req).getProperty());
        assertSame(EventResponse.class, ((EventRequestImpl) req).getType());

    }

}
