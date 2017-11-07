package qmaze.View;

/**
 *
 * @author katharine
 */
public class TrainingConfig {
    private final int episodes;
    private final int rows;
    private final int columns;
    private final double gamma;
    private final double epsilon;
    private final double alpha;
    
    public TrainingConfig(int episodes, int rows, int columns, double gamma, double epsilon, double alpha) {
        this.episodes = episodes;
        this.rows = rows;
        this.columns = columns;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.alpha = alpha;
    }
    
    public int getEpisodes() {
        return episodes;
    }
    public int getRows() {
        return rows;
    } 
    public int getColumns() {
        return columns;
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
    
    @Override
    public String toString() {
        return "Episodes: " + episodes + " Gamma: " + gamma + " Epsilon: " + epsilon + " Alpha: " + alpha
                + "\n and " + rows + " rows by " + columns + " columns.";
        
    }
}
