/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze.QLearning;

import java.util.HashMap;
import java.util.Set;
import javafx.util.Pair;

/**
 *
 * @author katharine
 */
public class Journey {
    
    private HashMap<Integer, HashMap<Integer, Pair>> journeyMemory;
    
    public Journey() {
        journeyMemory = new HashMap();
    }
    
    public void put(int i, HashMap episodeMemory) {
        journeyMemory.put(i, episodeMemory);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("****Journey Memory****");
        Set<Integer> episodes = journeyMemory.keySet();
        for (Integer episode: episodes) {
            sb.append("For episode: ");
            sb.append(episode);
            sb.append("\n");
            HashMap steps = journeyMemory.get(episode);
            Set<Integer> stepsInJourney = steps.keySet();
            sb.append("I took ");
            sb.append(stepsInJourney.size());
            sb.append(" steps.");
            for (Integer individualStep: stepsInJourney) {
                sb.append("\nStep ");
                sb.append(individualStep);
                sb.append(" was to ");
                sb.append(steps.get(individualStep));
            }
            sb.append("\n");
        }
        return sb.toString();
        
    }
}
