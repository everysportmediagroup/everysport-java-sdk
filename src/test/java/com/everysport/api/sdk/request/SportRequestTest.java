package com.everysport.api.sdk.request;

import com.everysport.api.domain.api.SportsResponse;
import com.everysport.api.sdk.request.impl.SportRequestImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class SportRequestTest extends BaseRequestTest {

    private SportRequest req;

    @Before
    public void setup() throws IOException {
        super.setup();
        req = new SportRequestImpl(client);
    }

    @Test
    @Override
    public void checkParams() {

        Set<String> availableParams = new HashSet();
        availableParams.add("limit");
        availableParams.add("offset");

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
    public void properArgumentsForList() throws IOException {

        req.list();

        assertEquals("/sports", ((SportRequestImpl) req).getPath());
        assertEquals("Sports", ((SportRequestImpl) req).getProperty());
        assertSame(SportsResponse.class, ((SportRequestImpl) req).getType());

    }

}
