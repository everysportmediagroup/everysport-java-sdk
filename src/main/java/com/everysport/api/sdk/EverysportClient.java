package com.everysport.api.sdk;

import com.everysport.api.domain.api.ApiBaseResponse;
import com.everysport.api.sdk.request.*;
import com.everysport.api.sdk.request.impl.EventRequestImpl;
import com.everysport.api.sdk.request.impl.LeagueRequestImpl;
import com.everysport.api.sdk.request.impl.SportRequestImpl;
import com.everysport.api.sdk.request.impl.StandingsRequestImpl;
import com.everysport.api.sdk.response.EverysportResponse;
import com.everysport.api.sdk.utils.ClientProxy;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import static java.net.HttpURLConnection.*;

public class EverysportClient {

    private String endpoint;
    private String apiKey;
    private ObjectMapper objectMapper;
    private ClientProxy clientProxy;

    private int requestConnectionTimeout;

    /* CONSTANTS */
    protected static final int HTTP_UNPROCESSABLE_ENTITY = 422;
    protected static final String DEFAULT_ENDPOINT = "http://api.everysport.com/v1";
    protected static final String CREDENTIALS_IDENTIFIER = "apikey";

    public EventRequest getEventRequest() {
        return new EventRequestImpl(this);
    }

    public SportRequest getSportRequest() {
        return new SportRequestImpl(this);
    }

    public StandingsRequest getStandingsRequest() {
        return new StandingsRequestImpl(this);
    }

	public LeagueRequest getLeagueRequest() {
		return new LeagueRequestImpl(this);
	}

    public static class Builder {

        // required params
        private final String apiKey;

        // optional params
        private String endpoint = DEFAULT_ENDPOINT;

        private ObjectMapper objectMapper;
        private int requestConnectionTimeout;
        private ClientProxy clientProxy;

        public Builder(String apiKey) {
            this.apiKey = apiKey;
        }

        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder objectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public Builder connectionTimeout(int milliseconds) {
            requestConnectionTimeout = milliseconds;
            return this;
        }

        public Builder proxy(ClientProxy clientProxy) {
            this.clientProxy = clientProxy;
            return this;
        }

        public EverysportClient build() {

            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
                objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ"));
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            }

