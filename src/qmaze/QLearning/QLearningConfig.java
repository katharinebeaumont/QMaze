package qmaze.QLearning;

/**
 *
 * @author katharine
 */
public class QLearningConfig {
    private final int episodes;
    private final double gamma;
    private final double epsilon;
    private final double alpha;
   
    public QLearningConfig(int episodes, double gamma, double epsilon, double alpha) {
        this.episodes = episodes;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.alpha = alpha;
    }
    
    public int getEpisodes() {
        return episodes;
    }
    public double getGamma() {
        return gamma;
    }
    public double getEpsilon() {
        return epsilon;
    }
    public double getAlpha() {
        return alpha;
    } 
    
    public String toString() {
        return "Episodes: " + episodes + " Gamma: " + gamma + " Epsilon: " + epsilon + " Alpha: " + alpha;
    }
}
