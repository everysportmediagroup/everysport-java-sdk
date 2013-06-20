package com.everysport.api.sdk.examples;

import com.everysport.api.domain.api.Group;
import com.everysport.api.domain.api.Standings;
import com.everysport.api.domain.api.TeamStats;
import com.everysport.api.sdk.EverysportClient;
import com.everysport.api.sdk.response.EverysportResponse;

import java.io.IOException;
import java.util.List;

public class StandingsExample {

    private static final String APIKEY = "YOUR-API-KEY";

    public static void main(String args[]) throws IOException {

        EverysportClient client = new EverysportClient.Builder(APIKEY).build();
        EverysportResponse<List<Group>> response;

        //Get standings for Bowling league with id 55581
        response = client.getStandingsRequest().get(55581);
        for (Group group : response.getEntity()) {
            System.out.println("Group " + group.getLabels().get(0).getName());
            boolean labels = true;
            for(Standings standing : group.getStandings()) {
                System.out.print(standing.getTeam().getShortName() + "\t");
                for(TeamStats stats : standing.getStats()) {
                    System.out.print(stats.getValue()+"\t");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
