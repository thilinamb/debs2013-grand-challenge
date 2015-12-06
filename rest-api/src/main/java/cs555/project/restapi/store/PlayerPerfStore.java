package cs555.project.restapi.store;

import cs555.project.restapi.store.dataobjects.PlayerPerformance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Thilina Buddhika
 */
public class PlayerPerfStore {

    private static PlayerPerfStore instance = new PlayerPerfStore();

    private Map<String, PlayerPerformance> perfMap = new HashMap<>();

    private PlayerPerfStore() {
    }

    public static PlayerPerfStore getInstance(){
        return instance;
    }

    public synchronized void updatePerf(String playerName, String team, double perf){
        PlayerPerformance playerPerformance = perfMap.get(playerName);
        if(playerPerformance == null){
            playerPerformance = new PlayerPerformance(playerName, team);
            perfMap.put(playerName, playerPerformance);
        }
        playerPerformance.setAvgRunningSpeed(perf);
    }

    public synchronized List<PlayerPerformance> getAllPlayerPerf(){
        List<PlayerPerformance> performances = new ArrayList<>();
        performances.addAll(perfMap.values());
        return performances;
    }

}
