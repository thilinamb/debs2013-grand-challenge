package cs555.project.restapi;

import cs555.project.restapi.model.BallPossessionModel;
import cs555.project.restapi.model.PlayerPerformanceModel;
import cs555.project.restapi.model.ShotsOnGoalModel;
import cs555.project.restapi.store.BallPossessionInfoStore;
import cs555.project.restapi.store.PlayerPerfStore;
import cs555.project.restapi.store.ShotsOnGoalStore;

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
    BallPossessionInfoStore ballPossesionInfoStore = BallPossessionInfoStore.getInstance();
    ShotsOnGoalStore shotsOnGoalStore = ShotsOnGoalStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerPerformanceModel playerPerformance(){
        PlayerPerformanceModel playerPerf = new PlayerPerformanceModel();
        playerPerf.setPerformances(perfStore.getAllPlayerPerf());
        return playerPerf;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BallPossessionModel ballPossession(){
        BallPossessionModel ballPossessionModel = new BallPossessionModel();
        ballPossessionModel.setBallPossessionInfo(ballPossesionInfoStore.getValues());
        return ballPossessionModel;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ShotsOnGoalModel shotsOnGoal(){
        ShotsOnGoalModel shotsOnGoalModel = new ShotsOnGoalModel();
        shotsOnGoalModel.setShotsOnGoalData(shotsOnGoalStore.getValues());
        return shotsOnGoalModel;
    }
}
