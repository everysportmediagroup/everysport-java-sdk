package com.everysport.api.sdk.examples;

import com.everysport.api.domain.api.Event;
import com.everysport.api.domain.api.EventStatus;
import com.everysport.api.sdk.EverysportClient;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Example of listing events
 */
public class ListEventExample {

    private static final String APIKEY = "YOUR-API-KEY";

    public static void main(String[] args) throws IOException {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        String today = sf.format(new Date(System.currentTimeMillis()));
        String day3 = sf.format(new Date(System.currentTimeMillis() + 3600 * 24 * 3 * 1000));

        EverysportClient client = new EverysportClient.Builder(APIKEY).build();
        EverysportResponse<List<Event>> response;

        //Get list of upcoming events
        response = client.getEventRequest().fromDate(today).toDate(day3).status(EventStatus.UPCOMING).list();

        System.out.println("Upcoming Events");
        System.out.println();
        for (Event event : response.getEntity()) {
            System.out.println("Event #"+event.getId());
            System.out.println(sf.format(event.getStartDate()));
            System.out.println(event.getHomeTeam().getName() + " vs " + event.getVisitingTeam().getName());
            System.out.println();
        }

        //Get list of finished events sorted desc
        response = client.getEventRequest().status(EventStatus.FINISHED).sort("startDate:desc").limit(5).list();
        System.out.println("Finished Events");
        System.out.println();
        for (Event event : response.getEntity()) {
            System.out.println("Event #"+event.getId());
            System.out.println(sf.format(event.getStartDate()));
            System.out.println(event.getHomeTeam().getName() + " vs " + event.getVisitingTeam().getName());
            System.out.println(event.getHomeTeamScore() + " - " + event.getVisitingTeamScore());
            System.out.println();
        }

        //Get next page
        response = client.getNextPage(response);
        System.out.println("Finished Events - Page 2");
        System.out.println();
        for (Event event : response.getEntity()) {
            System.out.println("Event #"+event.getId());
            System.out.println(sf.format(event.getStartDate()));
            System.out.println(event.getHomeTeam().getName() + " vs " + event.getVisitingTeam().getName());
            System.out.println(event.getHomeTeamScore() + " - " + event.getVisitingTeamScore());
            System.out.println();
        }
    }

}
