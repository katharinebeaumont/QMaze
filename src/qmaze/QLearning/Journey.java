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
    private int maxVisits = 0;
    
    public Journey() {
        journeyMemory = new HashMap();
    }
    
    public void put(int i, HashMap episodeMemory) {
        journeyMemory.put(i, episodeMemory);
    }
    
    //Probs a much better way of organising this data. H2 in memory DB perhaps.
    // Or better Collection......... 
    // TODO: look at this. Journey should be DB?
    public HashMap<Pair,Integer> getVisit() {
       //Convert journeyMemory to Pair, count
       maxVisits = 0;
       HashMap<Pair,Integer> coordinateToVisitCount = new HashMap();
       Set<Integer> episodes = journeyMemory.keySet();
        for (Integer episode: episodes) {
            HashMap steps = journeyMemory.get(episode);
            Set<Integer> stepsInJourney = steps.keySet();
            for (Integer individualStep: stepsInJourney) {
                Pair coordinate = (Pair)steps.get(individualStep);
                Integer count = coordinateToVisitCount.get(coordinate);
                if (count == null) {
                    coordinateToVisitCount.put(coordinate, 1);
                } else {
                    count++;
                    coordinateToVisitCount.put(coordinate, count);
                    if (count > maxVisits) maxVisits = count;
                }                
            }
        }
        return coordinateToVisitCount;
    }
    
    public int getMaxVisits() {
        return maxVisits;
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
