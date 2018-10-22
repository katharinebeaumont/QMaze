package qmaze.Agent;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author katharine
 * TODO: should this belong to the agent
 */
@Getter
@AllArgsConstructor
public class AgentLearningParameters {

    private final double epsilon;
    private final double learningRate;
    private final double gamma;
}
