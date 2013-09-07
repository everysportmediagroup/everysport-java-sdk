This is an official Java client for the [Everysport API](https://github.com/menmo/everysport-api-documentation)

## Maven
```
<dependency>
    <groupId>com.everysport</groupId>
    <artifactId>everysport-java-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```


## Quickstart

```java
/*
 * Create a EverysportClient
 *
 * This should be done once and be reused.
 */

EverysportClient client = new EverysportClient.Builder("your-apikey").build();

/*
 * Tip!
 *
 * It is possible to change the behaviour of the client with different settings
 * EverysportClient client = new EverysportClient.Builder("your-apikey").connectionTimeout(100).build();
 *
 */

/*
 * Make a request
 *
 * A request to the API is done through any of the differnt Request objects (EventRequest, SportRequest etc.).
 * Different requests objects takes different parameters (see the everysport-api documentation).
 * In general, Request objects has a list() and a get() method that returns a EverysportResponse object.
 */

EverysportResponse<List<Event>> response = client.getEventRequest().league(59776).limit(5).list();

/*
 * Get the list of events from the response
 */

List<Event> events = response.getEntity();

for(Event event : events) {
    System.out.println(event.getHomeTeam().getName());
}

/*
 * Pagination of Result
 *
 * Since we did a list() request it is possible to get the next (or the previous page) from the API.
 */

EverysportResponse<List<Event>> nextPage = client.getNextPage(response);

/*
 * Get one Event
 *
 * If we already know what event we want to get (we know the ID) then instead
 * of making a list() request we do a get() request.
 */
EverysportResponse<List<Event>> response = client.getEventRequest().get(2186306);

/*
 * Note!
 * When using get() all params (e.g. limit, offset, sort etc.) is not giving any effect even if they are set.
 */


 /*
  * Generic Request
  *
  * If you for some reason want to do a request without using any of the pre-defined Request objects
  * it is possible to do so by using the GenericRequest object.
  */

client.get(new GenericRequest(client).withPath("/events").withParam("sport", 1)).getRawBody()
```

## Examples

For working code examples see the package ```com.everysport.api.sdk.examples```.


