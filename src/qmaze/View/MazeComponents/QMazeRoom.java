package qmaze.View.MazeComponents;

import qmaze.Environment.Coordinates;

/**
 *
 * @author katharine
 */
public class QMazeRoom {
    private boolean open = true;
    private double reward = 0;
    private final Coordinates coordinates;
    private double percentageVisits = 0;
    private double visitCount = 0;
    
    public QMazeRoom(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    
    public void setPercentageVisits(double percentageVisits) {
        this.percentageVisits = percentageVisits;
    }
    
    public double getPercentageVisits() {
        return percentageVisits;
    }
    
    public void setVisitCount(double visitCount) {
        this.visitCount = visitCount;
    }
    
    public double getVisitCount() {
        return visitCount;
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
