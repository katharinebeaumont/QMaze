/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qmaze.QLearning;

/**
 *
 * @author katharine
 */
public class Room {
    
    private boolean open = true;
    private double reward = 0;
    private boolean hasAgent = false;

    public void setOpen(boolean open) {
        if (!hasAgent && reward == 0) {
            this.open = open;
        } else {
            System.out.println("Can't touch this.");
        }
        
    }
    
    public boolean getOpen() {
        return open;
    }

    public void setHasAgent(boolean hasAgent) {
        this.hasAgent = hasAgent;
    }
    
    public boolean getHasAgent() {
        return hasAgent;
    }
    
    public double getReward() {
        return reward;
    }

    public void setReward(double reward) {
        this.reward = reward;
    } 
    
}
