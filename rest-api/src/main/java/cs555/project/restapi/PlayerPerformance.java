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
@Path("/playerPerf")
public class PlayerPerformance {

    PlayerPerfStore perfStore = PlayerPerfStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerPerformanceModel playerPerformance(){
        PlayerPerformanceModel playerPerf = new PlayerPerformanceModel();
        playerPerf.setPerformances(perfStore.getAllPlayerPerf());
        return playerPerf;
    }
}
