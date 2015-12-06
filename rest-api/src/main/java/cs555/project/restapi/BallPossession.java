package cs555.project.restapi;

import cs555.project.restapi.model.BallPossessionModel;
import cs555.project.restapi.store.BallPossessionInfoStore;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Thilina Buddhika
 */
@Path("/ballPossession")
public class BallPossession {

    BallPossessionInfoStore ballPossesionInfoStore = BallPossessionInfoStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BallPossessionModel ballPossession(){
        BallPossessionModel ballPossessionModel = new BallPossessionModel();
        ballPossessionModel.setBallPossessionInfo(ballPossesionInfoStore.getValues());
        return ballPossessionModel;
    }
}
