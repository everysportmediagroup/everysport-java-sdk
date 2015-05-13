package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.LeagueResponse;
import com.everysport.api.domain.api.LeaguesResponse;
import com.everysport.api.sdk.request.impl.LeagueRequestImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class LeagueRequestTest extends BaseRequestTest {

    private LeagueRequest req;

    @Before
    public void setup() throws IOException {
        super.setup();
        req = new LeagueRequestImpl(client);
    }


    @Test
    @Override
    public void checkParams() {

        Set<String> availableParams = new HashSet();
        availableParams.add("sport");
        availableParams.add("limit");
        availableParams.add("offset");
        availableParams.add("teamClass");

        Method[] methods = req.getClass().getMethods();

        Set<String> methodNames = new HashSet();
        for (Method method : methods) {
            methodNames.add(method.getName());
        }

        for (String param : availableParams) {
            assertTrue("Missing '" + param +"' as an available parameter.", methodNames.contains(param));
        }

    }

    @Test
    public void properArgumentsForList() throws IOException {

        req.list();

        assertEquals("/leagues", ((LeagueRequestImpl) req).getPath());
        assertEquals("Leagues", ((LeagueRequestImpl) req).getProperty());
        assertSame(LeaguesResponse.class, ((LeagueRequestImpl) req).getType());

    }

    @Test
    public void properArgumentsForGet() throws IOException {

        req.get(1);

        assertEquals("/leagues/1", ((LeagueRequestImpl) req).getPath());
        assertEquals("League", ((LeagueRequestImpl) req).getProperty());
        assertSame(LeagueResponse.class, ((LeagueRequestImpl) req).getType());

    }
}
