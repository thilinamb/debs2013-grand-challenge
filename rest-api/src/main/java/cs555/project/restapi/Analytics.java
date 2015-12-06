package cs555.project.restapi;

import cs555.project.restapi.model.PlayerPerformanceModel;
import cs555.project.restapi.store.PlayerPerfStore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Thilina Buddhika
 */
@Path("/analytics")
public class Analytics {

    PlayerPerfStore perfStore = PlayerPerfStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerPerformanceModel getStats(){
        PlayerPerformanceModel playerPerf = new PlayerPerformanceModel();
        playerPerf.setPerformances(perfStore.getAllPlayerPerf());
        return playerPerf;
    }

}
