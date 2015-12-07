package cs555.project.restapi;

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

    private BallPossessionInfoStore ballPossessionInfoStore = BallPossessionInfoStore.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public cs555.project.restapi.store.dataobjects.BallPossession[] ballPossession(){
        /*BallPossessionModel ballPossessionModel = new BallPossessionModel();
        ballPossessionModel.setBallPossessionInfo(ballPossessionInfoStore.getValues());
        return ballPossessionModel; */
        return ballPossessionInfoStore.getValues();
    }
}
