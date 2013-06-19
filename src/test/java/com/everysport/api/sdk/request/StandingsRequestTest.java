package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.StandingsResponse;
import com.everysport.api.sdk.request.impl.StandingsRequestImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class StandingsRequestTest extends BaseRequestTest {

    private StandingsRequest req;

    @Before
    public void setup() throws IOException {
        super.setup();
        req = new StandingsRequestImpl(client);
    }

    @Test
    @Override
    public void checkParams() {

        Set<String> availableParams = new HashSet();
        availableParams.add("size");
        availableParams.add("sort");
        availableParams.add("type");
        availableParams.add("round");
        availableParams.add("group");

        Method[] methods = req.getClass().getMethods();

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
    public void properArgumentsForGet() throws IOException {

        req.get(1);

        assertEquals("/leagues/1/standings", ((StandingsRequestImpl) req).getPath());
        assertEquals("Groups", ((StandingsRequestImpl) req).getProperty());
        assertSame(StandingsResponse.class, ((StandingsRequestImpl) req).getType());

    }


}
