package qmaze.View;

import qmaze.Environment.Coordinates;

/**
 *
 * @author katharine
 */
public class QMazeRoom {
    private boolean open = true;
    private double reward = 0;
    private final Coordinates coordinates;
    private double weightedVisitCount = 0;
    
    public QMazeRoom(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    
    public void setWeightedVisitCount(double weightedVisitCount) {
        this.weightedVisitCount = weightedVisitCount;
    }
    
    public double getWeightedVisitCount() {
        return weightedVisitCount;
    }
    
    public void open(boolean value) {
        open = value;
    }
    
    public boolean getOpen() {
        return open;
    }
    
    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }
    
    public Coordinates getCoordinates() {
        return coordinates;
    }
}
