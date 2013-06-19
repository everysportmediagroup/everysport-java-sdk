package com.everysport.api.sdk.request;

import com.everysport.api.sdk.EverysportClient;
import com.everysport.api.sdk.response.EverysportResponse;
import org.junit.Before;
import org.mockito.Mockito;

import java.io.IOException;

public abstract class BaseRequestTest {


    EverysportResponse everysportResponse;
    EverysportClient client;

    @Before
    public void setup() throws IOException {
        client = Mockito.mock(EverysportClient.class);
        everysportResponse = new EverysportResponse();
        Mockito.when(client.get(Mockito.<AbstractRequest>anyObject())).thenReturn(everysportResponse);
    }

    public abstract void checkParams();

}
