package cs555.project.restapi;

import cs555.project.restapi.model.ShotsOnGoalModel;
import cs555.project.restapi.store.ShotsOnGoalStore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Thilina Buddhika
 */
@Path("/shotsOnGoal")
public class ShotsOnGoal {

    ShotsOnGoalStore shotsOnGoalStore = ShotsOnGoalStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ShotsOnGoalModel shotsOnGoal(){
        ShotsOnGoalModel shotsOnGoalModel = new ShotsOnGoalModel();
        shotsOnGoalModel.setShotsOnGoalData(shotsOnGoalStore.getValues());
        return shotsOnGoalModel;
    }
}
