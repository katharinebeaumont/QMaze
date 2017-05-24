
package qmaze.Agent;

/**
 *
 * @author katharine
 *  TODO: should this belong to the agent
 */
public class AgentLearningParameters {
    
    private final double epsilon;
    private final double learningRate;
    private final double gamma;
    
    public AgentLearningParameters(double epsilon, double learningRate, double gamma) {
        this.epsilon = epsilon;
        this.learningRate = learningRate;
        this.gamma = gamma;
    }
    
    public double getEpsilon() {
        return epsilon;
    }
    public double getGamma() {
        return gamma;
    }
    public double getLearningRate() {
        return learningRate;
    }
    
}
