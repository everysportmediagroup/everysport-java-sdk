package com.everysport.api.sdk;

import com.everysport.api.sdk.request.GenericRequest;
import com.everysport.api.sdk.response.EverysportResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.zip.GZIPInputStream;

public class EverysportClientTest
{
	private static WireMockServer wireMockServer;

	private static EverysportClient unitUnderTest;

	@Test
	public void testWithGenericRequestDontMapProperty() throws Exception
	{
		GenericRequest genericRequest = new GenericRequest(unitUnderTest);
		String expectedUrl = "/events?apikey=test";

		EverysportResponse<TestResponse> eventsResponses = genericRequest.<TestResponse, TestResponse>get("/events", TestResponse.class, null);

		Assert.assertNull(eventsResponses.getEntity());
		WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(expectedUrl)));
	}

	@Test
	public void testWithGenericRequestMapProperty() throws Exception
	{
		GenericRequest genericRequest = new GenericRequest(unitUnderTest);
		String expectedUrl = "/events?apikey=test";

		EverysportResponse<TestResponse> eventsResponses = genericRequest.<TestResponse, TestResponse>get("/events", TestResponse.class, "events");

		Assert.assertNotNull(eventsResponses.getEntity());
		WireMock.verify(WireMock.getRequestedFor(WireMock.urlEqualTo(expectedUrl)));
	}

	@Test(expected = com.everysport.api.sdk.EverysportException.class)
	public void testErrorRequest() throws Exception
	{
		GenericRequest genericRequest = new GenericRequest(unitUnderTest);
		configureWireMockForResponseOnUrlWithContentFile("/error?apikey=test", "error", 400);

		genericRequest.<TestResponse, TestResponse>get("/error", TestResponse.class, "events");
	}

	@Test
	public void testPagination() throws Exception
	{
		GenericRequest genericRequest = new GenericRequest(unitUnderTest);
		String expectedUrl = "/events?apikey=test";

		EverysportResponse<TestResponse> start = genericRequest.<TestResponse, TestResponse>get("/events", TestResponse.class, "events");

		EverysportResponse<TestResponse> next = unitUnderTest.getNextPage(start);
		EverysportResponse<TestResponse> previousFromNext = unitUnderTest.getPreviousPage(next);
		EverysportResponse<TestResponse> previousFromStart = unitUnderTest.getPreviousPage(start);


		WireMock.verify(1,WireMock.getRequestedFor(WireMock.urlEqualTo("/events?apikey=test"))); //from start
		WireMock.verify(1, WireMock.getRequestedFor(WireMock.urlEqualTo("/events?offset=2&limit=2&apikey=test"))); //from next
		WireMock.verify(2, WireMock.getRequestedFor(WireMock.urlEqualTo("/events?offset=0&limit=2&apikey=test"))); //one from previousFromNext and one from previousFromStart
	}

    @Test
    public void testGzip() throws IOException {

        InputStream is = this.getClass().getResourceAsStream("/__files/events.json.gz");
        GZIPInputStream gis = new GZIPInputStream(is);
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            stringBuilder.append(line);
        }
        bf.close();
        gis.close();

    }



	private void configureWireMockForResponseOnUrlWithContentFile(String url, String contentFile, int statusCode)
	{
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo(url))
				.willReturn(
						WireMock.aResponse()
								.withStatus(statusCode)
								.withBodyFile(contentFile)));
	}

	@Before
	public void setUpHttpMock() throws Exception
	{
		//Get random open port, should be platform independent
		//And start wireMockServer on that port for test cases to use
		ServerSocket serverSocket = new ServerSocket(0);
		int port = serverSocket.getLocalPort();
		serverSocket.close();
		wireMockServer = new WireMockServer(port);
		wireMockServer.start();
		WireMock.configureFor("localhost", port);
		unitUnderTest = new EverysportClient.Builder("test").endpoint("http://localhost:" + port).build();
	}

	@After
	public void stopMockServer()
	{
		wireMockServer.stop();
	}
}
