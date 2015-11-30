package cs555.project.restapi;

import cs555.project.restapi.model.PlayerPerformance;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author Thilina Buddhika
 */
@Path("/analytics")
public class Analytics {

    private Random random = new Random(123);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PlayerPerformance getStats(){
        Map<String, Double> perf = new HashMap<>();
        perf.put("Tom Sawyer", 123.345);
        perf.put("Huckelbery Fin", 1267.345);
        PlayerPerformance playerPerf = new PlayerPerformance();
        playerPerf.setPerformances(perf);
        return playerPerf;
    }

}