            return new EverysportClient(this);
        }
    }

    private EverysportClient(Builder builder) {
        apiKey = builder.apiKey;
        endpoint = builder.endpoint;
        objectMapper = builder.objectMapper;
        requestConnectionTimeout = builder.requestConnectionTimeout;
        clientProxy = builder.clientProxy;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getApiKey() {
        return apiKey;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Configure request with standard headers
     *
     * @param request
     * @return configured request
     */
    protected HttpURLConnection configureConnection(final HttpURLConnection request) {
        request.setConnectTimeout(requestConnectionTimeout);
        request.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
        request.setRequestProperty("Accept", "application/json; charset=utf-8");
        return request;
    }

    /**
     * Configure request URI
     *
     * @param uri
     * @return configured URI
     */
    protected String configureUri(final String uri) {
        return uri;
    }

    /**
     * Create connection to URI
     *
     * @param uri
     * @return connection
     * @throws IOException
     */
    protected HttpURLConnection createConnection(String uri) throws IOException {
        URL url = new URL(createUri(uri));

        Proxy proxy;
        if(clientProxy != null) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(clientProxy.getHost(), clientProxy.getPort()));
            return (HttpURLConnection) url.openConnection(proxy);
        }

        return (HttpURLConnection) url.openConnection();
    }

    /**
     * Create connection to URI
     *
     * @param uri
     * @param method
     * @return connection
     * @throws IOException
     */
    protected HttpURLConnection createConnection(String uri, String method)
            throws IOException {
        HttpURLConnection connection = createConnection(uri);
        connection.setRequestMethod(method);
        return configureConnection(connection);
    }

    /**
     * Create a GET request connection to the URI
     *
     * @param uri
     * @return connection
     * @throws IOException
     */
    protected HttpURLConnection createGet(String uri) throws IOException {
        return createConnection(uri, "GET");
    }

    /**
     * Create a POST request connection to the URI
     *
     * @param uri
     * @return connection
     * @throws IOException
     */
    protected HttpURLConnection createPost(String uri) throws IOException {
        return createConnection(uri, "POST");
    }

    /**
     * Does status code denote an error
     *
     * @param code
     * @return true if error, false otherwise
     */
    protected boolean isError(final int code) {
        switch (code) {
            case HTTP_BAD_REQUEST:
            case HTTP_UNAUTHORIZED:
            case HTTP_FORBIDDEN:
            case HTTP_NOT_FOUND:
            case HTTP_CONFLICT:
            case HTTP_GONE:
            case HTTP_UNPROCESSABLE_ENTITY:
            case HTTP_INTERNAL_ERROR:
                return true;
            default:
                return false;
        }
    }

    /**
     * Does status code denote a non-error response?
     *
     * @param code
     * @return true if okay, false otherwise
     */
    protected boolean isOk(final int code) {
        switch (code) {
            case HTTP_OK:
            case HTTP_CREATED:
            case HTTP_ACCEPTED:
                return true;
            default:
                return false;
        }
    }

    /**
     * Create error exception from response and throw it
     *
     * @param response
     * @param code
     * @param status
     * @return non-null newly created {@link IOException}
     */
    protected IOException createException(InputStream response, int code,
                                          String status) {
        if (isError(code)) {
            final com.everysport.api.domain.api.Error error;
            try {
                error = getObjectMapper().readValue(response, com.everysport.api.domain.api.Error.class);
            } catch (IOException e) {
                return e;
            }
            if (error != null)
                return new EverysportException(error, code, error.toString());
        } else
            try {
                response.close();
            } catch (IOException ignored) {
                // Ignored
            }
        String message;
        if (status != null && status.length() > 0)
            message = status + " (" + code + ')'; //$NON-NLS-1$
        else
            message = "Unknown error occurred (" + code + ')'; //$NON-NLS-1$
        return new IOException(message);
    }

    /**
     * Send body to output stream of request
     *
     * @param request
     * @param body
     * @throws IOException
     */
    protected void sendBody(HttpURLConnection request, String body)
            throws IOException {
        request.setDoOutput(true);
        if (body != null) {
            request.setRequestProperty("Content-Type", "application/json"
                    + "; charset=" + "UTF-8");
            byte[] data = body.getBytes("UTF-8");
            request.setFixedLengthStreamingMode(data.length);
            BufferedOutputStream output = new BufferedOutputStream(
                    request.getOutputStream(), 8192);
            try {
                output.write(data);
                output.flush();
            } finally {
                try {
                    output.close();
                } catch (IOException ignored) {
                    // Ignored
                }
            }
        } else {
            request.setFixedLengthStreamingMode(0);
            request.setRequestProperty("Content-Length", "0");
        }
    }

    /**
     * Create full URI from path
     *
     * @param path
     * @return uri
     */
    protected String createUri(final String path) {
        String uri = endpoint + configureUri(path);
        return addAuthentication(uri);
    }

    protected String addAuthentication(String uri) {

        if (uri.indexOf(CREDENTIALS_IDENTIFIER + "=") == -1) {
            StringBuilder stringBuilder = new StringBuilder(uri);
            if (uri.indexOf("?") == -1) {
                stringBuilder.append("?");
            } else {
                stringBuilder.append("&");
            }

            stringBuilder.append(CREDENTIALS_IDENTIFIER).append("=").append(apiKey);
            return stringBuilder.toString();
        }

        return uri;
    }

    /**
     * Get stream from request
     *
     * @param request
     * @return stream
     * @throws IOException
     */
    protected InputStream getStream(HttpURLConnection request)
            throws IOException {
        if (request.getResponseCode() < HTTP_BAD_REQUEST) {
            return request.getInputStream();
        } else {
            InputStream stream = request.getErrorStream();
            return stream != null ? stream : request.getInputStream();
        }
    }

    /**
     * Get response from URI and bind to specified type
     *
     * @param request
     * @return response
     * @throws IOException
     */
    public <V, T extends ApiBaseResponse> EverysportResponse<V> get(AbstractRequest request) throws IOException {

        EverysportResponse<V> everysportResponse = new EverysportResponse<V>();
        everysportResponse.setRequest(request);

        HttpURLConnection httpRequest = createGet(request.generateUri());
        String encoding = httpRequest.getContentEncoding();
        String contentType = httpRequest.getContentType();
        final int code = httpRequest.getResponseCode();

        if (isOk(code)) {
            String responseBody = getBodyFromHttpRequest(httpRequest);
            everysportResponse.setRawBody(responseBody);
            if(request.getType() != null) {
                T t = (T) getObjectMapper().readValue(responseBody, request.getType());
                everysportResponse.setMetadata(t.getMetadata());
                everysportResponse.setCredit(t.getCredit());

                if (request.getProperty() != null)
                {
                    try {
                        Method getter = getGetter(request.getType(), request.getProperty());
                        everysportResponse.setEntity((V) getter.invoke(t));
                    } catch (IllegalAccessException|InvocationTargetException|NullPointerException e) {
                        throw new EverysportException("Can not map " + request.getProperty() + " to " + request.getType().getName());
                    }
                }
            }

            return everysportResponse;
        }

        throw createException(getStream(httpRequest), code,
                httpRequest.getResponseMessage());
    }

    /**
     * Post data to URI
     *
     * @param <V>
     * @return response
     * @throws IOException
     */
    public <V> EverysportResponse<V> post(AbstractRequest request)
            throws IOException {

        EverysportResponse<V> everysportResponse = new EverysportResponse<V>();
        everysportResponse.setRequest(request);

        HttpURLConnection httpRequest = createPost(request.generateUri());
        sendBody(httpRequest, request.getBody());
        final int code = httpRequest.getResponseCode();
        String body;
        if(isOk(code)) {
            everysportResponse.setRawBody(getBodyFromHttpRequest(httpRequest));
            return everysportResponse;
        }

        throw createException(getStream(httpRequest), code,
                httpRequest.getResponseMessage());
    }

    private String getBodyFromHttpRequest(HttpURLConnection httpRequest) throws IOException {

        String encoding = httpRequest.getContentEncoding();

        InputStream resultingInputStream;
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
            resultingInputStream = new GZIPInputStream(httpRequest.getInputStream());
        }
        else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
            resultingInputStream = new InflaterInputStream(httpRequest.getInputStream(), new Inflater(true));
        }
        else {
            resultingInputStream = httpRequest.getInputStream();
        }

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            br = new BufferedReader(new InputStreamReader(resultingInputStream));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    private static Method getGetter(Class clazz, String property) {
        Method getter = null;
        try {
            for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
                Method method = propertyDescriptor.getReadMethod();
                if (method != null && ("get" + property).equalsIgnoreCase(method.getName())) {
                    getter = method;
                    break;
                }
            }
        } catch (IntrospectionException e) {
            e.getStackTrace();
        }
        return getter;
    }

    public EverysportResponse getNextPage(EverysportResponse everysportResponse) throws IOException {

        Listable clone;
        try {
			checkIfPageable(everysportResponse);
            clone = (Listable) everysportResponse.getRequest().clone();
			int offset = getMetaDataProperty(everysportResponse, "offset");
			int limit = getMetaDataProperty(everysportResponse, "limit");
			int totalCount = getMetaDataProperty(everysportResponse, "totalCount");
			clone.offset(Math.min(offset + limit, totalCount));
			clone.limit(limit);
        } catch (CloneNotSupportedException e) {
            throw new EverysportException(e);
        }

        return this.get((AbstractRequest) clone);

    }

    public EverysportResponse getPreviousPage(EverysportResponse everysportResponse) throws IOException {

        Listable clone;
        try {
			checkIfPageable(everysportResponse);
            clone = (Listable) everysportResponse.getRequest().clone();
			int offset = getMetaDataProperty(everysportResponse, "offset");
			int limit = getMetaDataProperty(everysportResponse, "limit");
            clone.offset(Math.max(0, offset - limit));
			clone.limit(limit);
        } catch (CloneNotSupportedException e) {
            throw new EverysportException(e);
        }

        return this.get((AbstractRequest) clone);

    }

	private int getMetaDataProperty(EverysportResponse response, String property)
	{
		Object objectProperty = response.getMetadata().get(property);
		if (objectProperty instanceof Integer)
		{
			return (Integer) objectProperty;
		}
		return Integer.valueOf((String)response.getMetadata().get(property));
	}

	private void checkIfPageable(EverysportResponse response) throws EverysportException
	{
		if (!(response.getRequest() instanceof Listable))
		{
			throw new EverysportException("Request " + response.getRequest().getClass().getName() + " is not pageable");
		}
	}


}